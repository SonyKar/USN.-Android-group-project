package com.example.mobproject.controllers;

import android.util.Log;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.db.CommentDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Comment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CourseController {
    public void addRating(String courseId, double rating) {

    }

    public void addComment(String courseId, String userId, String commentText) {
        String refPath = FirebaseFirestore.getInstance()
                .collection(DatabaseCollections.USER_COLLECTION)
                .document(userId).getPath();
        DocumentReference userRef = FirebaseFirestore.getInstance().document(refPath);
        Comment comment = new Comment(userRef, commentText);

        CommentDatabase commentDatabase = new CommentDatabase();
        commentDatabase.insertItem(comment, new Callback<DocumentReference>() {
            @Override
            public void OnFinish(ArrayList<DocumentReference> arrayList) {
                FirebaseFirestore.getInstance()
                        .collection(DatabaseCollections.COURSES_COLLECTION)
                        .document(courseId).update("commentsReferences", FieldValue.arrayUnion(arrayList.get(0)));
            }
        });
    }
}
