package com.example.mobproject.controllers;

import com.example.mobproject.models.Course;

import java.util.ArrayList;
import java.util.Collections;

public class CourseSort {
    public ArrayList<Course> sortByName(ArrayList<Course> courseList, boolean isAscending) {
        Collections.sort(courseList, (course, nextCourse) -> {
            if (isAscending) {
                return course.getName().compareTo(nextCourse.getName());
            } else {
                return nextCourse.getName().compareTo(course.getName());
            }
        });

        return courseList;
    }

    public ArrayList<Course> sortByPrice(ArrayList<Course> courseList, boolean isAscending) {
        Collections.sort(courseList, (course, nextCourse) -> {
            if (isAscending) {
                return (int)(course.getPrice() - nextCourse.getPrice());
            } else {
                return (int)(nextCourse.getPrice() - course.getPrice());
            }
        });

        return courseList;
    }

    public ArrayList<Course> sortByRating(ArrayList<Course> courseList, boolean isAscending) {
        Collections.sort(courseList, (course, nextCourse) -> {
            if (isAscending) {
                return (int)(course.getRating() - nextCourse.getRating());
            } else {
                return (int)(nextCourse.getRating() - course.getRating());
            }
        });

        return courseList;
    }

    public ArrayList<Course> sortByTime(ArrayList<Course> courseList, boolean isAscending) {
        Collections.sort(courseList, (course, nextCourse) -> {
            if (isAscending) {
                return course.getStartDate().compareTo(nextCourse.getStartDate());
            } else {
                return nextCourse.getStartDate().compareTo(course.getStartDate());
            }
        });

        return courseList;
    }
}
