package com.example.mobproject.models;

import com.example.mobproject.constants.DatabaseCollections;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class UserCourses {
    private ArrayList<DocumentReference> courses = new ArrayList<>();
    private String id;

    public UserCourses(String id, ArrayList<DocumentReference> courses) {
        this.courses = courses;
        this.id = id;
    }
    public UserCourses(String userId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(DatabaseCollections.FAVOURITES_COLLECTION).document(userId);
        this.id = userId;
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            this.courses =(ArrayList<DocumentReference>) documentSnapshot.get("courses");
        });

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
