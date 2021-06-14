package com.srvraj311.smart_health_management.HospitalInfoScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.srvraj311.smart_health_management.API.RetrofitAPICall;
import com.srvraj311.smart_health_management.Config.Config;
import com.srvraj311.smart_health_management.HospitalScreen.Hospital;
import com.srvraj311.smart_health_management.Models.EmergencyCases;
import com.srvraj311.smart_health_management.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HospitalInfoScreen extends AppCompatActivity {

    TextView name;
    TextView type;
    TextView grade;
    String id;
    Hospital hospital;
    List<EmergencyCases> emergencyCasesList;

    private void setViews() {
        // Setting Name and City
        String hname = hospital.getName() + ", " + hospital.getCity_name() +  ", " + hospital.getState_name();
        name.setText(hname);
        // Setting Type
        String htype = hospital.getType();
        htype = "" + Character.toUpperCase(htype.charAt(0)) + htype.substring(1,htype.length());
        type.setText(htype);
        if(htype.charAt(0) == 'p' || htype.charAt(0) == 'P'){
            type.setTextColor(Color.BLUE);
        }else{
            type.setTextColor(Color.MAGENTA);
        }
        // Setting Grade
        String hgrade = hospital.getGrade();
        grade.setText(hgrade);
        if(hgrade.equals("A")){
            grade.setTextColor(Color.parseColor("#247700"));
        }else if(hgrade.equals("B")){
            grade.setTextColor(Color.parseColor("#FF9800"));
        }else{
            grade.setTextColor(Color.parseColor("#FF2222"));
        }
        // TODO : Add distance here
//        String geoCord = hospital.getGeolocation().trim();
//        String current = "40.6655101,-73.89188969999998";
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://maps.googleapis.com/maps/api/distancematrix/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);
//        Call<String> call = apiCall.getMatrix(current, geoCord);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                Log.e("MATRIX API ", response.body());
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_info_screen);

        id = getIntent().getExtras().getString("id");
        // Getting data from Server
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        });

        // Hooks
        name = findViewById(R.id.display_name);
        type = findViewById(R.id.display_type);
        grade = findViewById(R.id.display_grade);

        // Assigning Data to Valued


    }

    private void getData() {
        // Make a web call and get data
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);
        //Pass ID of Hospital clicked To URL
        // Getting Emergency Cases On The Hospital --------------------------------------------------------------------------
        Call<List<EmergencyCases>> call = apiCall.getEmergencyCases(id);
        call.enqueue(new Callback<List<EmergencyCases>>() {
            @Override
            public void onResponse(Call<List<EmergencyCases>> call, Response<List<EmergencyCases>> response) {
                if(response.code() == 200){
                    emergencyCasesList = response.body();
                }else{
                    emergencyCasesList = new ArrayList<>();
                }
            }

            @Override
            public void onFailure(Call<List<EmergencyCases>> call, Throwable t) {
                new AlertDialog.Builder(HospitalInfoScreen.this)
                        .setTitle("Network Error")
                        .setMessage("Seems like you are not Connected to Internet, Retry")
                        .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.retry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getData();
                            }
                        })
                        .show();
            }
        });
        //----------------------------------------------------------------------------------------------------------------------------------------------------------

        // Getting Hospital of ID
        // getting Local Token
        HashMap<String, String> token = new HashMap<>();
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        try {
            String json = sharedPreferences.getString("token", "");
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            token = gson.fromJson(json, type);
            token.put("licence_id", id);
        }catch (Exception e){
            //TODO :  Write Session Expired Code Here
        }

        Call<Hospital> call2 = apiCall.getHospitalById(token);
       call2.enqueue(new Callback<Hospital>() {
           @Override
           public void onResponse(Call<Hospital> call, Response<Hospital> response) {
               if(response.code() == 200){
                   hospital = response.body();
                   Log.e("HOSPITAL DATA ", "Success");
                   setViews();
               }else if(response.code() == 406){
                   //TODO Session Expired Code
               }else{
                   // Some Error
                   Toast.makeText(getApplicationContext(), "Some Error Occurred While Getting Details", Toast.LENGTH_LONG).show();
               }
           }

           @Override
           public void onFailure(Call<Hospital> call, Throwable t) {
               new AlertDialog.Builder(HospitalInfoScreen.this)
                       .setTitle("Network Error")
                       .setMessage("Seems like you are not Connected to Internet, Retry")
                       .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               finish();
                           }
                       })
                       .setNegativeButton(R.string.retry, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               getData();
                           }
                       })
                       .show();
           }
       });
    }
}