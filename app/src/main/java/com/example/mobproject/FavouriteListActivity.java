package com.example.mobproject;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mobproject.adapters.FavouriteListAdapter;
import com.example.mobproject.constants.UserInfo;
import com.example.mobproject.db.CourseDatabase;
import com.example.mobproject.db.FavouriteCoursesDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Course;
import com.example.mobproject.navigation.MenuDrawer;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class FavouriteListActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;

    private String userId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_list);

        context = this;

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        UserInfo userInfo = new UserInfo(this);
        userId = userInfo.getUserId();

        MenuDrawer.actionBarInit(this);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            RearrangeItems();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fillCourses();
    }

    private void fillCourses() {
        FavouriteCoursesDatabase database = new FavouriteCoursesDatabase();
        database.getItems(userId, recyclerViewCallback);
    }

    private final Callback<DocumentReference> recyclerViewCallback = new Callback<DocumentReference>() {
        @Override
        public void OnFinish(ArrayList<DocumentReference> favouriteReferences) {
            ArrayList<Course> favouriteCourseList = new ArrayList<>();
            CourseDatabase courseDatabase = new CourseDatabase();

            for (DocumentReference courseRef : favouriteReferences) {
                courseDatabase.getItem(courseRef.getId(), new Callback<Course>() {
                    @Override
                    public void OnFinish(ArrayList<Course> arrayList) {
                        favouriteCourseList.add(arrayList.get(0));

                        if (favouriteCourseList.size() == favouriteReferences.size()) {
                            RecyclerView favListRecyclerView = findViewById(R.id.fav_list);
                            favListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                            FavouriteListAdapter adapter = new FavouriteListAdapter(context, favouriteCourseList, userId);
                            favListRecyclerView.setAdapter(adapter);
                        }
                    }
                });
            }
        }
    };


    public void onBackPressed() {
        MenuDrawer.onBackHandler();
    }

    public void RearrangeItems() {
        fillCourses();
    }

}
