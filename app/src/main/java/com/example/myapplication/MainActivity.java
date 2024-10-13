package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the content view

        // Initialize DBHelper
        DBHelper dbHelper = new DBHelper(this);

        // Initialize TextView
        tvWelcome = findViewById(R.id.tv_welcome); // Ensure this ID matches your XML layout

        // Get username from intent
        String username = getIntent().getStringExtra("username");

        if (username != null) {
            // Get the user type from the database
            String userType = dbHelper.getUserType(username);

            // Redirect to appropriate dashboard based on user type
            if (userType != null) {
                Intent intent;
                if (userType.equals("customer")) {
                    intent = new Intent(MainActivity.this, CustomerDashboardActivity.class);
                } else if (userType.equals("shopkeeper")) {
                    intent = new Intent(MainActivity.this, ShopkeeperDashboardActivity.class);
                } else {
                    // In case the user type is neither "customer" nor "shopkeeper"
                    tvWelcome.setText("Unknown user type");
                    return; // Exit the method
                }

                intent.putExtra("username", username);
                startActivity(intent);
                finish(); // Close MainActivity
            } else {
                // Fallback in case userType is null
                tvWelcome.setText("Welcome, " + username);
            }
        }
    }
}