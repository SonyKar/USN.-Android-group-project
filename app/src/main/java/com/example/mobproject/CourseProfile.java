package com.example.mobproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CourseProfile extends AppCompatActivity {

    RatingBar rateEdit, finalRating;
    TextView ratingScore, finalRatingScore;
    int totalRating, isEdit = 0 ;
    ImageButton editCourse;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_page);

        rateEdit = (RatingBar) findViewById(R.id.rating_edit);
        finalRating = (RatingBar) findViewById(R.id.final_rating);
        ratingScore = (TextView) findViewById(R.id.rating_score);
        finalRatingScore = (TextView) findViewById(R.id.final_rating_score);
        editCourse = (ImageButton) findViewById(R.id.edit_btn);

        //got to Edit Course
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
}
