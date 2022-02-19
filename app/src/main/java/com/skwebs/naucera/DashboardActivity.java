package com.skwebs.naucera;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {
    String userToken, userName, userEmail;
    int userId;
    TextView tvUserId, tvUserName, tvUserEmail, tvUserToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Objects.requireNonNull(getSupportActionBar()).setTitle("User Dashboard");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Intent intent = getIntent();

        userId = intent.getIntExtra("userId",0);
        userName = intent.getStringExtra("userName");
        userEmail = intent.getStringExtra("userEmail");
        userToken = intent.getStringExtra("userToken");

        tvUserId = findViewById(R.id.tv_user_id);
        tvUserName = findViewById(R.id.tv_user_name);
        tvUserEmail = findViewById(R.id.tv_user_email);
        tvUserToken = findViewById(R.id.tv_user_token);

        tvUserId.setText(String.valueOf(userId));
        tvUserName.setText(String.valueOf(userName));
        tvUserEmail.setText(String.valueOf(userEmail));
        tvUserToken.setText(String.valueOf(userToken));
    }
}