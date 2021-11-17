package com.example.mobproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MenuDrawer {



    public static void setupDrawerContent(NavigationView navigationView, Context context, DrawerLayout drawer) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    switch(menuItem.getItemId()){

                        case R.id.nav_profile:
                            Toast.makeText(context, "Profile clicked", Toast.LENGTH_SHORT).show();
                            Intent toProfile = new Intent(context, UserProfile.class);
                            context.startActivity(toProfile);

                            break;
                        case R.id.nav_courses:
                            Intent toCourseList = new Intent(context, CourseList.class);
                            context.startActivity(toCourseList);
                            break;
                        case R.id.nav_my_courses:
                            Toast.makeText(context, "MyCourses clicked", Toast.LENGTH_SHORT).show();
//                Intent toMyCourses = new Intent(this, MyCourses.class);
//                startActivity(toMyCourses);
//                finish();
                            break;
                        case R.id.nav_fav:
                            Toast.makeText(context, "Favourites clicked", Toast.LENGTH_SHORT).show();
                            Intent toFavourites = new Intent(context, FavouriteList.class);
                            context.startActivity(toFavourites);

                            break;
                        case R.id.nav_create_course:
                            Intent toCreateCourse = new Intent(context, CreateCourse.class);
                            context.startActivity(toCreateCourse);
                            break;
                        case R.id.nav_log_out:
                            FirebaseAuth.getInstance().signOut();
                            Intent toLogin = new Intent(context, LoginActivity.class);
                            context.startActivity(toLogin);
                            break;

                    }

                    drawer.closeDrawer(GravityCompat.START);
                    return true;//the item was selected

                });
    }

//    private static void selectDrawerItem(MenuItem menuItem) {
//        Fragment fragment = null;
//        Class fragmentClass;
//
//        switch(menuItem.getItemId()) {
//            case R.id.nav_profile:
//                fragmentClass = UserProfile.class;
//                break;
//            case R.id.nav_courses:
//                fragmentClass = CourseList.class;
//                break;
//            case R.id.nav_fav:
//                //fragmentClass = .class;
//                break;
//            case R.id.nav_create_course:
//                fragmentClass = CreateCourse.class;
//                break;
//            case R.id.nav_log_out:
//                FirebaseAuth.getInstance().signOut();
//                fragmentClass = LoginActivity.class;
//                break;
//        }
//
//        try {
//            fragment = (Fragment) Class.newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.fragment_container, new Fragment()).commit();
//    }


//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch(item.getItemId()){
//
//            case R.id.nav_profile:
//                Toast.makeText(getApplicationContext(), "Profile clicked", Toast.LENGTH_SHORT).show();
//                Intent toProfile = new Intent(this, UserProfile.class);
//                startActivity(toProfile);
//
//                break;
//            case R.id.nav_courses:
//                Intent toCourseList = new Intent(this, CourseList.class);
//                startActivity(toCourseList);
//                startActivity(toCourseList);
//                break;
//            case R.id.nav_my_courses:
//                Toast.makeText(getApplicationContext(), "MyCourses clicked", Toast.LENGTH_SHORT).show();
////                Intent toMyCourses = new Intent(this, MyCourses.class);
////                startActivity(toMyCourses);
////                finish();
//                break;
//            case R.id.nav_fav:
//                Toast.makeText(getApplicationContext(), "Favourites clicked", Toast.LENGTH_SHORT).show();
////                Intent toFavourites = new Intent(this, Favourites.class);
////                startActivity(toFavourites);
////                finish();
//                break;
//            case R.id.nav_create_course:
//                Intent toCreateCourse = new Intent(this, CreateCourse.class);
//                startActivity(toCreateCourse);
//                break;
//            case R.id.nav_log_out:
//                FirebaseAuth.getInstance().signOut();
//                Intent toLogin = new Intent(this, LoginActivity.class);
//                startActivity(toLogin);
//                break;
//
//        }
//
//        drawer.closeDrawer(GravityCompat.START);
//        return true;//the item was selected*/
//    }

}
