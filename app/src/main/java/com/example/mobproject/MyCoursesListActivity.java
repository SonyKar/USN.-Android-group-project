package com.example.mobproject;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobproject.adapters.MyCoursesAdapter;
import com.example.mobproject.constants.UserInfo;
import com.example.mobproject.db.EnrolledCoursesDatabase;
import com.example.mobproject.db.FavouriteCoursesDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.example.mobproject.navigation.MenuDrawer;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class MyCoursesListActivity extends AppCompatActivity {
    private Context context;
    private String userId;
    private RecyclerView myCourseListRecyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_courses_list);

        context = this;
        myCourseListRecyclerView = findViewById(R.id.my_courses_list);
        UserInfo userInfo = new UserInfo(this);
        userId = userInfo.getUserId();

        MenuDrawer.actionBarInit(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fillCourses();
    }

    private void fillCourses() {
        EnrolledCoursesDatabase database = new EnrolledCoursesDatabase();
        database.getItems(userId, new Callback<Course>() {
            @Override
            public void OnFinish(ArrayList<Course> courseList) {
                myCourseListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                FavouriteCoursesDatabase favouriteCoursesDatabase = new FavouriteCoursesDatabase();
                favouriteCoursesDatabase.getItems(userId, new Callback<DocumentReference>() {
                    @Override
                    public void OnFinish(ArrayList<DocumentReference> favouriteList) {
                        MyCoursesAdapter adapter = new MyCoursesAdapter(context, courseList,
                                favouriteList, userId);
                        myCourseListRecyclerView.setAdapter(adapter);
                    }
                });
            }
        });
    }

    public void onBackPressed() {
        MenuDrawer.onBackHandler();
    }
}
