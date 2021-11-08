package com.example.mobproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mobproject.validations.UserValidation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // TODO after initiating log in process add a drawable to log in button and disable it so the person know that the application is not blocked.
    private EditText loginEmail, loginPass;
    private Button loginBtn;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            switchToCourseList();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button forgotPass = findViewById(R.id.btn_forgot_pass);
        Button btnToSignUp = findViewById(R.id.btn_tosignup);
        loginBtn = findViewById(R.id.login_btn);
        loginEmail = findViewById(R.id.login_email);
        loginPass = findViewById(R.id.login_password);

        loginBtn.setOnClickListener(loginValidation);

        btnToSignUp.setOnClickListener(switchToSignUp);

        forgotPass.setOnClickListener(switchToNewPass);
    }

    private final View.OnClickListener loginValidation = view -> {
        boolean validLoginEmail = true, validLoginPassword = true;
        final String email = loginEmail.getText().toString();
        final String password = loginPass.getText().toString();

        //Check login email address
        if(email.isEmpty()){
            loginEmail.setError(getResources().getString(R.string.email_error));
            validLoginEmail = false;
        } else if(UserValidation.isEmail(email)){
            loginEmail.setError(getResources().getString(R.string.error_invalid_email));
            validLoginEmail = false;
        }

        //Check login password
        if(password.isEmpty()){
            loginPass.setError(getResources().getString(R.string.password_error));
            validLoginPassword = false;
        }

        if(validLoginEmail && validLoginPassword) {
            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            switchToCourseList();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Sign in", "signInWithEmail:failure", task.getException());
                            // TODO outputError() to show the error message if something went wrong!
                        }
                    });
        }
    };

    private void switchToCourseList() {
        Intent toCourseList = new Intent(this, CourseList.class);
        startActivity(toCourseList);
        finish();
    }

    private final View.OnClickListener switchToSignUp = view -> {
        Intent toSignUp = new Intent(this, RegisterActivity.class);
        startActivity(toSignUp);
        finish();
    };

    private final View.OnClickListener switchToNewPass = view -> {
        Intent toNewPass = new Intent(this, ForgotPassword.class);
        startActivity(toNewPass);
        finish();
    };
}