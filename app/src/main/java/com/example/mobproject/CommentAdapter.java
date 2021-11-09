package com.example.mobproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> data;//change to List<Comments>

    CommentAdapter(Context context, List<String> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.comments_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //bind the textview with data received
        String name = data.get(position);
        holder.commentUserName.setText(name);//fName + " " + lName

        String comment = data.get(position);
        holder.commentUserText.setText(comment);

        /*CircleImageView avatar = data.get(position);//get avatar from DB
        holder.userAvatar.setImageResource(avatar);*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView commentUserName, commentUserText;
        CircleImageView userAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentUserName = itemView.findViewById(R.id.comment_user_name);
            commentUserText = itemView.findViewById(R.id.comment_user_text);
            userAvatar = itemView.findViewById(R.id.comment_avatar_others);
        }
    }

}

