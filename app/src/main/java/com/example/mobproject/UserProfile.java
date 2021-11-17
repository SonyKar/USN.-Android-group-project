package com.example.mobproject;

import static com.example.mobproject.MenuDrawer.setupDrawerContent;
import static com.example.mobproject.R.menu.menu_search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.models.Course;
import com.example.mobproject.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class UserProfile extends AppCompatActivity
        //implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawer;
    private FloatingActionButton editProfile;
    private SharedPreferences sharedPref;
    private TextView userName, userStatus, userEmail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        drawer = findViewById(R.id.drawer_layout_profile);
        editProfile = (FloatingActionButton) findViewById(R.id.edit_profile_fab);

        actionBarInit();

        //change toggle color
        //toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.primary_dark_purple));

        //go to Edit Profile
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToEditProfile();

            }
        });


        userName = findViewById(R.id.profile_name);
        userStatus = findViewById(R.id.user_status);
        userEmail = findViewById(R.id.profile_email);


        //get User infos
        sharedPref = getApplicationContext().getSharedPreferences("com.example.mobproject", Context.MODE_PRIVATE);
        String userID = sharedPref.getString("id","NoIdFound");


        FirebaseFirestore.getInstance().collection(DatabaseCollections.USER_COLLECTION).document(userID).
                get().addOnSuccessListener(documentSnapshot -> {
                    User user =  documentSnapshot.toObject(User.class);
                    userName.setText(user.getName());
//                    userStatus.setText(user.getUserType());
                    userEmail.setText(user.getEmail());
        });

    }

    private void actionBarInit() {
        NavigationView navigationView = findViewById(R.id.nav_view_profile);
        setupDrawerContent(navigationView, getApplicationContext(), drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
    }

    private void switchToEditProfile() {
        Intent toEditProfile = new Intent(this, UserEdit.class);
        startActivity(toEditProfile);
    }

    //color menu item


    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }

    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch(item.getItemId()){
//
//            case R.id.nav_profile:
//                Toast.makeText(getApplicationContext(), "Profile clicked", Toast.LENGTH_SHORT).show();
//                Intent toProfile = new Intent(this, UserProfile.class);
//                startActivity(toProfile);
//                finish();
//                break;
//            case R.id.nav_courses:
//                Intent toCourseList = new Intent(this, CourseList.class);
//                startActivity(toCourseList);
//                finish();
//                break;
//            case R.id.nav_my_courses:
//                Toast.makeText(getApplicationContext(), "MyCourses clicked", Toast.LENGTH_SHORT).show();
//                /*Intent toMyCourses = new Intent(this, MyCourses.class);
//                startActivity(toMyCourses);
//                finish();*/
//                break;
//            case R.id.nav_fav:
//                Toast.makeText(getApplicationContext(), "Favourites clicked", Toast.LENGTH_SHORT).show();
//               /* Intent toFavourites = new Intent(this, Favourites.class);
//                startActivity(toFavourites);
//                finish();*/
//                break;
//            case R.id.nav_create_course:
//                Intent toCreateCourse = new Intent(this, CreateCourse.class);
//                startActivity(toCreateCourse);
//                finish();
//                break;
//            case R.id.nav_log_out:
//                Intent toLogin = new Intent(this, LoginActivity.class);
//                startActivity(toLogin);
//                finish();
//                break;
//
//        }
//
//        drawer.closeDrawer(GravityCompat.START);
//        return true;//the item was selected
//    }
}
