package com.example.mobproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.constants.Other;
import com.example.mobproject.constants.UserInfo;
import com.example.mobproject.db.UserDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.User;
import com.example.mobproject.validations.Validator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    TextView fNameEdit, lNameEdit, emailEdit;
    ImageView profilePicture;
    boolean isValidated = true;
    UserInfo userInfo;
    public static Context appContext;
    ActivityResultLauncher<Intent> pictureResultLauncher;
    StorageReference storageReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userInfo = new UserInfo(this);
        appContext = getApplicationContext();

        fNameEdit = findViewById(R.id.fname_edit);
        lNameEdit = findViewById(R.id.lname_edit);
        emailEdit = findViewById(R.id.email_edit);
        Button saveProfile = findViewById(R.id.save_profile);
        profilePicture = findViewById(R.id.profile_avatar_edit);

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileImgRef = storageReference.child("profileImages")
                .child(userInfo.getUserId()+Other.PROFILE_PHOTO_EXTENSION);
        profileImgRef.getDownloadUrl().addOnSuccessListener(uri ->
                Picasso.get().load(uri).into(profilePicture));

        profilePicture.setOnClickListener(changeProfilePicture);
        saveProfile.setOnClickListener(saveAndGoBackToProfile);
        pictureResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK&&result.getData()!=null) {
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        pictureUpload(uri);
                    }
                });

        initEditProfile();

        Objects.requireNonNull(this.getSupportActionBar()).setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    }

    private void initEditProfile() {
        UserDatabase userDatabase = new UserDatabase();
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

    private final View.OnClickListener saveAndGoBackToProfile = view -> saveProfile();

    private final View.OnClickListener changeProfilePicture = view ->{
        Intent gallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (gallery.resolveActivity(getPackageManager()) != null) {
            pictureResultLauncher.launch(gallery);
        } else {
            Toast.makeText(this, "There is no app that support this action",
                    Toast.LENGTH_SHORT).show();
        }
    };

    private void pictureUpload(Uri imageUri){
        StorageReference fileRef = storageReference.child("profileImages")
                .child(userInfo.getUserId()+Other.PROFILE_PHOTO_EXTENSION);
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                fileRef.getDownloadUrl().addOnSuccessListener(uri ->{
                    profilePicture.setImageURI(uri);
                    Picasso.get().load(uri).into(profilePicture);}))
                .addOnFailureListener(e ->
                Toast.makeText(EditProfileActivity.this,"Failed to upload image", Toast.LENGTH_LONG).show());
    }

    private void saveProfile() {
        isValidated = true;
        String fName = fNameEdit.getText().toString();
        String lName = lNameEdit.getText().toString();
        String name = fName + " " + lName;
        String email = emailEdit.getText().toString();

        validateInputs(fName, lName, email);
        UserDatabase userDatabase = new UserDatabase();
        UserInfo userInfo = new UserInfo(this);
        userDatabase.getItems( new Callback<User>() {
            @Override
            public void OnFinish(ArrayList<User> userList) {
                String userId = userInfo.getUserId();
                for(User user: userList){
                    if((!user.getId().equals(userId)) && user.getEmail().equals(email)) {
                        emailEdit.setError(getResources().getString(R.string.error_taken_email));
                        isValidated = false;
                        break;
                    }
                }
                if(isValidated) {
                    DocumentReference userType = FirebaseFirestore.getInstance()
                            .collection(DatabaseCollections.USERTYPES_COLLECTION).document(userInfo.getUserType());
                    User user = new User(name, email, userType);
                    UserDatabase userDatabase = new UserDatabase();
                    userDatabase.updateItem(userInfo.getUserId(), user);
                    goToProfile();
                }
            }
        });
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
        Intent backToUser = new Intent(this, UserProfileActivity.class);
        startActivity(backToUser);
        finish();
    }

    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up

        return false;
    }
    public static Context getContextOfApplication(){
        return appContext;
    }
}
