package com.srvraj311.smart_health_management.HospitalInfoScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.srvraj311.smart_health_management.R;

public class HospitalInfoScreen extends AppCompatActivity {

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_info_screen);

        id = getIntent().getExtras().getString("id");

    }
}