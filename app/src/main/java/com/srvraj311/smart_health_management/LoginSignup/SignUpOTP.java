package com.srvraj311.smart_health_management.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.jaredrummler.android.device.DeviceName;
import com.srvraj311.smart_health_management.API.RetrofitAPICall;
import com.srvraj311.smart_health_management.Config.Config;
import com.srvraj311.smart_health_management.HomePage.ui.HomeScreen;
import com.srvraj311.smart_health_management.Models.User;
import com.srvraj311.smart_health_management.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpOTP extends AppCompatActivity {

    TextView message;
    Button verify;
    TextInputEditText otp_box;
    Button resend;
    User user;
    FrameLayout darken;
    ProgressBar progressBar;
    // Declaring General Elements;
    HashMap<String, String> userToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_otp);

        // Hooks
        message = findViewById(R.id.otp_email_message);
        verify = findViewById(R.id.verify_otp);
        otp_box = findViewById(R.id.otp_box);
        resend = findViewById(R.id.resend_otp);
        darken = findViewById(R.id.darken);
        progressBar = findViewById(R.id.progressbar_otp_2);


        Intent i = getIntent();
        // Getting Data and Creating a New User
        String first_name = i.getExtras().getString("first_name");
        String last_name = i.getExtras().getString("last_name");
        String email = i.getExtras().getString("email");
        String password = i.getExtras().getString("password");
        String dog_tag = i.getExtras().getString("dog_tag");
        String mobile_num = i.getExtras().getString("mobile_num");
        String age = i.getExtras().getString("age");
        Set<String> known_device = new HashSet<>();

        // creating a User with Received data
        user = new User(first_name, last_name, email, password, age, known_device);


        // Sending an Actual OTP from server
        sendOTP(email);

        // Resend Button Configuration
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP(email);
            }
        });

    }

    private void sendOTP(String email) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                darken.setVisibility(View.VISIBLE);
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);

        Call<HashMap<String, String>> call = apiCall.sendOtp(email);

        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if(response.code() == 200){
                    if(response.body().containsKey("error")){
                        message.setText(response.body().get("error"));
                        message.setTextColor(Color.RED);
                        //Progress Bar INVISIBLE below
                        darken.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }else{
                        message.setText(response.body().get("status"));
                        //Progress Bar INVISIBLE below
                        darken.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        String s = "OTP sent to Email-ID" + email;
                        message.setText(s);
                        verification(email);
                    }
                }
            }
            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                message.setText(R.string.network_error_3);
                t.printStackTrace();
            }
        });
    }

    private void verification(String email) {
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!otp_box.getText().equals("")) {

                    String otp = String.valueOf(otp_box.getText());
                    sendOtpForVerification(otp, email);
                }
            }
        });
    }

    private void sendOtpForVerification(String otp, String email) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Progress Bar INVISIBLE below
                darken.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("otp", otp);
        map.put("email", email);
        Call<HashMap<String, String>> call = apiCall.verifyOtp(map);
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                assert response.body() != null;
                if(response.body().containsKey("error")){
                    message.setText(response.body().get("error"));
                    message.setTextColor(Color.RED);
                    progressBar.setVisibility(View.INVISIBLE);
                    darken.setVisibility(View.INVISIBLE);
                }else if(response.code() != 200){
                    message.setText(response.message());
                    message.setTextColor(Color.RED);
                    progressBar.setVisibility(View.INVISIBLE);
                    darken.setVisibility(View.INVISIBLE);
                }else {
                    message.setText(R.string.otp_verified);
                    signUp(user);
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                message.setText(R.string.network_error_3);
                t.printStackTrace();
            }
        });
    }

    private void signUp(User newUser) {

        // Creating a retrofit Instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Instancing my interface
        RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);

        // Creating a call
        Call<HashMap<String, String>> call = apiCall.signUp(newUser);

        //getting data
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                // Can check code of status using response.code()
                // Getting actual Data
                try {
                    assert response.body() != null;
                    if (response.body().containsKey("error")) {
                        message.setText(response.body().get("error"));
                        message.setTextColor(Color.RED);

                        // Progress Bar INVISIBLE below
                        darken.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    } else {
                        // Progress Bar INVISIBLE below
//                        darken.setVisibility(View.INVISIBLE);
//                        progressBar.setVisibility(View.INVISIBLE);

                        message.setText(R.string.signedUpSucces);
                        userToken = response.body();

                        // Call to save token to SharedPreference
                        saveToken(userToken, "token");
                        Intent intent = new Intent(SignUpOTP.this, HomeScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }catch (Exception e){
                    message.setText(R.string.networkError);
                    message.setTextColor(Color.RED);
                    // Progress Bar INVISIBLE below
//                    darken.setVisibility(View.INVISIBLE);
//                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Log.e("Get Request :" , "Error, Request Failed");
                t.printStackTrace();
            }
        });
    }
    private void saveToken(HashMap<String, String> userToken, String place) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String token = gson.toJson(userToken);
        editor.clear();
        editor.putString(place,token);
        editor.apply();
    }
}