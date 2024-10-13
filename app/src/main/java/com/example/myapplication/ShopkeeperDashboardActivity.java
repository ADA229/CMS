package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShopkeeperDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper_dashboard);

        // Get username from intent
        String username = getIntent().getStringExtra("username");

        // Initialize views
        TextView tvWelcomeShopkeeper = findViewById(R.id.tv_welcome_shopkeeper);
        Button btnLogout = findViewById(R.id.btn_logout_shopkeeper);

        // Set welcome text
        tvWelcomeShopkeeper.setText("Welcome, Shopkeeper " + username);

        // Set logout button listener
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(ShopkeeperDashboardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        });
    }
}