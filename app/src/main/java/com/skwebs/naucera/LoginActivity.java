package com.skwebs.naucera;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {
    //    variables declaration
    TextInputLayout etEmail, etPassword;
    Button btnLogin, btnRegister, btnShowUsersList;
    String email, password, apiBaseUrl;
    TextView forget;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Objects.requireNonNull(getSupportActionBar()).setTitle("LOGIN");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        variable initialisation
        apiBaseUrl = "https://anshumemorial.in/lv8_api/api/";
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        forget = findViewById(R.id.forgot);
        btnShowUsersList = findViewById(R.id.btn_show_users_list);

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

        Button btnWebView = findViewById(R.id.btnWebView);
        btnWebView.setOnClickListener(view -> {
            Intent intent = new Intent(this, WebviewActivity.class);
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
            etEmail.setError(null);
            etPassword.setError("Password is required.");
        } else {
            etPassword.setError(null);
            authenticateLogin();

        }
    }

    private void authenticateLogin() {
        String apiLoginUrl = apiBaseUrl + "login";

        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiLoginUrl,
                response -> {
                    Log.d("Login", "UserLogin: " + response);

                    try {
                        JSONObject responseJsonObject = new JSONObject(response);

                        String status = responseJsonObject.getString("status");
                        if (status.equals("success")) {
                            sendLoginUserToDashboard(responseJsonObject);
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
                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void sendLoginUserToDashboard(JSONObject responseJsonObject) throws JSONException {
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
}