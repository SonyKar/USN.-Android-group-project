package com.example.mobproject.models;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class UserCourses {
    private String id;
    private ArrayList<DocumentReference> courses;

    public UserCourses(String id, ArrayList<DocumentReference> courses) {
        this.courses = courses;
        this.id = id;
    }

    public ArrayList<DocumentReference> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<DocumentReference> courses) {
        this.courses = courses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
