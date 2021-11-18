package com.example.mobproject.controllers;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.db.CommentDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Comment;
import com.example.mobproject.models.Course;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CourseController {
    public Course addRating(Course course, double rating) {
        double totalRating = course.getRating();
        int ratingCounter = course.getRateCounter();

        totalRating = ((totalRating * ratingCounter) + rating) / (++ratingCounter);

        FirebaseFirestore.getInstance()
                .collection(DatabaseCollections.COURSES_COLLECTION)
                .document(course.getId())
                .update(
                        "rateCounter", ratingCounter,
                        "rating", totalRating
                );

        course.setRating(totalRating);
        course.setRateCounter(ratingCounter);

        return course;
    }

    public Course addComment(Course course, String userId, String commentText) {
        String refPath = FirebaseFirestore.getInstance()
                .collection(DatabaseCollections.USER_COLLECTION)
                .document(userId).getPath();
        DocumentReference userRef = FirebaseFirestore.getInstance().document(refPath);
        Comment comment = new Comment(userRef, commentText);

        CommentDatabase commentDatabase = new CommentDatabase();
        DocumentReference newCommentRef = commentDatabase.insertItem(comment, new Callback<DocumentReference>() {
            @Override
            public void OnFinish(ArrayList<DocumentReference> arrayList) {
                FirebaseFirestore.getInstance()
                        .collection(DatabaseCollections.COURSES_COLLECTION)
                        .document(course.getId()).update("commentsReferences", FieldValue.arrayUnion(arrayList.get(0)));
            }
        });

        List<DocumentReference> tmpList = course.getCommentsReferences();
        if (tmpList == null) tmpList = new ArrayList<>();
        tmpList.add(newCommentRef);
        course.setCommentsReferences(tmpList);

        return course;
    }
}
