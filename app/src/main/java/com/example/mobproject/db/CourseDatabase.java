package com.example.mobproject.db;

import android.util.Log;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.constants.ErrorCodes;
import com.example.mobproject.constants.ErrorMessages;
import com.example.mobproject.models.Course;
import com.example.mobproject.models.Error;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class CourseDatabase extends Database<Course> {

    @Override
    public Course getItem(String id) {
        final Course[] course = new Course[1];

        DocumentReference docRef = db.collection(DatabaseCollections.COURSES_COLLECTION).document(id);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            course[0] = documentSnapshot.toObject(Course.class);
            if (course[0] != null) {
                course[0].setId(documentSnapshot.getId());
            }
        });

        return course[0];
    }

    @Override
    public ArrayList<Course> getItems() {
        final ArrayList<Course> coursesList = new ArrayList<>();

        db.collection(DatabaseCollections.COURSES_COLLECTION).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Course tmp = document.toObject(Course.class);
                            tmp.setId(document.getId());
                            coursesList.add(tmp);
                        }
                    } else {
                        Log.d("Collections retrieval", "Error getting documents: ", task.getException());
                    }
        });

        return coursesList;
    }

    @Override
    public Error insertItem(Course item) {
        Error error;
        DocumentReference newCourseRef = db.collection(DatabaseCollections.COURSES_COLLECTION).document(    );

        if ((error = validateItem(item)) != null) {
            return error;
        }

        newCourseRef.set(item);

        return null;
    }

    @Override
    public Error updateItem(String id, Course item) {
        Error error;
        DocumentReference courseRef = db.collection(DatabaseCollections.COURSES_COLLECTION).document(id);

        if ((error = validateItem(item)) != null) {
            return error;
        }

        courseRef.set(item);

        return null;
    }

    @Override
    public Error removeItem(String id) {
        final Error[] error = { null };

        DocumentReference courseRef = db.collection(DatabaseCollections.COURSES_COLLECTION).document(id);
        courseRef.get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                error[0] = new Error(ErrorCodes.INVALID_ID, ErrorMessages.INVALID_ID);
            }
        });

        if (error[0] != null) {
            return error[0];
        }

        courseRef.delete();

        return null;
    }

    @Override
    public Error validateItem(Course item) {
        if (item.getName().trim().isEmpty() ||
            item.getStartDate().toString().isEmpty() || item.getEndDate().toString().isEmpty() ||
            item.getMeetDays().isEmpty() || item.getDescription().isEmpty()) {

            return new Error(ErrorCodes.EMPTY_FIELDS, ErrorMessages.EMPTY_FIELDS);
        }

        if (!item.getStartDate().before(item.getEndDate())) {
            return new Error(ErrorCodes.INVALID_DATE, ErrorMessages.INVALID_DATE);
        }
        if (item.getPrice() < 0) {
            return new Error(ErrorCodes.INVALID_PRICE, ErrorMessages.INVALID_PRICE);
        }

        return null;
    }
}
