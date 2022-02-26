package com.skwebs.naucera;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

  UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Objects.requireNonNull(getSupportActionBar()).hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userSession = UserSession.getInstance(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent;
        if (userSession.isLoggedIn()) {
            intent = new Intent(MainActivity.this, DashboardActivity.class);
        } else {
            intent = new Intent(MainActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }

}