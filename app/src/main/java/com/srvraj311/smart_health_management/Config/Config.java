package com.srvraj311.smart_health_management.Config;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.AndroidException;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.srvraj311.smart_health_management.API.RetrofitAPICall;
import com.srvraj311.smart_health_management.HospitalScreen.Hospital;
import com.srvraj311.smart_health_management.Models.ErrorScreen;

import java.lang.reflect.Type;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Config {

    private static final String URL = "http://65.2.141.250:8080/";

    public static String getURL(Context context) {
        String url = "";

        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("BaseUrl", MODE_PRIVATE);
            return sharedPreferences.getString("url", "");
        } catch (Exception e) {
            Log.e("URL", "URL not Available");
        }

        if (url.equals("")) {
            Intent intent = new Intent(context, ErrorScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            throw new RuntimeException("No internet, or Issues connecting to Network, Restart App to fix.");
        }
        return url;
    }

    public static boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static String convertSecondsToTime(String durationInSeconds) {
        long seconds = Long.parseLong(durationInSeconds);
        long minutes = seconds / 60;
        if (minutes <= 60) {
            return String.valueOf(minutes) + "min";
        } else {
            long hours = minutes / 60;
            if (hours <= 24) {
                return String.valueOf(hours) + "hour";
            } else {
                long day = hours / 24;
                return String.valueOf(day) + "days";
            }
        }
    }

    public static void saveLocationDataToSharedPref(Context context, String currCord, String licence_id, String durationText) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("location-data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        HashMap<String, String> locMap = new HashMap<>();
        String fromStorage = sharedPreferences.getString(currCord, "{}");
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        locMap = gson.fromJson(fromStorage, type);
        locMap.put(licence_id, durationText);
        String locationPerHospital = gson.toJson(locMap);
        editor.putString(currCord, locationPerHospital);
        editor.apply();
        Log.e("LOCATION-STORAGE", "Writing location data to storage");
    }

    public static boolean locationAlreadyInSharedPref(Context context, String currCord, String licence_id) {
        Log.e("LOCATION-STORAGE", "Checking");
        SharedPreferences sharedPreferences = context.getSharedPreferences("location-data", MODE_PRIVATE);
        if (sharedPreferences != null) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString(currCord, "");
            if (json.equals("")) {
                return false;
            }
            HashMap<String, String> locMap = new HashMap<>();
            String fromStorage = sharedPreferences.getString(currCord, "{}");
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            locMap = gson.fromJson(fromStorage, type);
            return locMap.containsKey(licence_id);
        }
        //Log.e("LOCATION-STORAGE", "Shared Pref null");
        return false;
    }

    public static boolean isAnyBedAvailable(Hospital hospital) {
        try {
            int bed = Integer.parseInt(hospital.getVacant_bed());
            int icu = Integer.parseInt(hospital.getIcu());
            int ccu = Integer.parseInt(hospital.getVacant_ccu());
            int ventilator = Integer.parseInt(hospital.getVentilators());
            if (bed > 0 || icu > 0 || ccu > 0 || ventilator > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
