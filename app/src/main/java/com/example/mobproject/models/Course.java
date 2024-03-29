package com.example.mobproject.models;

import com.google.firebase.firestore.DocumentReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Course {
    private String id;
    private String name;
    private double rating;
    private DocumentReference categoryId;
    private double price;
    private int difficulty;
    private DocumentReference ownerId;
    private Date startDate;
    private Date endDate;
    private int rateCounter;
    private int studentCounter;
    private final List<Integer> meetDays = new ArrayList<>();
    private String description;
    private List<DocumentReference> commentsReferences = new ArrayList<>();

    //Create new course
    public Course(String name, DocumentReference categoryId, double price,
                  int difficulty, DocumentReference ownerId, String startDate, String endDate,
                  List<Integer> meetDays, String description, int rateCounter,
                  int studentCounter, double rating, List<DocumentReference> commentsReferences) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.difficulty = difficulty;
        this.ownerId = ownerId;
        this.startDate = formatter.parse(startDate);
        this.endDate = formatter.parse(endDate);
        this.meetDays.addAll(meetDays);
        this.commentsReferences = commentsReferences;
        this.description = description;
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
        return startDate.after(Calendar.getInstance().getTime());
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

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setRateCounter(int rateCounter) {
        this.rateCounter = rateCounter;
    }

    public void setCommentsReferences(List<DocumentReference> commentsReferences) {
        this.commentsReferences = commentsReferences;
    }
}
