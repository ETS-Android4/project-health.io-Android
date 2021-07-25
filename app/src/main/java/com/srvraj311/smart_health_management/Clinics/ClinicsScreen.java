package com.srvraj311.smart_health_management.Clinics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.srvraj311.smart_health_management.HospitalInfoScreen.HospitalInfoScreen;
import com.srvraj311.smart_health_management.HospitalScreen.Hospital;
import com.srvraj311.smart_health_management.HospitalScreen.HospitalsAdapter;
import com.srvraj311.smart_health_management.R;

import java.util.Objects;

public class ClinicsScreen extends AppCompatActivity {

    RecyclerView recyclerView;
    ClinicsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Removing Top Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Assigning a View Model
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinics_screen);

        // Hooks
        recyclerView = findViewById(R.id.clinic_recycler);



        // --------------------- Setting Up Recycler View ------------------//
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ClinicsAdapter();
//        adapter = new HospitalsAdapter(hospitals, new HospitalsAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Hospital hospital) {
//                Intent intent = new Intent(getApplicationContext(), HospitalInfoScreen.class);
//                intent.putExtra("id",hospital.getLicence_id());
//                startActivity(intent);
//            }
//        });
        recyclerView.setAdapter(adapter);
        // -------------------- Setting Item Click Option of Recycler View ---------////

    }
}