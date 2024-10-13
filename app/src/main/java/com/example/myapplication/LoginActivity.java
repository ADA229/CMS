package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Initialize views
        EditText username = findViewById(R.id.et_username);
        EditText password = findViewById(R.id.et_password);
        Button loginButton = findViewById(R.id.btn_login);
        TextView signupText = findViewById(R.id.tv_signup);

        // Signup link click listener
        signupText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        // Login button click listener
        loginButton.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            // Input validation
            if (user.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pass.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate credentials
            if (dbHelper.validateUser(user, pass)) {
                String userType = dbHelper.getUserType(user);

                // Log the user type for debugging purposes
                Log.d("LoginActivity", "User type retrieved from database: " + userType);

                if (userType != null) {
                    // Normalize user type to lowercase for case-insensitive comparison
                    userType = userType.toLowerCase();

                    Intent intent;
                    if (userType.equals("customer")) {
                        intent = new Intent(LoginActivity.this, CustomerDashboardActivity.class);
                    } else if (userType.equals("shopkeeper")) {
                        intent = new Intent(LoginActivity.this, ShopkeeperDashboardActivity.class);
                    } else {
                        // If an unknown user type is encountered
                        Toast.makeText(LoginActivity.this, "Unknown user type", Toast.LENGTH_SHORT).show();
                        return;  // Exit the function to prevent any further execution
                    }

                    // Start the appropriate activity
                    startActivity(intent);
                    finish();  // Close the login activity

                } else {
                    // If the user type was not found
                    Toast.makeText(LoginActivity.this, "User type not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle invalid credentials here
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}