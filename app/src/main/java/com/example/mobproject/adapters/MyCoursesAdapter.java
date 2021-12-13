package com.example.mobproject.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobproject.CoursePageActivity;
import com.example.mobproject.R;
import com.example.mobproject.constants.Intents;
import com.example.mobproject.constants.Other;
import com.example.mobproject.models.Course;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MyCoursesAdapter extends RecyclerView.Adapter<MyCoursesAdapter.ViewHolder>{

    private final LayoutInflater layoutInflater;
    private final ArrayList<Course> data;//change to List<Course>
    private final String[] difficulties;

    public MyCoursesAdapter(Context context, ArrayList<Course> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.difficulties = context.getResources().getStringArray(R.array.difficulties);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCoursesAdapter.ViewHolder holder, int position) {

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


        double finalScoreValue = Math.round(data.get(position).getRating() * 100.0) / 100.0;
        String finalScore = finalScoreValue + holder.itemView.getContext().getString(R.string.ratingOutOf);//function to calculate final score
        holder.courseFinalScore.setText(finalScore);

        SimpleDateFormat sdf = new SimpleDateFormat(holder.itemView.getContext().getString(R.string.date_format));
        String period = sdf.format(data.get(position).getStartDate()) + " - " + sdf.format(data.get(position).getEndDate());//startDate + " - " + endDate
        holder.coursePeriod.setText(period);

        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        String currency = format.format(data.get(position).getPrice());
        holder.coursePrice.setText(currency);

        boolean enroll = data.get(position).isOpenEnroll();
        String openEnroll = holder.itemView.getContext().getString(R.string.open_enroll);
        String closeEnroll = holder.itemView.getContext().getString(R.string.close_enroll);
        holder.courseEnroll.setText(enroll ? openEnroll : closeEnroll);

        holder.courseEnroll.setTextColor( enroll ?
                Color.parseColor("#22E865") :
                Color.parseColor("#F61616"));

        String categoryId = data.get(position).getCategoryId().getId();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileImgRef = storageReference.child(Other.CATEGORY_STORAGE_FOLDER)
                .child(categoryId+Other.CATEGORY_PHOTO_EXTENSION);
        profileImgRef.getDownloadUrl().addOnSuccessListener(uri ->
                Picasso.get().load(uri).into(holder.courseImage));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView courseTitle, courseDifficulty, courseFinalScore, coursePeriod, coursePrice, courseEnroll;
        ImageView courseImage;
        ImageButton addToFav;
        Integer addedToFav;

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



            //activate addToFav button
//            addedToFav = 0;
//            addToFav.setOnClickListener(view -> {
//
//                //check for addedToFav for each Course from DB
//                if(addedToFav == 0)//it is not a favourite Course
//                {
//                    addedToFav = 1;
//                    addToFav.setImageResource(R.drawable.ic_favourite_red);
//                }
//                else //it is already a favourite (addedToFav == 1)
//                {
//                    addedToFav = 0;
//                    addToFav.setImageResource(R.drawable.ic_favourite_black);
//                }
//            });

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
