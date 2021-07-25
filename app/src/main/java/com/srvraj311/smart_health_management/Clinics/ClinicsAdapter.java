package com.srvraj311.smart_health_management.Clinics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.srvraj311.smart_health_management.HospitalScreen.HospitalsAdapter;
import com.srvraj311.smart_health_management.R;

import org.jetbrains.annotations.NotNull;

public class ClinicsAdapter extends RecyclerView.Adapter<ClinicsAdapter.ViewHolder> {

    @NonNull
    @NotNull
    @Override
    public ClinicsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View ClinicsView = inflater.inflate(R.layout.layout_clinic_item, parent, false);

        // Return a new holder instance
        return new ClinicsAdapter.ViewHolder(ClinicsView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ClinicsAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

        }
    }
}
