package com.example.dropex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(isFirstLaunch()) {
            startActivity(new Intent(this,OnboardingActivity.class));
            finish();
        }
        else if(currentUser != null){
            startActivity(new Intent(this,HomeActivity.class));
            finish();
        }

        else{
            startActivity(new Intent(this,SplashScreenActivity.class));
            finish();
        }



    }

    @Override
    protected void onStart() {
        super.onStart();



    }

    private Boolean isFirstLaunch() {
        return SharedPref.getInstance(this.getApplicationContext()).isFirstLaunch();
    }
}