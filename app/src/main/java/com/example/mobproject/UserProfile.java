package com.example.mobproject;

import static com.example.mobproject.MenuDrawer.setupDrawerContent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mobproject.constants.UserType;
import com.example.mobproject.db.UserDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {
    private DrawerLayout drawer;
    private TextView userName, userStatus, userEmail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        drawer = findViewById(R.id.drawer_layout_profile);
        FloatingActionButton editProfile = findViewById(R.id.edit_profile_fab);
        userName = findViewById(R.id.profile_name);
        userStatus = findViewById(R.id.user_status);
        userEmail = findViewById(R.id.profile_email);

        actionBarInit();

        editProfile.setOnClickListener(switchToEditProfile);

        initProfile();
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

    private void actionBarInit() {
        NavigationView navigationView = findViewById(R.id.nav_view_profile);
        setupDrawerContent(navigationView, getApplicationContext(), drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
    }

    private final View.OnClickListener switchToEditProfile = view -> {
        Intent toEditProfile = new Intent(this, UserEdit.class);
        startActivity(toEditProfile);
    };

    //color menu item

    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }
}
