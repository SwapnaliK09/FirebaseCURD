package com.example.feedapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.feedapplication.adapter.AdminListAdapter;
import com.example.feedapplication.models.PostModel;
import com.example.feedapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    public static AdminListAdapter adapter;
    DatabaseReference databaseReference;
    public static ArrayList<PostModel> arrayList;
    private ImageButton btnLogout;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.recycler_view);
        btnLogout = findViewById(R.id.btnLogOut);
        databaseReference = FirebaseDatabase.getInstance().getReference("Post_data");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<>();
        adapter = new AdminListAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(AdminActivity.this);
                builder.setTitle("Logout");

                builder.setMessage("Are you want to Logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(AdminActivity.this, "Logout Successfully!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
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
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    boolean isApprovedPost = Boolean.TRUE.equals(dataSnapshot.child("approvePost").getValue(boolean.class));
                    if (!isApprovedPost) {
                        System.out.println("<<<<<<<123" + dataSnapshot.toString());

                        PostModel model = dataSnapshot.getValue(PostModel.class);
                        arrayList.add(model);
                    }
//                    else {
//                        Toast.makeText(AdminActivity.this, "Post Not available.", Toast.LENGTH_SHORT).show();
//                    }


                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}