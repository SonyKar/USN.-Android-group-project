package com.example.mobproject.db;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Error;
import com.example.mobproject.models.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

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
    public Error insertItem(User item) {
        return null;
    }

    @Override
    public Error updateItem(String id, User item) {
        return null;
    }

    @Override
    public Error removeItem(String id) {
        return null;
    }

    @Override
    public Error validateItem(User item) {
        return null;
    }

//    @Override
//    User getItem(String id) {
//        User user = null;
//
//        for (User tmpUser : users) {
//            if (tmpUser.getId().equals(id)) {
//                user = tmpUser;
//                break;
//            }
//        }
//
//        return user;
//    }
//
//    @Override
//    ArrayList<User> getItems() {
//        // Read from the database
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
//                    String id = singleSnapshot.getKey();
//                    User user = singleSnapshot.getValue(User.class);
//                    if (user != null) {
//                        user.setId(id);
//                        users.add(user);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("Read users", error.getMessage());
//            }
//        });
//
//        return users;
//    }
//
//    @Override
//    Error updateItem(String id, User item) {
//        Error error = validateItem(item);
//
//        if (error == null) {
//            Error EXISTENT_EMAIL = isExistentEmail(item.getEmail());
//            if (EXISTENT_EMAIL != null) return EXISTENT_EMAIL;
//            reference.child(id).setValue(item);
//        }
//
//        return error;
//    }
//
//    @Override
//    public void getItem(String id, Callback<User> callback) {
//
//    }
//
//    @Override
//    public void getItems(Callback<User> callback) {
//
//    }
//
//    @Override
//    Error insertItem(User item) {
//        Error error = validateItem(item);
//
//        if (error == null) {
//            Error EXISTENT_EMAIL = isExistentEmail(item.getEmail());
//            if (EXISTENT_EMAIL != null) return EXISTENT_EMAIL;
//            reference.push().setValue(item);
////            users.add(item);
//        }
//
//        return error;
//    }
//
//    @Nullable
//    private Error isExistentEmail(String email) {
//        for (User user : users) {
//            if (user.getEmail().equals(email)) {
//                return new Error(ErrorCodes.EXISTENT_EMAIL, ErrorMessages.EXISTENT_EMAIL);
//            }
//        }
//        return null;
//    }
//
//    @Override
//    Error removeItem(String id) {
//        Query userQuery = reference.child(id);
//        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
//                    userSnapshot.getRef().removeValue();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("Remove user", error.getMessage());
//            }
//        });
//
//        return null;
//    }
//
//    @Override
//    Error validateItem(User item) {
//
//        if (!UserValidation.isEmail(item.getEmail())) {
//            return new Error(ErrorCodes.INVALID_EMAIL, ErrorMessages.INVALID_EMAIL);
//        }
//
//        if (!UserValidation.isCorrectType(item.getUserType())) {
//            return new Error(ErrorCodes.INVALID_USER_TYPE, ErrorMessages.INVALID_USER_TYPE);
//        }
//
//        if (!UserValidation.isEmpty(item)) {
//            return new Error(ErrorCodes.EMPTY_FIELDS, ErrorMessages.EMPTY_FIELDS);
//        }
//
//        return null;
//    }

}
