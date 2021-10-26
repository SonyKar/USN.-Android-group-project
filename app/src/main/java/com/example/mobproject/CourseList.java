package com.example.mobproject;

import static com.example.mobproject.R.menu.menu_search;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class CourseList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ArrayAdapter<String> arrayAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //arrayAdapter = new ArrayAdapter<String>(this, //add list layout);
        //listView.setAdapter(arrayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Find a course");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //arrayAdapter.getFilter().filter(newText);//looks for options in the course array



                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){

            case R.id.nav_profile:
                Toast.makeText(getApplicationContext(), "Profile clicked", Toast.LENGTH_SHORT).show();
               /* Intent toProfile = new Intent(this, Profile.class);
                startActivity(toProfile);*/
                break;
            case R.id.nav_courses:
                Intent toCourseList = new Intent(this, CourseList.class);
                startActivity(toCourseList);
                break;
            case R.id.nav_my_courses:
                Toast.makeText(getApplicationContext(), "MyCourses clicked", Toast.LENGTH_SHORT).show();
                /*Intent toMyCourses = new Intent(this, MyCourses.class);
                startActivity(toMyCourses);*/
                break;
            case R.id.nav_fav:
                Toast.makeText(getApplicationContext(), "Favourites clicked", Toast.LENGTH_SHORT).show();
               /* Intent toFavourites = new Intent(this, Favourites.class);
                startActivity(toFavourites);*/
                break;
            case R.id.nav_create_course:
                Intent toCreateCourse = new Intent(this, CreateCourse.class);
                startActivity(toCreateCourse);
                break;
            case R.id.nav_log_out:
                Intent toLogin = new Intent(this, LoginActivity.class);
                startActivity(toLogin);
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;//the item was selected
    }
}
