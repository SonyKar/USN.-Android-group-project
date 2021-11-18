package com.example.mobproject.validations;

public class Validator {
    public static boolean isInvalidEmail(String email) {//!!doesn't work!!
        String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        return !email.matches(emailPattern);
    }
}
