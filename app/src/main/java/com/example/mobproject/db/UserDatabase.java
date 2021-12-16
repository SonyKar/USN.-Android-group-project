package com.example.mobproject.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.mobproject.EditProfileActivity;
import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.constants.SharedPreferencesInfo;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class UserDatabase extends Database<User> {

    @Override
    public void getItem(String id, Callback<User> callback) {
        DocumentReference docRef = db.collection(DatabaseCollections.USER_COLLECTION).document(id);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            if (user != null) {
                user.setId(documentSnapshot.getId());
                ArrayList<User> userList = new ArrayList<>();
                userList.add(user);
                callback.OnFinish(userList);
            }
        });
    }

    @Override
    public void getItems(Callback<User> callback) {
        db.collection(DatabaseCollections.USER_COLLECTION).get().addOnSuccessListener(snapshots -> {
            ArrayList<User> usersList = new ArrayList<>();
            for (QueryDocumentSnapshot document : snapshots) {
                User tmp = document.toObject(User.class);
                tmp.setId(document.getId());
                usersList.add(tmp);
            }
            callback.OnFinish(usersList);
        });
    }

    @Override
    public void insertItem(User item) {

    }

    @Override
    public void updateItem(String id, User item) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Context appContext = EditProfileActivity.getContextOfApplication();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(Objects.requireNonNull(user).getEmail()), sharedPreferences.getString(SharedPreferencesInfo.SHARED_PREF_PASSWORD, SharedPreferencesInfo.SHARED_PREF_NODATA_STRING));

        user.reauthenticate(credential).addOnSuccessListener(unused -> user.updateEmail(item.getEmail())
                .addOnSuccessListener(unused1 -> db.collection(DatabaseCollections.USER_COLLECTION).document(id)
                        .update(
                                "name", item.getName(),
                                "email", item.getEmail(),
                                "userType", item.getUserType()
                        )));
    }
}
