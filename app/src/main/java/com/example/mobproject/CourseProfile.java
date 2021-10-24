package com.example.mobproject;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CourseProfile extends AppCompatActivity {

    RatingBar rateEdit, finalRating;
    TextView ratingScore, finalRatingScore;
    int totalRating;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_page);

        rateEdit = (RatingBar) findViewById(R.id.rating_edit);
        finalRating = (RatingBar) findViewById(R.id.final_rating);
        ratingScore = (TextView) findViewById(R.id.rating_score);
        finalRatingScore = (TextView) findViewById(R.id.final_rating_score);

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
