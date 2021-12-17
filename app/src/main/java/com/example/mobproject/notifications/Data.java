package com.example.mobproject.notifications;

public class Data {
    private String courseName;
    private String senderName;

    public Data(String courseName, String senderName) {
        this.courseName = courseName;
        this.senderName = senderName;
    }

    public Data(){}

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
