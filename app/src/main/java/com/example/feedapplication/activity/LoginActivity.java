package com.example.feedapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feedapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    AppCompatButton buttonLogin,admin;
    AppCompatEditText username, password;
    TextView create_new_account;
    ProgressBar progressBar;

    DatabaseReference databaseReference  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        getSupportActionBar().setTitle("Login");

        buttonLogin = findViewById(R.id.button_login);
        username = findViewById(R.id.edit_username);
        password = findViewById(R.id.edit_password);
        create_new_account = findViewById(R.id.register_btn);
        admin = findViewById(R.id.button_admin);


        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textUsername = username.getText().toString();
                String textPass = password.getText().toString();

                if (TextUtils.isEmpty(textUsername)) {
                    username.setError("Username is Required");
                    username.requestFocus();
                } else if (TextUtils.isEmpty(textPass)) {
                    password.setError("Password is Required");
                    password.requestFocus();
                } else {
                    databaseReference = FirebaseDatabase.getInstance().getReference("admin");
                    databaseReference.child("1").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String getUsername = snapshot.child("username").getValue(String.class);
                                String getPassword = snapshot.child("password").getValue(String.class);
                                if ((getUsername.equals(textUsername)) && (getPassword.equals(textPass))) {
                                    Toast.makeText(LoginActivity.this, "Successfully Logged in!!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.VISIBLE);
                                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                    startActivity(intent);
                                    username.setText("");
                                    password.setText("");
                                }
                                else {

                                    Toast.makeText(LoginActivity.this, "invalid username and password !!", Toast.LENGTH_SHORT).show();

                                    username.setText("");
                                    password.setText("");
                                }
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) ;


                }
            }
        });
        create_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        progressBar = findViewById(R.id.progress_bar);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textUsername = username.getText().toString();
                String textPass = password.getText().toString();


                if (TextUtils.isEmpty(textUsername)) {
                    username.setError("Username is Required");
                    username.requestFocus();
                } else if (TextUtils.isEmpty(textPass)) {
                    password.setError("Password is Required");
                    password.requestFocus();
                }
                else {
                    databaseReference = FirebaseDatabase.getInstance().getReference("users");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(textUsername)) {
                                 String getPassword = snapshot.child(textUsername).child("password")
                                        .getValue(String.class);
                                if (getPassword.equals(textPass)) {
                                    Toast.makeText(LoginActivity.this, "Successfully Logged in!!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.VISIBLE);
                                    Intent intent = new Intent(LoginActivity.this, UserCreatePostActivity.class);
                                    intent.putExtra("username", textUsername);
                                    username.setText("");
                                    password.setText("");
                                    startActivity(intent);


                                } else {
                                    progressBar.setVisibility(View.VISIBLE);
                                    Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                    username.setText("");
                                    password.setText("");

                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }
        });
    }
}