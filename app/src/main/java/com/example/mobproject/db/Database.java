package com.example.mobproject.db;

import com.example.mobproject.interfaces.Callback;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class Database<T> {
    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public abstract void getItem(String id, Callback<T> callback);
    public abstract void getItems(Callback<T> callback);
    public abstract void insertItem(T item);
    public abstract void updateItem(String id, T item, Callback<T> callback);
}
