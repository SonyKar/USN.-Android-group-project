package com.example.mobproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobproject.adapters.CourseAdapter;
import com.example.mobproject.constants.Difficulty;
import com.example.mobproject.constants.UserInfo;
import com.example.mobproject.controllers.CourseFilter;
import com.example.mobproject.controllers.CourseSort;
import com.example.mobproject.db.CategoryDatabase;
import com.example.mobproject.db.CourseDatabase;
import com.example.mobproject.db.Database;
import com.example.mobproject.db.FavouriteCoursesDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Category;
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
    private CheckBox Beginner, Intermediate, Advanced;
    private RangeSlider priceRange;
    private BottomSheetDialog bottomSheetDialog;
    private EditText searchbar;

    private String userId;
    private Context activityContext;

    private ArrayList<Course> initCourseList;
    private ArrayList<Course> filteredCourseList;
    private ArrayList<DocumentReference> favouriteList;
    private RecyclerView courseListRecyclerView;
    private int sortType;

    @SuppressLint("ResourceAsColor")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_list);

        searchbar = findViewById(R.id.search_bar);
        searchbar.setOnKeyListener((view, i, keyEvent) -> {
            filterCourses();
            return true;
        });

        MenuDrawer.actionBarInit(this);
        UserInfo userInfo = new UserInfo(this);
        userId = userInfo.getUserId();
        initFilterModal();

        FloatingActionButton filterBtn = findViewById(R.id.filter_fab);
        filterBtn.setOnClickListener(view -> bottomSheetDialog.show());
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
                initCourseList = coursesList;
                filteredCourseList = coursesList;
                courseListRecyclerView = findViewById(R.id.course_list);
                courseListRecyclerView.setLayoutManager(new LinearLayoutManager(activityContext));
                FavouriteCoursesDatabase favouriteCoursesDatabase = new FavouriteCoursesDatabase();
                favouriteCoursesDatabase.getItems(userId, new Callback<DocumentReference>() {
                    @Override
                    public void OnFinish(ArrayList<DocumentReference> arrayList) {
                        favouriteList = arrayList;
                        CourseAdapter adapter = new CourseAdapter(activityContext, filteredCourseList, favouriteList, userId);
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
        Spinner sortingCategory = findViewById(R.id.sort_spn);
        sortingCategory.getBackground().setColorFilter(getResources().getColor(R.color.primary_dark_purple), PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter<CharSequence> categoriesAdapter = ArrayAdapter.createFromResource(CourseListActivity.this,
                R.array.sorting_criteria,
                R.layout.spinner_dropdwon_layout);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortingCategory.setAdapter(categoriesAdapter);

        sortingCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                sortType = position;
                filteredCourseList = sortCourses(filteredCourseList);

                CourseAdapter adapter = new CourseAdapter(activityContext, filteredCourseList, favouriteList, userId);
                courseListRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //generate filter drawer
    private void initFilterModal() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.filter_drawer);

        enrollSwitch = bottomSheetDialog.findViewById(R.id.enroll_switch);
        categoryFilter = bottomSheetDialog.findViewById(R.id.category_spn);
        Beginner = bottomSheetDialog.findViewById(R.id.beginner_checkbox);
        Intermediate = bottomSheetDialog.findViewById(R.id.intermediate_checkbox);
        Advanced = bottomSheetDialog.findViewById(R.id.advanced_checkbox);
        priceRange = bottomSheetDialog.findViewById(R.id.price_range);
        Button applyFilters = bottomSheetDialog.findViewById(R.id.apply_btn);
        Button resetFilters = bottomSheetDialog.findViewById(R.id.reset_btn);

        enrollSwitch.setChecked(true);

        Callback<Category> spinnerCallback = new Callback<Category>() {
            @Override
            public void OnFinish(ArrayList<Category> categoryList) {
                Collections.sort(categoryList, (category, t1) -> category.getName().compareTo(t1.getName()));
                ArrayAdapter<Category> categoriesAdapter = new ArrayAdapter<>
                        (CourseListActivity.this,
                                R.layout.spinner_item_dark,
                                categoryList);
                categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categoryFilter.setAdapter(categoriesAdapter);
            }
        };
        Database<Category> categoryDatabase = new CategoryDatabase();
        categoryDatabase.getItems(spinnerCallback);

        //add "$" label
        assert priceRange != null;
        priceRange.setLabelFormatter(value -> {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            currencyFormat.setCurrency(Currency.getInstance("USD"));

            return currencyFormat.format(value);
        });

        assert applyFilters != null;
        applyFilters.setOnClickListener(view -> filterCourses());

        assert resetFilters != null;
        resetFilters.setOnClickListener(view -> resetFilters());
    }

    private void resetFilters() {
        //reset priceRange
        priceRange.setValues(priceRange.getValueFrom(), priceRange.getValueTo());

        //reset Category spinner
        categoryFilter.setSelection(0);

        //reset Difficulty checkbox
        Beginner.setChecked(false);
        Intermediate.setChecked(false);
        Advanced.setChecked(false);

        //reset Enroll Switch
        enrollSwitch.setChecked(true);

        filteredCourseList = initCourseList;
        CourseAdapter adapter = new CourseAdapter(activityContext, filteredCourseList, favouriteList, userId);
        courseListRecyclerView.setAdapter(adapter);

        bottomSheetDialog.hide();
    }

    private void filterCourses() {
        CourseFilter courseFilter = new CourseFilter();

        filteredCourseList = courseFilter.filterName(initCourseList, searchbar.getText().toString());

        //PRICE RANGE
        List<Float> priceValues = priceRange.getValues();
        float minPrice = Collections.min(priceValues);
        float maxPrice = Collections.max(priceValues);
        filteredCourseList = courseFilter.filterPrice(filteredCourseList, minPrice, maxPrice);

        // TODO read categories to spinner from db
        //CATEGORY SPINNER
        // String chosenCategory = categoryFilter.getSelectedItem().toString();
        //send to DB - when "ALL" => no filter

        //DIFFICULTY CHECKBOX
        ArrayList<Integer> difficulties = new ArrayList<>();
        if(Beginner.isChecked()) {
            difficulties.add(Difficulty.BEGINNER);
        }
        if(Intermediate.isChecked()){
            difficulties.add(Difficulty.INTERMEDIATE);
        }
        if(Advanced.isChecked()){
            difficulties.add(Difficulty.ADVANCED);
        }
        if (difficulties.isEmpty()) {
            difficulties.add(Difficulty.BEGINNER);
            difficulties.add(Difficulty.INTERMEDIATE);
            difficulties.add(Difficulty.ADVANCED);
        }
        filteredCourseList = courseFilter.filterDifficulty(filteredCourseList, difficulties);

        //ENROLL SWITCH
        filteredCourseList = courseFilter.filterEnroll(filteredCourseList, enrollSwitch.isChecked());

        sortCourses(filteredCourseList);

        CourseAdapter adapter = new CourseAdapter(activityContext, filteredCourseList, favouriteList, userId);
        courseListRecyclerView.setAdapter(adapter);

        bottomSheetDialog.hide();
    }

    private ArrayList<Course> sortCourses(ArrayList<Course> courseList) {
        if (courseList != null) {
            CourseSort courseSort = new CourseSort();
            switch (sortType) {
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
        }

        return courseList;
    }

    public void onBackPressed(){
        MenuDrawer.onBackHandler();
    }
}
