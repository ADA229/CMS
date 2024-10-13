package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Initialize views
        EditText username = findViewById(R.id.et_signup_username);
        EditText password = findViewById(R.id.et_signup_password);
        EditText confirmPassword = findViewById(R.id.et_signup_confirm_password);
        Spinner userTypeSpinner = findViewById(R.id.spinner_user_type); // Initialize Spinner
        Button signupButton = findViewById(R.id.btn_signup);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        userTypeSpinner.setAdapter(adapter);

        // Set signup button click listener
        signupButton.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String confirmPass = confirmPassword.getText().toString().trim();
            String selectedUserType = userTypeSpinner.getSelectedItem().toString(); // Get selected user type

            // Input validation
            if (user.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pass.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (confirmPass.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if passwords match
            if (!pass.equals(confirmPass)) {
                Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add user to the database
            try {
                dbHelper.addUser(user, pass, selectedUserType);
                Toast.makeText(SignupActivity.this, "User created successfully as " + selectedUserType, Toast.LENGTH_SHORT).show();

                // Navigate to LoginActivity after successful signup
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close SignupActivity
            } catch (IllegalArgumentException e) {
                Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}