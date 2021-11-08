package com.example.mobproject.db;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mobproject.constants.DatabaseTables;
import com.example.mobproject.constants.ErrorCodes;
import com.example.mobproject.constants.ErrorMessages;
import com.example.mobproject.models.Error;
import com.example.mobproject.models.User;
import com.example.mobproject.validations.UserValidation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserDatabase extends Database<User> {
    DatabaseReference reference = database.getReference(DatabaseTables.USER_TABLE);
    private final ArrayList<User> users = new ArrayList<>();

    public UserDatabase() {
        getItems();
    }

    @Override
    User getItem(String id) {
        User user = null;
        
        for (User tmpUser : users) {
            if (tmpUser.getId().equals(id)) {
                user = tmpUser;
                break;
            }
        }
        
        return user;
    }

    @Override
    ArrayList<User> getItems() {
        // Read from the database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    String id = singleSnapshot.getKey();
                    User user = singleSnapshot.getValue(User.class);
                    if (user != null) {
                        user.setId(id);
                        users.add(user);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Read users", error.getMessage());
            }
        });

        return users;
    }

    @Override
    Error updateItem(String id, User item) {
        Error error = validateItem(item);

        if (error == null) {
            Error EXISTENT_EMAIL = isExistentEmail(item.getEmail());
            if (EXISTENT_EMAIL != null) return EXISTENT_EMAIL;
            reference.child(id).setValue(item);
        }

        return error;
    }

    @Override
    Error insertItem(User item) {
        Error error = validateItem(item);

        if (error == null) {
            Error EXISTENT_EMAIL = isExistentEmail(item.getEmail());
            if (EXISTENT_EMAIL != null) return EXISTENT_EMAIL;
            reference.push().setValue(item);
//            users.add(item);
        }

        return error;
    }

    @Nullable
    private Error isExistentEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return new Error(ErrorCodes.EXISTENT_EMAIL, ErrorMessages.EXISTENT_EMAIL);
            }
        }
        return null;
    }

    @Override
    Error removeItem(String id) {
        Query userQuery = reference.child(id);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    userSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Remove user", error.getMessage());
            }
        });

        return null;
    }

    @Override
    Error validateItem(User item) {

        if (!UserValidation.isEmail(item.getEmail())) {
            return new Error(ErrorCodes.INVALID_EMAIL, ErrorMessages.INVALID_EMAIL);
        }

        if (!UserValidation.isCorrectType(item.getUserType())) {
            return new Error(ErrorCodes.INVALID_USER_TYPE, ErrorMessages.INVALID_USER_TYPE);
        }

        if (!UserValidation.isEmpty(item)) {
            return new Error(ErrorCodes.EMPTY_FIELDS, ErrorMessages.EMPTY_FIELDS);
        }

        return null;
    }
}
