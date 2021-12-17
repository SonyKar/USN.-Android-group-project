package com.example.mobproject.notifications;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Receiver {
    private static Retrofit retrofit = null;
    public static Retrofit getReceiver(String url){
        if(retrofit == null)
            retrofit = new Retrofit.Builder().baseUrl(url).
                    addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;
    }
}
