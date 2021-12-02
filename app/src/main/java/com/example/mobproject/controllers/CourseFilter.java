package com.example.mobproject.controllers;

import com.example.mobproject.models.Course;

import java.util.ArrayList;

public class CourseFilter {
    public ArrayList<Course> filterDifficulty (ArrayList<Course> courseList, ArrayList<Integer> difficulties) {
        ArrayList<Course> filteredCourses = new ArrayList<>();
        for (Course course : courseList) {
            if (difficulties.contains(course.getDifficulty())) {
                filteredCourses.add(course);
            }
        }

        courseList = filteredCourses;

        return courseList;
    }

    public ArrayList<Course> filterPrice (ArrayList<Course> courseList, double minPrice, double maxPrice) {
        ArrayList<Course> filteredCourses = new ArrayList<>();
        for (Course course : courseList) {
            if ((course.getPrice() >= minPrice) && (course.getPrice() <= maxPrice)) {
                filteredCourses.add(course);
            }
        }

        courseList = filteredCourses;

        return courseList;
    }

    public ArrayList<Course> filterEnroll (ArrayList<Course> courseList, boolean isOpenEnroll) {
        ArrayList<Course> filteredCourses = new ArrayList<>();
        for (Course course : courseList) {
            if (course.isOpenEnroll() == isOpenEnroll) {
                filteredCourses.add(course);
            }
        }

        courseList = filteredCourses;

        return courseList;
    }

    public ArrayList<Course> filterName (ArrayList<Course> courseList, String name) {
        ArrayList<Course> filteredCourses = new ArrayList<>();
        for (Course course : courseList) {
            if (course.getName().toLowerCase().contains(name.toLowerCase())) {
                filteredCourses.add(course);
            }
        }

        courseList = filteredCourses;

        return courseList;
    }
}
