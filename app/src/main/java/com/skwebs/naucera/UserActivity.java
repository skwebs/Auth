package com.skwebs.naucera;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class UserActivity extends AppCompatActivity {


    private static final String TAG = "UserActivity:";
    final ArrayList<UserModel> userList = new ArrayList<>();
    RequestQueue requestQueue;
    RecyclerView userRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Users List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        userRecyclerView = findViewById(R.id.user_recycler_view);
        userRecyclerView.setHasFixedSize(true);

        getUserDetails();
    }

    private void getUserDetails() {
        ProgressDialog progressDialog = ProgressDialog.show(this, null, "Please wait");
        String url = "http://192.168.29.122:8000/api/users";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String name = jsonObject.getString("name");
                            String email = jsonObject.getString("email");

                            userList.add(new UserModel(id, name, email));

                            Log.d(TAG, "id: "+id+" | Name: " + name + ", | email: " + email);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    userRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    UserAdapter userAdapter = new UserAdapter(this, userList);
                    userRecyclerView.setAdapter(userAdapter);

                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();

                }, error -> {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            Log.d(TAG, "getUserDetails: " + error);
        });

        requestQueue.add(jsonObjectRequest);
    }

}