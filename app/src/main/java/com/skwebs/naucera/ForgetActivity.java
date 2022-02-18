package com.skwebs.naucera;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Objects;

public class ForgetActivity extends AppCompatActivity {

    TextView mobileNumber;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Objects.requireNonNull(getSupportActionBar()).setTitle("FORGET PASSWORD");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        final TextView textView;
        textView = findViewById(R.id.text);
        mobileNumber = findViewById(R.id.mobile_number);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://anshumemorial.in/lv8_api/api/users";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Display the first 500 characters of the response string.
                    textView.setText("Response is: " + response.substring(0, 500));
                }, error -> textView.setText("That didn't work!"));

// Add the request to the RequestQueue.
        queue.add(stringRequest);

        GetNumber();
//        getMobileNumber();
    }

//    private void getMobileNumber() {
//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mobileNumber.setText(telephonyManager.getLine1Number());
//    }


    // Function will run after click to button
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void GetNumber() {
//    public void GetNumber(View v) {

        if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // Permission check

            // Create obj of TelephonyManager and ask for current telephone service
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String phoneNumber = telephonyManager.getLine1Number();
            System.out.println(telephonyManager);
            mobileNumber.setText(phoneNumber);
        } else {
            // Ask for permission
            requestPermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, READ_SMS) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                closeNow();
                return;
            }
            String phoneNumber = telephonyManager.getLine1Number();
            mobileNumber.setText(phoneNumber);
        } else {
            throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    private void closeNow() {
        finishAffinity();
    }
}