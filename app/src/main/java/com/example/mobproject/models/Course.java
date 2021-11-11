package com.example.mobproject.models;

import com.example.mobproject.constants.WeekDay;

import java.util.ArrayList;
import java.util.Date;

public class Course {
    private String id;
    private String name;
    private double rating;
    private ArrayList<String> categoryId = new ArrayList<>();
    private double price;
    private int difficulty;
    private String ownerId;
    private boolean openEnroll;
    private Date startDate;
    private Date endDate;
    private int rateCounter;
    private int studentCounter;
    private ArrayList <WeekDay> meetDays = new ArrayList<>();
    private String description;
    private ArrayList <Comment> comments = new ArrayList<>();

    public Course(String name, ArrayList<String> categoryId, double price,
                  int difficulty, String ownerId, Date startDate, Date endDate,
                  ArrayList<WeekDay> meetDays, String description) {
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.difficulty = difficulty;
        this.ownerId = ownerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.meetDays = meetDays;
        this.description = description;
    }

    public Course(String id) {
        this.id = id;
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

    public ArrayList<String> getCategoryId() {
        return categoryId;
    }

    public double getPrice() {
        return price;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getOwnerId() {
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

    public ArrayList<WeekDay> getMeetDays() {
        return meetDays;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setCategoryId(ArrayList<String> categoryId) {
        this.categoryId = categoryId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setOwnerId(String ownerId) {
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

    public void setMeetDays(ArrayList<WeekDay> meetDays) {
        this.meetDays = meetDays;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
