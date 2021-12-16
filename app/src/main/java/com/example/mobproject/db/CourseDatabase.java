package com.example.mobproject.db;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CourseDatabase extends Database<Course> {

    @Override
    public void getItem(String id, Callback<Course> callback) {
        DocumentReference docRef = db.collection(DatabaseCollections.COURSES_COLLECTION).document(id);
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
        db.collection(DatabaseCollections.COURSES_COLLECTION).get().addOnSuccessListener(snapshots -> {
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
    public void insertItem(Course item) {
        DocumentReference newCourseRef = db.collection(DatabaseCollections.COURSES_COLLECTION).document();
        newCourseRef.set(item);
    }

    public void incrementStudentCounter(String id) {
        DocumentReference courseRef = db.collection(DatabaseCollections.COURSES_COLLECTION).document(id);
        courseRef.update("studentCounter", FieldValue.increment(1));
    }

    @Override
    public void updateItem(String id, Course item) {
        DocumentReference courseRef = db.collection(DatabaseCollections.COURSES_COLLECTION).document(id);
        courseRef.set(item);
    }
}
