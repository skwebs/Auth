package com.skwebs.naucera;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity:";
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    TextInputLayout etName, etEmail, etPassword, etConfirmation;
    Button btnRegister;
    String name, email, password, confirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Objects.requireNonNull(getSupportActionBar()).setTitle("REGISTER");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

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

    private void sendRegister() {

//        String url = "https://anshumemorial.in/lv8_api/api/users";
        String url = "http://192.168.29.122:8000/api/users";
        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("Register", "UserLogin: " + response);

                    try {
                        JSONObject responseJsonObject = new JSONObject(response);

                        boolean error = responseJsonObject.getBoolean("error");
                        if (error) {
                            JSONObject errorObj = new JSONObject(responseJsonObject.getString("errors"));
//                            if name has error
                            if (errorObj.has("name")) {
                                etName.setError(errorObj.getString("name"));
                                Toast.makeText(this, errorObj.getString("name"), Toast.LENGTH_SHORT).show();
                            }else
//                            if email has error
                            if (errorObj.has("email")) {
                                etEmail.setError(errorObj.getString("email"));
                                Toast.makeText(this, errorObj.getString("email"), Toast.LENGTH_SHORT).show();
//                                remove name error
                                etName.setError(null);
                            }else
//                            if password has error
                            if (errorObj.has("password")) {
                                etPassword.setError(errorObj.getString("password"));
                                Toast.makeText(this, errorObj.getString("password"), Toast.LENGTH_SHORT).show();
//                                remove email error
                                etEmail.setError(null);
                            }else
//                            if confirm password has error
                            if (errorObj.has("password_confirmation")) {
                                etConfirmation.setError(errorObj.getString("password_confirmation"));
                                Toast.makeText(this, errorObj.getString("password_confirmation"), Toast.LENGTH_SHORT).show();
//                                remove password email
                                etPassword.setError(null);
                            }
                            Log.d(TAG, "errorObj:: " + errorObj.getString("email"));

                        } else {
                            etName.setError(null);
                            etEmail.setError(null);
                            etPassword.setError(null);
                            etConfirmation.setError(null);

                            sendRegisteredUserToDashboard(responseJsonObject);
                        }

                    } catch (JSONException e) {
                        Log.d("Login", "UserLogin: " + e);
                        e.printStackTrace();
                    }
                    // Hiding the progress dialog after all task complete.
                    progressDialog.dismiss();
                },
                error -> {
                    // Hiding the progress dialog after all task complete.
                    progressDialog.dismiss();
                    // Showing error message if something goes wrong.
                    Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "sendRegister: " + error.getMessage());
                    Log.d(TAG, "sendRegister: " + error);
                    Log.d(TAG, "sendRegister: " + error.networkResponse);
                    error.printStackTrace();
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<>();

                // Adding All values to Params.
                // The firs argument should be same sa your MySQL database table columns.
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("password_confirmation", confirmation);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-Requested-With", "XMLHttpRequest");
//                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        requestQueue.add(stringRequest);
    }

    private void sendRegisteredUserToDashboard(JSONObject responseJsonObject) throws JSONException {
        String userToken, userName, userEmail;

        JSONObject userJsonObject = responseJsonObject.getJSONObject("user");

        int userId = userJsonObject.getInt("id");
        userName = userJsonObject.getString("name");
        userEmail = userJsonObject.getString("email");
        userToken = responseJsonObject.getString("token");

        finish();

        Intent intent = new Intent(this, DashboardActivity.class);

        intent.putExtra("userId", userId);
        intent.putExtra("userName", userName);
        intent.putExtra("userEmail", userEmail);
        intent.putExtra("userToken", userToken);

        startActivity(intent);
    }

    private void alertFail(String s) {
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setIcon(R.drawable.ic_warning)
                .setMessage(s)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
    }
}