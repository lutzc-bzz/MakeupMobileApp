package com.example.product_guru;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ProductDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ImageView productImage = findViewById(R.id.productImage);
        TextView productTitle = findViewById(R.id.productTitle);
        TextView productPrice = findViewById(R.id.productPrice);
        TextView productDescription = findViewById(R.id.productDescription);
        TextView productLink = findViewById(R.id.productLink);
        RatingBar productRating = findViewById(R.id.productRating);

        // Retrieve data from intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("product_name");
        String price = intent.getStringExtra("product_price");
        String currency = intent.getStringExtra("product_currency");
        String description = intent.getStringExtra("product_description");
        String imageUrl = intent.getStringExtra("product_image");
        String link = intent.getStringExtra("product_link");

        // Set data to views
        productTitle.setText(name);
        productPrice.setText(currency + price);
        productDescription.setText(description);
        productLink.setText(link);
        Glide.with(this).load(imageUrl).into(productImage);
    }
}
