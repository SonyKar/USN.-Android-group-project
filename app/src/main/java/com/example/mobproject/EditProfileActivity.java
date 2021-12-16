package com.example.mobproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.content.FileProvider;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.constants.Other;
import com.example.mobproject.constants.UserInfo;
import com.example.mobproject.controllers.PictureController;
import com.example.mobproject.db.UserDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.User;
import com.example.mobproject.validations.Validator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    TextView fNameEdit, lNameEdit, emailEdit;
    ImageView profilePicture;
    boolean isValidated = true;
    UserInfo userInfo;
    public static Context appContext;
    private Button saveProfile;
    ActivityResultLauncher<Intent> pictureResultLauncher;
    StorageReference storageReference;
    Uri uri;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userInfo = new UserInfo(this);
        appContext = getApplicationContext();

        fNameEdit = findViewById(R.id.fName_edit);
        lNameEdit = findViewById(R.id.lName_edit);
        emailEdit = findViewById(R.id.email_edit);
        saveProfile = findViewById(R.id.save_profile);
        Button changePassword = findViewById(R.id.btn_change_pass);
        profilePicture = findViewById(R.id.profile_avatar_edit);


        initEditProfile();
        profilePicture.setOnClickListener(changeProfilePicture);
        saveProfile.setOnClickListener(saveAndGoBackToProfile);
        changePassword.setOnClickListener(goToChangePassword);

        pictureResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

                        Intent data = result.getData();
                        if (data.getExtras() != null) {
                            Bitmap photo = (Bitmap) data.getExtras().get("data");
                            profilePicture.setImageBitmap(photo);
                            encodeBitmapAndSave(photo);
                        } else {
                            uri = data.getData();
                            Picasso.get().load(uri).into(profilePicture);
                            pictureUpload(uri);
                        }
                    }
                });

        Objects.requireNonNull(this.getSupportActionBar()).setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    }

    private void initEditProfile() {
        UserDatabase userDatabase = new UserDatabase();
        userDatabase.getItem(userInfo.getUserId(), getUserData);
        PictureController.getProfilePicture(userInfo.getUserId(), profilePicture);
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

    private final View.OnClickListener goToChangePassword = view -> changePassword();

    private final View.OnClickListener changeProfilePicture = view -> {

        final CharSequence[] optionsMenu = {getResources().getString(R.string.take_photo), getResources().getString(R.string.gallery_photo), getResources().getString(R.string.close)}; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (optionsMenu[i].equals(getResources().getString(R.string.take_photo))) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        createImageUri();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pictureResultLauncher.launch(takePicture);

                } else if (optionsMenu[i].equals(getResources().getString(R.string.gallery_photo))) {
                    Intent gallery = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if (gallery.resolveActivity(getPackageManager()) != null) {
                        pictureResultLauncher.launch(gallery);
                    }
                } else if (optionsMenu[i].equals(getResources().getString(R.string.close))) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    };

    private void pictureUpload(Uri imageUri) {
        StorageReference fileRef = storageReference.child(Other.PROFILE_STORAGE_FOLDER)
                .child(userInfo.getUserId() + Other.PROFILE_PHOTO_EXTENSION);
        UploadTask uploadTask = fileRef.putFile(imageUri);
        uploadTask.addOnFailureListener(e ->
                Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.upload_error), Toast.LENGTH_LONG).show());
    }

    public void encodeBitmapAndSave(Bitmap bitmap) {
        StorageReference fileRef = storageReference.child(Other.PROFILE_STORAGE_FOLDER)
                .child(userInfo.getUserId() + Other.PROFILE_PHOTO_EXTENSION);
        ByteArrayOutputStream biteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, biteArrayOutputStream);
        byte[] data = biteArrayOutputStream.toByteArray();
        UploadTask uploadTask = fileRef.putBytes(data);
        uploadTask.addOnFailureListener(Throwable::printStackTrace);
    }

    private void createImageUri() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat(getResources().getString(R.string.datetime_format), Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                Other.TEMPORARY_PHOTO_EXTENSION,         /* suffix */
                storageDir      /* directory */
        );
        uri = FileProvider.getUriForFile(this,
                getApplicationContext().getPackageName() + ".provider",
                image);
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
        userDatabase.getItems(new Callback<User>() {
            @Override
            public void OnFinish(ArrayList<User> userList) {
                String userId = userInfo.getUserId();
                for (User user : userList) {
                    if ((!user.getId().equals(userId)) && user.getEmail().equals(email)) {
                        emailEdit.setError(getResources().getString(R.string.error_taken_email));
                        isValidated = false;
                        break;
                    }
                }

                if (isValidated) {
                    //disable saveProfile button
                    saveProfile.setEnabled(false);

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

    private void changePassword() {
        Intent toChangePassword = new Intent(this, ChangePasswordActivity.class);
        startActivity(toChangePassword);
    }

    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up

        return false;
    }

    public static Context getContextOfApplication() {
        return appContext;
    }
}
