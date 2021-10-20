package com.example.mobproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

     Button loginBtn, forgotPass, btnToSignUp;
     EditText loginEmail, loginPass;
     String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
     boolean validLoginEmail, validLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginBtn = (Button) findViewById(R.id.login_btn);
        loginEmail = (EditText) findViewById(R.id.login_email);
        forgotPass = (Button) findViewById(R.id.btn_forgot_pass);
        btnToSignUp = (Button) findViewById(R.id.btn_tosignup);
        loginPass = findViewById(R.id.login_password);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginValidation();
            }
        });

        btnToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 switchToSignUp();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToNewPass();
            }
        });
    }

    private void loginValidation() {
        //Check login email address
        if(loginEmail.getText().toString().isEmpty()){
            loginEmail.setError(getResources().getString(R.string.email_error));
            validLoginEmail = false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(loginEmail.getText().toString()).matches()){
            loginEmail.setError(getResources().getString(R.string.error_invalid_email));
            validLoginEmail = false;
        } else{
            validLoginEmail = true;
        }

        //Check login password
        if(loginPass.getText().toString().isEmpty()){
            loginPass.setError(getResources().getString(R.string.password_error));
            validLoginPassword = false;
        }
        else
            validLoginPassword = true;

        if(validLoginEmail && validLoginPassword)
            Toast.makeText(getApplicationContext(),
                    "Successful Login!",
                    Toast.LENGTH_SHORT).show();
    }


    private void switchToSignUp(){
        Intent toSignUp = new Intent(this, RegisterActivity.class);
        startActivity(toSignUp);
    }

    private void switchToNewPass(){
        Intent toNewPass = new Intent(this, ForgotPassword.class);
        startActivity(toNewPass);
    }
}