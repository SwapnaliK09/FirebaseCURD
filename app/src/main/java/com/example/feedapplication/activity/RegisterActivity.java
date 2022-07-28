package com.example.feedapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feedapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    TextView login_text;
    Button register_btn;
    ProgressBar progressBar;
    AppCompatEditText username, password, email, phone;
    String[] items = {"User", "Admin"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> stringArrayAdapter;
    private ImageButton btnLogOut;
    private AlertDialog.Builder builder;
    DatabaseReference databaseReference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        login_text = findViewById(R.id.login_btn);
        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        email = findViewById(R.id.register_email);
        phone = findViewById(R.id.register_phone);
        autoCompleteTextView = findViewById(R.id.register_role);
        progressBar = findViewById(R.id.register_progress_bar);
        databaseReference =FirebaseDatabase.getInstance().getReference();
        register_btn = findViewById(R.id.button_register);
        btnLogOut = findViewById(R.id.btnLogOut);

        stringArrayAdapter = new ArrayAdapter<>(this, R.layout.list_view, items);
        autoCompleteTextView.setAdapter(stringArrayAdapter);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Logout");

                builder.setMessage("Are you want to Logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(RegisterActivity.this, "Logout Successfully!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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

        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtUsername = username.getText().toString();
                String txtPassword = password.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPhone = phone.getText().toString();
                String txtRole = autoCompleteTextView.getText().toString();

                String mobileRegex = "[6-9][0-9]{9}";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(txtPhone);

                if (TextUtils.isEmpty(txtUsername)) {
                    username.setError("Username is Required");
                    username.requestFocus();
                }else if (TextUtils.isEmpty(txtPassword)) {
                    password.setError("Password is Required");
                    password.requestFocus();
                }
                else if (txtPassword.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password should be at least  10 digits ", Toast.LENGTH_SHORT).show();
                    password.setError("Password too weak");
                    password.requestFocus();
                }
                else if (TextUtils.isEmpty(txtEmail)) {
                    email.setError("Email is Required");
                    email.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
                    email.setError(" Valid Email is Required");
                    email .requestFocus();
                } else if (!Patterns.PHONE.matcher(txtPhone).matches()) {
                    phone.setError("Mobile No. is Required");
                    phone.requestFocus();
                } else if (txtPhone.length() != 10) {
                    phone.setError("Mobile No. should be 10 digits");
                    phone.requestFocus();
                } else if (!mobileMatcher.find()) {
                    phone.setError("Mobile No. is not valid");
                    phone.requestFocus();
                }else if (TextUtils.isEmpty(txtRole)) {
                    autoCompleteTextView.setError("Select Gender");
                    autoCompleteTextView.requestFocus();
                    Toast.makeText(RegisterActivity.this, "Please Select Your Role", Toast.LENGTH_SHORT).show();
                }
                else {
                    txtRole = autoCompleteTextView.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(txtUsername, txtPassword, txtEmail, txtPhone,txtRole);
                }
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String items = adapterView.getItemAtPosition(i).toString();
                        Toast.makeText(RegisterActivity.this, "Selected role is:" + items, Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

    }

    private void registerUser(String txtUsername, String txtPassword, String txtEmail, String txtPhone, String txtRole) {

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(txtUsername)){
                    Toast.makeText(RegisterActivity.this, "Username is already register.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }else{
                    databaseReference.child("users").child(txtUsername).child("username").setValue(txtUsername);
                    databaseReference.child("users").child(txtUsername).child("email").setValue(txtEmail);
                    databaseReference.child("users").child(txtUsername).child("phone").setValue(txtPhone);
                    databaseReference.child("users").child(txtUsername).child("password").setValue(txtPassword);
                    databaseReference.child("users").child(txtUsername).child("role").setValue(txtRole);

                    Toast.makeText(RegisterActivity.this, "register Successfully.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}