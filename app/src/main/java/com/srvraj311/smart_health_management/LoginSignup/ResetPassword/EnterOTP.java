package com.srvraj311.smart_health_management.LoginSignup.ResetPassword;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.srvraj311.smart_health_management.API.RetrofitAPICall;
import com.srvraj311.smart_health_management.Config.Config;
import com.srvraj311.smart_health_management.LoginSignup.LoginActivity;
import com.srvraj311.smart_health_management.LoginSignup.WelcomeScreen;
import com.srvraj311.smart_health_management.R;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EnterOTP extends AppCompatActivity {

    TextView tagline;
    TextInputEditText otpBox;
    Button verify;
    ProgressBar progressBar;
    TextInputEditText pw1;
    TextInputEditText pw2;
    String email;
    TextInputLayout pBox1;
    TextInputLayout pBox2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Remove Title Box
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        // Assign Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        //Hooks
        tagline = findViewById(R.id.heading_enter_otp);
        otpBox = findViewById(R.id.otp_box);
        verify = findViewById(R.id.verify_otp);
        progressBar = findViewById(R.id.progressbar_otp_2);
        pw1 = findViewById(R.id.password_changed1);
        pw2 = findViewById(R.id.password_changed2);
        pBox1 = findViewById(R.id.pw1);
        pBox2 = findViewById(R.id.pw2);


        // Getting email from previous intent
        Intent i = getIntent();
        email = i.getExtras().getString("email");
        // Onclick
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
                String otp = String.valueOf(otpBox.getText());
                if(!otp.equals(""))
                verifyOtp(email, otp);
            }
        });

    }

    private void verifyOtp(String email, String otp) {
        // Create Data to Send
        HashMap<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("otp", otp);

        // Creating a API call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);

        Call<HashMap<String, String>> call = apiCall.verifyOtp(data);

        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                try{
                    if(response.body().containsKey("error")){
                        tagline.setText(response.body().get("error"));
                        tagline.setTextColor(Color.RED);
                        progressBar.setVisibility(View.INVISIBLE);
                    }else if(response.code() != 200){
                        tagline.setText(response.message());
                        tagline.setTextColor(Color.RED);
                        progressBar.setVisibility(View.INVISIBLE);
                    }else{
                        tagline.setText(R.string.otp_verified);
                        progressBar.setVisibility(View.INVISIBLE);

                        // TODO : Below Is Not Working
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pBox1.setVisibility(View.VISIBLE);
                                pBox2.setVisibility(View.VISIBLE);
                            }
                        });

                        verify.setText(R.string.change_pwd);

                        // UPDATING ONCLICK FUNCTION TO NOW CHANGE PW
                        verify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String p1 = String.valueOf(pw1.getText());
                                String p2 = String.valueOf(pw2.getText());
                                if(verifyDetails(p1) && checkTwoPass(p1, p2)) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    changePw(otp, p1);
                                }
                            }
                        });
                    }
                }catch (Exception e){
                    tagline.setText(R.string.failed_fetch_from_server);
                    e.printStackTrace();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                tagline.setText(R.string.networkError);
                t.printStackTrace();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    private boolean checkTwoPass(String p1, String p2) {
        if(!p1.equals(p2)){
            pw2.setError("Password Does not Matched");
            return false;
        }
        return true;
    }

    private boolean verifyDetails(String  p1) {
        String pwRegex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";

        Pattern pat = Pattern.compile(pwRegex);
        if (p1 == null) {
            pw1.setError("Empty Password Box");
            return false;
        }else if(!pat.matcher(p1).matches()){
            pw1.setError("Weak Password");
            return false;
        }
        return true;
    }

    private void changePw(String otp, String p1) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("otp",otp);
        map.put("password",p1);

        // Create API Call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPICall apiCall = retrofit.create(RetrofitAPICall.class);
        Call<HashMap<String,String>> call = apiCall.changePw(map);

        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                try {
                    if (response.body().containsKey("error")) {
                        tagline.setText(response.body().get("error"));
                        tagline.setTextColor(Color.RED);
                        progressBar.setVisibility(View.INVISIBLE);
                    } else if (response.code() != 200) {
                        tagline.setText(response.message());
                        tagline.setTextColor(Color.RED);
                        progressBar.setVisibility(View.INVISIBLE);
                    } else {
                        tagline.setText(response.body().get("status"));
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Password Changed, Login to Continue",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    tagline.setText(R.string.failed_fetch_from_server);
                    e.printStackTrace();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                    tagline.setText(R.string.networkError);
                    t.printStackTrace();
                    progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}