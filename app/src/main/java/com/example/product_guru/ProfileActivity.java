package com.example.product_guru;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import entity.AppDatabase;
import entity.User;
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
        int id = preferences.getInt("id", -1);

        if (id == -1) {
            Toast.makeText(this, "No user is logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        new Thread(() -> {
            User user = userDao.getUserById(id);
            if (user != null) {
                runOnUiThread(() -> {
                    emailText.setText(user.getEmail());
                    nameText.setText(user.getName() + " " + user.getId()); // You can add a real name if available
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        }).start();

        closeButton.setOnClickListener(v -> finish());
    }
}
