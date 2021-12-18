package com.example.mobproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.constants.UserInfo;
import com.example.mobproject.models.User;
import com.example.mobproject.models.UserCourses;
import com.example.mobproject.validations.Validator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private EditText signupEmail, signupFirstName, signupLastName, signupPass, signupConfPass;
    private RadioGroup status;
    private Button register;
    private User user;
    private String userId;
    private boolean isValid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        signupFirstName = findViewById(R.id.signup_fName);
        signupLastName = findViewById(R.id.signup_lName);
        signupEmail = findViewById(R.id.signup_email);
        signupPass = findViewById(R.id.signup_password);
        signupConfPass = findViewById(R.id.signup_conf_password);
        register = findViewById(R.id.signup_btn);
        status = findViewById(R.id.rad_status);
        Button btnToLogin = findViewById(R.id.btn_to_login);

        register.setOnClickListener(signupValidation);
        btnToLogin.setOnClickListener(switchBackToSignIn);
    }

    public View.OnClickListener signupValidation = view -> {

        String fName = signupFirstName.getText().toString();
        String lName = signupLastName.getText().toString();
        String email = signupEmail.getText().toString();
        String password = signupPass.getText().toString();
        String repeatPassword = signupConfPass.getText().toString();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        validateInputs(fName, lName, email, password, repeatPassword);

        int radioBtnId = status.getCheckedRadioButtonId();
        RadioButton radioBtn = findViewById(radioBtnId);
        int selectedStatusId = status.indexOfChild(radioBtn); //0 - STUDENT; 1 - TEACHER

        String refPath = FirebaseFirestore.getInstance()
                .collection(DatabaseCollections.USERTYPES_COLLECTION)
                .document(String.valueOf(selectedStatusId)).getPath();
        DocumentReference userType = FirebaseFirestore.getInstance().document(refPath);

        if (isValid) {
            //disable register button
            register.setEnabled(false);

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                            DocumentReference docRef = FirebaseFirestore.getInstance().
                                    collection(DatabaseCollections.USER_COLLECTION).
                                    document(userId);
                            user = new User(fName + " " + lName, email, userType);
                            UserInfo userInfo = new UserInfo(this);
                            userInfo.setUserId(auth.getUid());
                            userInfo.setUserType(String.valueOf(selectedStatusId));
                            userInfo.setUserCoursesNo(0);
                            userInfo.setUserFavouritesNo(0);
                            userInfo.setUserPassword(password);

                            docRef.set(user).addOnFailureListener(e -> Log.d("addUserData",
                                    "onFailure: " + e.toString()));

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            UserCourses favourites = new UserCourses(userId, new ArrayList<>());
                            db.collection(DatabaseCollections.FAVOURITES_COLLECTION)
                                    .document(userId).set(favourites);
                            UserCourses enrolled = new UserCourses(userId, new ArrayList<>());
                            db.collection(DatabaseCollections.ENROLLED_COLLECTION)
                                    .document(userId).set(enrolled);
                            switchToMessagePage();
                        } else {
                            // If sign up fails, display a message to the user.
                            Log.w("Sign up", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    };

    private void validateInputs(String fName, String lName, String email, String password, String repeatPassword) {
        isValid = true;

        // validate signup FIRST NAME
        if (fName.trim().isEmpty()) {
            signupFirstName.setError(getResources().getString(R.string.first_name_error));
            isValid = false;
        }

        //validate signup LAST NAME
        if (lName.trim().isEmpty()) {
            signupLastName.setError(getResources().getString(R.string.last_name_error));
            isValid = false;
        }

        //validate signup EMAIL ADDRESS
        if (email.trim().isEmpty()) {
            signupEmail.setError(getResources().getString(R.string.email_error));
            isValid = false;
        } else if (Validator.isInvalidEmail(email)) {
            signupEmail.setError(getResources().getString(R.string.error_invalid_email));
            isValid = false;
        }

        //validate signup PASSWORD
        if (password.trim().isEmpty()) {
            signupPass.setError(getResources().getString(R.string.password_error));
            isValid = false;
        } else if (repeatPassword.trim().isEmpty()) {
            signupConfPass.setError(getResources().getString(R.string.password_error));
        } else if (!repeatPassword.equals(password)) {
            signupConfPass.setError(getResources().getString(R.string.password_match_error));
            isValid = false;
        }
    }

    private void switchToMessagePage() {
        Intent toMessagePage = new Intent(this, SignUpMessageActivity.class);
        startActivity(toMessagePage);
        finish();
    }

    private final View.OnClickListener switchBackToSignIn = view -> {
        Intent backToSignIn = new Intent(this, LoginActivity.class);
        startActivity(backToSignIn);
        finish();
    };


}
