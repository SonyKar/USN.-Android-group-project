package com.example.mobproject;

import static com.example.mobproject.navigation.MenuDrawer.setupDrawerContent;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobproject.adapters.FavouriteListAdapter;
import com.example.mobproject.constants.UserInfo;
import com.example.mobproject.db.CourseDatabase;
import com.example.mobproject.db.FavouriteCoursesDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Arrays;


public class FavouriteListActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private UserInfo userInfo;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_list);
        userInfo = new UserInfo(this);

        drawer = findViewById(R.id.fav_drawer_layout);

        fillCourses();

        actionBarInit();
    }

    private void actionBarInit() {
        Toolbar toolbar = findViewById(R.id.fav_toolbar);
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
        String userId = userInfo.getUserId();
        Callback<DocumentReference> recyclerViewCallback = new Callback<DocumentReference>() {
            @Override
            public void OnFinish(ArrayList<DocumentReference> favouriteReferences) {
                Log.d("faves", "fillCourses" + Arrays.toString(favouriteReferences.toArray()));
                ArrayList<Course> favouriteCourseList = new ArrayList<>();
                CourseDatabase courseDatabase = new CourseDatabase();
                Callback<Course> courseCallback = new Callback<Course>() {
                    @Override
                    public void OnFinish(ArrayList<Course> arrayList) {
                        favouriteCourseList.add(arrayList.get(0));
                        if (favouriteCourseList.size() == favouriteReferences.size()) {
                            RecyclerView favListRecyclerView = findViewById(R.id.fav_list);
                            favListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                            FavouriteListAdapter adapter = new FavouriteListAdapter(context, favouriteCourseList, userId);
                            favListRecyclerView.setAdapter(adapter);
                        }
                        Log.d("faves", "retrieved"+String.valueOf(arrayList.get(0)));
                    }
                };

                for(DocumentReference courseRef:favouriteReferences){
                    courseDatabase.getItem(courseRef.getId(), courseCallback);
                    Log.d("faves","getting courses" );
                }
            }
        };

        FavouriteCoursesDatabase database = new FavouriteCoursesDatabase();
        database.getItems(userId, recyclerViewCallback);
    }


    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }

    }

}
