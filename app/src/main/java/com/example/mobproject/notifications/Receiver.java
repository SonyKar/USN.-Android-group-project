package com.example.mobproject.notifications;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Receiver {
    private static Retrofit retrofit = null;
    public static Retrofit getReceiver(String url){
        Log.d("notificationSend","retrofit");
        if(retrofit == null)
            retrofit = new Retrofit.Builder().baseUrl(url).
                    addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;
    }
}
