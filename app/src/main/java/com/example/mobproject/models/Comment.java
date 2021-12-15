package com.example.mobproject.models;

import com.google.firebase.firestore.DocumentReference;

public class Comment {
    private String id;
    private DocumentReference userId;
    private String commentText;

    public Comment() {
    }

    public Comment(DocumentReference userId, String commentText) {
        this.userId = userId;
        this.commentText = commentText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocumentReference getUserId() {
        return userId;
    }

    public void setUserId(DocumentReference userId) {
        this.userId = userId;
    }

    public String getCommentText() {
        return commentText;
    }
}
