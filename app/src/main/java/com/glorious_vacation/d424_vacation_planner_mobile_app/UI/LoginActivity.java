package com.glorious_vacation.d424_vacation_planner_mobile_app.UI;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.glorious_vacation.d424_vacation_planner_mobile_app.R;
import com.glorious_vacation.d424_vacation_planner_mobile_app.database.Repository;
import com.glorious_vacation.d424_vacation_planner_mobile_app.entities.User;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameField, passwordField;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = findViewById(R.id.etUsername);
        passwordField = findViewById(R.id.etPassword);
        Button loginButton = findViewById(R.id.btnLogin);
        TextView registerLink = findViewById(R.id.tvRegister);
        repository = new Repository(getApplication());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                String hashedPassword = HashUtils.hashPassword(password);

                User user = repository.authenticateUser(username, hashedPassword);
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userId", user.getUserId());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}

