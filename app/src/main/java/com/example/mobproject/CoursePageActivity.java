package com.example.mobproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobproject.adapters.CommentAdapter;
import com.example.mobproject.constants.Course;
import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.constants.Intents;
import com.example.mobproject.constants.UserInfo;
import com.example.mobproject.controllers.CourseController;
import com.example.mobproject.controllers.PictureController;
import com.example.mobproject.db.CourseDatabase;
import com.example.mobproject.db.Database;
import com.example.mobproject.db.EnrolledCoursesDatabase;
import com.example.mobproject.db.FavouriteCoursesDatabase;
import com.example.mobproject.db.UserDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.User;
import com.example.mobproject.notifications.APIService;
import com.example.mobproject.notifications.Data;
import com.example.mobproject.notifications.NotificationSender;
import com.example.mobproject.notifications.Receiver;
import com.example.mobproject.notifications.Response;
import com.example.mobproject.notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;

public class CoursePageActivity extends AppCompatActivity {
    private RatingBar finalRating;
    private TextView ratingScore, finalRatingScore, courseEnroll, courseDescription, courseName, coursePrice,
            courseDifficulty, coursePeriod, courseMeetingDays, numberOfComments, commentTextView, no_students, teacherName;
    private ImageView courseImage;
    private FloatingActionButton addToFav;
    private Button enrollMe;
    private ImageButton editCourse;
    private UserInfo userInfo;
    private String courseId;
    private com.example.mobproject.models.Course courseInfo;
    private String userId;
    private String categoryName;
    private boolean isFavourite = false;
    private APIService apiService;
    private DocumentReference ownerId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_page);

        RatingBar rateEdit = findViewById(R.id.rating_edit);
        finalRating = findViewById(R.id.final_rating);
        ratingScore = findViewById(R.id.rating_score);
        finalRatingScore = findViewById(R.id.final_rating_score);
        editCourse = findViewById(R.id.edit_btn);
        addToFav = findViewById(R.id.add_to_fav);
        courseEnroll = findViewById(R.id.open_to_enroll_course_page);
        courseDescription = findViewById(R.id.course_description);
        ImageButton backToMain = findViewById(R.id.back_btn);
        courseName = findViewById(R.id.course_name);
        coursePrice = findViewById(R.id.course_price);
        courseDifficulty = findViewById(R.id.difficulty_course_page);
        coursePeriod = findViewById(R.id.course_period);
        courseMeetingDays = findViewById(R.id.meeting_days);
        enrollMe = findViewById(R.id.enroll_me_btn);
        numberOfComments = findViewById(R.id.no_comments);
        Button postCommentButton = findViewById(R.id.post_btn);
        commentTextView = findViewById(R.id.comment_input);
        ImageView commentAvatar = findViewById(R.id.comment_avatar);
        courseImage = findViewById(R.id.course_bg);
        no_students = findViewById(R.id.no_students);
        teacherName = findViewById(R.id.teacher_name);

        apiService = Receiver.getReceiver("https://fcm.googleapis.com/").create(APIService.class);

        userInfo = new UserInfo(this);
        userId = userInfo.getUserId();

        backToMain.setOnClickListener(switchToMain);
        addToFav.setOnClickListener(onFavouriteHandler);
        editCourse.setOnClickListener(onEditHandler);
        rateEdit.setOnRatingBarChangeListener(onVoteHandler);
        postCommentButton.setOnClickListener(postCommentHandler);
        enrollMe.setOnClickListener(onEnrollHandler);

        // get course ID
        Intent intent = getIntent();
        courseId = intent.getStringExtra(Intents.COURSE_ID);

        // set values for all fields
        Database<com.example.mobproject.models.Course> database = new CourseDatabase();
        database.getItem(courseId, initValuesCallback);

        //make button invisible for Student User
        String userTypeString = userInfo.getUserType();
        if (userTypeString.equals("0"))
            editCourse.setVisibility(View.GONE);


        initFavouriteButton();
        initEnrolledButton();

        //set your commentAvatar
        PictureController.getProfilePicture(userId, commentAvatar);
    }

    private void initFavouriteButton() {
        FavouriteCoursesDatabase favouriteDatabase = new FavouriteCoursesDatabase();

        favouriteDatabase.getItems(userId, new Callback<DocumentReference>() {
            @Override
            public void OnFinish(ArrayList<DocumentReference> favouriteReferences) {
                for (DocumentReference docRef : favouriteReferences)
                    if (courseId.equals(docRef.getId())) {
                        isFavourite = true;
                        break;
                    }
                if (isFavourite) {
                    addToFav.setImageResource(R.drawable.ic_favourite_red);
                } else {
                    addToFav.setImageResource(R.drawable.ic_favourite_purple);
                }
            }
        });
    }

    private void initEnrolledButton() {
        //disable and change button color if teacher user
        String userTypeString = userInfo.getUserType();

        EnrolledCoursesDatabase enrolledDatabase = new EnrolledCoursesDatabase();

        enrolledDatabase.getItems(userId, new Callback<com.example.mobproject.models.Course>() {
            @Override
            public void OnFinish(ArrayList<com.example.mobproject.models.Course> enrolledReferences) {
                boolean isEnrolled = false;

                for (com.example.mobproject.models.Course course : enrolledReferences) {
                    if (courseId.equals(course.getId())) {
                        isEnrolled = true;
                        break;
                    }
                }

                //init enrollMe button
                if (isEnrolled || !courseInfo.isOpenEnroll()) {
                    enrollMe.setEnabled(false);
                    enrollMe.setText(R.string.cannot_enroll_message);
                }
            }
        });

        //check enrollMe button
        if (userTypeString.equals("1")) {
            enrollMe.setEnabled(false);
            enrollMe.setText(R.string.cannot_enroll_teacher);
            enrollMe.setTextColor(Color.parseColor("#FFFFFF"));
            enrollMe.setTypeface(null, Typeface.NORMAL);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        courseId = intent.getStringExtra(Intents.COURSE_ID);

        // set values for all fields
        Database<com.example.mobproject.models.Course> database = new CourseDatabase();
        database.getItem(courseId, initValuesCallback);

    }

    private final RatingBar.OnRatingBarChangeListener onVoteHandler = (ratingBar, givenScore, b) -> {
        int rating = (int) givenScore; // to be sent
        String ratingText = rating + this.getString(R.string.ratingOutOf);
        ratingScore.setText(ratingText);

        CourseController courseController = new CourseController();
        courseInfo = courseController.addRating(courseInfo, givenScore);
        updateTotalRating();
    };

    private final View.OnClickListener onEditHandler = view -> {
        Intent toEditCourse = new Intent(CoursePageActivity.this,
                CreateCourseActivity.class);
        toEditCourse.putExtra(Intents.EDIT_TYPE, Course.EDIT_MODE);
        toEditCourse.putExtra(Intents.COURSE_ID, courseId);
        startActivity(toEditCourse);
    };

    private final View.OnClickListener onFavouriteHandler = view -> {
        FavouriteCoursesDatabase favouriteDatabase = new FavouriteCoursesDatabase();
        if (!isFavourite) {
            addToFav.setImageResource(R.drawable.ic_favourite_red);
            favouriteDatabase.insertItem(userId, courseId);
            userInfo.setUserFavouritesNo(userInfo.getUserFavouritesNo()+1);
        } else {
            addToFav.setImageResource(R.drawable.ic_favourite_purple);
            favouriteDatabase.removeItem(userId, courseId);
            userInfo.setUserFavouritesNo(userInfo.getUserFavouritesNo()-1);
        }
        isFavourite = !isFavourite;
    };

    private final View.OnClickListener onEnrollHandler = view -> {
        EnrolledCoursesDatabase enrolledDatabase = new EnrolledCoursesDatabase();
        enrolledDatabase.insertItem(userId, courseId);
        Toast.makeText(getApplicationContext(), getString(R.string.enrollment_message), Toast.LENGTH_SHORT).show();

        CourseDatabase courseDatabase = new CourseDatabase();
        courseDatabase.incrementStudentCounter(courseId);
        incrementStudentCounter();
        userInfo.setUserCoursesNo(userInfo.getUserCoursesNo()+1);

        enrollMe.setEnabled(false);
    };

    private void incrementStudentCounter() {
        no_students.setText(getResources().getString(R.string.student_counter, courseInfo.getStudentCounter() + 1));
    }

    private final Callback<com.example.mobproject.models.Course> initValuesCallback = new Callback<com.example.mobproject.models.Course>() {
        @Override
        public void OnFinish(ArrayList<com.example.mobproject.models.Course> courseList) {
            courseInfo = courseList.get(0);
            courseName.setText(courseInfo.getName());

            no_students.setText(getResources().getString(R.string.student_counter, courseInfo.getStudentCounter()));

            UserDatabase userDatabase = new UserDatabase();
            ownerId = courseInfo.getOwnerId();
            userDatabase.getItem(ownerId.getId(), new Callback<User>() {
                @Override
                public void OnFinish(ArrayList<User> arrayList) {
                    teacherName.setText(arrayList.get(0).getName());
                }
            });

            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
            String currency = format.format(courseInfo.getPrice());
            coursePrice.setText(currency);

            int difficulty = courseInfo.getDifficulty();
            courseDifficulty.setText(getResources().getStringArray(R.array.difficulties)[difficulty]);

            //set difficulty color
            courseDifficulty.setTextColor(getResources().getIntArray(R.array.difficultyColors)[difficulty]);

            if (courseInfo.isOpenEnroll()) {
                courseEnroll.setText(R.string.open_to_enroll);
                courseEnroll.setTextColor(getResources().getColor(R.color.beginner_green));
            } else {
                courseEnroll.setText(R.string.cannot_enroll);
                courseEnroll.setTextColor(getResources().getColor(R.color.advanced_red));
            }
            courseDescription.setText(courseInfo.getDescription());

            String dateString = SimpleDateFormat.getDateInstance().format(courseInfo.getStartDate()) + " - " + SimpleDateFormat.getDateInstance().format(courseInfo.getEndDate());
            coursePeriod.setText(dateString);

            StringBuilder meetingDaysString = new StringBuilder();
            for (int day : courseInfo.getMeetDays()) {
                meetingDaysString.append(" ").append(getResources().getStringArray(R.array.meeting_days)[day]);
            }
            courseMeetingDays.setText(meetingDaysString.toString());

            if (userId.equals(ownerId.getId()))
                editCourse.setVisibility(View.VISIBLE);
            updateComments();
            updateTotalRating();

            String categoryId = courseInfo.getCategoryId().getId();
            DocumentReference categoryRef = FirebaseFirestore.getInstance()
                    .collection(DatabaseCollections.CATEGORIES_COLLECTION).document(categoryId);
            categoryRef.get().addOnCompleteListener(task ->{
                if ( task.isSuccessful() ) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if ( documentSnapshot != null && documentSnapshot.exists() ){
                        categoryName = (String) documentSnapshot.get("fileName");
                        Context context = courseImage.getContext();
                        int drawableId = context.getResources().getIdentifier(
                                categoryName, "drawable", context.getPackageName());
                        Picasso.get().load(drawableId).into(courseImage);
                    }
                }
            });

        }
    };

    private void updateComments() {
        if (courseInfo.getCommentsReferences() != null) {
            RecyclerView commentsList = findViewById(R.id.comments_list);
            commentsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            CommentAdapter adapter = new CommentAdapter(getApplicationContext(), courseInfo.getCommentsReferences());
            commentsList.setAdapter(adapter);
            numberOfComments.setText(String.valueOf(courseInfo.getCommentsReferences().size()));
        }
    }

    private void updateTotalRating() {
        float finalRatingValue = (float) (Math.round(courseInfo.getRating() * 100.0) / 100.0);
        String finalRatingString = String.valueOf(finalRatingValue);
        String finalRatingText = finalRatingString + getString(R.string.ratingOutOf);

        finalRatingScore.setText(finalRatingText);
        finalRating.setRating(finalRatingValue);
    }

    private final View.OnClickListener switchToMain = view -> finish();

    private final View.OnClickListener postCommentHandler = view -> {
        String commentMessage = commentTextView.getText().toString();

        if (!commentMessage.trim().isEmpty()) {
            CourseController courseController = new CourseController();
            UserInfo userInfo = new UserInfo(this);
            courseInfo = courseController.addComment(courseInfo, userInfo.getUserId(), commentMessage);
            commentTextView.setText("");
            updateComments();
//            handleNotification();

        } else {
            Toast.makeText(this, R.string.comment_error, Toast.LENGTH_SHORT);
        }
    };


//    private void handleNotification(){
//        FirebaseDatabase.getInstance().getReference().child(DatabaseCollections.TOKENS_COLLECTION)
//                .child(ownerId.getId()).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String userToken=dataSnapshot.getValue(String.class);
//                FirebaseFirestore.getInstance().collection(DatabaseCollections.USER_COLLECTION)
//                        .document(userId).get().addOnSuccessListener(documentSnapshot -> {
//                            String userName = (String) documentSnapshot.get("name");
//                            sendNotifications(userToken, courseInfo.getName(),userName);
//                        });
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//        updateToken();
//    }

//    private void updateToken(){
//        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//            @Override
//            public void onComplete(@NonNull Task<String> task) {
//                if (!task.isSuccessful()) {
//                    Log.w("tokenFetch", "Fetching FCM registration token failed", task.getException());
//                    return;
//                }
//                String refreshToken = task.getResult();
//                Token token= new Token(refreshToken);
//                FirebaseDatabase.getInstance().getReference(DatabaseCollections.TOKENS_COLLECTION)
//                        .child(firebaseUser.getUid()).setValue(token);
//            }
//        });
//    }

//    public void sendNotifications(String userToken, String courseName, String senderName){
//        Data data = new Data(courseName, senderName);
//        Log.d("notificationSend","sending Notif");
//        NotificationSender notificationSender = new NotificationSender(data, userToken);
//        Log.d("notificationSend","made sender");
//        apiService.sendNotification(notificationSender).enqueue(new retrofit2.Callback<Response>() {
//            @Override
//            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
//                Log.d("notificationSend","onResponse");
//                Log.d("notificationSend","response code"+String.valueOf(response.code()));
////                Log.d("notificationSend","response body"+String.valueOf(response.body().success));
//                Log.d("notificationSend","onResponse");
//                if(response.code() == 200 && response.body().success != 1){
//                    Log.d("notificationSending","sent successfully");
//                }
//            }
//            @Override
//            public void onFailure(Call<Response> call, Throwable t) {
//                Log.d("notificationSending","failure sending");
//            }
//        });
//    }
}
