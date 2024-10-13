package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        // Get username from intent
        String username = getIntent().getStringExtra("username");

        // Initialize views
        TextView tvWelcomeCustomer = findViewById(R.id.tv_welcome_customer);
        Button btnLogout = findViewById(R.id.btn_logout_customer);

        // Set welcome text
        tvWelcomeCustomer.setText("Welcome, Customer " + username);

        // Set logout button listener
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        });
    }
}