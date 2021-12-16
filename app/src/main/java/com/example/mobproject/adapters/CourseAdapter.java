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

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final ArrayList<Course> data;
    private final ArrayList<DocumentReference> favouriteReferences;
    private final String[] difficulties;
    private final String userId;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final UserInfo userInfo;
    private final Context context;

    public CourseAdapter(Context context, ArrayList<Course> data,
                         ArrayList<DocumentReference> favouriteReferences, String userId) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.difficulties = context.getResources().getStringArray(R.array.difficulties);
        this.userId = userId;
        this.favouriteReferences = favouriteReferences;
        this.userInfo = new UserInfo(context.getApplicationContext());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

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

        SimpleDateFormat sdf = new SimpleDateFormat(holder.itemView.getContext().getString(R.string.date_format), Locale.US);
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

        if (isFavourite(data.get(position).getId())) {
            holder.addToFav.setImageResource(R.drawable.ic_favourite_red);
        } else {
            holder.addToFav.setImageResource(R.drawable.ic_favourite_black);
        }

        FavouriteCoursesDatabase favouriteDatabase = new FavouriteCoursesDatabase();
        String courseId = data.get(position).getId();
        DocumentReference courseRef = db.collection(DatabaseCollections.COURSES_COLLECTION)
                .document(courseId);
        holder.addToFav.setOnClickListener(view -> {
            if (!isFavourite(courseId)) {
                holder.addToFav.setImageResource(R.drawable.ic_favourite_red);
                favouriteDatabase.insertItem(userId, courseId);
                favouriteReferences.add(courseRef);
                userInfo.setUserFavouritesNo(userInfo.getUserFavouritesNo()+1);
            } else {
                holder.addToFav.setImageResource(R.drawable.ic_favourite_black);
                favouriteDatabase.removeItem(userId, courseId);
                favouriteReferences.remove(courseRef);
                userInfo.setUserFavouritesNo(userInfo.getUserFavouritesNo()-1);
            }
        });

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent toCourseProfile = new Intent(view.getContext(), CoursePageActivity.class);
            toCourseProfile.putExtra(Intents.COURSE_ID, data.get(getAbsoluteAdapterPosition()).getId());
            startActivity(view.getContext(), toCourseProfile, new Bundle());
        }
    }

    public boolean isFavourite(String courseID) {
        DocumentReference courseRef = db.collection(DatabaseCollections.COURSES_COLLECTION)
                .document(courseID);
        for (DocumentReference docRef : favouriteReferences)
            if (courseRef.equals(docRef))
                return true;

        return false;
    }
}
