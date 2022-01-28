package com.skwebs.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class UserActivity extends AppCompatActivity {

    TextView tvName, tvEmail, tvCreated;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setTitle("USER DATA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvCreated = findViewById(R.id.tvCreated);
        btnLogout = findViewById(R.id.btnLogout);

        getUser();

        btnLogout.setOnClickListener(v -> logout());
    }


    private void getUser() {
        tvName.setText("NAME : -");
        tvEmail.setText("EMAIL : -");
        tvCreated.setText("CREATED AT : -");
    }

    private void logout() {
        Intent intent;
        intent = new Intent(UserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
