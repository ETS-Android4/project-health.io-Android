package com.srvraj311.smart_health_management.HomePage.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.srvraj311.smart_health_management.R;
import com.srvraj311.smart_health_management.databinding.FragmentHomeBinding;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {

    TextView locationName;


    public HomeFragment(){
        super(R.layout.fragment_home);
    }


    // Access View Elements Here
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationName = view.findViewById(R.id.discover);

        locationName.setText("Discover Durgapur");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}