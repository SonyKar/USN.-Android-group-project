package com.example.mobproject.notifications;

import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.mobproject.R;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    String courseName, senderName, receiver;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        courseName = remoteMessage.getData().get("courseName");
        senderName = remoteMessage.getData().get("senderName");

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_app_mob)
                .setContentTitle("New comment")
                .setContentText(senderName+" added a comment on your course "+courseName);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
