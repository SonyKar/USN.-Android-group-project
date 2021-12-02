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
import com.example.mobproject.db.EnrolledCoursesDatabase;
import com.example.mobproject.models.UserCourses;
import com.example.mobproject.models.User;
import com.example.mobproject.validations.Validator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    //TODO Hash password
    private EditText signupEmail, signupFirstName, signupLastName, signupPass, signupConfPass;
    private RadioGroup status;

    private User user;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        signupFirstName = findViewById(R.id.signup_fname);
        signupLastName = findViewById(R.id.signup_lname);
        signupEmail = findViewById(R.id.signup_email);
        signupPass = findViewById(R.id.signup_password);
        signupConfPass = findViewById(R.id.signup_conf_password);
        Button register = findViewById(R.id.signup_btn);
        status = findViewById(R.id.rad_status);
        Button btnToLogin = findViewById(R.id.btn_to_login);

        register.setOnClickListener(signupValidation);
        btnToLogin.setOnClickListener(switchBackToSignIn);

        /* get selected radio button from radioGroup
        int selectedId = gender.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        selectedRadioButton = (RadioButton)findViewById(selectedId);
        Toast.makeText(getApplicationContext(), selectedRadioButton.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();*/
    }

    public View.OnClickListener signupValidation = view -> {
        Boolean validSignUpEmail = true, validSignUpPassword = true, validSignUpFirstName = true, validSignUpLastName = true;
        Boolean passConfirm = signupPass.getText().toString().equals(
                signupConfPass.getText().toString());

        String fName = signupFirstName.getText().toString();
        String lName = signupLastName.getText().toString();
        String email = signupEmail.getText().toString();
        String password = signupPass.getText().toString();

        FirebaseAuth auth = FirebaseAuth.getInstance();

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
        } else if (Validator.isInvalidEmail(email)) {
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

            // one of the radio buttons is checked
            // get selected radio button from radioGroup
            // find the radiobutton by returned id
            //selectedStatus = (RadioButton)findViewById(selectedId);

            int radioBtnId = status.getCheckedRadioButtonId();
            RadioButton radioBtn = findViewById(radioBtnId);
            int selectedStatusId = status.indexOfChild(radioBtn); //0 - STUDENT; 1 - TEACHER

            String refPath = FirebaseFirestore.getInstance()
                    .collection(DatabaseCollections.USERTYPES_COLLECTION)
                    .document(String.valueOf(selectedStatusId)).getPath();
            DocumentReference userType = FirebaseFirestore.getInstance().document(refPath);

        if(validSignUpFirstName && validSignUpLastName && validSignUpEmail && validSignUpPassword) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                            DocumentReference docRef = FirebaseFirestore.getInstance().
                                    collection(DatabaseCollections.USER_COLLECTION).
                                    document(userId);
                            user = new User(fName+" "+lName, email, userType);
                            UserInfo userInfo = new UserInfo(this);
                            userInfo.setUserId(auth.getUid());
                            userInfo.setUserPassword(password);
                            docRef.set(user).addOnFailureListener(e -> Log.d("addUserData",
                                    "onFailure: "+e.toString()));

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

    private void switchToMessagePage(){
        Intent toMessagePage = new Intent(this, SignUpMessageActivity.class);
                startActivity(toMessagePage);
    }

    private final View.OnClickListener switchBackToSignIn = view -> {
        Intent backToSignIn = new Intent(this, LoginActivity.class);
        startActivity(backToSignIn);
        finish();
    };



}
