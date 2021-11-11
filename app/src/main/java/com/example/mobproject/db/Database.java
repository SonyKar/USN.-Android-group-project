package com.example.mobproject.db;

import com.example.mobproject.models.Error;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class Database<T> {
    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();

//    public abstract T getItem(String id);
//    public abstract ArrayList<T> getItems();
    public abstract Error insertItem(T item);
    public abstract Error updateItem(String id, T item);
    public abstract Error removeItem(String id);
    public abstract Error validateItem(T item);
}
