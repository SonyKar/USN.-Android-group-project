package com.example.mobproject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mobproject.constants.UserInfo;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText currentPass, newPass, confirmNewPass;
    boolean isValidated = true;
    UserInfo userInfo;
    private Button savePassword;
    public static Context appContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        Toolbar toolbar = findViewById(R.id.change_pass_toolbar);
        setSupportActionBar(toolbar);

        userInfo = new UserInfo(this);

        currentPass = findViewById(R.id.current_pass);
        newPass = findViewById(R.id.new_pass);
        confirmNewPass = findViewById(R.id.confirm_new_pass);
        savePassword = findViewById(R.id.save_password);
        
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        savePassword.setOnClickListener(saveAndGoBackToEditProfile);
    }

    private final View.OnClickListener saveAndGoBackToEditProfile = view -> savePassword();

    private void savePassword() {
        String currentPassword = currentPass.getText().toString();
        String newPassword = newPass.getText().toString();
        String confirmNewPassword = confirmNewPass.getText().toString();

        validateInputs(currentPassword, newPassword, confirmNewPassword);

        if (isValidated) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String email = user.getEmail();

                assert email != null; // if (email != null)
                AuthCredential credential = EmailAuthProvider.getCredential(email, currentPassword);

                user.reauthenticate(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        //disable savePassword button
                                        savePassword.setEnabled(false);
                                        userInfo.setUserPassword(newPassword);
                                        Toast.makeText(getApplicationContext(), getString(R.string.password_change_message), Toast.LENGTH_SHORT).show();
                                        goToProfile();
                                    } else { // updating password was not successful
                                        //enable button
                                        savePassword.setEnabled(true);

                                        Toast.makeText(this, getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else { // incorrect password
                        currentPass.setError(getResources().getString(R.string.incorrect_password_error));
                    }
                });
            } else { // null user
                Toast.makeText(this, getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void validateInputs(String currentPassword, String newPassword, String confirmNewPassword) {
        isValidated = true;
        
        if (currentPassword.trim().isEmpty()) {
            currentPass.setError(getResources().getString(R.string.password_error));
            isValidated = false;
        }
        if (newPassword.trim().isEmpty()) {
            newPass.setError(getResources().getString(R.string.conf_password_error));
            isValidated = false;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            confirmNewPass.setError(getResources().getString(R.string.password_match_error));
            isValidated = false;
        }
        if (newPassword.equals(currentPassword)) {//if the new password is the same as the current one
            newPass.setError(getResources().getString(R.string.same_pass_error));
            isValidated = false;
        }
    }

    private void goToProfile() {
        finish();
    }

    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }

}
