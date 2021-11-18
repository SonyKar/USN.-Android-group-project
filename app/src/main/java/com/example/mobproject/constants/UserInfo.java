package com.example.mobproject.constants;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobproject.constants.Other;

public class UserInfo {
    SharedPreferences sharedPreferences;

    public UserInfo(AppCompatActivity view) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getApplicationContext());
    }

    public String getUserId() {
        return sharedPreferences.getString(Other.SHARED_PREF_USERID, Other.SHARED_PREF_NODATA);
    }

    public String getUserType() {
        return sharedPreferences.getString(Other.SHARED_PREF_USERTYPE, Other.SHARED_PREF_NODATA);
    }

    public void setUserId(String uid) {
        sharedPreferences.edit().putString(Other.SHARED_PREF_USERID, uid).apply();
    }

    public void setUserType(String userType) {
        sharedPreferences.edit().putString(Other.SHARED_PREF_USERTYPE, userType).apply();
    }
}
