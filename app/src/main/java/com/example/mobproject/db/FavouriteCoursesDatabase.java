package com.example.mobproject.db;

import android.util.Log;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.constants.ErrorCodes;
import com.example.mobproject.constants.ErrorMessages;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.example.mobproject.models.Error;
import com.example.mobproject.models.Favourites;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
            Log.d("faves", String.valueOf(favouriteCourseReference));
//            ArrayList<Course> favouriteCourseList = new ArrayList<>();
//            CourseDatabase courseDatabase = new CourseDatabase();
//            Callback<Course> courseCallback = new Callback<Course>() {
//                @Override
//                public void OnFinish(ArrayList<Course> arrayList) {
//                    favouriteCourseList.add(arrayList.get(0));
//                    Log.d("faves", "retrieved"+String.valueOf(arrayList.get(0)));
//                }
//            };
//
//            for(DocumentReference courseRef:favouriteCourseReference){
//                courseDatabase.getItem(courseRef.getId(), courseCallback);
//                Log.d("faves","getting courses" );
//            }

            callback.OnFinish(favouriteCourseReference);

        });
//
    }


    public Error insertItem(String userId, DocumentReference courseReference) {
        Error error;

        DocumentReference docRef = db.collection(DatabaseCollections.FAVOURITES_COLLECTION).document(userId);
        docRef.get().addOnFailureListener(documentSnapshot ->{
            Favourites favourites = new Favourites(userId, null);
            db.collection(DatabaseCollections.FAVOURITES_COLLECTION).document(userId).set(favourites);
        });

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            ArrayList<DocumentReference> favouriteCourseReference =
                    (ArrayList<DocumentReference>) documentSnapshot.get("courses");

            favouriteCourseReference.add(courseReference);


        });
        if ((error = validateItem(courseReference)) != null) {
            return error;
        }

        return null;

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


    public Error removeItem(String userId, int courseId) {
        final Error[] error = { null };

        DocumentReference docRef = db.collection(DatabaseCollections.FAVOURITES_COLLECTION)
                .document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                error[0] = new Error(ErrorCodes.INVALID_ID, ErrorMessages.INVALID_ID);
            }
            else
            {
                ArrayList<DocumentReference> favouriteCourseList =
                        (ArrayList<DocumentReference>) documentSnapshot.get("courses");
                favouriteCourseList.remove(courseId);
            }
        });

        if (error[0] != null) {
            return error[0];
        }

        return null;
    }


    public Error validateItem(DocumentReference item) {
        if (item == null) {
            return new Error(ErrorCodes.EMPTY_FIELDS, ErrorMessages.EMPTY_FIELDS);
        }

        return null;
    }
}
