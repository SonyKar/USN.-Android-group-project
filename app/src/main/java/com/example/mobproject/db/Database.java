package com.example.mobproject.db;

import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Error;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public abstract class Database<T> {
    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public abstract void getItem(String id, Callback<T> callback);
    public abstract void getItems(Callback<T> callback);
    public abstract Error insertItem(T item);
    public abstract Error updateItem(String id, T item);
    public abstract Error removeItem(String id);
    public abstract Error validateItem(T item);
}
