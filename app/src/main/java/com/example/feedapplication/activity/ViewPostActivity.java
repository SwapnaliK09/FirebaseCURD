package com.example.feedapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.feedapplication.R;
import com.example.feedapplication.adapter.PostViewAdapter;
import com.example.feedapplication.models.PostModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewPostActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    public static PostViewAdapter adapter;
    DatabaseReference databaseReference;
    public static ArrayList<PostModel> list;
    private String userName;
    private ImageButton button;
    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        userName = getIntent().getExtras().getString("username");

        recyclerView = findViewById(R.id.recycler_view);
        button = findViewById(R.id.btnLogOut);
        databaseReference = FirebaseDatabase.getInstance().getReference("Post_data");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new PostViewAdapter(this,list);
        recyclerView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(ViewPostActivity.this);
                builder.setTitle("Logout");

                builder.setMessage("Are you want to Logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(ViewPostActivity.this, "Logout Successfully!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ViewPostActivity.this, UserCreatePostActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    boolean isApprovedPost = Boolean.TRUE.equals(dataSnapshot.child("approvePost").getValue(boolean.class));
                    boolean username = Boolean.parseBoolean(String.valueOf(userName.equals(dataSnapshot.child("created").getValue(String.class))));
                    if (isApprovedPost && username) {
                        PostModel model = dataSnapshot.getValue(PostModel.class);
                        list.add(model);
                        adapter.notifyItemChanged(model.getId());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}