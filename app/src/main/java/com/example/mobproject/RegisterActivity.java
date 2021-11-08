package com.example.mobproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobproject.validations.UserValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText signupEmail, signupFirstName, signupLastName, signupPass, signupConfPass;
    Button register, btnToLogin;
    RadioGroup status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        signupFirstName = findViewById(R.id.signup_fname);
        signupLastName = findViewById(R.id.signup_lname);
        signupEmail = findViewById(R.id.signup_email);
        signupPass = findViewById(R.id.signup_password);
        signupConfPass = findViewById(R.id.signup_conf_password);
        register = findViewById(R.id.signup_btn);
        status = findViewById(R.id.rad_status);
        btnToLogin = findViewById(R.id.btn_to_login);

        /* get selected radio button from radioGroup
        int selectedId = gender.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        selectedRadioButton = (RadioButton)findViewById(selectedId);
        Toast.makeText(getApplicationContext(), selectedRadioButton.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();*/

        register.setOnClickListener(signupValidation);

        btnToLogin.setOnClickListener(view -> finish());
    }

    public View.OnClickListener signupValidation = view -> {
        Boolean validSignUpEmail = true, validSignUpPassword = true, validSignUpFirstName = true, validSignUpLastName = true, validSignUpStatus = true;
        Boolean passConfirm = signupPass.getText().toString().equals(
                signupConfPass.getText().toString());

        String fName = signupFirstName.getText().toString();
        String lName = signupLastName.getText().toString();
        String email = signupEmail.getText().toString();
        String password = signupPass.getText().toString();

        // validate signup FIRST NAME
        if(fName.isEmpty()){
            signupFirstName.setError(getResources().getString(R.string.first_name_error));
            validSignUpFirstName = false;
        }

        //validate signup LAST NAME
        if(lName.isEmpty()){
            signupLastName.setError(getResources().getString(R.string.last_name_error));
            validSignUpLastName = false;
        }

        //validate signup EMAIL ADDRESS
        if (email.isEmpty()) {
            signupEmail.setError(getResources().getString(R.string.email_error));
            validSignUpEmail = false;
        } else if (UserValidation.isEmail(email)) {
            signupEmail.setError(getResources().getString(R.string.error_invalid_email));
            validSignUpEmail = false;
        }

        //validate signup PASSWORD
        if(password.isEmpty()){
            signupPass.setError(getResources().getString(R.string.password_error));
            validSignUpPassword = false;
        } else if (!passConfirm) {
                signupConfPass.setError(getResources().getString(R.string.password_match_error));
                validSignUpPassword = false;
        }

        // TODO to make a radio button selected by default
        // validate STATUS
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

            // get selected radio button from radioGroup
            int selectedStatusId = status.getCheckedRadioButtonId();

            // find the radiobutton by returned id

            //selectedStatus = (RadioButton)findViewById(selectedId);

        }


        if(validSignUpFirstName && validSignUpLastName && validSignUpEmail && validSignUpPassword
                && validSignUpStatus) {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            switchToMessagePage();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Sign up", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    };

    private void switchToMessagePage(){
        Intent toMessagePage = new Intent(this, SignUpMessage.class);
                startActivity(toMessagePage);
    }

}
