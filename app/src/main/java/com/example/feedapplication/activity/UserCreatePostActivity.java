package com.example.feedapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feedapplication.R;
import com.example.feedapplication.models.PostModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserCreatePostActivity extends AppCompatActivity {
    private TextInputEditText created_post;
    private AppCompatButton b1, b2, b3;
    private String user_name;
    private ImageButton btnLogOut;
    private AlertDialog.Builder builder;
    private DatabaseReference databaseReference;
    private int maxId = 1;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_create_post);


        created_post = findViewById(R.id.edit_create_post);
        b1 = findViewById(R.id.btn_create_post);
        b2 = findViewById(R.id.View_my_post_btn);
        b3 = findViewById(R.id.btn_other_view_post);
        txt = findViewById(R.id.welcomeText);
        Intent intent = getIntent();
        user_name = intent.getStringExtra("username");
        txt.setText("Welcome " + user_name);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(UserCreatePostActivity.this);
                builder.setTitle("Logout");

                builder.setMessage("Are you want to Logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(UserCreatePostActivity.this, "Logout Successfully!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UserCreatePostActivity.this, LoginActivity.class));
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
//        getSupportActionBar().setTitle(" Welcome  "+ user_name);

        databaseReference = FirebaseDatabase.getInstance().getReference("Post_data");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressLint("SimpleDateFormat")
            public void onClick(View view) {
                String postValue = created_post.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String currentDate = sdf.format(new Date());

                if (TextUtils.isEmpty(postValue)) {
                    created_post.setError("Please Enter you want to Post it.");
                } else {
                    PostModel postModel = new PostModel();
                    postModel.setPost_txt(postValue);
                    postModel.setCreated(user_name);
                    postModel.setDate(currentDate);
                    postModel.setId(maxId);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                maxId = (int) snapshot.getChildrenCount()+1;
                                System.out.println("<<<<<<<<<<<<<id  " + maxId + " " +snapshot.getChildrenCount());

                            }
                            Toast.makeText(UserCreatePostActivity.this, "post created Successfully.", Toast.LENGTH_SHORT).show();
                            created_post.setText("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            System.out.println("<<<<<<<<" + error.getMessage());
                        }
                    });

                    databaseReference.child(String.valueOf(maxId)).setValue(postModel);



                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UserCreatePostActivity.this, ViewPostActivity.class);
//                String user_name = toString();
                Bundle bundle = new Bundle();
                bundle.putString("username", user_name);
                intent.putExtras(bundle);
                created_post.setText("");
                startActivity(intent);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = (new Intent(UserCreatePostActivity.this, ViewOtherPostActivity.class));
                Bundle bundle = new Bundle();
                bundle.putString("username", user_name);
                intent.putExtras(bundle);
                created_post.setText("");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxId = (int) snapshot.getChildrenCount()+1;
                    System.out.println("<<<<<<<<<<<<<Pause  " + maxId + " " +snapshot.getChildrenCount());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("<<<<<<<<" + error.getMessage());
            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxId = (int) snapshot.getChildrenCount()+1;
                    System.out.println("<<<<<<<<<<<<<Stop  " + maxId + " " +snapshot.getChildrenCount());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("<<<<<<<<" + error.getMessage());
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxId = (int) snapshot.getChildrenCount()+1;
                    System.out.println("<<<<<<<<<<<<<Restart " + maxId + " " +snapshot.getChildrenCount());

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("<<<<<<<<" + error.getMessage());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxId = (int) snapshot.getChildrenCount()+1;
                    System.out.println("<<<<<<<<<<<<<Destroy  " + maxId + " " +snapshot.getChildrenCount());

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("<<<<<<<<" + error.getMessage());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxId = (int) snapshot.getChildrenCount()+1;
                    System.out.println("<<<<<<<<<<<<<Resume  " + maxId + " " +snapshot.getChildrenCount());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("<<<<<<<<" + error.getMessage());
            }
        });

    }
}