package com.example.feedapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedapplication.R;
import com.example.feedapplication.activity.AdminActivity;
import com.example.feedapplication.models.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminListAdapter extends RecyclerView.Adapter<AdminListAdapter.ViewHolder> {

    Context context;
    ArrayList<PostModel> postModels;
    DatabaseReference databaseReference;

    public AdminListAdapter(Context context, ArrayList<PostModel> postModels) {
        this.context = context;
        this.postModels = postModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    @SuppressLint("RecyclerView")
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Post_data");

        PostModel postModel = postModels.get(position);
        holder.post.setText(postModel.getPost_txt());
        holder.date.setText(postModel.getDate());
        holder.created.setText(postModel.getCreated());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete Post")
                        .setMessage("Are you sure want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseReference.child(String.valueOf(postModel.getId())).removeValue();
                                postModels.remove(position);
                                notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                builder.show();
            }
        });

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Approve Post")
                        .setMessage("Are you sure want to Approve")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean approvePost = true;

                                HashMap<String, Object> User = new HashMap();
                                User.put("approvePost", approvePost);
                                User.put("date",postModel.getDate());
                                User.put("created",postModel.getCreated());
                                User.put("id",postModel.getId());
                                User.put("post_txt",postModel.getPost_txt());

                                databaseReference.child(String.valueOf(postModel.getId())).updateChildren(User).addOnCompleteListener(new OnCompleteListener() {
                                    @Override

                                    public void onComplete(@NonNull Task task) {
                                        if (approvePost) {
                                            notifyItemRemoved(position);
                                            AdminActivity.adapter.notifyDataSetChanged();
                                            Toast.makeText(context, "Post Approved Successfully!!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Unable to Approve!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Unable to Approve!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView post, date, created;
        AppCompatButton delete, approve;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post = itemView.findViewById(R.id.getPost);
            date = itemView.findViewById(R.id.getDate);
            created = itemView.findViewById(R.id.getCreated);

            delete = itemView.findViewById(R.id.delete_btn);
            approve = itemView.findViewById(R.id.approve_btn);

        }
    }
}
