package com.example.mobproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobproject.constants.UserInfo;
import com.example.mobproject.db.CourseDatabase;
import com.example.mobproject.db.EnrolledCoursesDatabase;
import com.example.mobproject.db.FavouriteCoursesDatabase;
import com.example.mobproject.db.UserDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.example.mobproject.models.User;
import com.example.mobproject.validations.Validator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail, loginPass;
    private Button loginBtn;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private UserInfo userInfo;
    private boolean isValid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button forgotPass = findViewById(R.id.btn_forgot_pass);
        Button btnToSignUp = findViewById(R.id.btn_toSignup);
        loginBtn = findViewById(R.id.login_btn);
        loginEmail = findViewById(R.id.login_email);
        loginPass = findViewById(R.id.login_password);

        loginBtn.setOnClickListener(loginValidation);
        btnToSignUp.setOnClickListener(switchToSignUp);
        forgotPass.setOnClickListener(switchToNewPass);

        userInfo = new UserInfo(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            switchToCourseList();
        }
    }

    private final View.OnClickListener loginValidation = view -> {
        final String email = loginEmail.getText().toString();
        final String password = loginPass.getText().toString();

        validateInputs(email, password);

        if (isValid) {
            //disable loginBtn
            loginBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_loading_purple, 0, 0, 0);
            loginBtn.setEnabled(false);

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            String userId = auth.getUid();
                            userInfo.setUserId(userId);
                            userInfo.setUserPassword(password);
                            UserDatabase userDatabase = new UserDatabase();
                            userDatabase.getItem(auth.getUid(), new Callback<User>() {
                                @Override
                                public void OnFinish(ArrayList<User> arrayList) {
                                    User user = arrayList.get(0);
                                    DocumentReference userType = user.getUserType();
                                    if (userType != null) {
                                        String userTypeId = userType.getId();
                                        userInfo.setUserType(userTypeId);

                                        if(userTypeId.equals("0")) {
                                            EnrolledCoursesDatabase enrolledDatabase = new EnrolledCoursesDatabase();
                                            enrolledDatabase.getItems(userId, new Callback<Course>() {
                                                @Override
                                                public void OnFinish(ArrayList<Course> enrolledList) {
                                                    userInfo.setUserCoursesNo(enrolledList.size());
                                                }
                                            });
                                        }
                                        else
                                        {
                                            CourseDatabase courseDatabase = new CourseDatabase();
                                            courseDatabase.getItems(new Callback<Course>() {
                                                @Override
                                                public void OnFinish(ArrayList<Course> courseList) {
                                                    int userCourses = 0;
                                                    for(Course course : courseList){
                                                        if(course.getOwnerId().getId().equals(userId))
                                                            userCourses++;
                                                    }
                                                    userInfo.setUserCoursesNo(userCourses);
                                                }
                                            });
                                        }

                                        switchToCourseList();
                                    }
                                }
                            });

                            FavouriteCoursesDatabase favouritesDatabase = new FavouriteCoursesDatabase();
                            Callback<DocumentReference> favouritesCallback = new Callback<DocumentReference>() {
                                @Override
                                public void OnFinish(ArrayList<DocumentReference> favouritesList) {
                                    userInfo.setUserFavouritesNo(favouritesList.size());
                                }
                            };
                            favouritesDatabase.getItems(userId, favouritesCallback);



                        } else {
                            loginBtn.setCompoundDrawables(null, null, null, null);
                            loginBtn.setEnabled(true);

                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.credentials_error), Toast.LENGTH_SHORT).show();
                            Log.w("Sign in", "signInWithEmail:failure", task.getException());
                        }
                    });
        }
    };

    private void validateInputs(String email, String password) {
        isValid = true;

        if (email.trim().isEmpty()) {
            loginEmail.setError(getResources().getString(R.string.email_error));
            isValid = false;
        } else if (Validator.isInvalidEmail(email)) {
            loginEmail.setError(getResources().getString(R.string.error_invalid_email));
            isValid = false;
        }

        if (password.trim().isEmpty()) {
            loginPass.setError(getResources().getString(R.string.password_error));
            isValid = false;
        }
    }

    private void switchToCourseList() {
        Intent toCourseList = new Intent(this, CourseListActivity.class);
        startActivity(toCourseList);
        finish();
    }

    private final View.OnClickListener switchToSignUp = view -> {
        Intent toSignUp = new Intent(this, SignUpActivity.class);
        startActivity(toSignUp);
    };

    private final View.OnClickListener switchToNewPass = view -> {
        Intent toNewPass = new Intent(this, ForgotPasswordActivity.class);
        startActivity(toNewPass);
    };

    @Override
    public void onBackPressed() {
    }
}

