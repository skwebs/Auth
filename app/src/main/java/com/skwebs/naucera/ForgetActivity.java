package com.skwebs.naucera;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class ForgetActivity extends AppCompatActivity {

    private static final String TAG = "ForgetActivity:";
    TextView mobileNumber;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Objects.requireNonNull(getSupportActionBar()).setTitle("FORGET PASSWORD");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        Button btnSubscribe = findViewById(R.id.btn_subscribe);

        mobileNumber = findViewById(R.id.mobile_number);

        btnSubscribe.setOnClickListener(view -> FirebaseMessaging.getInstance().subscribeToTopic("NauceraAppTopic"));

        GetNumber();
    }

    // Function will run after click to button
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void GetNumber() {

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

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast

                    Log.d(TAG, "token: "+token);
                    Toast.makeText(ForgetActivity.this, "fmc token: "+token, Toast.LENGTH_SHORT).show();
                });
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