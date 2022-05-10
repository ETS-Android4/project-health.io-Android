package com.srvraj311.smart_health_management.Booking;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.srvraj311.smart_health_management.Config.Config;
import com.srvraj311.smart_health_management.R;

import java.util.Objects;

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getURL(getApplicationContext()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

}