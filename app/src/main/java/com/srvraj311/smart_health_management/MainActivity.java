package com.srvraj311.smart_health_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.srvraj311.smart_health_management.API.RetrofitAPICall;
import com.srvraj311.smart_health_management.Config.Config;
import com.srvraj311.smart_health_management.HomePage.ui.HomeScreen;
import com.srvraj311.smart_health_management.LoginSignup.WelcomeScreen;
import com.srvraj311.smart_health_management.Models.ErrorScreen;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///Hide the Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //Set the view
        setContentView(R.layout.activity_splash);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                setBaseURl();
                if (TokenExists()) {
                    Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), WelcomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1500);
    }

    private void setBaseURl() {
        if(!Config.isOnline(MainActivity.this)){
            Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
            startActivity(intent);
            finish();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/srvraj311/health.io-API/main/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);
        Call<HashMap<String, String>> call = apiCall.getUrl();
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    String url = response.body().get("url");
                    Log.e("URL GOT FROM GITHUB", url);
                    setUrlToSharedPreferences(url);
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Log.e("URL GOT FROM GITHUB", "ERROR");
                String url = "null";
                setUrlToSharedPreferences(url);
                Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        try {
            call.wait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUrlToSharedPreferences(String url){
        SharedPreferences sharedPreferences = getSharedPreferences("BaseUrl", MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.clear();
        editor.putString("url", url);
        editor.apply();
    }

    private boolean TokenExists() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        try {
            String json = sharedPreferences.getString("token", "");
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            HashMap<String, String> data = gson.fromJson(json, type);
            if (data.containsKey("dog_tag")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}