package com.example.mobproject.models;

public class Comment {
    private String id;
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String commentText;


    //Constructor based on comment id
    public Comment(String id) {
        this.id = id;
    }

    //Constructor based on userId and commentText

    public Comment(String userId, String commentText) {
        this.userId = userId;
        this.commentText = commentText;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
