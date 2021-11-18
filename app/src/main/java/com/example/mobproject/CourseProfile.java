package com.example.mobproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobproject.constants.Intents;
import com.example.mobproject.controllers.CourseController;
import com.example.mobproject.db.CourseDatabase;
import com.example.mobproject.db.Database;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CourseProfile extends AppCompatActivity {

    private RatingBar finalRating;
    private TextView ratingScore, finalRatingScore, courseEnroll, courseDescription, courseName, coursePrice,
    courseDifficulty, coursePeriod, courseMeetingDays, numberOfComments, commentTextView;
    private int addedToFav ;
    private FloatingActionButton addToFav;
    private String courseID;
    private Course courseInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_page);

        RatingBar rateEdit = findViewById(R.id.rating_edit);
        finalRating = findViewById(R.id.final_rating);
        ratingScore = findViewById(R.id.rating_score);
        finalRatingScore = findViewById(R.id.final_rating_score);
        ImageButton editCourse = findViewById(R.id.edit_btn);
        addToFav = findViewById(R.id.add_to_fav);
        courseEnroll = findViewById(R.id.open_to_enroll_course_page);
        courseDescription = findViewById(R.id.course_descr);
        ImageButton backToMain = findViewById(R.id.back_btn);
        courseName = findViewById(R.id.course_name);
        coursePrice = findViewById(R.id.course_price);
        courseDifficulty = findViewById(R.id.difficulty_course_page);
        coursePeriod = findViewById(R.id.course_period);
        courseMeetingDays = findViewById(R.id.meeting_days);
        // Button enrollMe = findViewById(R.id.enroll_me_btn);
        numberOfComments = findViewById(R.id.no_comments);
        Button postCommentButton = findViewById(R.id.post_btn);
        commentTextView = findViewById(R.id.comment_input);

        backToMain.setOnClickListener(switchToMain);
        addToFav.setOnClickListener(onFavouriteHandler);
        editCourse.setOnClickListener(onEditHandler);
        rateEdit.setOnRatingBarChangeListener(onVoteHandler);
        postCommentButton.setOnClickListener(postCommentHandler);

        // get course ID
        Intent intent = getIntent();
        courseID = intent.getStringExtra(Intents.COURSE_ID);

        // set values for all fields
        Database<Course> database = new CourseDatabase();
        database.getItem(courseID, profileCallback);

        //check course availability - open to enroll?
        //if user is already enrolled or currentDate > startDate
        //enrollMe.setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        courseID = intent.getStringExtra(Intents.COURSE_ID);

        // set values for all fields
        Database<Course> database = new CourseDatabase();
        database.getItem(courseID, profileCallback);

    }

    private final RatingBar.OnRatingBarChangeListener onVoteHandler = (ratingBar, givenScore, b) -> {
        int rating = (int) givenScore; // to be sent
        String ratingText = rating + this.getString(R.string.ratingOutOf);
        ratingScore.setText(ratingText);
    };
    //TODO add rating to total rating

    private final View.OnClickListener onEditHandler = view -> {
        int isEdit = 1;
        Intent toEditCourse = new Intent(CourseProfile.this,
                CreateCourse.class);
        toEditCourse.putExtra("EDIT_COURSE", isEdit);
        toEditCourse.putExtra("COURSE_ID",courseID);
        startActivity(toEditCourse);
    };

    private final View.OnClickListener onFavouriteHandler = view -> {
        //get addedToFav from DB (is already in Favourites?)
        if(addedToFav == 0){
            addedToFav = 1;//add to Favourites
            addToFav.setImageResource(R.drawable.ic_favourite_purple);
        }
        else {
            //is already in Favourites => delete from Favourites
            addedToFav = 0;
            addToFav.setImageResource(R.drawable.ic_favourite_red);
        }

    };

    private final Callback<Course> profileCallback = new Callback<Course>() {
        @Override
        public void OnFinish(ArrayList<Course> arrayList) {
            courseInfo = arrayList.get(0);
            courseName.setText(courseInfo.getName());

            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
            String currency = format.format(courseInfo.getPrice());
            coursePrice.setText(currency);

            int difficulty = courseInfo.getDifficulty();
            courseDifficulty.setText(getResources().getStringArray(R.array.difficulties)[difficulty]);

            //set difficulty color
            courseDifficulty.setTextColor(getResources().getIntArray(R.array.difficultyColors)[difficulty]);

            if(courseInfo.isOpenEnroll())
            {
                courseEnroll.setText(R.string.open_to_enroll);
                courseEnroll.setTextColor(getResources().getColor(R.color.beginner_green));
            }
            else {
                courseEnroll.setText(R.string.cannot_enroll);
                courseEnroll.setTextColor(getResources().getColor(R.color.advanced_red));
            }

            courseDescription.setText(courseInfo.getDescription());

            String dateString = SimpleDateFormat.getDateInstance().format(courseInfo.getStartDate()) + " - " + SimpleDateFormat.getDateInstance().format(courseInfo.getEndDate());
            coursePeriod.setText(dateString);

            StringBuilder meetingDaysString = new StringBuilder();
            for(int day : courseInfo.getMeetDays()){
                meetingDaysString.append(" ").append(getResources().getStringArray(R.array.meeting_days)[day]);
            }
            courseMeetingDays.setText(meetingDaysString.toString());

            updateComments();

            BigDecimal number = BigDecimal.valueOf(courseInfo.getRating());

            String finalRatingString = String.valueOf(courseInfo.getRating());
            String finalRatingText = finalRatingString + getString(R.string.ratingOutOf);
            finalRatingScore.setText(finalRatingText);
            finalRating.setRating(Float.parseFloat(String.valueOf(number.floatValue())));
        }
    };

    private void updateComments() {
        RecyclerView commentsList = findViewById(R.id.comments_list);
        commentsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        CommentAdapter adapter = new CommentAdapter(getApplicationContext(), courseInfo.getCommentsReferences());
        commentsList.setAdapter(adapter);
        numberOfComments.setText(String.valueOf(courseInfo.getCommentsReferences().size()));
    }

    private final View.OnClickListener switchToMain = view -> finish();

    private final View.OnClickListener postCommentHandler = view -> {
        CourseController courseController = new CourseController();
        UserInfo userInfo = new UserInfo(this);
        courseController.addComment(courseID, userInfo.getUserId(), commentTextView.getText().toString());
        commentTextView.setText("");

        CourseDatabase courseDatabase = new CourseDatabase();
        courseDatabase.getItem(courseID, new Callback<Course>() {
            @Override
            public void OnFinish(ArrayList<Course> arrayList) {
                courseInfo = arrayList.get(0);
                updateComments();
            }
        });
    };
}
