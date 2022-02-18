package com.skwebs.naucera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class UserDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Objects.requireNonNull(getSupportActionBar()).setTitle("Users List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");

        TextView tvUserName = findViewById(R.id.tv_user_name);
        TextView tvUserEmail = findViewById(R.id.tv_user_email);

        tvUserName.setText(String.valueOf(name));
        tvUserEmail.setText(String.valueOf(email));
    }
}