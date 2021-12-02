package com.example.mobproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobproject.constants.UserInfo;
import com.example.mobproject.constants.UserType;
import com.example.mobproject.db.UserDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.User;
import com.example.mobproject.navigation.MenuDrawer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {
    private TextView userName, userStatus, userEmail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        FloatingActionButton editProfile = findViewById(R.id.edit_profile_fab);
        userName = findViewById(R.id.profile_name);
        userStatus = findViewById(R.id.user_status);
        userEmail = findViewById(R.id.profile_email);

        MenuDrawer.actionBarInit(this);

        editProfile.setOnClickListener(switchToEditProfile);

    }

    private void initProfile() {
        UserInfo userInfo = new UserInfo(this);
        String userID = userInfo.getUserId();
        UserDatabase userDatabase = new UserDatabase();
        userDatabase.getItem(userID, new Callback<User>() {
            @Override
            public void OnFinish(ArrayList<User> arrayList) {
                User user = arrayList.get(0);
                userName.setText(user.getName());
                userEmail.setText(user.getEmail());

                int userType = Integer.parseInt(userInfo.getUserType());

                userStatus.setText(UserType.values()[userType].toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initProfile();
    }

    private final View.OnClickListener switchToEditProfile = view -> {
        Intent toEditProfile = new Intent(this, EditProfileActivity.class);
        startActivity(toEditProfile);
    };

    //color menu item

    public void onBackPressed(){
        MenuDrawer.onBackHandler();
    }
}
