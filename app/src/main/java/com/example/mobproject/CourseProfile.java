package com.example.mobproject;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.db.Database;
import com.example.mobproject.models.Course;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourseProfile extends AppCompatActivity {

    private RatingBar rateEdit, finalRating;
    private TextView ratingScore, finalRatingScore, courseEnroll, courseDescription, courseName, coursePrice,
    courseDifficulty, coursePeriod, coursemeetingDays;
    private int totalRating, isEdit = 0, addedToFav ;
    private ImageButton editCourse, backToMain;
    private Button enrollMe;
    private FloatingActionButton addToFav;
    private RecyclerView commentsList;
    private ArrayList<String> items;//-> <Comment>
    private CommentAdapter adapter;
    private String courseID, meetingDaysString;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_page);

        rateEdit = findViewById(R.id.rating_edit);
        finalRating = findViewById(R.id.final_rating);
        ratingScore = findViewById(R.id.rating_score);
        finalRatingScore = findViewById(R.id.final_rating_score);
        editCourse = findViewById(R.id.edit_btn);
        addToFav = findViewById(R.id.add_to_fav);
        courseEnroll = findViewById(R.id.open_to_enroll_course_page);
        courseDescription = findViewById(R.id.course_descr);
        backToMain = findViewById(R.id.back_btn);
        courseName = findViewById(R.id.course_name);
        coursePrice = findViewById(R.id.course_price);
        courseDifficulty = findViewById(R.id.difficulty_course_page);
        coursePeriod = findViewById(R.id.course_period);
        coursemeetingDays = findViewById(R.id.meeting_days);
        enrollMe = findViewById(R.id.enroll_me_btn);


        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToMain();
            }
        });

        //get course ID
        Intent intent = getIntent();
        courseID= intent.getStringExtra("COURSE_ID");

        //set values for all fields
        FirebaseFirestore.getInstance().collection(DatabaseCollections.COURSES_COLLECTION).document(courseID).
                get().addOnSuccessListener(documentSnapshot -> {
                Course course = documentSnapshot.toObject(Course.class);
                courseName.setText(course.getName());
                coursePrice.setText("$" + Double.toString(course.getPrice()));

                int difficulty = course.getDifficulty();
                courseDifficulty.setText(getResources().getStringArray(R.array.difficulties)[difficulty]);

                //set difficulty color
                if(difficulty == 0)
                    courseDifficulty.setTextColor(getResources().getColor(R.color.beginner_green));
                else if (difficulty == 1)
                    courseDifficulty.setTextColor(getResources().getColor(R.color.intermediate_yellow));
                else
                    courseDifficulty.setTextColor(getResources().getColor(R.color.advanced_red));

                if(course.isOpenEnroll())
                {
                    courseEnroll.setText(R.string.open_to_enroll);
                    courseEnroll.setTextColor(getResources().getColor(R.color.beginner_green));
                }
                else {
                    courseEnroll.setText(R.string.cannot_enroll);
                    courseEnroll.setTextColor(getResources().getColor(R.color.advanced_red));
                }



                courseDescription.setText(course.getDescription());

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                coursePeriod.setText(sdf.format(course.getStartDate()) + " - " + sdf.format(course.getEndDate()));

                meetingDaysString = "";
                List<Integer> meetDays = course.getMeetDays();
                for(int day : course.getMeetDays()){
                    meetingDaysString = meetingDaysString + " " + getResources().getStringArray(R.array.meeting_days)[day];
                }
                coursemeetingDays.setText(meetingDaysString);

                //TODO isOpenToEnroll()

        });


        //create RecyclerView

        items = new ArrayList<>();
        items.add("Anne Marie");
        items.add("Greta Sasha");
        items.add("Erik Stivulescu");
        items.add("Nicolae Ceausescu");
        items.add("Esteban Julio Ricardo Montoya Dela Rosa Ramirez");

        commentsList = findViewById(R.id.comments_list);
        commentsList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentAdapter(this, items);
        commentsList.setAdapter(adapter);


        //check course availability - open to enroll?
        //if user is already enrolled or currentDate > startDate
        //enrollMe.setEnabled(false);

        /*if(//id open to enroll//) {
            courseEnroll.setTextColor(Color.GREEN);
            courseEnroll.setText(R.string.open_to_enroll);
        else courseEnroll.setTextColor(Color.RED);*/


        //add course to Favourites
        addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change color

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

            }
        });


        //go to Edit Course
        //1 - go to EditCourse; 0 - go to Create Course (same Activity)

        editCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = 1;
                Intent toEditCourse = new Intent(CourseProfile.this,
                        CreateCourse.class);
                toEditCourse.putExtra("EDIT_COURSE", isEdit);
                startActivity(toEditCourse);
            }
        });




        rateEdit.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float givenScore, boolean b) {
                int rating = (int) givenScore;//to be sent
                ratingScore.setText(rating + "/5") ;
            }
        });

        //get final rating score and show it
        finalRatingScore.setText(totalRating + "/5");

    }

    private void switchToMain() {
        Intent toMain = new Intent(this, CourseList.class);
        startActivity(toMain);
        finish();
    }
}
