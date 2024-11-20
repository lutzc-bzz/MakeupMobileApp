package com.example.product_guru;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

            AppDatabase db = AppDatabase.getInstance(this);
            UserDao userDao = db.userDao();
            User user = userDao.getUserByEmail(email);

            if (user == null || !HashUtil.hashPassword(password).equals(user.getHashedPassword())) {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
            startActivity(intent);
        });
    }
}
