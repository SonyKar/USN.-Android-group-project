package com.example.mobproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobproject.validations.Validator;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        EditText emailEditText = findViewById(R.id.send_email);
        Button newPasswordButton = findViewById(R.id.new_pass);

        newPasswordButton.setOnClickListener(view -> newPasswordButtonClickHandler(emailEditText));
    }

    private void newPasswordButtonClickHandler(EditText emailEditText) {
        String email = emailEditText.getText().toString();
        if (email.trim().isEmpty()) {
            emailEditText.setError(getResources().getString(R.string.email_error));
        } else if (Validator.isInvalidEmail(email)) {
            emailEditText.setError(getResources().getString(R.string.error_invalid_email));
        } else {
            resetPassword(email);
        }
    }

    private void resetPassword(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    Toast.makeText(this, getResources().getString(R.string.password_reset), Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}
