package com.srvraj311.smart_health_management.Config;

import android.util.Log;
import android.widget.Toast;

import com.srvraj311.smart_health_management.API.RetrofitAPICall;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Config {


    // TODO : Add a hosted text to change the text of the IP address without updating the app.
    private static String URL = "http://15.206.123.255:8080/";

    public static String getURL(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/srvraj311/health.io-API/main/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);
        Call<HashMap<String, String>> call = apiCall.getUrl();
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if(response.code() == 200) {
                    URL = response.body().get("url");
                    Log.e("URL GOT FROM GITHUB", URL);
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Log.e("URL GOT FROM GITHUB", "ERROR");
                URL = "http://15.206.123.255:8080/";
            }
        });
        return URL;
    }

//    public static String getURL() {
//        setUrl();
//        return URL;
//    }
}
