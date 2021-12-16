package com.example.mobproject.db;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class EnrolledCoursesDatabase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

    public void getItems(String userId, Callback<Course> callback) {
        ArrayList<Course> enrolledCourseList = new ArrayList<>();
        CourseDatabase courseDatabase = new CourseDatabase();
        DocumentReference docRef = db.collection(DatabaseCollections.ENROLLED_COLLECTION).document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            ArrayList<DocumentReference> enrolledReferences =
                    (ArrayList<DocumentReference>) documentSnapshot.get("courses");
            Callback<Course> courseCallback = new Callback<Course>() {
                @Override
                public void OnFinish(ArrayList<Course> arrayList) {
                    enrolledCourseList.add(arrayList.get(0));
                    if (enrolledReferences != null && enrolledCourseList.size() == enrolledReferences.size()) {
                        callback.OnFinish(enrolledCourseList);
                    }
                }
            };
            for (DocumentReference courseRef : Objects.requireNonNull(enrolledReferences)) {
                courseDatabase.getItem(courseRef.getId(), courseCallback);
            }
        });
    }

    public void insertItem(String userId, String courseId) {
        DocumentReference docRef = db.collection(DatabaseCollections.ENROLLED_COLLECTION).document(userId);
        DocumentReference courseToInsert = db.collection(DatabaseCollections.COURSES_COLLECTION)
                .document(courseId);

        docRef.update("courses", FieldValue.arrayUnion(courseToInsert));
    }
}

