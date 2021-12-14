package com.example.mobproject;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobproject.adapters.CourseAdapter;
import com.example.mobproject.adapters.MyCoursesAdapter;
import com.example.mobproject.constants.UserInfo;
import com.example.mobproject.db.CourseDatabase;
import com.example.mobproject.db.EnrolledCoursesDatabase;
import com.example.mobproject.db.FavouriteCoursesDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.example.mobproject.navigation.MenuDrawer;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class MyCoursesListActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private ArrayList<DocumentReference> favouriteList;
    private String userId;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_courses_list);
        userInfo = new UserInfo(this);
        userId = userInfo.getUserId();
        //fillCourses();

        MenuDrawer.actionBarInit(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fillCourses();
    }

    private void fillCourses() {

        String userId = userInfo.getUserId();
        Context context = this;

        Callback<Course> recyclerViewCallback = new Callback<Course>() {
            @Override
            public void OnFinish(ArrayList<Course> enrolledCourseList) {
                RecyclerView myCourseListRecyclerView = findViewById(R.id.my_courses_list);
                myCourseListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                FavouriteCoursesDatabase favouriteCoursesDatabase = new FavouriteCoursesDatabase();
                favouriteCoursesDatabase.getItems(userId, new Callback<DocumentReference>() {
                    @Override
                    public void OnFinish(ArrayList<DocumentReference> arrayList) {
                        favouriteList = arrayList;
                        MyCoursesAdapter adapter = new MyCoursesAdapter(context, enrolledCourseList,
                                favouriteList, userId);
                        myCourseListRecyclerView.setAdapter(adapter);
                    }
                });
            }
        };

        EnrolledCoursesDatabase database = new EnrolledCoursesDatabase();
        database.getItems(userId, recyclerViewCallback);

    }

    public void onBackPressed(){
        MenuDrawer.onBackHandler();
    }
}
