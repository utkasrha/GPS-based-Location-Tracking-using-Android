package com.college.employeetrackingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.college.util.AppController;
import com.college.util.SharedPreference;

import net.gotev.uploadservice.BuildConfig;
import net.gotev.uploadservice.UploadService;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        UploadService.NAMESPACE = "com.college.employeetrackingapplication";
        AppController.initialize(getApplicationContext());
        SharedPreference.initialize(getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreenActivity.this,MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}