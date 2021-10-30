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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CourseProfile extends AppCompatActivity {

    RatingBar rateEdit, finalRating;
    TextView ratingScore, finalRatingScore, courseEnroll, courseDescription;
    int totalRating, isEdit = 0, addedToFav ;
    ImageButton editCourse, backToMain;
    FloatingActionButton addToFav;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_page);

        rateEdit = (RatingBar) findViewById(R.id.rating_edit);
        finalRating = (RatingBar) findViewById(R.id.final_rating);
        ratingScore = (TextView) findViewById(R.id.rating_score);
        finalRatingScore = (TextView) findViewById(R.id.final_rating_score);
        editCourse = (ImageButton) findViewById(R.id.edit_btn);
        addToFav = (FloatingActionButton) findViewById(R.id.add_to_fav);
        courseEnroll = (TextView) findViewById(R.id.open_to_enroll_course_page);
        courseDescription = (TextView) findViewById(R.id.descr_field);
        backToMain = (ImageButton) findViewById(R.id.back_btn);

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToMain();
            }
        });



        //check course availability - open to enroll?

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
