package com.example.mobproject.constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

public class UserInfo {
    SharedPreferences sharedPreferences;

    public UserInfo(AppCompatActivity view) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getApplicationContext());
    }

    public UserInfo(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
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

    public void resetUserInfo() {
        sharedPreferences.edit().clear().apply();
    }
}
