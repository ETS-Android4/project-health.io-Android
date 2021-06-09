package com.srvraj311.smart_health_management.HospitalScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;
import com.srvraj311.smart_health_management.R;

import java.util.Objects;

public class HospitalScreen extends AppCompatActivity {

    AutoCompleteTextView sortByDropdownView;
    TextInputEditText textBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Removing Top Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();


        // Assigning a windows
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_screen);

        // Hooks
        sortByDropdownView = findViewById(R.id.sort_by_view);

        String[] sortArray = getResources().getStringArray(R.array.sort_methods);
        ArrayAdapter<String> sortOptionsAdapter = new ArrayAdapter<>(HospitalScreen.this, R.layout.sortby_dropdown, sortArray);
        sortByDropdownView.setAdapter(sortOptionsAdapter);

    }
}