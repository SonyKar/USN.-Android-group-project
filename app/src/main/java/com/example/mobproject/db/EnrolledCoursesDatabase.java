package com.example.mobproject.db;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.constants.ErrorCodes;
import com.example.mobproject.constants.ErrorMessages;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.example.mobproject.models.Error;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class EnrolledCoursesDatabase extends  Database<Course> {
    @Override
    public void getItem(String id, Callback<Course> callback) {
        DocumentReference docRef = db.collection(DatabaseCollections.ENROLLED_COLLECTION).document(id);
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

    @Override
    public void getItems(Callback<Course> callback) {
        db.collection(DatabaseCollections.ENROLLED_COLLECTION).get().addOnSuccessListener(snapshots -> {
            ArrayList<Course> coursesList = new ArrayList<>();
            for (QueryDocumentSnapshot document : snapshots) {
                Course tmp = document.toObject(Course.class);
                tmp.setId(document.getId());
                coursesList.add(tmp);
            }
            callback.OnFinish(coursesList);
        });
    }

    @Override
    public Error insertItem(Course item) {
        Error error;
        DocumentReference newCourseRef = db.collection(DatabaseCollections.ENROLLED_COLLECTION).document(    );

        if ((error = validateItem(item)) != null) {
            return error;
        }
        newCourseRef.set(item);
        return null;
    }

    @Override
    public Error updateItem(String id, Course item) {
        Error error;
        DocumentReference courseRef = db.collection(DatabaseCollections.ENROLLED_COLLECTION).document(id);

        if ((error = validateItem(item)) != null) {
            return error;
        }

        courseRef.set(item);

        return null;

    }

    @Override
    public Error removeItem(String id) {
        final Error[] error = { null };

        DocumentReference courseRef = db.collection(DatabaseCollections.ENROLLED_COLLECTION).document(id);
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
