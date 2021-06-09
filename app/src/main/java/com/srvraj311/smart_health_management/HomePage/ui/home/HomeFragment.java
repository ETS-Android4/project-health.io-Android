package com.srvraj311.smart_health_management.HomePage.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.srvraj311.smart_health_management.HospitalScreen.HospitalScreen;
import com.srvraj311.smart_health_management.R;


import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class HomeFragment extends Fragment {

    TextView locationName;
    TextInputEditText searchBox;
    ImageButton getCurrentLocation;
    CardView item1;


    public HomeFragment(){
        super(R.layout.fragment_home);
    }


    // Access View Elements Here
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hooks

        locationName = view.findViewById(R.id.discover);
        searchBox = view.findViewById(R.id.search);
        getCurrentLocation = view.findViewById(R.id.search_button);
        item1 = view.findViewById(R.id.item1);


        // Creating Screen for Hospital Intent
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HospitalScreen.class);
                startActivity(intent);
            }
        });




        // Setting Search Buttons
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                Toast.makeText(getContext(), " Editing Started", Toast.LENGTH_LONG).show();
                Log.e("TEXT BOX ", "Submitted");
                return true;
            }
        });

        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEXT BOX", "Clicked");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}