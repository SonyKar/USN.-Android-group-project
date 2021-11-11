package com.example.mobproject.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.models.Course;
import com.example.mobproject.models.Error;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class CourseDatabase extends Database<Course> {

    @Override
    Course getItem(String id) {
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
    ArrayList<Course> getItems() {
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
    Error insertItem(Course item) {
        Error error;
        DocumentReference newCourseRef = db.collection(DatabaseCollections.COURSES_COLLECTION).document();

        if ((error = validateItem(item)) != null) {
            return error;
        }

        newCourseRef.set(item);

        return null;
    }

    @Override
    Error updateItem(String id, Course item) {
        return null;
    }

    @Override
    Error removeItem(String id) {
        return null;
    }

    @Override
    Error validateItem(Course item) {
        Error error = null;

        // All the fields are completed (name, category, price, starDate, endDate, Meetings, Difficulty, Description)
        // startDate before endDate
        // price is not negative

        return null;
    }
}
