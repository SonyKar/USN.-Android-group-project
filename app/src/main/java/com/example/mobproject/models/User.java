package com.example.mobproject.models;

public class User {
    private String id;
    private String name;
    private String email;
    private int userType;

    // Constructor to create the user
    public User(String name, String email, int userType) {
        this.name = name;
        this.email = email;
        this.userType = userType;
    }

    // Constructor to read the user from the db
    public User(String id, String name, String email, int userType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userType = userType;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
