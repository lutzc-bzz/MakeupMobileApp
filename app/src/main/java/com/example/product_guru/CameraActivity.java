package com.example.product_guru;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    private Bitmap capturedBitmap;
    private TextView colorInfoTextView;
    private ImageView imageView;
    private final ActivityResultLauncher<Void> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), result -> {
                if (result != null) {
                    imageView.setImageBitmap(result);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.imageView);
        Button captureButton = findViewById(R.id.captureButton);
        Button analyzeButton = findViewById(R.id.analyzeButton);


        captureButton.setOnClickListener(v -> {
            takePictureLauncher.launch(null);
        });

        analyzeButton.setOnClickListener(v -> {
            if (capturedBitmap != null) {
                int x = capturedBitmap.getWidth() / 2;
                int y = capturedBitmap.getHeight() / 2;

                int pixelColor = capturedBitmap.getPixel(x, y);

                int red = Color.red(pixelColor);
                int green = Color.green(pixelColor);
                int blue = Color.blue(pixelColor);

                // Display the color information
                String colorInfo = "R: " + red + ", G: " + green + ", B: " + blue;
                colorInfoTextView.setText(colorInfo);
            }
        });
    }
}
