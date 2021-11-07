package com.example.mobproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> data;
    private SelectCourseListener myselectCourseListener;


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

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView courseTitle;
        SelectCourseListener selectCourseListener;

        public ViewHolder(@NonNull View itemView, SelectCourseListener selectCourseListener) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.course_title);
            this.selectCourseListener = selectCourseListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            selectCourseListener.onItemClicked(getAdapterPosition());
        }
    }

}
