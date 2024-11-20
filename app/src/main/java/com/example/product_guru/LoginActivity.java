package com.example.product_guru;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import entity.AppDatabase;
import entity.HashUtil;
import entity.User;
import entity.UserDao;

public class LoginActivity extends AppCompatActivity {
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UserDao
        AppDatabase db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        TextView signUpLink = findViewById(R.id.signUpLink);
        TextView forgotPasswordLink = findViewById(R.id.forgotPassword);
        Button signInButton = findViewById(R.id.signInButton);
        EditText emailInput = findViewById(R.id.email);
        EditText passwordInput = findViewById(R.id.password);

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });


        signInButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                User user = userDao.getUserByEmail(email);

                if (user == null) {
                    runOnUiThread(() -> Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show());
                    return;
                }

                // Compare hashed password
                String hashedInputPassword = HashUtil.hashPassword(password);
                if (!hashedInputPassword.equals(user.getHashedPassword())) {
                    runOnUiThread(() -> Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show());
                    return;
                }

                SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", user.getEmail());
                editor.apply();

                runOnUiThread(() -> {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
                    startActivity(intent);
                    finish();
                });
            }).start();
        });
    }
}
