package com.example.mobproject.models;

import com.google.firebase.firestore.DocumentReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Course {
    private String id;
    private String name;
    private double rating;
    private DocumentReference categoryId;
    private double price;
    private int difficulty;
    private DocumentReference ownerId;
    private boolean openEnroll;
    private Date startDate;
    private Date endDate;
    private int rateCounter;
    private int studentCounter;
    private List<Integer> meetDays = new ArrayList<>();
    private String description;
    private List<DocumentReference> commentsReferences = new ArrayList<>();

    //Create new course
    public Course(String name, DocumentReference categoryId, double price,
                  int difficulty, DocumentReference ownerId, String startDate, String endDate,
                  ArrayList<Integer> meetDays, String description, int rateCounter,
                  int studentCounter, double rating) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.difficulty = difficulty;
        this.ownerId = ownerId;
        this.startDate = formatter.parse(startDate);
        this.endDate = formatter.parse(endDate);
        for (int i: meetDays) {
            this.meetDays.add(i);
        }
        this.description = description;
        this.openEnroll = true;
        this.rating = rating;
        this.studentCounter = studentCounter;
        this.rateCounter = rateCounter;
    }

    public Course(String id) {
        this.id = id;
    }

    public Course() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public DocumentReference getCategoryId() {
        return categoryId;
    }

    public double getPrice() {
        return price;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public DocumentReference getOwnerId() {
        return ownerId;
    }

    public boolean isOpenEnroll() {
        return openEnroll;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getRateCounter() {
        return rateCounter;
    }

    public int getStudentCounter() {
        return studentCounter;
    }

    public List<Integer> getMeetDays() {
        return meetDays;
    }

    public String getDescription() {
        return description;
    }

    public List<DocumentReference> getCommentsReferences() {
        return commentsReferences;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setCategoryId(DocumentReference categoryId) {
        this.categoryId = categoryId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setOwnerId(DocumentReference ownerId) {
        this.ownerId = ownerId;
    }

    public void setOpenEnroll(boolean openEnroll) {
        this.openEnroll = openEnroll;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMeetDays(List<Integer> meetDays) {
        this.meetDays = meetDays;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCommentsReferences(List<DocumentReference> commentsReferences) {
        this.commentsReferences = commentsReferences;
    }
}
