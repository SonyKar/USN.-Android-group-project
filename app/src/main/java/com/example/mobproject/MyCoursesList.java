package com.example.mobproject;

import static com.example.mobproject.MenuDrawer.setupDrawerContent;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobproject.db.CourseDatabase;
import com.example.mobproject.db.Database;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MyCoursesList extends AppCompatActivity {

    private DrawerLayout drawer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_courses_list);

        drawer = findViewById(R.id.my_courses_drawer_layout);

        fillCourses();

        actionBarInit();
    }

    private void actionBarInit() {
        Toolbar toolbar = findViewById(R.id.my_courses_toolbar);
        setSupportActionBar(toolbar);


        NavigationView navDrawer = findViewById(R.id.nav_view);
        setupDrawerContent(navDrawer, getApplicationContext(), drawer);

//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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

        Database<Course> database = new CourseDatabase();
        database.getItems(recyclerViewCallback);
    }


    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }

    }
}
