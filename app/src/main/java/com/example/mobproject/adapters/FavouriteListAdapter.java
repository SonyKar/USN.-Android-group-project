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
import com.example.mobproject.constants.Intents;
import com.example.mobproject.db.FavouriteCoursesDatabase;
import com.example.mobproject.models.Course;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FavouriteListAdapter extends RecyclerView.Adapter<FavouriteListAdapter.ViewHolder>{

    private final LayoutInflater layoutInflater;
    private final ArrayList<Course> data;
    private final String[] difficulties;
    private final String userId;

    public FavouriteListAdapter(Context context, ArrayList<Course> data, String userId){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.difficulties = context.getResources().getStringArray(R.array.difficulties);
        this.userId = userId;
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
        switch(difficulty){
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


        String finalScore = Math.round(data.get(position).getRating() * 100.0) / 100.0 + "/5.00";//function to calculate final score
        holder.courseFinalScore.setText(finalScore);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String period = sdf.format(data.get(position).getStartDate()) + " - " + sdf.format(data.get(position).getEndDate());//startDate + " - " + endDate
        holder.coursePeriod.setText(period);

        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        String currency = format.format(data.get(position).getPrice());
        holder.coursePrice.setText(currency);

        // TODO to set all the hardcoded values to a variable
        boolean enroll = data.get(position).isOpenEnroll();
        holder.courseEnroll.setText(enroll ? "Open to enroll" : "Close to enroll");

        holder.courseEnroll.setTextColor( enroll ?
                Color.parseColor("#22E865") :
                Color.parseColor("#F61616"));

        /*ImageView image = data.get(position);//get from DB
        holder.courseImage.setImageResource(image);*/

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
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
            addToFav = itemView.findViewById(R.id.add_to_fav_cardview);

            addToFav.setImageResource(R.drawable.ic_favourite_red);

            addToFav.setOnClickListener(view -> {
                addToFav.setImageResource(R.drawable.ic_favourite_black);
                FavouriteCoursesDatabase favouritesDB = new FavouriteCoursesDatabase();
                favouritesDB.removeItem(userId, data.get(getAdapterPosition()).getId());
                itemView.setVisibility(View.GONE);
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent toCourseProfile = new Intent(view.getContext(), CoursePageActivity.class);
            toCourseProfile.putExtra(Intents.COURSE_ID, data.get(getAdapterPosition()).getId());
            startActivity(view.getContext() , toCourseProfile, new Bundle());
        }
    }
}
