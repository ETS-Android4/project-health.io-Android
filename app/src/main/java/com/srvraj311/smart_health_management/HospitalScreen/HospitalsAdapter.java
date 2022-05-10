package com.srvraj311.smart_health_management.HospitalScreen;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.srvraj311.smart_health_management.API.RetrofitAPICall;
import com.srvraj311.smart_health_management.Config.Config;
import com.srvraj311.smart_health_management.HospitalInfoScreen.HospitalInfoScreen;
import com.srvraj311.smart_health_management.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HospitalsAdapter extends RecyclerView.Adapter<HospitalsAdapter.ViewHolder> {

    List<Hospital> hospitals;
    private final OnItemClickListener listener;
    private final Context context;


    public HospitalsAdapter(List<Hospital> hospitals, OnItemClickListener listener, Context context) {
        this.hospitals = hospitals;
        this.listener = listener;
        this.context = context;
    }

    public void setData(List<Hospital> hospitals) {
        this.hospitals = hospitals;
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        void onItemClick(Hospital hospital);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // Define ClickListener for Each Items here. Add Hospital Launch screen Here
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View HospitalView = inflater.inflate(R.layout.layout_hospital_item, parent, false);


        // Return a new holder instance
        return new ViewHolder(HospitalView);
    }

    private void getData() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {


        // Setting Up the OnClick
        holder.bind(hospitals.get(position), listener);
        Hospital hospital = hospitals.get(position);
        if (hospitals.size() == 0) return;
        // Name of Hospital
        String name = hospital.getName();
        String out = "";
        String[] sp = name.split(",");
        if (sp.length > 1 && sp[1].equals("null")) {
            out = name.split(",")[0];
        } else {
            out = name;
        }
        if (out.length() > 40) {
            out = out.substring(0, 40);
        }
        holder.title.setText(out);

        // Type
        String type = hospital.getType();
        type = "" + Character.toUpperCase(type.charAt(0)) + type.substring(1, type.length());
        holder.hospital_type.setText(type);

        //Address
        // TODO : Move these filters to server as well
        try {
            String addrFinal = "";
            String addr = hospital.getAddress();
            String[] addrs = addr.split(",");
            for (int i = 0; i < addrs.length - 1; i++) {
                if (!addrs[i].equals("null")) {
                    addrFinal += addrs[i] + ",";
                }
            }
            if (!addrs[addrs.length - 1].equals("null")) {
                int pin = 0;
                try {
                    pin = (int) Float.parseFloat(addrs[addrs.length - 1]);
                } catch (Exception ignored) {
                }
                if (pin > 0)
                    addrFinal += String.valueOf(pin);
            }

            holder.address.setText(addrFinal);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("LOG", e.getMessage());
            holder.address.setText(hospital.getAddress());
        }

        // Hospital Timing
        String time = "";
        if (hospital.getIs_24_hr_service()) {
            time = "24 x 7";
        } else {
            time = String.format("%s-%s", hospital.getOpening_time(), hospital.getClosing_time());
        }
        holder.timing.setText(time);

        // Hospital Grade
        holder.grade.setText(hospital.getGrade());
        if (hospital.getGrade().equals("A")) {
            holder.grade.setTextColor(Color.parseColor("#247700"));
        } else if (hospital.getGrade().equals("B")) {
            holder.grade.setTextColor(Color.parseColor("#FF9800"));
        } else {
            holder.grade.setTextColor(Color.parseColor("#FF2222"));
        }


        // Hospital Bed
        int total = Integer.parseInt(hospital.getNo_of_bed());
        int vacant = Integer.parseInt(hospital.getVacant_bed());
        float percent;
        try {
            percent = total / vacant;
        } catch (Exception e) {
            percent = 0;
        }
        holder.vacant_bed.setText(String.valueOf(vacant));

        if (percent > 0.8) {
            holder.vacant_bed.setTextColor(Color.parseColor("#FF2222"));
        } else if (percent > 0.6) {
            holder.vacant_bed.setTextColor(Color.parseColor("#FF9800"));
        } else {
            holder.vacant_bed.setTextColor(Color.parseColor("#247700"));
        }

        // Geolocation
        // TODO : Set Direction using API
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("LOCATION", "LOCATION ACCESS NOT GIVEN");
            return;
        }

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

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
                            Log.e("LOC-INFO-SCREEN", "Checking localstorage for Location");
                            if (Config.locationAlreadyInSharedPref(context, currCord, hospital.getLicence_id())) {
                                Log.e("LOC-INFO-SCREEN", "Location found in localStorage");
                                setLocationToView(currCord, hospital.getLicence_id(), holder.eta);
                            } else {
                                HashMap<String, String> requestMap = new HashMap<>();
                                requestMap.put("wp1", currCord);
                                requestMap.put("wp2", hospital.getGeolocation());
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(Config.getURL(context))
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);
                                Call<HashMap<String, String>> call = apiCall.getETA(requestMap);
                                try {
                                    call.enqueue(new Callback<HashMap<String, String>>() {
                                        @Override
                                        public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                                            if (response != null) {
                                                //Log.e("RES", response.toString());
                                                if (response.body() != null) {
                                                    String durationInSeconds = response.body().get("travelDuration");
                                                    String durationText = Config.convertSecondsToTime(durationInSeconds);
                                                    // Set data to sharedPref
                                                    Log.e("LOC-INFO-SCREEN", "Getting locationn from request");
                                                    Config.saveLocationDataToSharedPref(context, currCord, hospital.getLicence_id(), durationText);
                                                    holder.eta.setText(durationText);
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


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("LOCATION", e.getMessage());
            }
        });
    }



    private void setLocationToView(String currCords, String licence_id, TextView eta) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("location-data", MODE_PRIVATE);
            Gson gson = new Gson();
            HashMap<String, String> locMap = new HashMap<>();
            String fromStorage = sharedPreferences.getString(currCords, "{}");
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            locMap = gson.fromJson(fromStorage, type);
            String finalETA = locMap.get(licence_id);
            eta.setText(finalETA);
            Log.e("LOC-INFO-SCREEN", "Setting location from localStorage");
        } catch (Exception e) {
            e.printStackTrace();
            eta.setText("--");
        }
    }



    @Override
    public int getItemCount() {
        return this.hospitals.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Define View Elements Here
        public TextView title;
        public TextView hospital_type;
        public TextView address;
        public TextView timing;
        public TextView grade;
        public TextView vacant_bed;
        public TextView eta;
        public CardView box;
        public CardView saved_box;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.hospital_name);
            hospital_type = itemView.findViewById(R.id.hospital_type);
            address = itemView.findViewById(R.id.hospital_address);
            timing = itemView.findViewById(R.id.hospital_timing);
            grade = itemView.findViewById(R.id.hospital_grade);
            vacant_bed = itemView.findViewById(R.id.hospital_bed_vacant);
            eta = itemView.findViewById(R.id.hospital_eta);
            box = itemView.findViewById(R.id.hospital_item_single);
        }

        public void bind(final Hospital hospital, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(hospital);
                }
            });
        }
    }



}
