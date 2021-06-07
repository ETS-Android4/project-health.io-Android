package com.srvraj311.smart_health_management.Config;

import android.util.Log;

import com.srvraj311.smart_health_management.API.RetrofitAPICall;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Config {


    // TODO : Add a hosted text to change the text of the IP address without updating the app.
    private static String URL = "http://13.127.211.185:8080/";
    public Config(){
        setUrl();
    }

    public static void setUrl(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/srvraj311/project-health.io-Android/master/url.json?token=AO3ZAOLQ5B3YWD6QNRHUXR3AY5XGQ")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);
        Call<String> call = apiCall.getUrl();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                URL = response.body();
                Log.e("BASE URL : ", "SUCCESSFULLY UPDATED");
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                URL = "http://13.127.211.185:8080/";
            }
        });
    }

    public static String getURL() {
        return URL;
    }
}
