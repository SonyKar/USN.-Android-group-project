package com.example.mobproject.notifications;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.mobproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    String courseName, senderName, receiver;

//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        courseName = remoteMessage.getData().get("courseName");
//        senderName = remoteMessage.getData().get("senderName");
//        Log.d("notificationSending","receivedMessage");
//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(getApplicationContext())
//                .setSmallIcon(R.mipmap.ic_app_mob)
//                .setContentTitle("New comment")
//                .setContentText(senderName+" added a comment on your course "+courseName);
//        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(0, builder.build());
//    }
//
//
//    @Override
//    public void onNewToken(@NonNull String s) {
//        super.onNewToken(s);
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//            @Override
//            public void onComplete(@NonNull Task<String> task) {
//                if (!task.isSuccessful()) {
//                    Log.w("tokenFetch", "Fetching FCM registration token failed", task.getException());
//                    return;
//                }
//                String refreshToken = task.getResult();
//                if(firebaseUser!=null){
//                    Token token = new Token(refreshToken);
//                    FirebaseDatabase.getInstance().getReference("Tokens")
//                            .child(firebaseUser.getUid()).setValue(token);
//                }
//            }
//        });
//
//    }
}
