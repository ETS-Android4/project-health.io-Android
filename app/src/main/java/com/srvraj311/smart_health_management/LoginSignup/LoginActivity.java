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
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.jaredrummler.android.device.DeviceName;
import com.srvraj311.smart_health_management.API.RetrofitAPICall;
import com.srvraj311.smart_health_management.Config.Config;
import com.srvraj311.smart_health_management.HomePage.ui.HomeScreen;
import com.srvraj311.smart_health_management.LoginSignup.ResetPassword.ChangePwd;
import com.srvraj311.smart_health_management.Models.LoginRequest;
import com.srvraj311.smart_health_management.R;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText emailField;
    TextInputEditText passwordField;
    CheckBox rememberMe;
    Button loginBtn;
    TextView message;
    TextView forgot_link;
    ProgressBar progressBar;
    FrameLayout darken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Hide the Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //Set Activity View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Hooks
        emailField = findViewById(R.id.email_login);
        passwordField = findViewById(R.id.password);
        rememberMe = findViewById(R.id.remember_me);
        loginBtn = findViewById(R.id.login);
        message = findViewById(R.id.message_login);
        forgot_link = findViewById(R.id.forgot_password);
        progressBar = findViewById(R.id.signup_progress);
        darken = findViewById(R.id.darken_bg);

        emailField.requestFocus();

        // Check input and Login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(emailField.getText().toString(), passwordField.getText().toString(), rememberMe.isChecked());
            }
        });
        forgot_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChangePwd.class);
                startActivity(intent);
            }
        });


    }

    private void login(String email, String password, boolean remember) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                darken.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        if(!isEmpty(email, password)){
            LoginRequest loginRequest;
            if(remember){
                loginRequest = new LoginRequest(email, password, DeviceName.getDeviceName()) ;
            }else{
                loginRequest = new LoginRequest(email, password);
            }

            // Making a request - POST
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.getURL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);
            Call<HashMap<String, String>> call = apiCall.login(loginRequest);
            call.enqueue(new Callback<HashMap<String, String>>() {
                @Override
                public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                    try {
                        if (response.body().containsKey("error")) {
                            message.setText(response.body().get("error"));
                            message.setTextColor(Color.RED);
                            // Progress Bar INVISIBLE below
                            darken.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            return;
                        }
                        HashMap<String, String> userToken = response.body();
                        saveToken(userToken, "token");
                        message.setText(R.string.success);
                        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                        // Progress Bar INVISIBLE below
                        darken.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }catch (Exception e){
                        message.setText(R.string.networkError);
                        message.setTextColor(Color.RED);
                        // Progress Bar INVISIBLE below
                        darken.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
                @Override
                public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                    Log.e("Get Request :" , "Error, Request Failed");
                    t.printStackTrace();
                    // Progress Bar INVISIBLE below
                    darken.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        }
    }

    private boolean isEmpty(String email, String password) {
        return email.equals("") || password.equals("");
    }
    private void saveToken(HashMap<String, String> userToken, String location){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String token = gson.toJson(userToken);
        editor.clear();
        editor.putString(location, token);
        editor.apply();
    }
}