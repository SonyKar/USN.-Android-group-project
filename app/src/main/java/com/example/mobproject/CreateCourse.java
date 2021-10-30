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

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateCourse extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{


    private TextView startDate, endDate;
    private EditText createCourseName, createCoursePrice, description;
    private boolean validCreateCourseName, validCreateCoursePrice, validCreateCourseDates,
            validCreateCourseDifficulty, validCreateCourseDays, validCreateCourseDescription;
    private Spinner categorySpinner;
    private ImageButton startDateBtn, endDateBtn, backBtn;
    private Integer StartEndContor, checkedID;
    private String isEditString;
    private ArrayList<Integer> daysChecked = new ArrayList<Integer>();
    private Button createCourse;
    private Date date1, date2;
    private RadioGroup difficulty;
    private RadioButton selectedDifficulty;
    private CheckBox Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
    int isEdit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_course);

        createCourseName = (EditText) findViewById(R.id.create_course_name);
        createCoursePrice = (EditText) findViewById(R.id.create_course_price);
        categorySpinner = (Spinner) findViewById(R.id.category_spn);
        startDateBtn = (ImageButton) findViewById(R.id.start_date_btn);
        endDateBtn = (ImageButton) findViewById(R.id.end_date_btn);
        startDate = (TextView) findViewById(R.id.start_date);
        endDate = (TextView) findViewById(R.id.end_date);
        createCourse = (Button) findViewById(R.id.create_course_btn);
        difficulty = (RadioGroup) findViewById(R.id.difficulty_radgr);
        Monday = (CheckBox) findViewById(R.id.monday_chck);
        Tuesday = (CheckBox) findViewById(R.id.tuesday_chck);
        Wednesday = (CheckBox) findViewById(R.id.wedn_chck);
        Thursday = (CheckBox) findViewById(R.id.thurs_chck);
        Friday = (CheckBox) findViewById(R.id.friday_chck);
        Saturday = (CheckBox) findViewById(R.id.saturday_chck);
        description = (EditText) findViewById(R.id.description_box);
        backBtn = (ImageButton) findViewById(R.id.back_to_main_btn);


        Intent intent = getIntent();
        isEdit = intent.getIntExtra("EDIT_COURSE", 0);

        if(isEdit == 1){
            //get fields from DB
            createCourseName.setText("myName");//it works
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMain();
            }
        });


        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartEndContor = 1;
                showDatePickerDialog();
            }
        });

        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartEndContor = 2;
                showDatePickerDialog();

            }
        });



        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(CreateCourse.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.categories));
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(categoriesAdapter);


        createCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCourseValidation();
            }
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
        if(startDate.getText().toString().isEmpty()){
            startDate.setError(getResources().getString(R.string.start_date_error));
            //Toast.makeText(getApplicationContext(), R.string.chronology_error, Toast.LENGTH_SHORT).show();
            validCreateCourseDates = false;
        }
        //validate End Date
       else
           if (endDate.getText().toString().isEmpty()) {
                endDate.setError(getResources().getString(R.string.end_date_error));
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
        if(difficulty.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getApplicationContext(), R.string.difficulty_error, Toast.LENGTH_SHORT).show();
            validCreateCourseDifficulty = false;
        }
        else
        {
            validCreateCourseDifficulty = true;
            // get selected radio button from radioGroup
            int selectedId = difficulty.getCheckedRadioButtonId();

            // find the radiobutton by returned id

            selectedDifficulty = (RadioButton)findViewById(selectedId);
           // Toast.makeText(getApplicationContext(), selectedDifficulty.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();
        }


        //validate Days
        //get Days
        if(Monday.isChecked()) {
            checkedID = 1;
            daysChecked.add(checkedID);
        }
        else if(Tuesday.isChecked()){
            checkedID = 2;
            daysChecked.add(checkedID);
        }
        else if(Wednesday.isChecked()){
            checkedID = 3;
            daysChecked.add(checkedID);
        }
        else if(Thursday.isChecked()){
            checkedID = 4;
            daysChecked.add(checkedID);
        }
        else if(Friday.isChecked()){
            checkedID = 5;
            daysChecked.add(checkedID);
        }
        else if(Tuesday.isChecked()){
            checkedID = 6;
            daysChecked.add(checkedID);
        }

        if(daysChecked.size() > 0)
            validCreateCourseDays = true;
        else {
            Toast.makeText(getApplicationContext(), R.string.days_error, Toast.LENGTH_SHORT).show();
            validCreateCourseDays = false;
        }


        //validate Description
        if(description.getText().toString().isEmpty()){
            description.setError(getResources().getString(R.string.description_error));
            validCreateCourseDescription = false;
        }
        else
            validCreateCourseDescription = true;

        //validate
        if(validCreateCourseName && validCreateCoursePrice && validCreateCourseDates && validCreateCourseDifficulty
        && validCreateCourseDays && validCreateCourseDescription)
            Toast.makeText(getApplicationContext(), R.string.course_created_mess, Toast.LENGTH_SHORT).show();

    }






    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        R.style.DatePicker,
                        this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    );
        datePickerDialog.show();


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + month + "/" + year;
        if(StartEndContor == 1){ //start date
            try {
                date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            startDate.setText(date);
        }


        else
            if(StartEndContor == 2){//end date
                try {
                    date2 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                endDate.setText(date);
            }


    }

}
