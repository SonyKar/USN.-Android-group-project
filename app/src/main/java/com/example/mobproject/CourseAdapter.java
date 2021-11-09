package com.example.mobproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<String> data;//change to List<Course>
    private final SelectCourseListener myselectCourseListener;


    CourseAdapter(Context context, List<String> data, SelectCourseListener selectCourseListener){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.myselectCourseListener = selectCourseListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view, myselectCourseListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //bind the textview with data received

        String title = data.get(position);
        holder.courseTitle.setText(title);

        String difficulty = data.get(position);
        holder.courseDifficulty.setText(difficulty);

        String finalScore = data.get(position) + "/5.00";//function to calculate final score
        holder.courseFinalScore.setText(finalScore);

        String period = data.get(position);//startDate + " - " + endDate
        holder.coursePeriod.setText(period);

        String price = data.get(position);
        holder.coursePrice.setText(price);

        String enroll = data.get(position);
        holder.courseEnroll.setText(enroll);

        /*ImageView image = data.get(position);//get from DB
        holder.courseImage.setImageResource(image);*/



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView courseTitle, courseDifficulty, courseFinalScore, coursePeriod, coursePrice, courseEnroll;
        ImageView courseImage;
        SelectCourseListener selectCourseListener;

        public ViewHolder(@NonNull View itemView, SelectCourseListener selectCourseListener) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.course_title);
            courseDifficulty = itemView.findViewById(R.id.course_difficulty);
            courseFinalScore = itemView.findViewById(R.id.final_score);
            coursePeriod = itemView.findViewById(R.id.card_view_period);
            coursePrice = itemView.findViewById(R.id.card_view_price);
            courseEnroll = itemView.findViewById(R.id.card_view_enroll);
            courseImage = itemView.findViewById(R.id.course_img);

            this.selectCourseListener = selectCourseListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            selectCourseListener.onItemClicked(getAdapterPosition());
        }
    }

}
