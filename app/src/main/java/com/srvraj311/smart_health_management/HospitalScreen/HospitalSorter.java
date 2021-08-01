package com.srvraj311.smart_health_management.HospitalScreen;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HospitalSorter {

    public List<Hospital> getHospitals() {
        return hospitals;
    }

    private final List<Hospital> hospitals;

    public HospitalSorter(List<Hospital> hospitals){
        this.hospitals = hospitals;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortHospital(int type){
        if(type == 0){
            // Sort By Name
            hospitals.sort(new Comparator<Hospital>() {
                @Override
                public int compare(Hospital o1, Hospital o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        } else if(type == 1){
            // Sort By Distance
            //TODO: Add Distance Sort Method
        }else if(type == 2){
            // Sort By Type
            hospitals.sort(new Comparator<Hospital>() {
                @Override
                public int compare(Hospital o1, Hospital o2) {
                    return o1.getType().compareTo(o2.getType());
                }
            });
        }else if(type == 3){
            // Sort by Availability
            hospitals.sort(new Comparator<Hospital>() {
                @Override
                public int compare(Hospital o1, Hospital o2) {
                    return o1.getVacant_bed().compareTo(o2.getVacant_bed());
                }
            });
        }else if(type == 4){
            // Sort By Fees
            hospitals.sort(new Comparator<Hospital>() {
                @Override
                public int compare(Hospital o1, Hospital o2) {
                    return o1.getGeneral_fees().compareTo(o2.getGeneral_fees());
                }
            });
        }
    }
}
