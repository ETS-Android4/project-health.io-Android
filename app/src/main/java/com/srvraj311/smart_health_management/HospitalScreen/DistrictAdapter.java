package com.srvraj311.smart_health_management.HospitalScreen;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.srvraj311.smart_health_management.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.zip.Inflater;

import static android.content.Context.MODE_PRIVATE;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.ViewHolder> {

    public String[] districts;

    public DistrictSelectorDialog districtSelectorDialog;
    public HospitalScreen hospitalScreen;
    public DistrictAdapter(String[] districts, DistrictSelectorDialog districtSelectorDialog, HospitalScreen hospitalScreen){
        this.districts = districts;
        this.districtSelectorDialog = districtSelectorDialog;
        this.hospitalScreen = hospitalScreen;
    }
    @NonNull
    @NotNull
    @Override
    public DistrictAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View districtItemView = inflater.inflate(R.layout.district_item , parent , false);
        return new ViewHolder(districtItemView , context , parent);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DistrictAdapter.ViewHolder holder, int position) {
        holder.districtName.setText(districts[position]);
        holder.districtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = holder.context.getSharedPreferences("district-data" , MODE_PRIVATE);
                HashMap<String, String> map = new HashMap<>();
                map.put("district" , districts[position]);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String districtName = gson.toJson(map);
                editor.clear();
                editor.putString("district", districtName);
                editor.apply();
                hospitalScreen.updateDistrictName();
                hospitalScreen.getData();
                districtSelectorDialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return districts.length;
    }

    public void setData(String[] dists) {
        this.districts = dists;
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView districtName;
        ViewGroup parent;

        public ViewHolder(@NonNull @NotNull View itemView, Context context, ViewGroup parent) {
            super(itemView);
            districtName = itemView.findViewById(R.id.district_holder);
            this.context = context;
            this.parent = parent;
        }
    }
}
