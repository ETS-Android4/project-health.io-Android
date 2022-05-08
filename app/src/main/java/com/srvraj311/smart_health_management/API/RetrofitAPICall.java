package com.srvraj311.smart_health_management.API;

import android.media.session.MediaSession;

import com.srvraj311.smart_health_management.HospitalScreen.Hospital;
import com.srvraj311.smart_health_management.Models.EmergencyCases;
import com.srvraj311.smart_health_management.Models.LoginRequest;
import com.srvraj311.smart_health_management.Models.User;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPICall {
    // Seperate the main url, http://localhost:8080/

    @GET("client/users/signup/")
    Call<HashMap<String, String>> getData();

    @POST("client/users/signup/")
    Call<HashMap<String, String>> signUp(@Body User user);

    @POST("client/users/login/")
    Call<HashMap<String, String>> login(@Body LoginRequest loginRequest);

    @POST("client/users/resetPassword")
    Call<HashMap<String, String>> resetPassword(@Body HashMap<String, String> map);

    @POST("client/users/verifyOtp")
    Call<HashMap<String, String>> verifyOtp(@Body HashMap<String, String> map);

    @POST("client/users/updatePw")
    Call<HashMap<String, String>> changePw(@Body HashMap<String, String> map);

    // For setting Base URL for Server. Refer Config.java
    @GET("url.json")
    Call<HashMap<String, String >> getUrl();

    @POST("client/hospitals/all")
    Call <List<Hospital>> getAllHospitals(@Body HashMap<String, String> map);

    @GET("client/hospitals/{DISTRICT_NAME}")
    Call <List<Hospital>> getHospitalsByDistrictName(@Path("DISTRICT_NAME") String district);

    @GET("hospitals/emergency/{id}")
    Call <List<EmergencyCases>> getEmergencyCases(@Path("id") String id);

    @POST("client/hospitals/id")
    Call <Hospital> getHospitalById(@Body HashMap<String, String> map);

    @GET("client/users/signup/sendOtp/{email}")
    Call <HashMap<String, String>> sendOtp(@Path("email") String email);

    @POST("api/distance/")
    Call <HashMap<String ,String>> getETA(@Body HashMap<String , String> requestMap);

    @GET("client/hospitals/districts")
    Call<List<String>> getDistricts();

//    @GET("json?origins={current}&destinations={hospital}&key=AIzaSyAakNhNaG19KkP5v5CEgkhlx-ISyEoxRe8")
//    Call <String > getMatrix(@Query("current") String current, @Query("hospital") String hospital);
}
