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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.models.User;
import com.example.mobproject.validations.UserValidation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText signupEmail, signupFirstName, signupLastName, signupPass, signupConfPass;
    private Button register, btnToLogin;
    private RadioGroup status;


    //private static final String USER = "Users";
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
        register = findViewById(R.id.signup_btn);
        status = findViewById(R.id.rad_status);
        btnToLogin = findViewById(R.id.btn_to_login);



        /* get selected radio button from radioGroup
        int selectedId = gender.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        selectedRadioButton = (RadioButton)findViewById(selectedId);
        Toast.makeText(getApplicationContext(), selectedRadioButton.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();*/

        register.setOnClickListener(signupValidation);

        btnToLogin.setOnClickListener(switchBackToSignIn);
    }

    public View.OnClickListener signupValidation = view -> {
        Boolean validSignUpEmail = true, validSignUpPassword = true, validSignUpFirstName = true, validSignUpLastName = true, validSignUpStatus = true;
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
        } else if (!UserValidation.isEmail(email)) {
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
            Log.d("userType", refPath);

            //TODO SharedPreferences also in SignUp?

        if(validSignUpFirstName && validSignUpLastName && validSignUpEmail && validSignUpPassword
                && validSignUpStatus) {

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                            DocumentReference docRef = FirebaseFirestore.getInstance().
                                    collection(DatabaseCollections.USER_COLLECTION).
                                    document(userId);
                            user = new User(fName+" "+lName, email, userType);
                            docRef.set(user).addOnSuccessListener( (OnSuccessListener) (aVoid)->{
                            } ).addOnFailureListener(e -> Log.d("addUserData","onFailure: "+e.toString()));

                            switchToMessagePage();

                        } else {
                            // If sign up fails, display a message to the user.
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

    private final View.OnClickListener switchBackToSignIn = view -> {
        Intent backToSignIn = new Intent(this, LoginActivity.class);
        startActivity(backToSignIn);
        finish();
    };



}
