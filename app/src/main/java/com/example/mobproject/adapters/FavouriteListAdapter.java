package com.example.mobproject.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobproject.CoursePageActivity;
import com.example.mobproject.R;
import com.example.mobproject.constants.DatabaseCollections;
import com.example.mobproject.constants.Intents;
import com.example.mobproject.constants.UserInfo;
import com.example.mobproject.db.FavouriteCoursesDatabase;
import com.example.mobproject.models.Course;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FavouriteListAdapter extends RecyclerView.Adapter<FavouriteListAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final ArrayList<Course> data;
    private final String[] difficulties;
    private final String userId;
    private boolean isFavourite = true;
    private final UserInfo userInfo;

    public FavouriteListAdapter(Context context, ArrayList<Course> data, String userId) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.difficulties = context.getResources().getStringArray(R.array.difficulties);
        this.userId = userId;
        this.userInfo = new UserInfo(context.getApplicationContext());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteListAdapter.ViewHolder holder, int position) {

        //bind the textview with data received

        String title = data.get(position).getName();
        holder.courseTitle.setText(title);

        int difficulty = data.get(position).getDifficulty();

        holder.courseDifficulty.setText(difficulties[difficulty]);
        switch (difficulty) {
            case 0:
                holder.courseDifficulty.setTextColor(Color.parseColor("#22E865"));
                break;
            case 1:
                holder.courseDifficulty.setTextColor(Color.parseColor("#F8C81C"));
                break;
            default:
                holder.courseDifficulty.setTextColor(Color.parseColor("#F61616"));
                break;
        }


        double finalScoreValue = Math.round(data.get(position).getRating() * 100.0) / 100.0;
        String finalScore = finalScoreValue + holder.itemView.getContext().getString(R.string.ratingOutOf);//function to calculate final score
        holder.courseFinalScore.setText(finalScore);

        SimpleDateFormat sdf = new SimpleDateFormat(holder.itemView.getContext().getString(R.string.date_format), Locale.getDefault());
        String period = sdf.format(data.get(position).getStartDate()) + " - " + sdf.format(data.get(position).getEndDate());//startDate + " - " + endDate
        holder.coursePeriod.setText(period);

        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        String currency = format.format(data.get(position).getPrice());
        holder.coursePrice.setText(currency);

        boolean enroll = data.get(position).isOpenEnroll();
        String openEnroll = holder.itemView.getContext().getString(R.string.open_enroll);
        String closeEnroll = holder.itemView.getContext().getString(R.string.close_enroll);
        holder.courseEnroll.setText(enroll ? openEnroll : closeEnroll);

        holder.courseEnroll.setTextColor(enroll ?
                Color.parseColor("#22E865") :
                Color.parseColor("#F61616"));

        String categoryId = data.get(position).getCategoryId().getId();
        DocumentReference categoryRef = FirebaseFirestore.getInstance()
                .collection(DatabaseCollections.CATEGORIES_COLLECTION).document(categoryId);
        categoryRef.get().addOnCompleteListener(task ->{
            if ( task.isSuccessful() ) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if ( documentSnapshot != null && documentSnapshot.exists() ){
                    String categoryName = (String) documentSnapshot.get("fileName");
                    Context context = holder.courseImage.getContext();
                    int drawableId = context.getResources().getIdentifier(categoryName, "drawable", context.getPackageName());
                    Picasso.get().load(drawableId).into(holder.courseImage);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        TextView courseTitle, courseDifficulty, courseFinalScore, coursePeriod, coursePrice, courseEnroll;
        ImageView courseImage;
        ImageButton addToFav;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.course_title);
            courseDifficulty = itemView.findViewById(R.id.course_difficulty);
            courseFinalScore = itemView.findViewById(R.id.final_score);
            coursePeriod = itemView.findViewById(R.id.card_view_period);
            coursePrice = itemView.findViewById(R.id.card_view_price);
            courseEnroll = itemView.findViewById(R.id.card_view_enroll);
            courseImage = itemView.findViewById(R.id.course_img);
            addToFav = itemView.findViewById(R.id.add_to_fav_cardView);

            addToFav.setImageResource(R.drawable.ic_favourite_red);

            addToFav.setOnClickListener(view -> {
                FavouriteCoursesDatabase favouritesDatabase = new FavouriteCoursesDatabase();
                String courseId = data.get(getAbsoluteAdapterPosition()).getId();
                if (!isFavourite) {
                    addToFav.setImageResource(R.drawable.ic_favourite_red);
                    favouritesDatabase.insertItem(userId, courseId);
                    isFavourite = true;
                    userInfo.setUserFavouritesNo(userInfo.getUserFavouritesNo()+1);
                } else {
                    addToFav.setImageResource(R.drawable.ic_favourite_black);
                    favouritesDatabase.removeItem(userId, courseId);
                    userInfo.setUserFavouritesNo(userInfo.getUserFavouritesNo()-1);
                    isFavourite = false;
                }
//                itemView.setVisibility(View.GONE);

            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent toCourseProfile = new Intent(view.getContext(), CoursePageActivity.class);
            toCourseProfile.putExtra(Intents.COURSE_ID, data.get(getAbsoluteAdapterPosition()).getId());
            startActivity(view.getContext(), toCourseProfile, new Bundle());
        }
    }
}
