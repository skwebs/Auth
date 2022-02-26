package com.skwebs.naucera;

import android.app.ProgressDialog;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    UserSession userSes;

    TextView tvUserName, tvUserEmail;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Objects.requireNonNull(getSupportActionBar()).setTitle("User Dashboard");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        progressDialog = new ProgressDialog(this);

        btnLogout = findViewById(R.id.btn_logout);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        tvUserName = findViewById(R.id.tv_user_name);
        tvUserEmail = findViewById(R.id.tv_user_email);

        btnLogout.setOnClickListener(view -> {

            logout();

//           redirect to login activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userSes = UserSession.getInstance(getApplicationContext());

        Log.d("onStart:", "userToken: " + userSes.getUserToken());

        if (!userSes.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        tvUserName.setText(String.format("Hi, %s", userSes.getUserName()));
        tvUserEmail.setText(MessageFormat.format("Your email id is {0}", userSes.getUserEmail()));
    }

    private void logout() {

        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.API_BASE_URL) + "/logout",
                response -> {
                    Log.d("Activity:", "response: " + response);
                    try {
                        JSONObject responseJsonObject = new JSONObject(response);

                        String message = responseJsonObject.getString("message");

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                        Log.d("Logout", "logout: " + message);

                        userSes.userLogout();

                    } catch (JSONException e) {
                        Log.d("Activity:", "JSONException: " + e);
                        e.printStackTrace();
                    }

                    // Hiding the progress dialog after all task complete.
                    progressDialog.dismiss();
                },
                error -> {
                    Log.d("Activity:", "ServerError: " + error);
                    // Hiding the progress dialog after all task complete.
                    progressDialog.dismiss();
                    // Showing error message if something goes wrong.
                    Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
                }) {

            @NonNull
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + userSes.getUserToken());
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

}