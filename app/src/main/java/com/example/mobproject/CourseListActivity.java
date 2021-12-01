package com.example.mobproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobproject.adapters.CourseAdapter;
import com.example.mobproject.constants.UserInfo;
import com.example.mobproject.controllers.CourseSort;
import com.example.mobproject.db.CourseDatabase;
import com.example.mobproject.db.Database;
import com.example.mobproject.db.FavouriteCoursesDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.example.mobproject.navigation.MenuDrawer;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.firestore.DocumentReference;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

public class CourseListActivity extends AppCompatActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch enrollSwitch;
    private Spinner categoryFilter;
    private final ArrayList<Integer> difficultyChecked = new ArrayList<>();
    private CheckBox Beginner, Intermediate, Advanced;
    private RangeSlider priceRange;

    private String userId;
    Context activityContext;

    private ArrayList<Course> courseList;
    private ArrayList<DocumentReference> favouriteList;
    RecyclerView courseListRecyclerView;

    @SuppressLint("ResourceAsColor")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_list);

        MenuDrawer.actionBarInit(this);
        UserInfo userInfo = new UserInfo(this);
        userId = userInfo.getUserId();
        MenuDrawer.actionBarInit(this);

        FloatingActionButton filterBtn = findViewById(R.id.filter_fab);
        filterBtn.setOnClickListener(view -> showFilterDialog());
    }

    @Override
    protected void onStart() {
        super.onStart();
        fillCourses();
    }

    private void fillCourses() {
        activityContext = this;
        Callback<Course> recyclerViewCallback = new Callback<Course>() {
            @Override
            public void OnFinish(ArrayList<Course> coursesList) {
                courseList = coursesList;
                courseListRecyclerView = findViewById(R.id.course_list);
                courseListRecyclerView.setLayoutManager(new LinearLayoutManager(activityContext));
                FavouriteCoursesDatabase favouriteCoursesDatabase = new FavouriteCoursesDatabase();
                favouriteCoursesDatabase.getItems(userId, new Callback<DocumentReference>() {
                    @Override
                    public void OnFinish(ArrayList<DocumentReference> arrayList) {
                        favouriteList = arrayList;
                        CourseAdapter adapter = new CourseAdapter(activityContext, courseList, favouriteList, userId);
                        courseListRecyclerView.setAdapter(adapter);
                        sortBarInit();
                    }
                });
            }
        };

        Database<Course> database = new CourseDatabase();
        database.getItems(recyclerViewCallback);
    }

    private void sortBarInit() {
        Spinner sortingCategory = (Spinner) findViewById(R.id.sort_spn);
        sortingCategory.getBackground().setColorFilter(getResources().getColor(R.color.primary_dark_purple), PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter<CharSequence> categoriesAdapter = ArrayAdapter.createFromResource(CourseListActivity.this,
                R.array.sorting_criteria,
                R.layout.spinner_dropdwon_layout);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortingCategory.setAdapter(categoriesAdapter);

        sortingCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                CourseSort courseSort = new CourseSort();

                if (courseList != null) {
                    switch (position) {
                        case 0:
                            courseList = courseSort.sortByName(courseList, true);
                            break;
                        case 1:
                            courseList = courseSort.sortByName(courseList, false);
                            break;
                        case 2:
                            courseList = courseSort.sortByPrice(courseList, true);
                            break;
                        case 3:
                            courseList = courseSort.sortByPrice(courseList, false);
                            break;
                        case 4:
                            courseList = courseSort.sortByRating(courseList, true);
                            break;
                        case 5:
                            courseList = courseSort.sortByRating(courseList, false);
                            break;
                        case 6:
                            courseList = courseSort.sortByTime(courseList, true);
                            break;
                        case 7:
                            courseList = courseSort.sortByTime(courseList, false);
                            break;
                    }
                    CourseAdapter adapter = new CourseAdapter(activityContext, courseList, favouriteList, userId);
                    courseListRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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

    public void onBackPressed(){
        MenuDrawer.onBackHandler();
    }
}
