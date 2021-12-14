package com.example.mobproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.mobproject.constants.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity {
    TextView currentPass, newPass, confirmNewPass;
    boolean isValidated = true;
    UserInfo userInfo;
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
        Button savePassword = findViewById(R.id.save_password);


        Objects.requireNonNull(this.getSupportActionBar()).setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        savePassword.setOnClickListener(saveAndGoBackToEditProfile);
    }

    private final View.OnClickListener saveAndGoBackToEditProfile = view -> savePassword();

    private void savePassword() {
        isValidated = true;
        String currentPassword = currentPass.getText().toString();
        String newPassword = newPass.getText().toString();
        String confirmNewPassword = confirmNewPass.getText().toString();
        Boolean passConfirm = newPassword.equals(confirmNewPassword);
        Boolean samePass = newPassword.equals(currentPassword);

        validateInputs(currentPassword, newPassword, passConfirm, samePass);

        if (isValidated) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user.getEmail();
            AuthCredential credential = EmailAuthProvider.getCredential(email, currentPassword);

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("pass_update", "Password successfully changed!");
                                            Toast.makeText(getApplicationContext(), getString(R.string.password_change_message), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            });
            goToProfile();
        }
    }

    private void validateInputs(String currentPassword, String newPassword, Boolean passConfirm, Boolean samePass) {
        if (currentPassword.trim().isEmpty()) {
            currentPass.setError(getResources().getString(R.string.first_name_error));
            isValidated = false;
        }

        if (newPassword.trim().isEmpty()) {
            newPass.setError(getResources().getString(R.string.last_name_error));
            isValidated = false;
        } else if (!passConfirm) {
            confirmNewPass.setError(getResources().getString(R.string.password_match_error));
            isValidated = false;
        } else if (samePass) {//if the new password is the same as the current one
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
