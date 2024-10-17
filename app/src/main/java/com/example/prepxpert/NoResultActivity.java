package com.example.prepxpert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NoResultActivity extends AppCompatActivity {
    Button dasnboardbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_result);

        dasnboardbtn=findViewById(R.id.dashboardbtn);

        dasnboardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoResultActivity.this,DashboardActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Create an intent to redirect to another activity
        Intent intent = new Intent(NoResultActivity.this, DashboardActivity.class);

        // Start the new activity
        startActivity(intent);

        // Optional: finish the current activity if you don't want it to be on the back stack
        finish();

        // Disable the default back button behavior (which would go to the previous activity)
        super.onBackPressed();
    }
}