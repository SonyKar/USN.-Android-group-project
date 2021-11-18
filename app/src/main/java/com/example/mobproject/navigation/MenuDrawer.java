package com.example.mobproject.navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mobproject.CourseListActivity;
import com.example.mobproject.CreateCourseActivity;
import com.example.mobproject.FavouriteListActivity;
import com.example.mobproject.LoginActivity;
import com.example.mobproject.MyCoursesListActivity;
import com.example.mobproject.R;
import com.example.mobproject.UserProfileActivity;
import com.example.mobproject.constants.Other;
import com.example.mobproject.constants.UserInfo;
import com.example.mobproject.db.UserDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MenuDrawer {

    private static DrawerLayout drawer;

    public static void actionBarInit(AppCompatActivity view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        view.setSupportActionBar(toolbar);

        NavigationView navDrawer = view.findViewById(R.id.nav_view);
        setupDrawerContent(navDrawer, view.getApplicationContext(), drawer);

        drawer = view.findViewById(R.id.drawer_layout);
        UserInfo userInfo = new UserInfo(view);
        UserDatabase userDatabase = new UserDatabase();
        userDatabase.getItem(userInfo.getUserId(), new Callback<User>() {
            @Override
            public void OnFinish(ArrayList<User> arrayList) {
                User user = arrayList.get(0);

                View tmp = navDrawer.getHeaderView(0);
                TextView textView = tmp.findViewById(R.id.nav_name);
                textView.setText(user.getName());
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(view,
                drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public static void onBackHandler() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public static void setupDrawerContent(NavigationView navigationView, Context context, DrawerLayout drawer) {
        final int PROFILE = R.id.nav_profile;
        final int COURSES = R.id.nav_courses;
        final int ENROLLED_COURSES = R.id.nav_my_courses;
        final int FAVOURITES_COURSES = R.id.nav_fav;
        final int CREATE_COURSE = R.id.nav_create_course;
        final int LOG_OUT = R.id.nav_log_out;

        SharedPreferences sharedPreferences = context.getSharedPreferences(Other.sharedPrefFile, Context.MODE_PRIVATE);

        if (sharedPreferences.getString(Other.SHARED_PREF_USERTYPE, Other.SHARED_PREF_NODATA).equals("0"))

        navigationView.getMenu().findItem(R.id.nav_create_course).setVisible(false);


        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    switch(menuItem.getItemId()){
                        case PROFILE:
                            Intent toProfile = new Intent(context, UserProfileActivity.class);
                            toProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(toProfile);
                            break;
                        case COURSES:
                            Intent toCourseList = new Intent(context, CourseListActivity.class);
                            toCourseList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(toCourseList);
                            break;
                        case ENROLLED_COURSES:
                            Intent toMyCourses = new Intent(context, MyCoursesListActivity.class);
                            toMyCourses.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(toMyCourses);
                            break;
                        case FAVOURITES_COURSES:
                            Intent toFavourites = new Intent(context, FavouriteListActivity.class);
                            toFavourites.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(toFavourites);
                            break;
                        case CREATE_COURSE:
                            Intent toCreateCourse = new Intent(context, CreateCourseActivity.class);
                            toCreateCourse.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(toCreateCourse);
                            break;
                        case LOG_OUT:
                            FirebaseAuth.getInstance().signOut();
                            Intent toLogin = new Intent(context, LoginActivity.class);
                            toLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(toLogin);
//                            TODO finish() ?!
                            break;
                        default:
                            drawer.closeDrawer(GravityCompat.START);
                    }

                    return true;
                });
    }
}
