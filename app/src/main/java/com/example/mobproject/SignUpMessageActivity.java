package com.example.mobproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpMessageActivity extends AppCompatActivity {
    Button toCourses;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_message);

        toCourses = findViewById(R.id.to_courses);

        toCourses.setOnClickListener(switchToCourseList);
    }

    private final View.OnClickListener switchToCourseList = view -> {
        Intent toCourseList = new Intent(view.getContext(), CourseListActivity.class);
        startActivity(toCourseList);
        finish();
    };
}
