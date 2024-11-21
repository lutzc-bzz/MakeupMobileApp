package com.example.product_guru;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
    private List<Product> filteredProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        double minPrice = getIntent().getDoubleExtra("minPrice", 0);
        double maxPrice = getIntent().getDoubleExtra("maxPrice", Double.MAX_VALUE);
        ArrayList<String> selectedBrands = getIntent().getStringArrayListExtra("selectedBrands");
        double rating = getIntent().getDoubleExtra("selectedRating", 0);

        RecyclerView productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setVerticalScrollBarEnabled(true);
        productRecyclerView.setScrollBarSize(10); // This should match the XML attribute if you want it wider than 2dp
        productRecyclerView.setScrollbarFadingEnabled(false); // Keeps the scrollbar visible at all times

        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        productList = new ArrayList<>();
        filteredProductList = new ArrayList<>();

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

        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        applyFilter(minPrice, maxPrice, selectedBrands, rating);
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
                String category = jsonObject.getString("category");
                String rating = jsonObject.getString("rating");

                productList.add(new Product(brand, name, price, currency, imageUrl, description, productLink, category, rating));
            }
            filteredProductList.addAll(productList);
            productAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void applyFilter(double minPrice, double maxPrice, ArrayList<String> selectedBrands, double rating) {
        List<Product> filteredList = new ArrayList<>();

        for (Product product : productList) {
            String priceString = product.getPrice();
            double productPrice = 0;

            if (priceString != null && !priceString.isEmpty()) {
                try {
                    productPrice = Double.parseDouble(priceString);
                } catch (NumberFormatException e) {
                    Log.e("SearchActivity", "Invalid product price: " + priceString);
                    continue;
                }
            } else {
                Log.e("SearchActivity", "Product price is null or empty: " + priceString);
                continue;
            }
            if (productPrice < minPrice || productPrice > maxPrice) {
                continue;
            }

            if (selectedBrands != null && !selectedBrands.isEmpty()) {
                String productBrand = product.getBrand();
                if (productBrand == null || !selectedBrands.contains(productBrand)) {
                    continue;
                }
            }

            String ProductRating = product.getRating();
            double productRating = 0;
            if (ProductRating != null && !ProductRating.isEmpty()) {
                try {
                    productRating = Double.parseDouble(ProductRating);
                } catch (NumberFormatException e) {
                    Log.e("SearchActivity", "Invalid product rating: " + ProductRating);
                    continue;
                }
            }

            if (productRating < rating) {
                continue;
            }

            filteredList.add(product);
        }
        productAdapter.updateList(filteredList);
    }


    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        productAdapter.updateList(filteredList);
    }
}
