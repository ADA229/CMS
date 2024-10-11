package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
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

        // Initialize Sign Up link
        TextView signupText = findViewById(R.id.tv_signup);
        signupText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        // Set login button click listener
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

            // Validate credentials using SQLite DB
            try {
                if (dbHelper.validateUser(user, pass)) {
                    // If login is successful, navigate to MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", user);
                    startActivity(intent);
                    finish(); // Close LoginActivity
                } else {
                    // Show error message if validation fails
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                e.printStackTrace(); // Log the error for debugging
            }
        });
    }
}
