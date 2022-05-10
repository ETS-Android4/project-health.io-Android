package com.srvraj311.smart_health_management.Booking;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.srvraj311.smart_health_management.API.RetrofitAPICall;
import com.srvraj311.smart_health_management.Config.Config;
import com.srvraj311.smart_health_management.R;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookingActivity extends AppCompatActivity {
    // Data
    String licence_id;
    String hospital_name;
    int POSITION;
    // Views
    TextView hName;
    TextView message;
    Button book;
    TextInputEditText patientName;
    TextInputEditText patientAge;
    TextInputEditText patientPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Removing Top Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

//        intent.putExtra("licence_id" , licence_id);
//        intent.putExtra("position", pos);
//        intent.putExtra("hospital_name", hospitalName);
        String[] arr = new String[] {"Bed", "CCU" , "Ventilator", "ICU"};

        licence_id = getIntent().getStringExtra("licence_id");
        hospital_name = getIntent().getStringExtra("hospital_name");
        POSITION = getIntent().getIntExtra("position", 0);

        // Hooks
        hName = findViewById(R.id.hospital_name);
        message = findViewById(R.id.message_display);
        book = findViewById(R.id.book);
        patientName = findViewById(R.id.patient_name);
        patientAge = findViewById(R.id.patient_age);
        patientPhone = findViewById(R.id.patient_phone);

        hName.setText(hospital_name);
        message.setText(String.valueOf("Book a " + arr[POSITION] + ""));

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookBed();
            }
        });
    }
    public void bookBed(){
        String name = patientName.getEditableText().toString();
        String age = patientAge.getEditableText().toString();
        String phone = patientPhone.getEditableText().toString();

        if(name.length() < 2){
            message.setText(String.valueOf("Name Invalid"));
            message.setTextColor(Color.RED);
            return;
        }
        if(age.equals("") || Integer.parseInt(age) < 1 || Integer.parseInt(age) > 99){
            message.setText(String.valueOf("Age Invalid, between 1-99"));
            message.setTextColor(Color.RED);
            return;
        }
        if(phone.length() != 10) {
            message.setText(String.valueOf("Phone Number Invalid"));
            message.setTextColor(Color.RED);
            return;
        }
        Date date = new Date();
        long timeStamp = date.getTime();
        HashMap<String, String> map = new HashMap<>();
        map.put("email", Config.getEmail(getApplicationContext()));
        map.put("patient_name", name);
        map.put("patient_phone", phone);
        map.put("patient_age", age);
        map.put("booking_timestamp", String.valueOf(timeStamp));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getURL(getApplicationContext()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);
        Call<HashMap<String, String>> call = apiCall.performBooking(licence_id, String.valueOf(POSITION),map);

        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if(response.body() != null && response.body().containsKey("status")){
                    Toast.makeText(getApplicationContext(), response.body().get("status"), Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"There Seems to be Some error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Issue detected, Retry Later", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

}