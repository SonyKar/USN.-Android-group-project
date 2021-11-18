package com.example.mobproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.mobproject.db.CategoryDatabase;
import com.example.mobproject.db.CourseDatabase;
import com.example.mobproject.db.Database;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Category;
import com.example.mobproject.models.Course;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CreateCourse extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    private TextView startDateTxv, endDateTxv;
    private EditText createCourseName, createCoursePrice, descriptionEdt;
    private boolean validCreateCourseName, validCreateCoursePrice, validCreateCourseDates,
            validCreateCourseDifficulty, validCreateCourseDays, validCreateCourseDescription;
    private Spinner categorySpinner;
    private ImageButton startDateBtn, endDateBtn, backBtn;
    private Integer StartEndContor, checkedID;
    private String isEditString;
    private List<Integer> daysChecked = new ArrayList<Integer>();
    private Button createCourse;
    private Date date1, date2;
    private Date startDate, endDate;
    private RadioGroup difficultyGroup;
    private RadioButton selectedDifficulty;
    private CheckBox Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
    private ArrayList<CheckBox>meetDaysCheckboxes = new ArrayList<>();
    int isEdit;
    String courseId;
    private ArrayList<Category> sortedCategoryList;
    private DocumentReference categoryId;
    int rateCounter=0;
    int studentCounter=0;
    double rating=0;
    private List <DocumentReference> comments;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_course);

        createCourseName = (EditText) findViewById(R.id.create_course_name);
        createCoursePrice = (EditText) findViewById(R.id.create_course_price);
        categorySpinner = (Spinner) findViewById(R.id.category_spn);
        startDateBtn = (ImageButton) findViewById(R.id.start_date_btn);
        endDateBtn = (ImageButton) findViewById(R.id.end_date_btn);
        startDateTxv = (TextView) findViewById(R.id.start_date);
        endDateTxv = (TextView) findViewById(R.id.end_date);
        createCourse = (Button) findViewById(R.id.create_course_btn);
        difficultyGroup = (RadioGroup) findViewById(R.id.difficulty_radgr);
        Monday = (CheckBox) findViewById(R.id.monday_chck);
        Tuesday = (CheckBox) findViewById(R.id.tuesday_chck);
        Wednesday = (CheckBox) findViewById(R.id.wedn_chck);
        Thursday = (CheckBox) findViewById(R.id.thurs_chck);
        Friday = (CheckBox) findViewById(R.id.friday_chck);
        Saturday = (CheckBox) findViewById(R.id.saturday_chck);
        descriptionEdt = (EditText) findViewById(R.id.description_box);
        backBtn = (ImageButton) findViewById(R.id.back_to_main_btn);

        meetDaysCheckboxes.addAll(Arrays.asList(Monday, Tuesday, Wednesday, Thursday, Friday, Saturday));
        DatePicker asd = new DatePicker(this);
        asd.init(2020, 11, 11, null);

        Intent intent = getIntent();
        isEdit = intent.getIntExtra("EDIT_COURSE", 0);

        if(isEdit == 1){
            //get fields from DB

            courseId = intent.getStringExtra("COURSE_ID");
            Database<Course> database = new CourseDatabase();
            if(courseId!=null)
                database.getItem(courseId, profileCallback);



        }

        backBtn.setOnClickListener(view -> backToMain());


        startDateBtn.setOnClickListener(view -> {
            StartEndContor = 1;
            showDatePickerDialog();
        });

        endDateBtn.setOnClickListener(view -> {
            StartEndContor = 2;
            showDatePickerDialog();

        });

        //TODO iar nu mere
        //Spinner Setup
        Context context = this;
        Callback<Category> spinnerCallback = new Callback<Category>() {
            @Override
            public void OnFinish(ArrayList<Category> categoryList) {
                Collections.sort(categoryList, (category, t1) -> category.getName().compareTo(t1.getName()));
                sortedCategoryList = categoryList;
                int categoryPosition =0;
                if(categoryId!=null)
                    for (Category category:sortedCategoryList)
                        if(category.getId().equals(categoryId.getId()))
                            categoryPosition = sortedCategoryList.indexOf(category);
                ArrayAdapter<Category> categoriesAdapter = new ArrayAdapter<>
                        (CreateCourse.this,
                                android.R.layout.simple_list_item_1,
                                categoryList);
                categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(categoriesAdapter);
                categorySpinner.setSelection(categoryPosition);
            }
        };
        Database<Category> categoryDatabase = new CategoryDatabase();
        categoryDatabase.getItems(spinnerCallback);


        //Save Button Listener
        createCourse.setOnClickListener(view -> {
            createCourseValidation();
            Category selectedCategory = (Category) categorySpinner.getSelectedItem();
            String categoryId = selectedCategory.getId();
            DocumentReference docRefCategory = FirebaseFirestore.getInstance().
                    collection(DatabaseCollections.CATEGORIES_COLLECTION).
                    document(categoryId);

            String courseName = createCourseName.getText().toString();
            double price = Double.parseDouble(String.valueOf(createCoursePrice.getText()));
            int radioBtnId = difficultyGroup.getCheckedRadioButtonId();
            RadioButton radioBtn = findViewById(radioBtnId);
            int difficultyId = difficultyGroup.indexOfChild(radioBtn);
            String ownerId = Objects.requireNonNull(FirebaseAuth.getInstance()
                    .getCurrentUser()).getUid();
            DocumentReference docRefOwner = FirebaseFirestore.getInstance().
                    collection(DatabaseCollections.USER_COLLECTION).
                    document(ownerId);
            String startDate = startDateTxv.getText().toString();
            String endDate = endDateTxv.getText().toString();
            String courseDesc = descriptionEdt.getText().toString();

            try {
                Course course = new Course(courseName, docRefCategory, price, difficultyId,
                            docRefOwner, startDate, endDate, daysChecked, courseDesc, rateCounter,
                            studentCounter, rating, comments);
                CourseDatabase courseDatabase = new CourseDatabase();
                if(isEdit == 0)
                    courseDatabase.insertItem(course);
                else
                    courseDatabase.updateItem(courseId,course);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            backToMain();
        });

    }

    private void backToMain() {
        if(isEdit == 1)
            finish();
        else{
            Intent backToMain = new Intent(this, CourseList.class);
            startActivity(backToMain);
            finish();
        }

    }

    private void createCourseValidation() {
        //validate course Name
        if(createCourseName.getText().toString().isEmpty()){
            createCourseName.setError(getResources().getString(R.string.first_name_error));
            validCreateCourseName = false;
        }
        else
            validCreateCourseName = true;

        //validate course Price
        if(createCoursePrice.getText().toString().isEmpty()){
            createCoursePrice.setError(getResources().getString(R.string.price_error));
            validCreateCoursePrice = false;
        }
        else
            validCreateCoursePrice = true;

        //validate Start Date
        if(startDateTxv.getText().toString().isEmpty()){
            startDateTxv.setError(getResources().getString(R.string.start_date_error));
            //Toast.makeText(getApplicationContext(), R.string.chronology_error, Toast.LENGTH_SHORT).show();
            validCreateCourseDates = false;
        }
        //validate End Date
       else
           if (endDateTxv.getText().toString().isEmpty()) {
                endDateTxv.setError(getResources().getString(R.string.end_date_error));
                //Toast.makeText(getApplicationContext(), R.string.chronology_error, Toast.LENGTH_SHORT).show();
                validCreateCourseDates = false;
            }

           //validate Date chronology

            else if(date1.after(date2) || date1.equals(date2)){
                //startDate.setError(getResources().getString(R.string.chronology_error));
                //endDate.setError(getResources().getString(R.string.chronology_error));
                Toast.makeText(getApplicationContext(), R.string.chronology_error, Toast.LENGTH_SHORT).show();
                validCreateCourseDates = false;
            }

            else
                validCreateCourseDates = true;

            //validate Difficulty
        if(difficultyGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getApplicationContext(), R.string.difficulty_error, Toast.LENGTH_SHORT).show();
            validCreateCourseDifficulty = false;
        }
        else
        {
            validCreateCourseDifficulty = true;
            // get selected radio button from radioGroup
            int selectedId = difficultyGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id

            selectedDifficulty = (RadioButton)findViewById(selectedId);
           // Toast.makeText(getApplicationContext(), selectedDifficulty.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();
        }


        //validate Days
        //get Days
        if(Monday.isChecked()) {
            checkedID = 0;
            daysChecked.add(checkedID);
        }
        if(Tuesday.isChecked()){
            checkedID = 1;
            daysChecked.add(checkedID);
        }
        if(Wednesday.isChecked()){
            checkedID = 2;
            daysChecked.add(checkedID);
        }
        if(Thursday.isChecked()){
            checkedID = 3;
            daysChecked.add(checkedID);
        }
        if(Friday.isChecked()){
            checkedID = 4;
            daysChecked.add(checkedID);
        }
        if(Saturday.isChecked()){
            checkedID = 5;
            daysChecked.add(checkedID);
        }

        Log.d("dayschecked", Arrays.toString(daysChecked.toArray()));
        if(daysChecked.size() > 0)
            validCreateCourseDays = true;
        else {
            Toast.makeText(getApplicationContext(), R.string.days_error, Toast.LENGTH_SHORT).show();
            validCreateCourseDays = false;
        }


        //validate Description
        if(descriptionEdt.getText().toString().isEmpty()){
            descriptionEdt.setError(getResources().getString(R.string.description_error));
            validCreateCourseDescription = false;
        }
        else
            validCreateCourseDescription = true;

        //validate
        if(validCreateCourseName && validCreateCoursePrice && validCreateCourseDates && validCreateCourseDifficulty
        && validCreateCourseDays && validCreateCourseDescription)
            Toast.makeText(getApplicationContext(), R.string.course_created_mess, Toast.LENGTH_SHORT).show();

    }



    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        if (StartEndContor == 1 && date1!=null) { // start date
            calendar.setTime(date1);
        } else
            if(date2!=null)
            {
            calendar.setTime(date2);
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

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
        if(StartEndContor == 1){ //start date
            try {
                date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            startDateTxv.setText(date);
        }

        else
            if(StartEndContor == 2){//end date
                try {
                    date2 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                endDateTxv.setText(date);
            }


    }

    private final Callback<Course> profileCallback = new Callback<Course>() {
        @Override
        public void OnFinish(ArrayList<Course> arrayList) {
            Course course = arrayList.get(0);
            createCourseName.setText(course.getName());
            createCoursePrice.setText(String.valueOf(course.getPrice()));
            categoryId = course.getCategoryId();
            rateCounter = course.getRateCounter();
            studentCounter = course.getStudentCounter();
            rating = course.getRating();
            comments = course.getCommentsReferences();
            int difficulty = course.getDifficulty();
            ((RadioButton)difficultyGroup.getChildAt(difficulty)) .setChecked(true);
            startDate = course.getStartDate();
            endDate = course.getEndDate();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            //Getting start&end dates
            date1 = startDate;
            String startDateString = format.format(date1);
            startDateTxv.setText(startDateString);
            date2 = endDate;
            String endDateString = format.format(date2);
            endDateTxv.setText(endDateString);

            descriptionEdt.setText(course.getDescription());

            for(int day : course.getMeetDays()){
                meetDaysCheckboxes.get(day).setChecked(true);
            }


            //TODO fix rating
//            String finalRatingString = String.valueOf(course.getRateCounter()) ;
//            finalRatingScore.setText(finalRatingString);
//            finalRating.setRating(course.getRateCounter());
        }
    };


}
