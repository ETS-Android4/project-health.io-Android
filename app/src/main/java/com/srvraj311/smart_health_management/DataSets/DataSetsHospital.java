package com.srvraj311.smart_health_management.DataSets;

import java.util.List;

public class DataSetsHospital {

    public static String[] state_names = {"Rajasthan",
            "Chhattisgarh",
            "Tamil Nadu",
            "Haryana",
            "West Bengal",
            "Jharkhand",
            "Uttar Pradesh",
            "Karnataka",
            "Madhya Pradesh",
            "Maharashtra",
            "Telengana",
            "Odisha",
            "Punjab",
            "Bihar",
            "Gujarat",
            "Uttarakhand",
            "Assam",
            "Sikkim ",
            "Delhi",
            "Himachal Pradesh",
            "Andhra Pradesh",
            "Puducherry",
            "Jammu & Kashmir",
            "Daman & Diu",
            "Goa",
            "Chandigarh",
            "Tripura",
            "Manipur",
            "Nagaland",
            "Meghalaya",
            "Mizoram",
            "Arunachal Pradesh",
            "Kerala",
            "Dadra & Nagar Haveli"};
    private static String[] city_names = {};

    public static String[] getCity_names() {
        return city_names;
    }

    public static void setCity_names(String[] city_names) {
        DataSetsHospital.city_names = city_names;
    }

    public static String[] getState_names() {
        return state_names;
    }

    public static void setState_names(String[] state_names) {
        DataSetsHospital.state_names = state_names;
    }
}