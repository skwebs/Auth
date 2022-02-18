package com.skwebs.naucera;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout etEmail, etPassword;
    Button btnLogin, btnRegister, btnShowUsersList;
    String email, password;
    TextView forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Objects.requireNonNull(getSupportActionBar()).setTitle("LOGIN");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        forget = findViewById(R.id.forgot);
        btnShowUsersList = findViewById(R.id.btn_show_users_list);

        btnShowUsersList.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> checkLogin());

        forget.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
            startActivity(intent);
        });
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        Button btnWebView = findViewById(R.id.btnWebView);

        btnWebView.setOnClickListener(view -> {
            Intent intent = new Intent(this, WebviewActivity.class);
            startActivity(intent);
        });
    }

    private void checkLogin() {
        email = Objects.requireNonNull(etEmail.getEditText()).getText().toString().trim();
        password = Objects.requireNonNull(etPassword.getEditText()).getText().toString().trim();
        if (email.isEmpty()) {
            etEmail.setError("Email is required.");
        } else if (password.isEmpty()) {
            etEmail.setError(null);
            etEmail.setError(null);
            etPassword.setError("Password is required.");
        } else {
            etPassword.setError(null);
            sendLogin();
        }
    }

    private void sendLogin() {
//        Toast.makeText(this, "Send to Login", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
        startActivity(intent);
    }


}