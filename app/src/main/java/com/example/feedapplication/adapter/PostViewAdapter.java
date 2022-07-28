package com.example.feedapplication.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
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
import com.example.feedapplication.activity.ViewPostActivity;
import com.example.feedapplication.models.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PostViewAdapter extends RecyclerView.Adapter<PostViewAdapter.ViewHolder> {
//public class PostViewAdapter extends FirebaseRrcyclerAdapter<> {

    Context context;
    ArrayList<PostModel> postModels;
    DatabaseReference databaseReference;


    public PostViewAdapter(Context context, ArrayList<PostModel> postModels) {
        this.context = context;
        this.postModels = postModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.display_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Post_data");
//        String key = reference.getRef().getKey();
//        System.out.println("<<<<<<<<<<" + key);

        PostModel postModel = postModels.get(position);
        holder.post.setText(postModel.getPost_txt());
        holder.date.setText(postModel.getDate());
        holder.created.setText("Own");
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.update_post_box);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String currentDateandTime = sdf.format(new Date());


                final TextInputEditText update_txt = (TextInputEditText) dialog.findViewById(R.id.edit_update_post);
                AppCompatButton btn_update = (AppCompatButton) dialog.findViewById(R.id.btn_update_post);
                AppCompatButton btnCancel = (AppCompatButton) dialog.findViewById(R.id.btn_cancel_post);
                update_txt.setText(postModels.get(position).getPost_txt());

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                update_txt.setText(postModel.getPost_txt());


                dialog.show();
                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String textUpdate = update_txt.getText().toString();
                        if (TextUtils.isEmpty(textUpdate)) {
                            update_txt.setError("Enter Text");
                            update_txt.requestFocus();
                        } else {
                            HashMap<String, Object> User = new HashMap();
                            User.put("post_txt", textUpdate);
                            User.put("date", currentDateandTime);
                            User.put("created", postModel.getCreated());
                            User.put("id",postModel.getId()) ;
                            System.out.println("<<<<>>>>" + postModel.getId()+1);

                            databaseReference.child(String.valueOf(postModel.getId())).updateChildren(User).addOnCompleteListener(new OnCompleteListener() {
                                @Override

                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {

                                        ViewPostActivity.list.get(position).setPost_txt(textUpdate);
                                        ViewPostActivity.list.get(position).setCreated(postModel.getCreated());
                                        ViewPostActivity.list.get(position).setDate(currentDateandTime);
                                        ViewPostActivity.list.get(position).setId(postModel.getId());
                                        ViewPostActivity.adapter.notifyItemChanged(position);
                                        dialog.dismiss();
                                        Toast.makeText(context, "successfully Update", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(context,ViewPostActivity.class);
//                                        context.startActivity(intent);


                                    } else {
                                        Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });



                        }

//

//
                    }
                });

                dialog.show();

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete Post")
                        .setMessage("Are you sure want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseReference.child(String.valueOf(postModel.getId()+1)).removeValue();
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


}



    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView post, date, created;
        public AppCompatButton update, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post = itemView.findViewById(R.id.getPost);
            date = itemView.findViewById(R.id.getDate);
            created = itemView.findViewById(R.id.getCreated);

            update = itemView.findViewById(R.id.update);
            delete = itemView.findViewById(R.id.delete);


        }
    }

}
