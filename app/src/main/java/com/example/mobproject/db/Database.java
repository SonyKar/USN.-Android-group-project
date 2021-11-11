package com.example.mobproject.db;

import com.example.mobproject.models.Error;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public abstract class Database<T> {
//    protected final FirebaseDatabase database = FirebaseDatabase.getInstance();
    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();

    abstract T getItem(String id);
    abstract ArrayList<T> getItems();
    abstract Error insertItem(T item);
    abstract Error updateItem(String id, T item);
    abstract Error removeItem(String id);
    abstract Error validateItem(T item);
}
