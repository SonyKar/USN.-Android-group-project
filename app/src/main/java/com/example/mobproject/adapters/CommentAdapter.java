package com.example.mobproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobproject.R;
import com.example.mobproject.controllers.PictureController;
import com.example.mobproject.db.CommentDatabase;
import com.example.mobproject.db.UserDatabase;
import com.example.mobproject.interfaces.Callback;
import com.example.mobproject.models.Comment;
import com.example.mobproject.models.User;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<DocumentReference> data;//change to List<Comments>

    public CommentAdapter(Context context, List<DocumentReference> data) {
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
        CommentDatabase commentDatabase = new CommentDatabase();
        commentDatabase.getItem(data.get(position).getId(), new Callback<Comment>() {
            @Override
            public void OnFinish(ArrayList<Comment> arrayList) {
                UserDatabase userDatabase = new UserDatabase();
                userDatabase.getItem(arrayList.get(0).getUserId().getId(), new Callback<User>() {
                    @Override
                    public void OnFinish(ArrayList<User> arrayList) {
                        User user = arrayList.get(0);
                        String name = user.getName();
                        holder.commentUserName.setText(name); //fName + " " + lName
                        PictureController.getProfilePicture(user.getId(), holder.userAvatar);
                    }
                });

                String comment = arrayList.get(0).getCommentText();
                holder.commentUserText.setText(comment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

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

