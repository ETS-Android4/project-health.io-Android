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
import android.view.Window;
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
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HospitalInfoScreen extends AppCompatActivity {

    TextView name;
    TextView type;
    TextView grade;
    TextView description;
    TextView bed;
    TextView icu;
    TextView ccu;
    TextView ventilators;
    TextView oxygen;
    TextView xray;
    TextView mri;
    TextView ecg;
    TextView ultraSound;
    TextView ambulance;

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
        // Description
        String hdescription = "This data functionality is not yet implemented onto the database part of the application, Will soon be added";
        description.setText(hdescription);

        // ------ Availabilities Section -----//
        // BED
        String hbed = hospital.getVacant_bed() + " / " + hospital.getNo_of_bed();
        bed.setText(hbed);
        bed.setTextColor(Color.parseColor(changeColour(hospital.getVacant_bed(), hospital.getNo_of_bed())));

        // ICU
        String hicu = hospital.getVacant_icu() + " / " + hospital.getIcu();
        icu.setText(hicu);
        icu.setTextColor(Color.parseColor(changeColour(hospital.getVacant_icu(), hospital.getIcu())));

        // CCU
        String hccu = hospital.getVacant_ccu() + " / " + hospital.getCcu();
        ccu.setText(hccu);
        ccu.setTextColor(Color.parseColor(changeColour(hospital.getVacant_ccu(), hospital.getCcu())));

        // Ventilator
        String hventilator = hospital.getVacant_ventilators() + " / " + hospital.getVentilators();
        ventilators.setText(hventilator);
        ventilators.setTextColor(Color.parseColor(changeColour(hospital.getVacant_ventilators() , hospital.getVentilators())));

        // Oxygen
        String hoxygen = hospital.getVacant_oxygen_cylinders() + " / " + hospital.getOxygen_cylinders();
        oxygen.setText(hoxygen);
        oxygen.setTextColor(Color.parseColor(changeColour(hospital.getVacant_oxygen_cylinders(), hospital.getOxygen_cylinders())));

        //----- Facilities -----
        // XRay
        String hxray = checkNull(hospital.getX_ray());
        xray.setText(hxray);
        xray.setTextColor(Color.parseColor(setColor(hxray)));

        // MRI
        String hmri = checkNull(hospital.getMri());
        xray.setText(hmri);
        xray.setTextColor(Color.parseColor(setColor(hmri)));

        // ECG
        String hecg = checkNull(hospital.getEcg());
        xray.setText(hecg);
        xray.setTextColor(Color.parseColor(setColor(hecg)));

        // ultra Sound
        String hultra_sound = checkNull(hospital.getUltra_sound());
        xray.setText(hultra_sound);
        xray.setTextColor(Color.parseColor(setColor(hultra_sound)));

        // Ambulance
        String hambulance = checkNull(hospital.getVacant_ambulance());
        xray.setText(hambulance);
        xray.setTextColor(Color.parseColor(setColor(hambulance)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
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
        description = findViewById(R.id.display_description);
        bed = findViewById(R.id.display_bed);
        icu = findViewById(R.id.display_icu);
        ccu = findViewById(R.id.display_ccu);
        ventilators = findViewById(R.id.display_ventilator);
        oxygen = findViewById(R.id.display_oxygen);
        xray = findViewById(R.id.display_xray);
        mri = findViewById(R.id.display_mri);
        ecg = findViewById(R.id.display_ecg);
        ultraSound = findViewById(R.id.display_ultra_sound);
        ambulance = findViewById(R.id.display_ambulance);

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


    public String changeColour(String num, String total){
        int a = Integer.parseInt(num);
        int b = Integer.parseInt(total);
        int percent = (a / b) * 100;
        if (percent > 75) {
            return "#247700";
        }else if(percent > 55){
            return "#FF9800";
        }else{
            return "#FF2222";
        }
    }
    public String checkNull(String data){
        if(data.equals("") || data.equals("NA") || data.equals("0")){
            return "NA";
        }else{
            return data;
        }
    }
    public String setColor(String nums){
        if(nums.equals("NA")){
            return "#FF2222";
        }else{
            return "#247700";
        }
    }
}