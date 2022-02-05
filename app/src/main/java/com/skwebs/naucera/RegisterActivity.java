package com.skwebs.naucera;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

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
        name = Objects.requireNonNull(etName.getEditText()).getText().toString();
        email = Objects.requireNonNull(etEmail.getEditText()).getText().toString();
        password = Objects.requireNonNull(etPassword.getEditText()).getText().toString();
        confirm = Objects.requireNonNull(etConfirmation.getEditText()).getText().toString();

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
        JSONObject params = new JSONObject();
        try {
            params.put("name",name);
            params.put("email",email);
            params.put("password",password);
            params.put("password_confirmation",confirm);
        }catch (JSONException e){
            e.printStackTrace();
        }

        String data = params.toString();
//        alertSuccess(data);
        String url = getString(R.string.api_server)+"/register";
//        alertSuccess(url);
//================================================================================
        Http http = new Http(RegisterActivity.this, url);
        http.setMethod("post");
        http.setData(data);
        http.send();

        Integer code = http.getStatusCode();
        if (code == 201 || code == 200){
            alertSuccess("Register successfully.");
        }else if (code==422){
            try {
                JSONObject response = new JSONObject(http.getResponse());
                String msg = response.getString("message");
                alertFail(msg);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(RegisterActivity.this, "Error: "+code, Toast.LENGTH_SHORT).show();
        }
//        ========================================================================

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Http http = new Http(RegisterActivity.this, url);
//                http.setMethod("post");
//                http.setData(data);
//                http.send();
//                Toast.makeText(RegisterActivity.this, http.getStatusCode(), Toast.LENGTH_SHORT).show();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Integer code = http.getStatusCode();
//                        if (code == 201 || code == 200){
//                            alertSuccess("Register successfully.");
//                        }else if (code==422){
//                            try {
//                                JSONObject response = new JSONObject(http.getResponse());
//                                String msg = response.getString("message");
//                                alertFail(msg);
//                            }catch (JSONException e){
//                                e.printStackTrace();
//                            }
//                        }else{
//                            Toast.makeText(RegisterActivity.this, "Error: "+code, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
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