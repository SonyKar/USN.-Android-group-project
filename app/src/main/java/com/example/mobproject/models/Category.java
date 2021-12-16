package com.example.mobproject.models;

import androidx.annotation.NonNull;

public class Category {
    private String id;
    private String name;
    private String fileName;

    public Category(String name) {
        this.name = name;
    }

    public Category() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
