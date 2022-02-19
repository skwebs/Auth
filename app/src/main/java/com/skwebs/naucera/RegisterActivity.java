package com.skwebs.naucera;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout etName, etEmail, etPassword, etConfirmation;
    Button btnRegister;
    String name, email, password, confirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Objects.requireNonNull(getSupportActionBar()).setTitle("REGISTER");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmation = findViewById(R.id.etConfirmation);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> checkRegister());

    }

    private void checkRegister() {

        name = String.valueOf(Objects.requireNonNull(etName.getEditText()).getText()).trim();
        email = String.valueOf(Objects.requireNonNull(etEmail.getEditText()).getText()).trim();
        password = String.valueOf(Objects.requireNonNull(etPassword.getEditText()).getText()).trim();
        confirmation = String.valueOf(Objects.requireNonNull(etConfirmation.getEditText()).getText()).trim();

        if (name.isEmpty()) {
            alertFail("Name is required.");
        } else if (email.isEmpty()) {
            alertFail("Email is required.");
        } else if (password.isEmpty()) {
            alertFail("Password is required.");
        } else if (confirmation.isEmpty()) {
            alertFail("Confirm Password is required.");
        } else if (!password.equals(confirmation)) {
            alertFail("Password and Confirm Password doesn't match.");
        } else {
            sendRegister();
        }
    }

    final String url = String.valueOf(R.string.api_server);

    private void sendRegister() {

        String url = "https://anshumemorial.in/lv8_api/api/users";




        Toast.makeText(this, "sendRegister : " + url, Toast.LENGTH_SHORT).show();

    }

    private void alertSuccess(String s) {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setIcon(R.drawable.ic_check_box)
                .setMessage(s)
                .setPositiveButton("LOGIN", (dialog, which) -> onBackPressed()).show();
    }

    private void alertFail(String s) {
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setIcon(R.drawable.ic_warning)
                .setMessage(s)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
    }









}