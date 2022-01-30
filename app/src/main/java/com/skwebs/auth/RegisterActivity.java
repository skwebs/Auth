package com.skwebs.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout etName,etEmail, etPassword, etConfirmation;
    Button btnRegister;
    String name, email, password, confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Objects.requireNonNull(getSupportActionBar()).setTitle("REGISTER");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail =findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmation = findViewById(R.id.etConfirmation);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> checkRegister());

    }

    private void checkRegister() {
        name = etName.toString();
        email = etEmail.toString();
        password = etPassword.toString();
        confirm = etConfirmation.toString();

        if (name.isEmpty()){
            alertFail("Name is require.");
        }else if (email.isEmpty()){
            alertFail("Email is require.");
        }else if (password.isEmpty()){
            alertFail("Password is require.");
        }else if (confirm.isEmpty()){
            alertFail("Confirm Password is require.");
        }else if (!password.equals(confirm)){
            alertFail("Password and Confirm Password doesn't match.");
        }else {
            sendRegister();
        }
    }

    private void sendRegister() {
        alertSuccess("Registered successfully.");
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