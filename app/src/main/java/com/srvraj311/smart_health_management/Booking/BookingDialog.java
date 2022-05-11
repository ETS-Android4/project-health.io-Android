package com.srvraj311.smart_health_management.Booking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.reflect.TypeToken;
import com.srvraj311.smart_health_management.API.RetrofitAPICall;
import com.srvraj311.smart_health_management.Booking.BookingActivity;
import com.srvraj311.smart_health_management.Config.Config;
import com.srvraj311.smart_health_management.DataSets.DataSetsHospital;
import com.srvraj311.smart_health_management.HospitalScreen.DistrictAdapter;
import com.srvraj311.smart_health_management.HospitalScreen.DistrictSelectorDialog;
import com.srvraj311.smart_health_management.HospitalScreen.HospitalsAdapter;
import com.srvraj311.smart_health_management.R;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookingDialog extends DialogFragment {
    // Refs
    Context context;
    Button cancelButton;
    RecyclerView bookingRecycler;
    String licence_id;
    HashMap<String, String> resMap;
    ProgressBar progressIndicator;
    String hospitalName;

    //Constructor
    public BookingDialog(Context context, String licence_id, String hospitalName) {
        this.context = context;
        this.licence_id = licence_id;
        this.hospitalName = hospitalName;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Objects.requireNonNull(getDialog()).requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
        View view = inflater.inflate(R.layout.booking_dialog, container);
        cancelButton = view.findViewById(R.id.booking_cancel);
        progressIndicator = view.findViewById(R.id.progress);
        progressIndicator.setVisibility(View.VISIBLE);
        bookingRecycler = view.findViewById(R.id.booking_recycler);
        bookingRecycler.setLayoutManager(new LinearLayoutManager(context));
        resMap = new HashMap<>();
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getDialog()).cancel();
            }
        });
        // Make Network Req
        getBookingAvailability(bookingRecycler);
        //bookingRecycler.setAdapter(new BookingAdapter());
        return view;
    }

    private void getBookingAvailability(RecyclerView bookingRecycler) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getURL(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);
        Call<HashMap<String, String>> call = apiCall.getBookingAvailability(licence_id);
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if (response.body() != null) {
                    HashMap<String, String> res = response.body();
                    if (res.size() > 0) {
                        resMap = res;
                        bookingRecycler.setAdapter(new BookingAdapter(context, resMap));
                    } else {
                        bookingRecycler.setAdapter(new BookingAdapter(context, resMap));
                    }
                } else {
                    Toast.makeText(context, "Unable to get current booking status, Please Retry", Toast.LENGTH_LONG).show();
                }
                progressIndicator.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                progressIndicator.setVisibility(View.GONE);
                t.printStackTrace();
                Toast.makeText(context, "Unable to get current booking status, Please Retry", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void performBooking(int pos) {
        Intent intent = new Intent(context, BookingActivity.class);
        intent.putExtra("licence_id", licence_id);
        intent.putExtra("position", pos);
        intent.putExtra("hospital_name", hospitalName);
        startActivity(intent);
        Objects.requireNonNull(getDialog()).cancel();
    }

    class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
        Context context;
        HashMap<String, String> availabilityMap;

        BookingAdapter(Context context, HashMap<String, String> availabilityMap) {
            this.context = context;
            this.availabilityMap = availabilityMap;
        }

        @NonNull
        @Override
        public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            // Inflate the custom layout
            View bookingView = inflater.inflate(R.layout.booking_item, parent, false);
            // Return a new holder instance
            return new ViewHolder(bookingView);
        }

        @Override
        public void onBindViewHolder(@NonNull BookingAdapter.ViewHolder holder, int position) {
            String type = (String) availabilityMap.keySet().toArray()[holder.getAdapterPosition()];
            String[] arr = new String[]{"bed", "ccu", "ventilator", "icu"};
            int responseCode;
            switch (type) {
                case "ccu":
                    responseCode = 1;
                    break;
                case "ventilator":
                    responseCode = 2;
                    break;
                case "icu":
                    responseCode = 3;
                    break;
                default:
                    responseCode = 0;
                    break;
            }

            String quantity = availabilityMap.get(type);
            String name = type.substring(0, 1).toUpperCase() + type.substring(1);
            holder.name.setText(name);
            holder.count.setText(quantity);

            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    performBooking(responseCode);
                }
            });
        }

        @Override
        public int getItemCount() {
            return availabilityMap.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView count;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.type);
                count = itemView.findViewById(R.id.available);
            }
        }
    }

}

