package com.example.mobproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText signupEmail, signupFirstName, signupLastName, signupPass, signupConfPass;
    Button register, btnToLogin;
    RadioGroup status;
    Boolean validSignUpEmail, validSignUpPassword, validSignUpFirstName, validSignUpLastName, validSignUpStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        signupFirstName = (EditText) findViewById(R.id.signup_fname);
        signupLastName = (EditText) findViewById(R.id.signup_lname);
        signupEmail = (EditText) findViewById(R.id.signup_email);
        signupPass = (EditText) findViewById(R.id.signup_password);
        signupConfPass = (EditText) findViewById(R.id.signup_conf_password);
        register = (Button) findViewById(R.id.signup_btn);
        status = (RadioGroup) findViewById(R.id.rad_status);
        btnToLogin = (Button) findViewById(R.id.btn_to_login);

        /* get selected radio button from radioGroup
        int selectedId = gender.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        selectedRadioButton = (RadioButton)findViewById(selectedId);
        Toast.makeText(getApplicationContext(), selectedRadioButton.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();*/

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupValidation();
            }
        });

        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void signupValidation(){

        Boolean passConfirm = signupPass.getText().toString().equals(
                signupConfPass.getText().toString());

        //validate signup FIRST NAME
        if(signupFirstName.getText().toString().isEmpty()){
            signupFirstName.setError(getResources().getString(R.string.first_name_error));
            validSignUpFirstName = false;
        }
        else
            validSignUpFirstName = true;

        //validate signup LAST NAME
        if(signupLastName.getText().toString().isEmpty()){
            signupLastName.setError(getResources().getString(R.string.last_name_error));
            validSignUpLastName = false;
        }
        else
            validSignUpLastName = true;

        //validate signup EMAIL ADDRESS
        if(signupEmail.getText().toString().isEmpty()){
            signupEmail.setError(getResources().getString(R.string.email_error));
            validSignUpEmail = false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(signupEmail.getText().toString()).matches()){
            signupEmail.setError(getResources().getString(R.string.error_invalid_email));
            validSignUpEmail = false;
        } else{
            validSignUpEmail = true;
        }

        //validate signup PASSWORD
        if(signupPass.getText().toString().isEmpty()){
            signupPass.setError(getResources().getString(R.string.password_error));
            validSignUpPassword = false;
        }
        else if(signupConfPass.getText().toString().isEmpty()){
            signupConfPass.setError(getResources().getString(R.string.conf_password_error));
            validSignUpPassword = false;
        }
        else if (!passConfirm) {
                signupConfPass.setError(getResources().getString(R.string.password_match_error));
                validSignUpPassword = false;
            }
        else
            validSignUpPassword = true;

        //validate STATUS
        if (status.getCheckedRadioButtonId() == -1)
        {
            // no radio buttons are checked
            Toast.makeText(getApplicationContext(),
                    R.string.status_error,
                    Toast.LENGTH_SHORT).show();
            validSignUpStatus = false;
        }
        else
        {
            // one of the radio buttons is checked
            validSignUpStatus = true;

            // get selected radio button from radioGroup
            int selectedStatusId = status.getCheckedRadioButtonId();

            // find the radiobutton by returned id

            //selectedStatus = (RadioButton)findViewById(selectedId);

        }


        if(validSignUpFirstName && validSignUpLastName && validSignUpEmail && validSignUpPassword
                && validSignUpStatus){
            Toast.makeText(getApplicationContext(),
                    "Successful Signup!!",
                    Toast.LENGTH_SHORT).show();

            //go to SignUpMessage
            switchToMessagePage();
        }
    }

    private void switchToMessagePage(){
        Intent toMessagePage = new Intent(this, SignUpMessage.class);
                startActivity(toMessagePage);
    }

}
