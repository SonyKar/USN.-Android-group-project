package com.example.mobproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.models.Course;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

public class CourseList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Switch enrollSwitch;
    private Spinner categoryFilter;
    private final ArrayList<Integer> difficultyChecked = new ArrayList<>();
    private CheckBox Beginner, Intermediate, Advanced;
    private RangeSlider priceRange;
    final ArrayList<Course> courseList = new ArrayList<>();
    private SharedPreferences sharedPref;

    @SuppressLint("ResourceAsColor")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_list);

//        sharedPref = getApplicationContext().getSharedPreferences("com.example.mobproject", Context.MODE_PRIVATE);
//        Log.d("prefCheck", sharedPref.getString("userType","NoIdFound"));
        actionBarInit();
        sortBarInit();

        /*View v = sortingCategory.getSelectedView();
        ((TextView)v).setTextColor(Integer.parseInt("4E0D3A"));*/

        fillCourses();

        FloatingActionButton filterBtn = findViewById(R.id.filter_fab);
        filterBtn.setOnClickListener(view -> showFilterDialog());


    }

    private void fillCourses() {
        FirebaseFirestore.getInstance().collection(DatabaseCollections.COURSES_COLLECTION).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Course tmp = document.toObject(Course.class);
                    tmp.setId(document.getId());
                    courseList.add(tmp);
                }
                RecyclerView courseListRecyclerView = findViewById(R.id.course_list);
                courseListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                CourseAdapter adapter = new CourseAdapter(this, courseList);
                courseListRecyclerView.setAdapter(adapter);
            }
        });
    }

    private void sortBarInit() {
        Spinner sortingCategory = (Spinner) findViewById(R.id.sort_spn);
        sortingCategory.getBackground().setColorFilter(getResources().getColor(R.color.primary_dark_purple), PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter<CharSequence> categoriesAdapter = ArrayAdapter.createFromResource(CourseList.this,
                R.array.sorting_criteria,
                R.layout.spinner_dropdwon_layout);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortingCategory.setAdapter(categoriesAdapter);
    }

    private void actionBarInit() {
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
    }

    //generate filter drawer
    private void showFilterDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.filter_drawer);

        //switch button
        enrollSwitch = bottomSheetDialog.findViewById(R.id.enroll_switch);

        //category Spinner
        categoryFilter = bottomSheetDialog.findViewById(R.id.category_spn);
        //after APPLY button


        //take difficulty - CheckBox
        Beginner = (CheckBox) bottomSheetDialog.findViewById(R.id.beginner_checkbox);
        Intermediate = (CheckBox) bottomSheetDialog.findViewById(R.id.intermediate_checkbox);
        Advanced = (CheckBox) bottomSheetDialog.findViewById(R.id.advanced_checkbox);


        //price Range
        priceRange = bottomSheetDialog.findViewById(R.id.price_range);

        //add "$" label
        assert priceRange != null;
        priceRange.setLabelFormatter(value -> {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            currencyFormat.setCurrency(Currency.getInstance("USD"));

            return currencyFormat.format(value);
        });


        //APPLY changes button
        Button applyFilters = bottomSheetDialog.findViewById(R.id.apply_btn);
        assert applyFilters != null;
        applyFilters.setOnClickListener(view -> filterCourses());

        //RESET button
        Button resetFilters = bottomSheetDialog.findViewById(R.id.reset_btn);
        assert resetFilters != null;
        resetFilters.setOnClickListener(view -> resetFilters());

        bottomSheetDialog.show();
    }

    private void resetFilters() {
        //reset priceRange
        priceRange.setValues(0.0F, 6000.0F);

        //reset Category spinner
        categoryFilter.setSelection(0);

        //reset Difficulty checkbox
        Beginner.setChecked(false);
        Intermediate.setChecked(false);
        Advanced.setChecked(false);

        //reset Enroll Switch
        enrollSwitch.setChecked(false);

    }

    private void filterCourses() {
        //PRICE RANGE
        List<Float> priceValues = priceRange.getValues();
        float minPrice = Collections.min(priceValues);
        float maxPrice = Collections.max(priceValues);
        //send price values to DB


        //CATEGORY SPINNER
        String chosenCategory = categoryFilter.getSelectedItem().toString();
        //send to DB - when "ALL" => no filter

        //DIFFICULTY CHECKBOX
        int diffCheckedID;
        if(Beginner.isChecked()) {
            diffCheckedID = 1;
            difficultyChecked.add(diffCheckedID);
        }
        else if(Intermediate.isChecked()){
            diffCheckedID = 2;
            difficultyChecked.add(diffCheckedID);
        }
        else if(Advanced.isChecked()){
            diffCheckedID = 3;
            difficultyChecked.add(diffCheckedID);
        }
        //send  difficultyChecked array to DB

        //ENROLL SWITCH
        enrollSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            if(isChecked){
                //send "open to enroll courses"
                Toast.makeText(getApplicationContext(),
                        "SWITCH ON", Toast.LENGTH_SHORT).show();
            }
            else{
                //send "all courses""
                Toast.makeText(getApplicationContext(),
                        "SWITCH OFF", Toast.LENGTH_SHORT).show();
            }
        });

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
                FirebaseAuth.getInstance().signOut();
                Intent toLogin = new Intent(this, LoginActivity.class);
                startActivity(toLogin);
                finish();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;//the item was selected
    }
}
