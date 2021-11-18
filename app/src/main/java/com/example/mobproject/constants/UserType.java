package com.example.mobproject.constants;
import androidx.annotation.NonNull;

public enum UserType {
    STUDENT("Student"),
    TEACHER("Teacher");

    private final String text;

    UserType(final String text) {
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        return text;
    }
}
