package com.example.mobproject.controllers;

import android.widget.ImageView;

import com.example.mobproject.constants.Other;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PictureController {
    public static void getProfilePicture(String userId, ImageView imageContainer) {
        //setting the profile picture
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileImgRef = storageReference.child(Other.PROFILE_STORAGE_FOLDER)
                .child(userId + Other.PROFILE_PHOTO_EXTENSION);
        profileImgRef.getDownloadUrl().addOnSuccessListener(uri ->
                Picasso.get().load(uri).into(imageContainer));
    }
}
