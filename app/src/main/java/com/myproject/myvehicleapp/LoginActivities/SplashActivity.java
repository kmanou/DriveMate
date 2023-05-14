package com.myproject.myvehicleapp.LoginActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myproject.myvehicleapp.DriveMate;
import com.myproject.myvehicleapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Use a Handler to delay the execution of code inside the Runnable
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if there is a currently logged-in user
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    // No logged-in user, start the LoginActivity
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } else {
                    // There is a logged-in user, start the DriveMate activity
                    startActivity(new Intent(SplashActivity.this, DriveMate.class));
                }
                // Finish the SplashActivity to prevent going back to it
                finish();
            }
        }, 1000); // Delay the execution for 1 second
    }
}
