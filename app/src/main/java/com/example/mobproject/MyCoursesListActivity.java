package com.example.mobproject;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobproject.adapters.FavouriteListAdapter;
import com.example.mobproject.adapters.MyCoursesAdapter;
import com.example.mobproject.constants.UserInfo;
import com.example.mobproject.db.CourseDatabase;
import com.example.mobproject.db.Database;
import com.example.mobproject.db.EnrolledCoursesDatabase;
import com.example.mobproject.db.FavouriteCoursesDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.example.mobproject.navigation.MenuDrawer;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class MyCoursesListActivity extends AppCompatActivity {
    private UserInfo userInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_courses_list);
        userInfo = new UserInfo(this);
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
//        Callback<Course> recyclerViewCallback = new Callback<Course>() {
//            @Override
//            public void OnFinish(ArrayList<Course> arrayList) {
//                RecyclerView myCourseListRecyclerView = findViewById(R.id.my_courses_list);
//                myCourseListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//                MyCoursesAdapter adapter = new MyCoursesAdapter(context, arrayList);
//                myCourseListRecyclerView.setAdapter(adapter);
//            }
//        };

        //TODO operatie pe cord deschis: scoate inima

        Callback<DocumentReference> recyclerViewCallback = new Callback<DocumentReference>() {
            @Override
            public void OnFinish(ArrayList<DocumentReference> favouriteReferences) {
                ArrayList<Course> enrolledCourseList = new ArrayList<>();
                CourseDatabase courseDatabase = new CourseDatabase();
                Callback<Course> courseCallback = new Callback<Course>() {
                    @Override
                    public void OnFinish(ArrayList<Course> arrayList) {
                        enrolledCourseList.add(arrayList.get(0));
                        if (enrolledCourseList.size() == favouriteReferences.size()) {
                            RecyclerView myCourseListRecyclerView = findViewById(R.id.my_courses_list);
                            myCourseListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                            MyCoursesAdapter adapter = new MyCoursesAdapter(context, arrayList);
                            myCourseListRecyclerView.setAdapter(adapter);
                        }
                    }
                };

                for (DocumentReference courseRef : favouriteReferences) {
                    courseDatabase.getItem(courseRef.getId(), courseCallback);
                }
            }
        };
        EnrolledCoursesDatabase database = new EnrolledCoursesDatabase();
        database.getItems(userId, recyclerViewCallback);

    }

    public void onBackPressed(){
        MenuDrawer.onBackHandler();
    }
}
