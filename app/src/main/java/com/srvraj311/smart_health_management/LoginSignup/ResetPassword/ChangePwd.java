package com.srvraj311.smart_health_management.LoginSignup.ResetPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.srvraj311.smart_health_management.API.RetrofitAPICall;
import com.srvraj311.smart_health_management.Config.Config;
import com.srvraj311.smart_health_management.R;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePwd extends AppCompatActivity {

    TextInputEditText emailBox;
    Button send_otp;
    TextView tagline;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Hide the Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        // Set The layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        // Hooks
        emailBox = findViewById(R.id.email_reset);
        send_otp = findViewById(R.id.send_verification);
        tagline = findViewById(R.id.heading_change_password);
        progressBar = findViewById(R.id.progressbar_otp);




        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });

                String email = String.valueOf(emailBox.getText());
                if(!email.equals(""))
                sendOtp(email);
            }
        });


    }

    private void sendOtp(String email) {
        // Initialising retrofit
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getURL(getApplicationContext()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);
        Call<HashMap<String, String>> call = apiCall.resetPassword(map);
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                try {
                    assert response.body() != null;
                    if (response.body().containsKey("error")) {
                        tagline.setText(response.body().get("error"));
                        tagline.setTextColor(Color.RED);
                        progressBar.setVisibility(View.INVISIBLE);
                    } else {
                        tagline.setText(response.body().get("status"));
                        // Open OTP Entering Page in this Condition
                        Intent intent = new Intent(getApplicationContext(), EnterOTP.class);
                        intent.putExtra("email", email);
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(intent);
                    }
                }catch (Exception e){
                    tagline.setText(R.string.failed_fetch_from_server);
                    e.printStackTrace();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                tagline.setText(R.string.network_error);
                tagline.setTextColor(Color.RED);
                t.printStackTrace();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
