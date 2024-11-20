package com.example.product_guru;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import entity.AppDatabase;
import entity.UserDao;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        AppDatabase db = AppDatabase.getInstance(this);
        UserDao userDao = db.userDao();

        // Initialize views
        TextView nameText = findViewById(R.id.nameText);
        TextView emailText = findViewById(R.id.emailText);
        ImageView closeButton = findViewById(R.id.closeButton);
        Switch notificationSwitch = findViewById(R.id.notificationSwitch);

        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String email = preferences.getString("email", null);

        emailText.setText(email);

        closeButton.setOnClickListener(v -> finish());
    }
}
