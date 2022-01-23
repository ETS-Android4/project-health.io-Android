package com.srvraj311.smart_health_management.HospitalScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.srvraj311.smart_health_management.API.RetrofitAPICall;
import com.srvraj311.smart_health_management.Config.Config;
import com.srvraj311.smart_health_management.HospitalInfoScreen.HospitalInfoScreen;
import com.srvraj311.smart_health_management.MainActivity;
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

public class HospitalScreen extends AppCompatActivity {

    AutoCompleteTextView sortByDropdownView;
    AutoCompleteTextView districtUpdater;
    TextInputEditText searchView;
    RecyclerView recyclerView;
    List<Hospital> hospitals;
    FrameLayout darken;
    ProgressBar progressBar;
    HospitalsAdapter adapter;
    SwipeRefreshLayout swipeDown;
    RecyclerView districtSelectorRecycler;



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Removing Top Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();


        // Assigning a windows
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_screen);


        // Hooks-----------------------------------------------------------//
        sortByDropdownView = findViewById(R.id.sort_by_view);
        districtUpdater = findViewById(R.id.get_location);
        recyclerView = findViewById(R.id.hospital_recycler);
        darken = findViewById(R.id.darken_frame);
        progressBar = findViewById(R.id.progressbar_hospital);
        swipeDown = findViewById(R.id.swipe_container);
        searchView = findViewById(R.id.search_hospital);


        // Creating a District Selector Dialog
        // ------------ Adding condition to Not show dialog when city already exists -----------//
       if(!checkDistrictAlreadySelected()){
           DialogFragment districtDialog = new DistrictSelectorDialog(HospitalScreen.this, getApplicationContext());
           districtDialog.show(getSupportFragmentManager() , "DistrictDialog");
       }

       // Initialising Array for Hospital
        hospitals = new ArrayList<Hospital>();



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
                searchView.setText("");
                swipeDown.setRefreshing(false);
            }
        });
        //------------------------------------------------------------------//

        // --------------------- Setting Up Recycler View ------------------//
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HospitalsAdapter(hospitals, new HospitalsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Hospital hospital) {
                Intent intent = new Intent(getApplicationContext(), HospitalInfoScreen.class);
                intent.putExtra("id",hospital.getLicence_id());
                startActivity(intent);
            }
        }, HospitalScreen.this);
        recyclerView.setAdapter(adapter);
        // -------------------- Setting Item Click Option of Recycler View ---------////


        // Checking Local Storage for fetched Hospitals
        if(!checkHospitalInSharedPreferences()){
            //-------------------------------------------------------------------//
            // Getting Data from Server , In a separate Thread.
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    getData();
                }
            });
        }


        //------------------ Changing Data On Search View ------------------//

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Hospital> searchedList = new ArrayList<>();
                s = s.toString().toLowerCase();
                for(Hospital hospital: hospitals){
                    if (hospital.getName().toLowerCase().contains(s) ||
                            hospital.getAddress().toLowerCase().contains(s) ||
                            hospital.getType().toLowerCase().contains(s) ||
                            hospital.getCity_name().toLowerCase().contains(s) ||
                            hospital.getDescription().toLowerCase().contains(s)){
                            searchedList.add(hospital);
                    }
                }
                // updating the sorted Array after Text change
                adapter.setData(searchedList);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        // Set Sort-By Options Array
        String[] sortArray = getResources().getStringArray(R.array.sort_methods);
        ArrayAdapter<String> sortOptionsAdapter = new ArrayAdapter<>(getApplication(), R.layout.sortby_dropdown, sortArray);
        sortByDropdownView.setAdapter(sortOptionsAdapter);
        sortByDropdownView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HospitalSorter sorter = new HospitalSorter(hospitals);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    sorter.sortHospital(position);
                    hospitals = sorter.getHospitals();
                    adapter.setData(hospitals);
                }
            }
        });

        // Set District From Hospital Screen
        districtUpdater.setText(getCurrentDistrict());
        districtUpdater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment districtDialog = new DistrictSelectorDialog(HospitalScreen.this, getApplicationContext());
                districtDialog.show(getSupportFragmentManager() , "District");
            }
        });

    }
    public void updateDistrictName(){
        districtUpdater.setText(getCurrentDistrict());
    }

    private String getCurrentDistrict() {
        try{
            SharedPreferences sharedPreferences = getSharedPreferences("district-data" ,MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("district", "");
            Type type = new TypeToken<HashMap<String, String>>() {}.getType();
            HashMap<String, String> data = gson.fromJson(json, type);
            return data.get("district");
        }
        catch (Exception e){
            return "Select a District Here";

        }

    }

    private boolean checkHospitalInSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("hospital-data" , MODE_PRIVATE);
        Gson gson = new Gson();
        try {
            // If hospitals already exist in the local storage then update the adapter to use that to display in recycler view
            String json = sharedPreferences.getString("hospitals", "[]");
            Type type = new TypeToken<List<Hospital>>() {}.getType();
            hospitals = gson.fromJson(json, type);
            adapter.setData(hospitals);
            return true;
        }catch (Exception e){
            Log.e("STATUS : " , "ERROR ENCOUNTERED IN READING DATA");
            return false;
        }
    }

    private boolean checkDistrictAlreadySelected(){
        SharedPreferences sharedPreferences = getSharedPreferences("district-data" ,MODE_PRIVATE);
        Gson gson = new Gson();
        try {

            String json = sharedPreferences.getString("district", "");
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            HashMap<String, String> data = gson.fromJson(json, type);
            return data.containsKey("district");
        }catch (Exception e){
            return false;
        }
    }


    public void getData() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                darken.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        // making a hashmap for user Token
        // ------------------------ Looking at Local Storage -------------------------//
        SharedPreferences sharedPreferences = getSharedPreferences("district-data", MODE_PRIVATE);
        // Get District From SharedPreferences
        String dist_json = sharedPreferences.getString("district", "{'district' : 'New Delhi'}");
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() {}.getType();
        HashMap<String, String > dist_data = gson.fromJson(dist_json , type);
        String district_name = dist_data.get("district");

        // Making an API call
        Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(Config.getURL(getApplicationContext()))
        .addConverterFactory(GsonConverterFactory.create())
        .build();

        RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);
        Call<List<Hospital>> call = apiCall.getHospitalsByDistrictName(district_name);

        call.enqueue(new Callback<List<Hospital>>() {
            @Override
            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                try {
                    if(response.code() == 200){
                        stopProgressBar();
                        saveDataToSharedPreferences(response.body());
                        hospitals = response.body();
                        adapter.setData(hospitals);
                    }
                    if(response.code() == 406){
                        // Log out
                        Log.e("Error in Call", "Token Validation Failed");
                        Toast.makeText(getApplicationContext(), "Session Expired, Login Again", Toast.LENGTH_LONG).show();
                        clearUserTokenInDB();
                        stopProgressBar();

                    }else if(response.code() == 500){
                        Log.e("Error in Call", " Email Does not Exist, Session Expired");
                        Toast.makeText(getApplicationContext(), "Unauthorised to View Data", Toast.LENGTH_LONG).show();
                        clearUserTokenInDB();
                        stopProgressBar();
                    }else{
                        Log.e("Status HospitalScreen", String.valueOf(response.code()));
                        stopProgressBar();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error Fetching data, Check Network", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    stopProgressBar();
                }

            }

            @Override
            public void onFailure(Call<List<Hospital>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error Fetching data, Check Network", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(HospitalScreen.this)
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
                        .create();
                t.printStackTrace();
                stopProgressBar();
            }
        });


    }

    private void saveDataToSharedPreferences(List<Hospital> body) {
        SharedPreferences sharedPreferences = getSharedPreferences("hospital-data" , MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        Gson gson = new Gson();
        String hospitalFetchedArray = gson.toJson(body);
        editor.putString("hospitals", hospitalFetchedArray);
        editor.apply();
    }

    private void stopProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        darken.setVisibility(View.INVISIBLE);
    }


    private void clearUserTokenInDB() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}