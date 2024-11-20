package com.example.product_guru;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import entity.AppDatabase;
import entity.HashUtil;
import entity.User;
import entity.UserDao;

public class MainActivity extends AppCompatActivity {
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UserDao
        AppDatabase db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        TextView signInLink = findViewById(R.id.signInLink);
        Button signUpButton = findViewById(R.id.signUpButton);
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);

        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }

            String hashedPassword = HashUtil.hashPassword(password);

            new Thread(() -> {
                try {
                    User existingUser = userDao.getUserByEmail(email);
                    if (existingUser != null) {
                        runOnUiThread(() -> Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show());
                    } else {
                        User newUser = new User(email, "User", hashedPassword);
                        userDao.insertUser(newUser);
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, LoginActivity.class));
                        });
                    }
                } catch (Exception e) {
                    Log.e("RegisterError", "Error during registration", e);
                    runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            }).start();
        });

    }
}
