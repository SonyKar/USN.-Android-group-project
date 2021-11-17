package com.example.mobproject.db;

import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Category;
import com.example.mobproject.models.Course;
import com.example.mobproject.models.Error;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CategoryDatabase extends Database<Category> {

    @Override
    public void getItem(String id, Callback<Category> callback) {
        DocumentReference docRef = db.collection(DatabaseCollections.CATEGORIES_COLLECTION).document(id);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Category category = documentSnapshot.toObject(Category.class);
            if (category != null) {
                category.setId(documentSnapshot.getId());
                ArrayList<Category> categoryList = new ArrayList<>();
                categoryList.add(category);
                callback.OnFinish(categoryList);
            }
        });
    }

    @Override
    public void getItems(Callback<Category> callback) {
        db.collection(DatabaseCollections.CATEGORIES_COLLECTION).get().addOnSuccessListener(snapshots -> {
            ArrayList<Category> categoryList = new ArrayList<>();
            for (QueryDocumentSnapshot document : snapshots) {
                Category tmp = document.toObject(Category.class);
                tmp.setId(document.getId());
                categoryList.add(tmp);
            }
            callback.OnFinish(categoryList);
        });
    }


    @Override
    public Error insertItem(Category item) {
        return null;
    }

    @Override
    public Error updateItem(String id, Category item) {
        return null;
    }

    @Override
    public Error removeItem(String id) {
        return null;
    }

    @Override
    public Error validateItem(Category item) {
        return null;
    }
}
