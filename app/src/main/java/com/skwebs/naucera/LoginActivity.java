package com.skwebs.naucera;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class LoginActivity extends AppCompatActivity {
    //    variables declaration
    TextInputLayout etEmail, etPassword;
    Button btnLogin, btnRegister, btnShowUsersList;
    String email, password, API_BASE_URL;
    TextView forget;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Objects.requireNonNull(getSupportActionBar()).setTitle("LOGIN");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        variable initialisation
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        forget = findViewById(R.id.forgot);
        btnShowUsersList = findViewById(R.id.btn_show_users_list);

        sharedPref = getSharedPreferences("session", Context.MODE_PRIVATE);

        API_BASE_URL = getString(R.string.API_BASE_URL);

        progressDialog = new ProgressDialog(this);
//        event on button
        btnShowUsersList.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> validEditInputText());

        forget.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
            startActivity(intent);
        });
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void validEditInputText() {
        email = Objects.requireNonNull(etEmail.getEditText()).getText().toString().trim();
        password = Objects.requireNonNull(etPassword.getEditText()).getText().toString().trim();
        if (email.isEmpty()) {
            etEmail.setError("Email is required.");
        } else if (password.isEmpty()) {
            etEmail.setError(null);
            etPassword.setError("Password is required.");
        } else {
            etPassword.setError(null);
            authenticateLogin();
        }
    }

    private void authenticateLogin() {
        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_BASE_URL + "/login",
                response -> {
                    Log.d("LoginActivity:", "response: " + response);
                    try {
                        JSONObject responseJsonObject = new JSONObject(response);

                        boolean error = responseJsonObject.getBoolean("error");
                        if (error) {
//                            if any message
                            if (responseJsonObject.has("message")) {
                                etEmail.setError(responseJsonObject.getString("message"));
                                etPassword.setError(responseJsonObject.getString("message"));
                                Log.d("LoginActivity:", "message: " + responseJsonObject.getString("message"));
                                Toast.makeText(this, responseJsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }


//                            if has errors key in
                            if (responseJsonObject.has("errors")) {
                                JSONObject errorObj = new JSONObject(responseJsonObject.getString("errors"));

//                            if email has error
                                if (errorObj.has("email")) {
                                    etEmail.setError(errorObj.getString("email"));
                                    Toast.makeText(this, errorObj.getString("email"), Toast.LENGTH_SHORT).show();
                                }
//                            if password has error
                                if (errorObj.has("password")) {
                                    etPassword.setError(errorObj.getString("password"));
                                    Toast.makeText(this, errorObj.getString("password"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            etEmail.setError(null);
                            etPassword.setError(null);

                            sendLoginUserToDashboard(responseJsonObject);
                        }

                    } catch (JSONException e) {
                        Log.d("LoginActivity:", "JSONException: " + e);
                        e.printStackTrace();
                    }

                    // Hiding the progress dialog after all task complete.
                    progressDialog.dismiss();
                },
                error -> {
                    Log.d("LoginActivity:", "ServerError: " + error);
                    // Hiding the progress dialog after all task complete.
                    progressDialog.dismiss();
                    // Showing error message if something goes wrong.
                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<>();

                // Adding All values to Params.
                // The firs argument should be same sa your MySQL database table columns.
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void sendLoginUserToDashboard(@NonNull JSONObject responseJsonObject) throws JSONException {
//        initialized variables
        String userToken, userName, userEmail;
//       initialize and store user data in json object
        JSONObject userJsonObject = responseJsonObject.getJSONObject("user");
//        store particular data in respective variable
        int userId = userJsonObject.getInt("id");
        userName = userJsonObject.getString("name");
        userEmail = userJsonObject.getString("email");
        userToken = responseJsonObject.getString("token");
//        store data in shared preferences to maintain session
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putInt("id", userId);
        editor.putString("name", userName);
        editor.putString("email", userEmail);
        editor.putString("token", userToken);
        editor.apply();

        Toast.makeText(this, "User data saved in session.", Toast.LENGTH_SHORT).show();
        Log.d("Session:", "User data saved in session.");
        finish();

        Intent intent = new Intent(this, DashboardActivity.class);

        intent.putExtra("userId", userId);
        intent.putExtra("userName", userName);
        intent.putExtra("userEmail", userEmail);
        intent.putExtra("userToken", userToken);

        startActivity(intent);

        finish();
    }
}