package com.example.mobproject.validations;

import android.util.Log;

import com.example.mobproject.constants.UserType;
import com.example.mobproject.models.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidation {
    public static boolean isEmail(String email) {//!!doesn't work!!
        /*Pattern p = Pattern.compile("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b");
        Matcher m = p.matcher(email);

        return m.find();*/

        //alternate regex:  "[a-zA-Z0-9._-]+@[a-z]+\.+[a-z]+"
        String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        if(email.matches(emailPattern)) {
            Log.d("Email", "email matches");
            return true;
        }
        return false;


    }

    public static boolean isCorrectType(int type) {
        return (type >= 0) && (type < UserType.TYPE_COUNT);
    }

    public static boolean isEmpty(User item) {
        return item.getEmail().isEmpty() || item.getName().isEmpty() || isCorrectType(item.getUserType());
    }
}
