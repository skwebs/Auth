package com.skwebs.naucera;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.MessageFormat;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {
    String userToken, userName, userEmail;
    int userId;
    TextView tvUserName, tvUserEmail;
    Button btnLogout;
    SharedPreferences sharedPref;
    boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Objects.requireNonNull(getSupportActionBar()).setTitle("User Dashboard");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        isLoggedIn = false;
        btnLogout = findViewById(R.id.btn_logout);

        sharedPref = getApplicationContext().getSharedPreferences("session", Context.MODE_PRIVATE);

        isLoggedIn = sharedPref.getBoolean("isLoggedIn", false);
        userId = sharedPref.getInt("id", 0);
        userName = sharedPref.getString("name", null);
        userEmail = sharedPref.getString("email", null);
        userToken = sharedPref.getString("token", null);

        tvUserName = findViewById(R.id.tv_user_name);
        tvUserEmail = findViewById(R.id.tv_user_email);

        tvUserName.setText(String.format("Hi, %s", userName));
        tvUserEmail.setText(MessageFormat.format("Your email id is {0}", userEmail));

        btnLogout.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();
//           redirect to login activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isLoggedIn) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}