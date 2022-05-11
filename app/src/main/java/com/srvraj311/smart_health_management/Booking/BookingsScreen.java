package com.srvraj311.smart_health_management.Booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.srvraj311.smart_health_management.API.RetrofitAPICall;
import com.srvraj311.smart_health_management.Config.Config;
import com.srvraj311.smart_health_management.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookingsScreen extends AppCompatActivity {

    List<BookingModel> bookingModelList;
    RecyclerView rv;
    SwipeRefreshLayout mSwipe;
    ProgressBar progressBar;
    androidx.appcompat.widget.Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Removing Top Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings_screen);
        rv = findViewById(R.id.bookings_recycler);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mSwipe= findViewById(R.id.swipe_container);
        mSwipe.setColorSchemeResources(R.color.primary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        bookingModelList = new ArrayList<>();
        String[] arr = new String[] {"Bed", "CCU" , "Ventilator", "ICU"};


        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipe.setRefreshing(true);
                        getData();
                        mSwipe.setRefreshing(false);
                    }
                });
            }
        });

        //-------------------------------------------------------------------//
        // Getting Data from Server , In a separate Thread.
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        });
    }

    private void getData() {
        String email = Config.getEmail(getApplicationContext()).toString().toLowerCase();
        System.out.println("Email :" +  email);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getURL(getApplicationContext()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPICall api = retrofit.create(RetrofitAPICall.class);
        Call<List<BookingModel>> call = api.getBookingsByEmail(email);
        call.enqueue(new Callback<List<BookingModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<BookingModel>> call, @NonNull Response<List<BookingModel>> res) {
                if(res.body() != null) {
                    bookingModelList = res.body();
                    for(BookingModel bookingModel : bookingModelList) System.out.println(bookingModel.toString());
                    rv.setAdapter(new BookingsAdapter(getApplicationContext(), bookingModelList));
                    progressBar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(getApplicationContext(), "Failed to Retrieve Bookings Information, Try Again", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }


            @Override
            public void onFailure(@NonNull Call<List<BookingModel>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Error, Try Again", Toast.LENGTH_LONG).show();
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
        //Toast.makeText(getApplicationContext(), "Unable to get Email from User", Toast.LENGTH_LONG).show();

    }

    static class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.ViewHolder>{
        List<BookingModel> list;
        Context context;
        public BookingsAdapter(Context context, List<BookingModel> list){
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public BookingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflator = LayoutInflater.from(context);
            View view = inflator.inflate(R.layout.bookings_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BookingsAdapter.ViewHolder v, int position) {
            BookingModel model = list.get(position);
            v.patient_name.setText(Config.checkForNull(model.getPatient_name()));
            v.patient_phone.setText(Config.checkForNull(model.getPatient_phone()));
            v.patient_age.setText(Config.checkForNull(model.getPatient_age()));
            v.hospital_name.setText(Config.checkForNull(model.getHospital_name()));
            v.booking_time.setText(Config.checkForNull(String.valueOf(model.getTime() + " " + model.getDate())));
            v.bookingNumber.setText(Config.checkForNull(model.getBooking_number()));
            v.status.setText(Config.checkForNull(model.getBooking_status()));
            if(model.getBooking_status().charAt(0) == 'B'){
                v.status.setTextColor(Color.parseColor("#186F00"));
            }else{
                v.status.setTextColor(Color.RED);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder{
            TextView patient_name;
            TextView patient_age;
            TextView patient_phone;
            TextView status;
            TextView hospital_name;
            TextView bookingNumber;
            TextView booking_time;
            public ViewHolder(@NonNull View v) {
                super(v);
                patient_name = v.findViewById(R.id.patient_name);
                patient_age = v.findViewById(R.id.patient_age);
                patient_phone = v.findViewById(R.id.patient_phone);
                status = v.findViewById(R.id.booking_status);
                hospital_name = v.findViewById(R.id.hospital_name);
                booking_time = v.findViewById(R.id.booking_time_and_date);
                bookingNumber = v.findViewById(R.id.booking_number);
            }
        }
    }

}