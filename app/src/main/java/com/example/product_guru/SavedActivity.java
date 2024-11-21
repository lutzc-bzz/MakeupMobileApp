package com.example.product_guru;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapter.ProductAdapter;
import model.Product;

public class SavedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        // Beispiel-Daten für gespeicherte Produkte
        List<Product> savedProducts = new ArrayList<>();
        savedProducts.add(new Product("Brand A", "Product 1", "10.99", "USD", "https://via.placeholder.com/150", "Description 1", "https://example.com", "lipstick", "5.0", "vegan", "water"));
        savedProducts.add(new Product("Brand B", "Product 2", "15.49", "USD", "https://via.placeholder.com/150", "Description 2", "https://example.com", "foundation", "4.5", "vegan", "water"));
        savedProducts.add(new Product("Brand C", "Product 3", "25.00", "USD", "https://via.placeholder.com/150", "Description 3", "https://example.com", "blush", "3.5", "vegan", "water"));
        savedProducts.add(new Product("Brand D", "Product 4", "8.99", "USD", "https://via.placeholder.com/150", "Description 4", "https://example.com", "concealer", "4.0", "vegan", "water"));

        // RecyclerView initialisieren
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSavedProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 Spalten in der Grid-Ansicht
        ProductAdapter adapter = new ProductAdapter(this, savedProducts);
        recyclerView.setAdapter(adapter);
    }
}
