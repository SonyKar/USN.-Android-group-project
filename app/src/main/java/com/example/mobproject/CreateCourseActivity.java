package com.example.mobproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.constants.Intents;
import com.example.mobproject.constants.Other;
import com.example.mobproject.constants.UserInfo;
import com.example.mobproject.db.CategoryDatabase;
import com.example.mobproject.db.CourseDatabase;
import com.example.mobproject.db.Database;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Category;
import com.example.mobproject.models.Course;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateCourseActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView startDateTxv, endDateTxv;
    private EditText createCourseName, createCoursePrice, descriptionEdt;
    private Spinner categorySpinner;
    private RadioGroup difficultyGroup;
    private Button createCourse;
    private boolean isValid = true;
    private Course courseInfo = null;
    private final List<Integer> daysChecked = new ArrayList<>();
    private Date startDate, endDate;
    private int startEndDateType;
    private final ArrayList<CheckBox> meetDaysCheckboxes = new ArrayList<>();
    private int isEdit;
    String courseId;
    private UserInfo userInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_course);

        createCourseName = findViewById(R.id.create_course_name);
        createCoursePrice = findViewById(R.id.create_course_price);
        categorySpinner = findViewById(R.id.category_spn);
        ImageButton startDateBtn = findViewById(R.id.start_date_btn);
        ImageButton endDateBtn = findViewById(R.id.end_date_btn);
        startDateTxv = findViewById(R.id.start_date);
        endDateTxv = findViewById(R.id.end_date);
        createCourse = findViewById(R.id.create_course_btn);
        difficultyGroup = findViewById(R.id.difficulty_radio_group);
        CheckBox monday = findViewById(R.id.monday_checkbox);
        CheckBox tuesday = findViewById(R.id.tuesday_checkbox);
        CheckBox wednesday = findViewById(R.id.wednesday_checkbox);
        CheckBox thursday = findViewById(R.id.thursday_checkbox);
        CheckBox friday = findViewById(R.id.friday_checkbox);
        CheckBox saturday = findViewById(R.id.saturday_checkbox);
        descriptionEdt = findViewById(R.id.description_box);
        ImageButton backBtn = findViewById(R.id.back_to_main_btn);

        userInfo = new UserInfo(this);

        meetDaysCheckboxes.addAll(Arrays.asList(monday, tuesday, wednesday, thursday, friday, saturday));

        Intent intent = getIntent();
        isEdit = intent.getIntExtra(Intents.EDIT_TYPE, Other.CREATE_MODE);

        if (isEdit == Other.EDIT_MODE) {
            courseId = intent.getStringExtra(Intents.COURSE_ID);
            Database<Course> courseDatabase = new CourseDatabase();
            if (courseId != null)
                courseDatabase.getItem(courseId, initEditCourseCallback);
        } else {
            Database<Category> categoryDatabase = new CategoryDatabase();
            categoryDatabase.getItems(spinnerInitCallback);
        }

        backBtn.setOnClickListener(view -> goBack());
        startDateBtn.setOnClickListener(view -> showDatePickerDialog(startDate, Other.START_DATE));
        endDateBtn.setOnClickListener(view -> showDatePickerDialog(endDate, Other.END_DATE));

        //Save Button Listener
        createCourse.setOnClickListener(saveButtonClickHandler);
    }

    private void goBack() {
        if (isEdit == Other.CREATE_MODE) {
            Intent backToMain = new Intent(this, CourseListActivity.class);
            startActivity(backToMain);
        }
        finish();
    }

    private void createCourseValidation() {
        isValid = true;

        //validate course Name
        if (createCourseName.getText().toString().trim().isEmpty()) {
            createCourseName.setError(getResources().getString(R.string.first_name_error));
            isValid = false;
        }

        //validate course Price
        if (createCoursePrice.getText().toString().trim().isEmpty()) {
            createCoursePrice.setError(getResources().getString(R.string.price_error));
            isValid = false;
        } else {
            double price = Double.parseDouble(createCoursePrice.getText().toString());
            if (price < 0 || price > 999.99) {
                createCoursePrice.setError(getResources().getString(R.string.price_range));
                isValid = false;
            }
        }

        //validate Start Date
        if (startDateTxv.getText().toString().trim().isEmpty()) {
            startDateTxv.setError(getResources().getString(R.string.start_date_error));
            isValid = false;
        }

        //validate End Date
        if (endDateTxv.getText().toString().trim().isEmpty()) {
            endDateTxv.setError(getResources().getString(R.string.end_date_error));
            isValid = false;
        }

        //validate Date chronology
        if (startDate.after(endDate) || startDate.equals(endDate)) {
            Toast.makeText(getApplicationContext(), R.string.chronology_error, Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        //validate Difficulty
        if (difficultyGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), R.string.difficulty_error, Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        //validate Days
        for (int dayNumber = 0; dayNumber < meetDaysCheckboxes.size(); dayNumber++) {
            if (meetDaysCheckboxes.get(dayNumber).isChecked()) {
                daysChecked.add(dayNumber);
            }
        }

        if (daysChecked.size() <= 0) {
            Toast.makeText(getApplicationContext(), R.string.days_error, Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        //validate Description
        if (descriptionEdt.getText().toString().trim().isEmpty()) {
            descriptionEdt.setError(getResources().getString(R.string.description_error));
            isValid = false;
        }
    }

    private void showDatePickerDialog(Date date, int dateType) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        R.style.DatePicker,
                        this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );


        startEndDateType = dateType;
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dateString = dayOfMonth + "/" + (month + 1) + "/" + year;
        Date date = null;
        try {
            date = new SimpleDateFormat(getResources().getString(R.string.date_format_2), Locale.getDefault()).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            if (startEndDateType == Other.START_DATE) { //start date
                startDate = date;
                startDateTxv.setText(dateString);
            } else if (startEndDateType == Other.END_DATE) { //end date
                endDate = date;
                endDateTxv.setText(dateString);
            }
        }

    }

    private final Callback<Course> initEditCourseCallback = new Callback<Course>() {
        @Override
        public void OnFinish(ArrayList<Course> arrayList) {
            Course course = arrayList.get(0);
            courseInfo = course;
            createCourseName.setText(course.getName());
            createCoursePrice.setText(String.valueOf(course.getPrice()));

            Database<Category> categoryDatabase = new CategoryDatabase();
            categoryDatabase.getItems(spinnerInitCallback);

            int difficulty = course.getDifficulty();
            ((RadioButton) difficultyGroup.getChildAt(difficulty)).setChecked(true);

            SimpleDateFormat datePickerFormat = new SimpleDateFormat(getString(R.string.date_format_2), Locale.getDefault());

            //Getting start&end dates
            startDate = course.getStartDate();
            String startDateString = datePickerFormat.format(startDate);
            startDateTxv.setText(startDateString);
            endDate = course.getEndDate();
            String endDateString = datePickerFormat.format(endDate);
            endDateTxv.setText(endDateString);

            descriptionEdt.setText(course.getDescription());

            for (int day : course.getMeetDays()) {
                meetDaysCheckboxes.get(day).setChecked(true);
            }
        }
    };

    private final Callback<Category> spinnerInitCallback = new Callback<Category>() {
        @Override
        public void OnFinish(ArrayList<Category> categoryList) {
            Collections.sort(categoryList, (category, t1) -> category.getName().compareTo(t1.getName()));
            int categoryPosition = 0;
            if (courseInfo != null) {
                for (Category category : categoryList) {
                    if (category.getId().equals(courseInfo.getCategoryId().getId()))
                        categoryPosition = categoryList.indexOf(category);
                }
            }

            ArrayAdapter<Category> categoriesAdapter = new ArrayAdapter<>
                    (CreateCourseActivity.this,
                            android.R.layout.simple_list_item_1,
                            categoryList);
            categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(categoriesAdapter);
            categorySpinner.setSelection(categoryPosition);
        }
    };

    private final View.OnClickListener saveButtonClickHandler = view -> {
        createCourseValidation();

        if (isValid) {
            //disable register button
            createCourse.setEnabled(false);

            Category selectedCategory = (Category) categorySpinner.getSelectedItem();
            String spinnerCategoryId = selectedCategory.getId();
            DocumentReference docRefCategory = FirebaseFirestore.getInstance().
                    collection(DatabaseCollections.CATEGORIES_COLLECTION).
                    document(spinnerCategoryId);

            String courseName = createCourseName.getText().toString();
            double price = Double.parseDouble(String.valueOf(createCoursePrice.getText()));

            int radioBtnId = difficultyGroup.getCheckedRadioButtonId();
            RadioButton radioBtn = findViewById(radioBtnId);
            int difficultyId = difficultyGroup.indexOfChild(radioBtn);

            String ownerId = userInfo.getUserId();
            DocumentReference docRefOwner = FirebaseFirestore.getInstance().
                    collection(DatabaseCollections.USER_COLLECTION).
                    document(ownerId);

            String startDate = startDateTxv.getText().toString();
            String endDate = endDateTxv.getText().toString();
            String courseDesc = descriptionEdt.getText().toString();

            int rateCounter = 0, studentCounter = 0;
            double rating = 0;
            List<DocumentReference> comments = new ArrayList<>();

            if (courseInfo != null) {
                rateCounter = courseInfo.getRateCounter();
                studentCounter = courseInfo.getStudentCounter();
                rating = courseInfo.getRating();
                comments = courseInfo.getCommentsReferences();
            }

            Course course = null;
            try {
                course = new Course(courseName, docRefCategory, price, difficultyId,
                        docRefOwner, startDate, endDate, daysChecked, courseDesc, rateCounter,
                        studentCounter, rating, comments);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (course != null) {
                CourseDatabase courseDatabase = new CourseDatabase();
                if (isEdit == Other.CREATE_MODE) {
                    courseDatabase.insertItem(course);
                    userInfo.setUserCoursesNo(userInfo.getUserCoursesNo()+1);
                } else {
                    courseDatabase.updateItem(courseId, course);
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
            }
            goBack();
        }
    };
}
