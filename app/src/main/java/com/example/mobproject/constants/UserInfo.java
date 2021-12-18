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
        return sharedPreferences.getString(SharedPreferencesInfo.SHARED_PREF_USERID, SharedPreferencesInfo.SHARED_PREF_NODATA_STRING);
    }

    public String getUserType() {
        return sharedPreferences.getString(SharedPreferencesInfo.SHARED_PREF_USERTYPE, SharedPreferencesInfo.SHARED_PREF_NODATA_STRING);
    }

    public void setUserId(String uid) {
        sharedPreferences.edit().putString(SharedPreferencesInfo.SHARED_PREF_USERID, uid).apply();
    }

    public void setUserType(String userType) {
        sharedPreferences.edit().putString(SharedPreferencesInfo.SHARED_PREF_USERTYPE, userType).apply();
    }

    public int getUserCoursesNo(){
        return sharedPreferences.getInt(SharedPreferencesInfo.SHARED_PREF_ENROLLED, SharedPreferencesInfo.SHARED_PREF_NODATA_INT);
    }

    public void setUserCoursesNo(int number){
        sharedPreferences.edit().putInt(SharedPreferencesInfo.SHARED_PREF_ENROLLED, number).apply();
    }

    public int getUserFavouritesNo(){
        return sharedPreferences.getInt(SharedPreferencesInfo.SHARED_PREF_FAVOURITES, SharedPreferencesInfo.SHARED_PREF_NODATA_INT);
    }

    public void setUserFavouritesNo(int number){
        sharedPreferences.edit().putInt(SharedPreferencesInfo.SHARED_PREF_FAVOURITES, number).apply();
    }

    public void setUserPassword(String password) {
        sharedPreferences.edit().putString(SharedPreferencesInfo.SHARED_PREF_PASSWORD, password).apply();
    }

    public void resetUserInfo() {
        sharedPreferences.edit().clear().apply();
    }

}
