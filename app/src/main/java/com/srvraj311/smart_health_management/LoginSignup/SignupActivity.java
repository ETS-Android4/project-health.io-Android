package com.srvraj311.smart_health_management.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.jaredrummler.android.device.DeviceName;
import com.srvraj311.smart_health_management.API.RetrofitAPICall;
import com.srvraj311.smart_health_management.Config.Config;
import com.srvraj311.smart_health_management.HomePage.ui.HomeScreen;
import com.srvraj311.smart_health_management.Models.User;
import com.srvraj311.smart_health_management.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {

    // Declaring View Elements
    TextInputEditText fname;
    TextInputEditText lname;
    TextInputEditText email;
    TextInputEditText password1;
    TextInputEditText password2;
    TextInputEditText age;
    Button signup;
    TextView message;
    ProgressBar progressBar;
    FrameLayout darken;

    // Declaring General Elements;
    HashMap<String, String> userToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Hide the Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //Set Activity View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Assignments
        progressBar = findViewById(R.id.signup_progress);
        darken = findViewById(R.id.darken_bg);

        signup = findViewById(R.id.signup);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.sname);
        email = findViewById(R.id.email);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        age = findViewById(R.id.age);
        message = findViewById(R.id.signup_message);
        fname.requestFocus();


        // OnClicks
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verifyDetails()){
                    String fn = String.valueOf(fname.getText());
                    String ln = String.valueOf(lname.getText());
                    String em = String.valueOf(email.getText());
                    String pw1 = String.valueOf(password1.getText());
                    String pw2 = String.valueOf(password2.getText());
                    String ag = String.valueOf(age.getEditableText());
                    String did = DeviceName.getDeviceName();
                    Set<String> dlist = new HashSet<>();
                    dlist.add(did);
                    // Creating a model from data
                    User newUser = new User(fn, ln, em, pw1, ag, dlist);
                    signUp(newUser);
                }

            }
        });
    }

    /**
     *
     * //Starting the functional Parts below instead of the structural parts.
     *
     */

    private void signUp(User newUser) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                darken.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        // Create a new OnScreen fragment that will check for otp
                Intent intent = new Intent(SignupActivity.this, SignUpOTP.class);
                intent.putExtra("email", newUser.getEmail());
                intent.putExtra("first_name", newUser.getFirst_name());
                intent.putExtra("last_name", newUser.getLast_name());
                intent.putExtra("mobile_num", newUser.getEmail());
                intent.putExtra("password", newUser.getEmail());
                intent.putExtra("age", newUser.getEmail());
                intent.putExtra("dog_tag", newUser.getEmail());
                startActivity(intent);
    }

    // Form validation Methods Below

    public boolean isValidEmail(String email){
        String emailRegex = "^(.+)@(.+)$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
    public boolean isValidPassword(String pwd){
        String pwRegex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{6,20}$";
        
        Pattern pat = Pattern.compile(pwRegex);
        if (pwd == null)
            return false;
        return pat.matcher(pwd).matches();
    }
    public boolean doesPasswordsMatch(String pw1, String pw2){
        return pw1.equals(pw2);
    }
    public boolean verifyName(String fname){
        return fname.length() != 0;
    }
    public boolean verifyAge(String age){
        if(age.equals("")) return false;
        int a = Integer.parseInt(age.trim());
        if (a > 100) return false;
        if (a < 3) return false;
        return true;
    }
    public boolean verifyDetails(){
        String fn = String.valueOf(fname.getText());
        String ln = String.valueOf(lname.getText());
        String em = String.valueOf(email.getText());
        String pw1 = String.valueOf(password1.getText());
        String pw2 = String.valueOf(password2.getText());
        String ag = String.valueOf(age.getEditableText());


        if(!verifyName(fn)){
            fname.setError("Invalid Name, Must contain a character");
            return false;
        }else{
            fname.setError(null);
        }

        if(!verifyName(ln)){
            lname.setError("Invalid Name, Must contain a character");
            return false;
        }else{
            lname.setError(null);
        }

        if(!isValidEmail(em)){
            email.setError("Invalid Email");
            return false;
        }else{
            email.setError(null);
        }

        if(!isValidPassword(pw1)){
            password1.setError("Weak Password, Must Contain a Special Symbol, One UpperCase, One Lowercase, One Numeric value and be Minimum 6 and Maximum 20 Characters Long");
            return false;
        }else{
            password1.setError(null);
        }

        if(!doesPasswordsMatch(pw1, pw2)){
            password2.setError("Passwords Does NOT Match");
            return false;
        }else{
            password2.setError(null);
        }

        if(!verifyAge(ag)){
            age.setError("Invalid Age");
            return false;
        }else{
            age.setError(null);
        }

        return true;
    }
}