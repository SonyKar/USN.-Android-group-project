package com.example.mobproject.db;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FavouriteCoursesDatabase {
    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getItem(String id, Callback<Course> callback) {
        DocumentReference docRef = db.collection(DatabaseCollections.FAVOURITES_COLLECTION).document(id);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Course course = documentSnapshot.toObject(Course.class);
            if (course != null) {
                course.setId(documentSnapshot.getId());
                ArrayList<Course> courseList = new ArrayList<>();
                courseList.add(course);
                callback.OnFinish(courseList);
            }
        });
    }

    public void getItems(String userId, Callback<DocumentReference> callback) {
        DocumentReference docRef = db.collection(DatabaseCollections.FAVOURITES_COLLECTION).document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            ArrayList<DocumentReference> favouriteCourseReference =
                    (ArrayList<DocumentReference>) documentSnapshot.get("courses");

            callback.OnFinish(favouriteCourseReference);

        });
    }

    public void insertItem (String userId, String courseId) {
        DocumentReference docRef = db.collection(DatabaseCollections.FAVOURITES_COLLECTION).document(userId);
        DocumentReference courseToInsert = db.collection(DatabaseCollections.COURSES_COLLECTION)
                .document(courseId);

        docRef.update("courses", FieldValue.arrayUnion(courseToInsert));
    }

//    public Error updateItem(String id,  item) {
//        Error error;
//        DocumentReference courseRef = db.collection(DatabaseCollections.FAVOURITES_COLLECTION).document(id);
//
//        if ((error = validateItem(item)) != null) {
//            return error;
//        }
//
//        courseRef.set(item);
//
//        return null;
//
//    }


    public void removeItem(String userId, String courseId) {
        DocumentReference docRef = db.collection(DatabaseCollections.FAVOURITES_COLLECTION)
                .document(userId);
        DocumentReference courseToRemove = db.collection(DatabaseCollections.COURSES_COLLECTION)
                .document(courseId);
        docRef.update("courses", FieldValue.arrayRemove(courseToRemove));
    }
}
