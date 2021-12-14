package com.example.mobproject.db;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobproject.R;
import com.example.mobproject.adapters.MyCoursesAdapter;
import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.constants.ErrorCodes;
import com.example.mobproject.constants.ErrorMessages;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.example.mobproject.models.Error;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

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
                    if (enrolledCourseList.size() == enrolledReferences.size()) {
                        callback.OnFinish(enrolledCourseList);
                    }
                }
            };
            for (DocumentReference courseRef : enrolledReferences) {
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


//    public Error updateItem(String id, Course item) {
//        Error error;
//        DocumentReference courseRef = db.collection(DatabaseCollections.ENROLLED_COLLECTION).document(id);
//
//        if ((error = validateItem(item)) != null) {
//            return error;
//        }
//
//        courseRef.set(item);
//        return null;
//
//    }

    public void removeItem(String userId, String courseId) {
        DocumentReference docRef = db.collection(DatabaseCollections.ENROLLED_COLLECTION)
                .document(userId);
        DocumentReference courseToRemove = db.collection(DatabaseCollections.COURSES_COLLECTION)
                .document(courseId);
        docRef.update("courses", FieldValue.arrayRemove(courseToRemove));
    }
}

