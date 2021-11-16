package com.example.mobproject;

import static java.lang.Float.*;

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
import com.example.mobproject.db.CourseDatabase;
import com.example.mobproject.db.Database;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CourseProfile extends AppCompatActivity {

    private RatingBar rateEdit, finalRating;
    private TextView ratingScore, finalRatingScore, courseEnroll, courseDescription, courseName, coursePrice,
    courseDifficulty, coursePeriod, courseMeetingDays;
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
        courseMeetingDays = findViewById(R.id.meeting_days);
        enrollMe = findViewById(R.id.enroll_me_btn);

        backToMain.setOnClickListener(switchToMain);
        addToFav.setOnClickListener(onFavouriteHandler);
        editCourse.setOnClickListener(onEditHandler);
        rateEdit.setOnRatingBarChangeListener(onVoteHandler);

        //get course ID
        Intent intent = getIntent();
        courseID = intent.getStringExtra(Intents.COURSE_ID);

        //set values for all fields
        Database<Course> database = new CourseDatabase();
        database.getItem(courseID, profileCallback);

        //get final rating score and show it
        //finalRatingScore.setText(totalRating + R.string.ratingOutOf);

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

        //go to Edit Course
        //1 - go to EditCourse; 0 - go to Create Course (same Activity)
    }

    private final RatingBar.OnRatingBarChangeListener onVoteHandler = (ratingBar, givenScore, b) -> {
        int rating = (int) givenScore;//to be sent
        ratingScore.setText(rating + R.string.ratingOutOf) ;
    };
    //TODO finalRating!!!

    private final View.OnClickListener onEditHandler = view -> {
        isEdit = 1;
        Intent toEditCourse = new Intent(CourseProfile.this,
                CreateCourse.class);
        toEditCourse.putExtra("EDIT_COURSE", isEdit);
        startActivity(toEditCourse);
    };

    private final View.OnClickListener onFavouriteHandler = view -> {
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

    };

    private final Callback<Course> profileCallback = new Callback<Course>() {
        @Override
        public void OnFinish(ArrayList<Course> arrayList) {
            Course course = arrayList.get(0);
            courseName.setText(course.getName());

            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
            String currency = format.format(course.getPrice());
            coursePrice.setText(currency);

            int difficulty = course.getDifficulty();
            courseDifficulty.setText(getResources().getStringArray(R.array.difficulties)[difficulty]);

            //set difficulty color
            courseDifficulty.setTextColor(getResources().getIntArray(R.array.difficultyColors)[difficulty]);

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

            coursePeriod.setText(SimpleDateFormat.getDateInstance().format(course.getStartDate()) + " - " + SimpleDateFormat.getDateInstance().format(course.getEndDate()));

            meetingDaysString = "";
            for(int day : course.getMeetDays()){
                meetingDaysString += " " + getResources().getStringArray(R.array.meeting_days)[day];
            }
            courseMeetingDays.setText(meetingDaysString);


            //TODO fix rating
            /*String finalRatingString = String.valueOf(course.getRateCounter()) ;
            finalRatingScore.setText(finalRatingString);
            finalRating.setRating(course.getRateCounter());*/
        }
    };

    private View.OnClickListener switchToMain = view -> {
        Intent toMain = new Intent(this, CourseList.class);
        startActivity(toMain);
        finish();
    };
}
