package com.example.mobproject.db;

import com.example.mobproject.models.Error;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public abstract class Database<T> {
    protected final FirebaseDatabase database = FirebaseDatabase.getInstance();

    abstract T getItem(String id);
    abstract ArrayList<T> getItems();
    abstract Error updateItem(String id, T item);
    abstract Error insertItem(T item);
    abstract Error removeItem(String id);
    abstract Error validateItem(T item);
}
