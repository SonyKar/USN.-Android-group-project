package com.example.mobproject.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type: application/json",
                    "Authorization: key=AAAAThSGAZE:APA91bENGlaj5gUU4JwZZZbwfab8vUIxJc5ksZIQjgBfNdLP8P_dSGsxHKM7LMCmB7zVoRT6F4pnWb1cUJmknlwkJahWLPn4elaRaa_zyzirOwdp31Ro8DRuK7sGnynDWJE5R4qCKy5t"
            }
    )

    @POST("fcm/send")
    Call<Response> sendNotification(@Body NotificationSender body);
}
