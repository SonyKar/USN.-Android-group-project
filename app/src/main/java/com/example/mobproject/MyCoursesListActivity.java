package com.example.mobproject;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobproject.adapters.MyCoursesAdapter;
import com.example.mobproject.db.Database;
import com.example.mobproject.db.EnrolledCoursesDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.example.mobproject.navigation.MenuDrawer;

import java.util.ArrayList;

public class MyCoursesListActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_courses_list);

        //fillCourses();

        MenuDrawer.actionBarInit(this);
    }

    private void fillCourses() {
        Context context = this;
        Callback<Course> recyclerViewCallback = new Callback<Course>() {
            @Override
            public void OnFinish(ArrayList<Course> arrayList) {
                RecyclerView myCourseListRecyclerView = findViewById(R.id.my_courses_list);
                myCourseListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                MyCoursesAdapter adapter = new MyCoursesAdapter(context, arrayList);
                myCourseListRecyclerView.setAdapter(adapter);
            }
        };

        Database<Course> database = new EnrolledCoursesDatabase();
        database.getItems(recyclerViewCallback);
    }


    public void onBackPressed(){
        MenuDrawer.onBackHandler();
    }
}
