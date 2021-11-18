package com.example.mobproject.db;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.constants.ErrorCodes;
import com.example.mobproject.constants.ErrorMessages;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Comment;
import com.example.mobproject.models.Course;
import com.example.mobproject.models.Error;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CommentDatabase extends Database<Comment> {

    @Override
    public void getItem(String id, Callback<Comment> callback) {
        DocumentReference docRef = db.collection(DatabaseCollections.COMMENTS_COLLECTION).document(id);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Comment comment = documentSnapshot.toObject(Comment.class);
            if (comment != null) {
                comment.setId(documentSnapshot.getId());
                ArrayList<Comment> commentList = new ArrayList<>();
                commentList.add(comment);
                callback.OnFinish(commentList);
            }
        });
    }

    @Override
    public void getItems(Callback<Comment> callback) {
        db.collection(DatabaseCollections.COMMENTS_COLLECTION).get().addOnSuccessListener(snapshots -> {
            ArrayList<Comment> commentsList = new ArrayList<>();
            for (QueryDocumentSnapshot document : snapshots) {
                Comment tmp = document.toObject(Comment.class);
                tmp.setId(document.getId());
                commentsList.add(tmp);
            }
            callback.OnFinish(commentsList);
        });
    }

    @Override
    public Error insertItem(Comment item) {
        Error error;
        DocumentReference newCommentRef = db.collection(DatabaseCollections.COMMENTS_COLLECTION).document();

        if ((error = validateItem(item)) != null) {
            return error;
        }

        newCommentRef.set(item);

        return null;
    }

    public void insertItem(Comment item, Callback<DocumentReference> callback) {
//        Error error;
        DocumentReference newCommentRef = db.collection(DatabaseCollections.COMMENTS_COLLECTION).document();

//        if ((error = validateItem(item)) != null) {
//            return error;
//        }

        newCommentRef.set(item);
        callback.OnFinish(new ArrayList<DocumentReference>() { { add(newCommentRef); } });
    }

    @Override
    public Error updateItem(String id, Comment item) {
        return null;
    }

    @Override
    public Error removeItem(String id) {
        return null;
    }

    @Override
    public Error validateItem(Comment item) {
        if (item.getCommentText().trim().isEmpty()) {
            return new Error(ErrorCodes.EMPTY_FIELDS, ErrorMessages.EMPTY_FIELDS);
        }

        return null;
    }
}
