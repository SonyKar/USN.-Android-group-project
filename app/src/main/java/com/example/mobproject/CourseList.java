package com.example.mobproject;

import static com.example.mobproject.R.menu.menu_search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class CourseList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ArrayAdapter<String> arrayAdapter;
//    private SearchView searchBar;
    private Spinner sortingCategory;
    private RecyclerView courseList;
    private CourseAdapter adapter;
    private ArrayList<String> items;
    private Button addToFav;



    @SuppressLint("ResourceAsColor")
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

        //search bar filter
//        searchBar = (SearchView) findViewById(R.id.search_bar);
//        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //arrayAdapter.getFilter().filter(newText);//looks for options in the course array
//                return false;
//            }
//        });

        //arrayAdapter = new ArrayAdapter<String>(this, //add list layout);
        //listView.setAdapter(arrayAdapter);

        sortingCategory = (Spinner) findViewById(R.id.sort_spn);
        sortingCategory.getBackground().setColorFilter(getResources().getColor(R.color.primary_dark_purple), PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter<CharSequence> categoriesAdapter = ArrayAdapter.createFromResource(CourseList.this,
                R.array.sorting_criteria,
                R.layout.spinner_dropdwon_layout);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortingCategory.setAdapter(categoriesAdapter);


        /*View v = sortingCategory.getSelectedView();
        ((TextView)v).setTextColor(Integer.parseInt("4E0D3A"));*/


        //create RecyclerView

        items = new ArrayList<>();
        items.add("Mathematics");
        items.add("Advanced English");
        items.add("Crypto");
        items.add("Spanish Literature");
        items.add("Emotional Intelligence");

        courseList = (RecyclerView) findViewById(R.id.course_list);
        courseList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(this, items);
        courseList.setAdapter(adapter);


       /* //add course to Favourites -> addedToFav variable for each course
        addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change color

                //get addedToFav from DB (is already in Favourites?)

                if(addedToFav == 0){
                    addedToFav = 1;//add to Favourites
                    addToFav.setImageResource(R.drawable.ic_favourite_purple);
                }
                else {
                    //is already in Favourites => delete from Favourites
                    addedToFav = 0;
                    addToFav.setImageResource(R.drawable.ic_favourite_red);
                }

            }
        });*/


    }

    //add Search Menu Item
    @Override
    /*public boolean onCreateOptionsMenu(Menu menu) {
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
    }*/

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
                Intent toProfile = new Intent(this, UserProfile.class);
                startActivity(toProfile);
                finish();
                break;
            case R.id.nav_courses:
                Intent toCourseList = new Intent(this, CourseList.class);
                startActivity(toCourseList);
                finish();
                break;
            case R.id.nav_my_courses:
                Toast.makeText(getApplicationContext(), "MyCourses clicked", Toast.LENGTH_SHORT).show();
                /*Intent toMyCourses = new Intent(this, MyCourses.class);
                startActivity(toMyCourses);
                finish();*/
                break;
            case R.id.nav_fav:
                Toast.makeText(getApplicationContext(), "Favourites clicked", Toast.LENGTH_SHORT).show();
               /* Intent toFavourites = new Intent(this, Favourites.class);
                startActivity(toFavourites);
                finish();*/
                break;
            case R.id.nav_create_course:
                Intent toCreateCourse = new Intent(this, CreateCourse.class);
                startActivity(toCreateCourse);
                finish();
                break;
            case R.id.nav_log_out:
                Intent toLogin = new Intent(this, LoginActivity.class);
                startActivity(toLogin);
                finish();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;//the item was selected
    }
}
