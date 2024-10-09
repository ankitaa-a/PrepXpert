package com.example.prepxpert;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int SPLASH_DISPLAY_LENGTH = 3000; // 3000 ms = 3 seconds

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After delay, start the login activity
                Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();  // Close the splash screen so it's not accessible with the back button
            }
        }, SPLASH_DISPLAY_LENGTH);


    }
}