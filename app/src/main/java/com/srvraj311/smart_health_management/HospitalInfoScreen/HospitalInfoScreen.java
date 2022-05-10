package com.srvraj311.smart_health_management.HospitalInfoScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.srvraj311.smart_health_management.API.RetrofitAPICall;
import com.srvraj311.smart_health_management.Config.Config;
import com.srvraj311.smart_health_management.HospitalScreen.DistrictSelectorDialog;
import com.srvraj311.smart_health_management.HospitalScreen.Hospital;
import com.srvraj311.smart_health_management.HospitalScreen.HospitalScreen;
import com.srvraj311.smart_health_management.Models.EmergencyCases;
import com.srvraj311.smart_health_management.R;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.srvraj311.smart_health_management.R.drawable.ic_private;

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
    TextView a_positive;
    TextView a_negative;
    TextView b_positive;
    TextView b_negative;
    TextView o_positive;
    TextView o_negative;
    TextView ab_positive;
    TextView ab_negative;
    TextView d_time;
    TextView lastUpdated;
    TextView address;
    TextView emergency_no;
    ImageButton refreshButton;
    ImageButton typeButton;
    ImageButton gradeButton;
    ImageButton directionsButton;
    ImageButton call;
    Button book;
    String id;
    List<Hospital> hospitals;
    Hospital hospital;
    List<EmergencyCases> emergencyCasesList;

    SwipeRefreshLayout swipeDown;
    TextView eta;


    private void setViews() {

        // Setting Name and City
        String hname = hospital.getName() + ", " + hospital.getCity_name() + ", " + hospital.getState_name();
        String out = "";
        if (hname.split(",")[1].equals("null")) {
            out = hname.split(",")[0];
        } else {
            out = hname;
        }

        name.setText(out);

        // Setting Type
        String htype = hospital.getType();
        htype = "" + Character.toUpperCase(htype.charAt(0)) + htype.substring(1, htype.length());
        type.setText(htype);
        if (htype.charAt(0) == 'p' || htype.charAt(0) == 'P') {
            typeButton.setImageResource(R.drawable.ic_private);
        } else {
            typeButton.setImageResource(R.drawable.ic_government);
        }

        // Setting Grade
        String hgrade = hospital.getGrade() + " Grade";
        grade.setText(hgrade);
        if (hospital.getGrade().equals("A")) {
            gradeButton.setImageResource(R.drawable.ic_alphabet_a);
        } else if (hospital.getGrade().equals("B")) {
            gradeButton.setImageResource(R.drawable.ic_alphabet_b);
        } else {
            gradeButton.setImageResource(R.drawable.ic_alphabet_c);
        }

        // Setting up Address
        try {
            StringBuilder addrFinal = new StringBuilder();
            String addr = hospital.getAddress();
            String[] addrs = addr.split(",");
            for (int i = 0; i < addrs.length - 1; i++) {
                if (!addrs[i].equals("null")) {
                    addrFinal.append(addrs[i]).append(",");
                }
            }
            System.out.println(addrFinal);
            if (!addrs[addrs.length - 1].equals("null")) {
                int pin = (int) Float.parseFloat(addrs[addrs.length - 1]);
                addrFinal.append(String.valueOf(pin));
            }
            address.setText(addrFinal.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("LOG", e.getMessage());
            address.setText(hospital.getAddress());
        }


        // Distance and ETA
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(HospitalInfoScreen.this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            //Log.e("Location", location.toString());
                            // Logic to handle location object
                            String currCord = String.valueOf(location.getLatitude()).trim() + "," + String.valueOf(location.getLongitude()).trim();
                            //
                            Log.e("LOCATION-STORAGE", "Checking localstorage for Location");
                            if (Config.locationAlreadyInSharedPref(HospitalInfoScreen.this, currCord, hospital.getLicence_id())) {
                                Log.e("LOCATION-STORAGE", "Location found in localStorage");
                                eta.setText(getETAFromSharedPref(hospital.getLicence_id(), currCord));
                            } else {
                                HashMap<String, String> requestMap = new HashMap<>();
                                requestMap.put("wp1", currCord);
                                requestMap.put("wp2", hospital.getGeolocation());
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(Config.getURL(HospitalInfoScreen.this))
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);
                                Call<HashMap<String, String>> call = apiCall.getETA(requestMap);
                                try {
                                    call.enqueue(new Callback<HashMap<String, String>>() {
                                        @Override
                                        public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                                            if (response != null) {
                                                if (response.body() != null) {
                                                    String durationInSeconds = response.body().get("travelDuration");
                                                    String durationText = Config.convertSecondsToTime(durationInSeconds);
                                                    Log.e("LOCATION-STORAGE", "Getting locationn from request");
                                                    Config.saveLocationDataToSharedPref(HospitalInfoScreen.this, currCord, hospital.getLicence_id(), durationText);
                                                    eta.setText(durationText);
                                                }
                                            } else {
                                                Log.e("ERROR", "Response Empty");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                                            t.printStackTrace();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });


        //eta.setText(getETAFromSharedPref(hospital.getLicence_id()));
//         Description
        String hospitalDescription = hospital.getDescription();
        description.setText(hospitalDescription);


        // ------ Availabilities Section -----//
        // BED
        String hbed = hospital.getVacant_bed() + " / " + hospital.getNo_of_bed();
        bed.setText(hbed);
        bed.setTextColor(Color.parseColor(

                changeColour(hospital.getVacant_bed(), hospital.

                        getNo_of_bed())));

        // ICU
        String hicu = hospital.getVacant_icu() + " / " + hospital.getIcu();
        icu.setText(hicu);
        icu.setTextColor(Color.parseColor(

                changeColour(hospital.getVacant_icu(), hospital.

                        getIcu())));

        // CCU
        String hccu = hospital.getVacant_ccu() + " / " + hospital.getCcu();
        ccu.setText(hccu);
        ccu.setTextColor(Color.parseColor(

                changeColour(hospital.getVacant_ccu(), hospital.

                        getCcu())));

        // Ventilator
        String hventilator = hospital.getVacant_ventilators() + " / " + hospital.getVentilators();
        ventilators.setText(hventilator);
        ventilators.setTextColor(Color.parseColor(

                changeColour(hospital.getVacant_ventilators(), hospital.

                        getVentilators())));

        // Oxygen
        String hoxygen = hospital.getVacant_oxygen_cylinders() + " / " + hospital.getOxygen_cylinders();
        oxygen.setText(hoxygen);
        oxygen.setTextColor(Color.parseColor(

                changeColour(hospital.getVacant_oxygen_cylinders(), hospital.

                        getOxygen_cylinders())));

        //----- Facilities -----
        // XRay
        String hxray = checkNull(hospital.getX_ray());
        xray.setText(hxray);
        xray.setTextColor(Color.parseColor(

                setColor(hxray)));

        // MRI
        String hmri = checkNull(hospital.getMri());
        mri.setText(hmri);
        mri.setTextColor(Color.parseColor(

                setColor(hmri)));

        // ECG
        String hecg = checkNull(hospital.getEcg());
        ecg.setText(hecg);
        ecg.setTextColor(Color.parseColor(

                setColor(hecg)));

        // ultra Sound
        String hultra_sound = checkNull(hospital.getUltra_sound());
        ultraSound.setText(hultra_sound);
        ultraSound.setTextColor(Color.parseColor(

                setColor(hultra_sound)));

        // Ambulance
        String hambulance = checkNull(hospital.getVacant_ambulance());
        ambulance.setText(hambulance);
        ambulance.setTextColor(Color.parseColor(setColor(hambulance)));
        // Setting up blood Bank
        HashMap<String, String> bg = hospital.getBlood_bank();
        try {
            // A+
            a_positive.setText(bg.get("a_positive"));
            a_positive.setTextColor(Color.parseColor(color(bg.get("a_positive"))));
            //A-
            a_negative.setText(bg.get("a_negative"));
            a_negative.setTextColor(Color.parseColor(color(bg.get("a_negative"))));
            //B+
            b_positive.setText(bg.get("b_positive"));
            b_positive.setTextColor(Color.parseColor(color(bg.get("b_positive"))));
            //B-
            b_negative.setText(bg.get("b_negative"));
            b_negative.setTextColor(Color.parseColor(color(bg.get("b_negative"))));
            //O+
            o_positive.setText(bg.get("o_positive"));
            o_positive.setTextColor(Color.parseColor(color(bg.get("o_positive"))));
            //O-
            o_negative.setText(bg.get("o_negative"));
            o_negative.setTextColor(Color.parseColor(color(bg.get("o_negative"))));
            //AB+
            ab_positive.setText(bg.get("ab_positive"));
            ab_positive.setTextColor(Color.parseColor(color(bg.get("ab_positive"))));
            //AB-
            ab_negative.setText(bg.get("ab_negative"));
            ab_negative.setTextColor(Color.parseColor(color(bg.get("ab_negative"))));
        } catch (
                Exception e) {
            Log.e("ERROR", "ERROR IN PARSING THE BLOOD_GROUP MAP");
            e.printStackTrace();
        }

        // Setting up time
        boolean is24hr = hospital.getIs_24_hr_service();
        if (!is24hr) {
            String open = hospital.getOpening_time();
            String close = hospital.getClosing_time();
            String time = open + " - " + close;
            d_time.setText(time);
        } else {
            d_time.setText(R.string.hrs_24);
        }

        // Setting up Last-Updated Section
        String hdate = "Last Updated : " + hospital.getLast_updated();
        lastUpdated.setText(hdate);

        // Setting Up Emergency cases
        emergency_no.setText(String.valueOf(emergencyCasesList.size()));

        // Setting up call Button : at end of screen
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + hospital.getContact()));
                startActivity(intent);
            }
        });
        if(Config.isAnyBedAvailable(hospital)){
            Log.e("BOOKING", "BED AVAILABLE");
            book.setVisibility(View.VISIBLE);
            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bookBed();
                }
            });
        }else{
            Log.e("BOOKING", "BED NOT AVAILABLE");
            book.setVisibility(View.INVISIBLE);
        }
    }


    private String getETAFromSharedPref(String licence_id, String currCords) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("location-data", MODE_PRIVATE);
            Gson gson = new Gson();
            HashMap<String, String> locMap = new HashMap<>();
            String fromStorage = sharedPreferences.getString(currCords, "{}");
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            locMap = gson.fromJson(fromStorage, type);
            return locMap.get(licence_id);
        } catch (Exception e) {
            e.printStackTrace();
            return "--";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_hospital_info_screen);

        id = getIntent().getExtras().getString("id");
        emergencyCasesList = new ArrayList<>();
        // Getting data from Server


        // Check If Hospitals Exist in localStorage
        if (!checkHospitalsInSharedPreferences()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    getData();
                }
            });
        }

        // Hooks
        name = findViewById(R.id.display_name);
        type = findViewById(R.id.display_type);
        grade = findViewById(R.id.display_grade);
        address = findViewById(R.id.display_address);
        description = findViewById(R.id.display_description);
        eta = findViewById(R.id.display_distance);
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
        a_positive = findViewById(R.id.a_positive);
        a_negative = findViewById(R.id.a_negative);
        b_positive = findViewById(R.id.b_positive);
        b_negative = findViewById(R.id.b_negative);
        o_positive = findViewById(R.id.o_positive);
        o_negative = findViewById(R.id.o_negative);
        ab_positive = findViewById(R.id.ab_positive);
        ab_negative = findViewById(R.id.ab_negative);
        d_time = findViewById(R.id.display_time);
        lastUpdated = findViewById(R.id.display_last_updated);
        refreshButton = findViewById(R.id.refresh_button);
        emergency_no = findViewById(R.id.display_emergency);
        typeButton = findViewById(R.id.display_type_icon);
        gradeButton = findViewById(R.id.display_grade_icon);
        directionsButton = findViewById(R.id.display_distance_icon);
        call = findViewById(R.id.call_button);

        swipeDown = findViewById(R.id.swipe_container);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        // --------------------- Swipe Refresh Layout --------------------//
        swipeDown.setOnRefreshListener(this::getData);
        swipeDown.setColorSchemeResources(R.color.primary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeDown.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeDown.setRefreshing(true);
                getData();
                swipeDown.setRefreshing(false);
            }
        });
        //------------------------------------------------------------------//
        book = findViewById(R.id.book);

    }

    private void bookBed() {
        BookingDialog bookingDialog = new BookingDialog( getApplicationContext(), hospital.getLicence_id(), hospital.getName());
        bookingDialog.show(getSupportFragmentManager(), "DistrictDialog");
    }


    private boolean checkHospitalsInSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("hospital-data", MODE_PRIVATE);
        Gson gson = new Gson();
        try {
            // If hospitals already exist in the local storage then update the adapter to use that to display in recycler view
            String json = sharedPreferences.getString("hospitals", "[]");
            Type type = new TypeToken<List<Hospital>>() {
            }.getType();
            hospitals = gson.fromJson(json, type);
            for (Hospital hs : hospitals) {
                if (hs.getLicence_id().equals(id)) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            hospital = hs;
                            setViews();
                        }
                    });
                }
            }
            return true;
        } catch (Exception e) {
            Log.e("STATUS :", "ERROR ENCOUNTERED IN READING DATA");
            return false;
        }
    }


    private void getData() {
        // Make a web call and get data
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getURL(getApplicationContext()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);
        //Pass ID of Hospital clicked To URL
        // Getting Emergency Cases On The Hospital --------------------------------------------------------------------------
        Call<List<EmergencyCases>> call = apiCall.getEmergencyCases(id);
        call.enqueue(new Callback<List<EmergencyCases>>() {
            @Override
            public void onResponse(Call<List<EmergencyCases>> call, Response<List<EmergencyCases>> response) {
                if (response.code() == 200) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            emergencyCasesList = response.body();
                        }
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            emergencyCasesList = new ArrayList<>();
                        }
                    });
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
        } catch (Exception e) {
            //TODO :  Write Session Expired Code Here
        }
        Call<Hospital> call2 = apiCall.getHospitalById(token);
        call2.enqueue(new Callback<Hospital>() {
            @Override
            public void onResponse(Call<Hospital> call, Response<Hospital> response) {
                if (response.code() == 200) {
                    hospital = response.body();
                    Log.e("HOSPITAL DATA ", "Success");
                    setViews();
                } else if (response.code() == 406) {
                    //TODO Session Expired Code
                } else {
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


    public String changeColour(String num, String total) {
        float a = Integer.parseInt(num);
        float b = Integer.parseInt(total);
        float res = a / b;
        int percent = (int) (res * 100);
        if (percent > 75) {
            return "#247700";
        } else if (percent > 55) {
            return "#FF9800";
        } else {
            return "#FF2222";
        }
    }

    public String checkNull(String data) {
        if (data.equals("") || data.equals("NA") || data.equals("0")) {
            return "NA";
        } else {
            return data;
        }
    }

    public String setColor(String nums) {
        if (nums.equals("NA")) {
            return "#FF2222";
        } else {
            return "#247700";
        }
    }

    public String color(String data) {
        if (data.equals("") || data.equals("NA") || data.equals("0") || data.equals("00")) {
            return "#FF2222";
        } else {
            return "#247700";
        }
    }


}