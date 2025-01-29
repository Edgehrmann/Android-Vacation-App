package com.glorious_vacation.d424_vacation_planner_mobile_app.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.glorious_vacation.d424_vacation_planner_mobile_app.R;
import com.glorious_vacation.d424_vacation_planner_mobile_app.database.Repository;
import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.User;

public class RegistrationActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnRegister;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize UI elements
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        repository = new Repository(getApplication());

        // Handle registration logic
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Input validation
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if user already exists
                User existingUser = repository.findUserByUsername(username);
                if (existingUser != null) {
                    Toast.makeText(RegistrationActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Hash the password (for simplicity, store as plain text for now)
                String passwordHash = HashUtils.hashPassword(password); // Replace with hashing logic if needed

                // Create and save the new user
                User newUser = new User(username, passwordHash);
                repository.insertUser(newUser);

                // Show success message and navigate to login
                Toast.makeText(RegistrationActivity.this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
                finish(); // Navigate back to LoginActivity
            }
        });
    }
}

