package com.example.mobproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.db.UserDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.User;
import com.example.mobproject.validations.Validator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class UserEdit extends AppCompatActivity {

    TextView fNameEdit, lNameEdit, emailEdit;
    boolean isValidated = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fNameEdit = findViewById(R.id.fname_edit);
        lNameEdit = findViewById(R.id.lname_edit);
        emailEdit = findViewById(R.id.email_edit);
        Button saveProfile = findViewById(R.id.save_profile);

        saveProfile.setOnClickListener(saveAndGoBackToProfile);

        initEditProfile();

        Objects.requireNonNull(this.getSupportActionBar()).setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    }

    private void initEditProfile() {
        UserDatabase userDatabase = new UserDatabase();
        UserInfo userInfo = new UserInfo(this);

        userDatabase.getItem(userInfo.getUserId(), getUserData);
    }

    private final Callback<User> getUserData = new Callback<User>() {
        @Override
        public void OnFinish(ArrayList<User> arrayList) {
            User user = arrayList.get(0);

            String[] nameSurname = user.getName().split(" ");
            String fName = nameSurname[0];
            String lName = nameSurname[1];

            fNameEdit.setText(fName);
            lNameEdit.setText(lName);
            emailEdit.setText(user.getEmail());
        }
    };

    private final View.OnClickListener saveAndGoBackToProfile = view -> {
        saveProfile();
        if (isValidated) goToProfile();
    };

    private void saveProfile() {
        String fName = fNameEdit.getText().toString();
        String lName = lNameEdit.getText().toString();
        String name = fName + " " + lName;
        String email = emailEdit.getText().toString();

        validateInputs(fName, lName, email);

        if (isValidated) {
            UserInfo userInfo = new UserInfo(this);
            DocumentReference userType = FirebaseFirestore.getInstance()
                    .collection(DatabaseCollections.USERTYPES_COLLECTION).document(userInfo.getUserType());

            User user = new User(name, email, userType);

            UserDatabase userDatabase = new UserDatabase();
            userDatabase.updateItem(userInfo.getUserId(), user);
        }
    }

    private void validateInputs(String fName, String lName, String email) {
        if (fName.trim().isEmpty()) {
            fNameEdit.setError(getResources().getString(R.string.first_name_error));
            isValidated = false;
        }

        if (lName.trim().isEmpty()) {
            lNameEdit.setError(getResources().getString(R.string.last_name_error));
            isValidated = false;
        }

        if (email.trim().isEmpty()) {
            emailEdit.setError(getResources().getString(R.string.email_error));
            isValidated = false;
        }

        if (Validator.isInvalidEmail(email)) {
            emailEdit.setError(getResources().getString(R.string.error_invalid_email));
            isValidated = false;
        }
    }

    private void goToProfile() {
        Intent backToUser = new Intent(this, UserProfile.class);
        startActivity(backToUser);
        finish();
    }

    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up

        return false;
    }
}
