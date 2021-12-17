package com.example.mobproject.notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseIdService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("tokenFetch", "Fetching FCM registration token failed", task.getException());
                    return;
                }
                String refreshToken = task.getResult();
                if(firebaseUser!=null){
                    Token token = new Token(refreshToken);
                    FirebaseDatabase.getInstance().getReference("Tokens")
                            .child(firebaseUser.getUid()).setValue(token);
                }
            }
        });
    }
}
