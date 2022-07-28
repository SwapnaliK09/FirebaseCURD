package com.example.feedapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedapplication.R;
import com.example.feedapplication.models.PostModel;

import java.util.ArrayList;

public class OtherUserListAdapter extends RecyclerView.Adapter<OtherUserListAdapter.ViewHolder> {

    Context context;
    ArrayList<PostModel> postModels ;

    public OtherUserListAdapter(Context context, ArrayList<PostModel> postModels) {
        this.context = context;
        this.postModels = postModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_other_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostModel postModel = postModels.get(position);
        holder.post.setText(postModel.getPost_txt());
        holder.date.setText(postModel.getDate());
        holder.created.setText(postModel.getCreated());
    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView post,date,created;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post = itemView.findViewById(R.id.getPost);
            date = itemView.findViewById(R.id.getDate);
            created = itemView.findViewById(R.id.getCreated);

        }
    }
}
