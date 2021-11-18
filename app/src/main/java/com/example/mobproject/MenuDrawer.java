package com.example.mobproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mobproject.constants.Other;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MenuDrawer {

    public static void setupDrawerContent(NavigationView navigationView, Context context, DrawerLayout drawer) {
        final int PROFILE = R.id.nav_profile;
        final int COURSES = R.id.nav_courses;
        final int ENROLLED_COURSES = R.id.nav_my_courses;
        final int FAVOURITES_COURSES = R.id.nav_fav;
        final int CREATE_COURSE = R.id.nav_create_course;
        final int LOG_OUT = R.id.nav_log_out;

        SharedPreferences sharedPreferences = context.getSharedPreferences(Other.sharedPrefFile, Context.MODE_PRIVATE);

        Log.d("check menu drawer", sharedPreferences.getString(Other.SHARED_PREF_USERTYPE, Other.SHARED_PREF_NODATA));
        if (sharedPreferences.getString(Other.SHARED_PREF_USERTYPE, Other.SHARED_PREF_NODATA).equals("0"))

        navigationView.getMenu().findItem(R.id.nav_create_course).setVisible(false);


        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    switch(menuItem.getItemId()){
                        case PROFILE:
                            Toast.makeText(context, "Profile clicked", Toast.LENGTH_SHORT).show();
                            Intent toProfile = new Intent(context, UserProfile.class);
                            toProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(toProfile);
                            break;
                        case COURSES:
                            Intent toCourseList = new Intent(context, CourseList.class);
                            toCourseList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(toCourseList);
                            break;
                        case ENROLLED_COURSES:
                            Toast.makeText(context, "MyCourses clicked", Toast.LENGTH_SHORT).show();
                            Intent toMyCourses = new Intent(context, MyCoursesList.class);
                            toMyCourses.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(toMyCourses);
                            break;
                        case FAVOURITES_COURSES:
                            //Toast.makeText(context, "Favourites clicked", Toast.LENGTH_SHORT).show();
                            Intent toFavourites = new Intent(context, FavouriteList.class);
                            toFavourites.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(toFavourites);
                            break;
                        case CREATE_COURSE:
                            Intent toCreateCourse = new Intent(context, CreateCourse.class);
                            toCreateCourse.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(toCreateCourse);
                            break;
                        case LOG_OUT:
                            FirebaseAuth.getInstance().signOut();
                            Intent toLogin = new Intent(context, LoginActivity.class);
                            toLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(toLogin);
                            break;
                        default:
                            drawer.closeDrawer(GravityCompat.START);
                    }

                    return true;//the item was selected
                });
    }
}
