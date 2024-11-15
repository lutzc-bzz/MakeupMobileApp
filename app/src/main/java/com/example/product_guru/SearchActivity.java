package com.example.product_guru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import adapter.ProductAdapter;
import model.Product;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        RecyclerView productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setVerticalScrollBarEnabled(true);
        productRecyclerView.setScrollBarSize(10); // This should match the XML attribute if you want it wider than 2dp
        productRecyclerView.setScrollbarFadingEnabled(false); // Keeps the scrollbar visible at all times

        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        productRecyclerView.setAdapter(productAdapter);

        loadProducts();


        ImageView profileIcon = findViewById(R.id.profileIcon);
        ImageView filterIcon = findViewById(R.id.filterIcon);
        ImageButton navSaved = findViewById(R.id.navSaved);
        ImageButton navCamera = findViewById(R.id.navCamera);

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, FilterActivity.class);
                startActivity(intent);
            }
        });

        navSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, SavedActivity.class);
                startActivity(intent);
            }
        });

        navCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadProducts() {
        try {
            InputStream is = getResources().openRawResource(R.raw.products);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String jsonText = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(jsonText);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String brand = jsonObject.getString("brand");
                String name = jsonObject.getString("name");
                String price = jsonObject.getString("price");
                String currency = jsonObject.getString("currency");
                String imageUrl = jsonObject.getString("image_link");
                String description = jsonObject.getString("description");
                String productLink = jsonObject.getString("product_link");

                productList.add(new Product(brand, name, price, currency, imageUrl, description, productLink));
            }
            productAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
