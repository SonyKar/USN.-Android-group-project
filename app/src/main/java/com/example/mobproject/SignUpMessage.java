package com.example.mobproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpMessage extends AppCompatActivity {

    Button toCourses;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_msg);

        toCourses = (Button) findViewById(R.id.to_courses);

        toCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent goToCourses = new Intent(this, /*CoursesPage.class*/);
               // startActivity(goToCourses);
            }
        });
    }
}
