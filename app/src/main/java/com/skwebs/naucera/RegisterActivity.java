package com.skwebs.naucera;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity:";
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    TextInputLayout etMobile, etOtp, etName, etEmail, etPassword, etConfirmation;
    Button btnChangeMobileNum, btnSendOtp, btnResendOtp, btnVerifyOtp, btnRegister;
    String mobileNum, otp, name, email, password, confirmation, mVerificationId;

    PhoneAuthProvider.ForceResendingToken mResendToken;

    LinearLayout layoutEnterMobile, layoutVerifyOtp, layoutRegisterDetails;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Objects.requireNonNull(getSupportActionBar()).setTitle("REGISTER");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        layoutEnterMobile = findViewById(R.id.ll_enter_mobile);
        layoutVerifyOtp = findViewById(R.id.ll_verify_otp);
        layoutRegisterDetails = findViewById(R.id.ll_register_details);

        etMobile = findViewById(R.id.et_mobile);
        etOtp = findViewById(R.id.et_otp);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmation = findViewById(R.id.etConfirmation);

        btnChangeMobileNum = findViewById(R.id.btn_change_mobile_num);
        btnRegister = findViewById(R.id.btnRegister);
        btnSendOtp = findViewById(R.id.btn_send_otp);
        btnResendOtp = findViewById(R.id.btn_resend_otp);
        btnVerifyOtp = findViewById(R.id.btn_verify_otp);

        btnRegister.setOnClickListener(v -> checkRegister());

        mVerificationId = "";
        mResendToken = null;

        btnSendOtp.setOnClickListener(view -> validateMobileNum());

        btnVerifyOtp.setOnClickListener(view -> verifyOtp());

        btnChangeMobileNum.setOnClickListener(view -> changeMobileNum());

    }

    private void changeMobileNum() {
        layoutEnterMobile.setVisibility(View.VISIBLE);
        layoutVerifyOtp.setVisibility(View.GONE);
    }

    private void validateMobileNum() {
        mobileNum = Objects.requireNonNull(etMobile.getEditText()).getText().toString().trim();
        if (mobileNum.isEmpty()) {
            Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
        } else if (!(mobileNum.charAt(0) == '6') && !(mobileNum.charAt(0) == '7') && !(mobileNum.charAt(0) == '8') && !(mobileNum.charAt(0) == '9')) {
            Toast.makeText(this, "Mobile number must be start with 6, 7, 8 or 9", Toast.LENGTH_SHORT).show();
        } else if (mobileNum.length() != 10) {
            Toast.makeText(this, "Mobile number must be in 10 digits.", Toast.LENGTH_SHORT).show();
        } else {
            sendOtp();
        }
    }

    private void sendOtp() {
        progressDialog.setMessage("Sending OTP.");
        progressDialog.show();
        //                on auto verification
        // This callback will be invoked in two situations:
        // 1 - Instant verification. In some cases the phone number can be instantly
        //     verified without needing to send or enter a verification code.
        // 2 - Auto-retrieval. On some devices Google Play services can automatically
        //     detect the incoming verification SMS and perform verification without
        //                //     user action.
        //
        //                signInWithPhoneAuthCredential(credential);
        // This callback is invoked in an invalid request for verification is made,
        // for instance if the the phone number format is not valid.
        //
        // Show a message and update the UI
        //                on otp sent hide mobile input layout and show otp verification layout
        // The SMS verification code has been sent to the provided phone number, we
        // now need to ask the user to enter the code and then construct a credential
        //                // by combining the code with a verification ID.
        //                // Save verification ID and resending token so we can use them later
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
//                on auto verification
                layoutVerifyOtp.setVisibility(View.GONE);
                layoutRegisterDetails.setVisibility(View.VISIBLE);

                Toast.makeText(RegisterActivity.this, "Number verified successfully.", Toast.LENGTH_SHORT).show();

                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
//                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
//
//                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
//
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(RegisterActivity.this, "Invalid request", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(RegisterActivity.this, "The SMS quota for the project has been exceeded", Toast.LENGTH_SHORT).show();
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                progressDialog.dismiss();

                Toast.makeText(RegisterActivity.this, "OTP Sent.", Toast.LENGTH_SHORT).show();
//                on otp sent hide mobile input layout and show otp verification layout
                layoutEnterMobile.setVisibility(View.GONE);
                layoutVerifyOtp.setVisibility(View.VISIBLE);
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
//                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
//                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
//        process to send otp here.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + mobileNum)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOtp() {
        progressDialog.setMessage("Verifying OTP.");
        progressDialog.show();
        otp = Objects.requireNonNull(etOtp.getEditText()).getText().toString().trim();
        if (otp.isEmpty()) {
            Toast.makeText(this, "OTP is required.", Toast.LENGTH_SHORT).show();
        } else if (otp.length() != 6) {
            Toast.makeText(this, "Invalid OTP.", Toast.LENGTH_SHORT).show();
        } else {
            String verificationId = mVerificationId;
            if (verificationId != null) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(task -> {
//                    dismiss progressbar on completing task
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        layoutVerifyOtp.setVisibility(View.GONE);
                        layoutRegisterDetails.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(RegisterActivity.this, "OTP is not valid.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
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

        String BASE_API_URL = getString(R.string.API_BASE_URL);
        String url = BASE_API_URL + "/users";
        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("Register", "response: " + response);

                    try {
                        JSONObject responseJsonObject = new JSONObject(response);

                        boolean error = responseJsonObject.getBoolean("error");
                        if (error) {
                            JSONObject errorObj = new JSONObject(responseJsonObject.getString("errors"));
//                            if errors has in error then
                            if (responseJsonObject.has("errors")) {

//                            if name has error
                                if (errorObj.has("name")) {
                                    etName.setError(errorObj.getString("name"));
                                    Toast.makeText(this, errorObj.getString("name"), Toast.LENGTH_SHORT).show();
                                }
//                            if email has error
                                if (errorObj.has("email")) {
                                    etEmail.setError(errorObj.getString("email"));
                                    Toast.makeText(this, errorObj.getString("email"), Toast.LENGTH_SHORT).show();
//                                remove name error
                                    etName.setError(null);
                                }
//                            if password has error
                                if (errorObj.has("password")) {
                                    etPassword.setError(errorObj.getString("password"));
                                    Toast.makeText(this, errorObj.getString("password"), Toast.LENGTH_SHORT).show();
//                                remove email error
                                    etEmail.setError(null);
                                }
//                            if confirm password has error
                                if (errorObj.has("password_confirmation")) {
                                    etConfirmation.setError(errorObj.getString("password_confirmation"));
                                    Toast.makeText(this, errorObj.getString("password_confirmation"), Toast.LENGTH_SHORT).show();
//                                remove password email
                                    etPassword.setError(null);
                                }
                                Log.d(TAG, "errorObj:: " + errorObj.getString("email"));

                            }
                        } else {
//                            REGISTRATION SUCCESSFUL COMPLETED THEN
//                            remove all error messages
                            etName.setError(null);
                            etEmail.setError(null);
                            etPassword.setError(null);
                            etConfirmation.setError(null);
//                            show toast for successful registration
                            Toast.makeText(this, "You have registered successfully!", Toast.LENGTH_SHORT).show();
//
//                            redirect to login activity
                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);
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

    private void alertFail(String s) {
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setIcon(R.drawable.ic_warning)
                .setMessage(s)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
    }
}