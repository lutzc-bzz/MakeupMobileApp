package com.example.product_guru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        TextView minPrice = findViewById(R.id.minPrice);
        TextView maxPrice = findViewById(R.id.maxPrice);

        LinearLayout brandCheckBoxContainer = findViewById(R.id.brandContainer);
        TextView brandToggleText = findViewById(R.id.toggleBrandText);

        RadioButton rating1 = findViewById(R.id.rating_1);
        RadioButton rating2 = findViewById(R.id.rating_2);
        RadioButton rating3 = findViewById(R.id.rating_3);
        RadioButton rating4 = findViewById(R.id.rating_4);

        LinearLayout ingredientCheckBoxContainer = findViewById(R.id.ingredientContainer);
        TextView ingredientToggleText = findViewById(R.id.toggleIngredientText);

        LinearLayout categoryCheckBoxContainer = findViewById(R.id.categoryContainer);
        TextView categoryToggleText = findViewById(R.id.toggleCategoryText);

        LinearLayout productTypeCheckBoxContainer = findViewById(R.id.productContainer);
        TextView productTypeToggleText = findViewById(R.id.toggleProductText);

        Button resetButton = findViewById(R.id.resetButton);
        Button applyButton = findViewById(R.id.applyButton);
        ImageView backIcon = findViewById(R.id.backIcon);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String minPriceStr = minPrice.getText().toString().trim();
                String maxPriceStr = maxPrice.getText().toString().trim();

                double minPriceValue = minPriceStr.isEmpty() ? 0 : Double.parseDouble(minPriceStr);
                double maxPriceValue = maxPriceStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceStr);

                Set<String> selectedBrands = new HashSet<>();

                for (int i = 0; i < brandCheckBoxContainer.getChildCount(); i++) {
                    CheckBox checkBox = (CheckBox) brandCheckBoxContainer.getChildAt(i);
                    if (checkBox.isChecked()) {
                        selectedBrands.add(checkBox.getText().toString());
                    }
                }
                Intent intent = new Intent(FilterActivity.this, SearchActivity.class);

                intent.putExtra("minPrice", minPriceValue);
                intent.putExtra("maxPrice", maxPriceValue);

                ArrayList<String> selectedBrandsList = new ArrayList<>(selectedBrands);
                intent.putStringArrayListExtra("selectedBrands", selectedBrandsList);

                startActivity(intent);
            }
        });


        resetButton.setOnClickListener(v -> {
            minPrice.setText("");
            maxPrice.setText("");

            rating1.setChecked(false);
            rating2.setChecked(false);
            rating3.setChecked(false);
            rating4.setChecked(false);

            for (int i = 0; i < brandCheckBoxContainer.getChildCount(); i++) {
                if (brandCheckBoxContainer.getChildAt(i) instanceof CheckBox) {
                    ((CheckBox) brandCheckBoxContainer.getChildAt(i)).setChecked(false);
                }
            }

            for (int i = 0; i < ingredientCheckBoxContainer.getChildCount(); i++) {
                if (ingredientCheckBoxContainer.getChildAt(i) instanceof CheckBox) {
                    ((CheckBox) ingredientCheckBoxContainer.getChildAt(i)).setChecked(false);
                }
            }

            for (int i = 0; i < categoryCheckBoxContainer.getChildCount(); i++) {
                if (categoryCheckBoxContainer.getChildAt(i) instanceof CheckBox) {
                    ((CheckBox) categoryCheckBoxContainer.getChildAt(i)).setChecked(false);
                }
            }

            for (int i = 0; i < productTypeCheckBoxContainer.getChildCount(); i++) {
                if (productTypeCheckBoxContainer.getChildAt(i) instanceof CheckBox) {
                    ((CheckBox) productTypeCheckBoxContainer.getChildAt(i)).setChecked(false);
                }
            }
        });

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.products);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String jsonString = new String(buffer, "UTF-8");

            JSONArray products = new JSONArray(jsonString);

            Set<String> brandSet = new HashSet<>();
            Set<String> ingredientSet = new HashSet<>();
            Set<String> categorySet = new HashSet<>();
            Set<String> productTypeSet = new HashSet<>();

            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);
                String brand = product.getString("brand");
                brandSet.add(brand);

                JSONArray tags = product.getJSONArray("tag_list");
                for (int j = 0; j < tags.length(); j++) {
                    ingredientSet.add(tags.getString(j));
                }

                String category = product.getString("category");
                categorySet.add(category);

                String productType = product.getString("product_type");
                productTypeSet.add(productType);
            }

            List<String> brandList = new ArrayList<>(brandSet);
            List<String> ingredientList = new ArrayList<>(ingredientSet);
            List<String> categoryList = new ArrayList<>(categorySet);
            List<String> productTypeList = new ArrayList<>(productTypeSet);

            for (int i = 0; i < Math.min(5, brandList.size()); i++) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(brandList.get(i));
                brandCheckBoxContainer.addView(checkBox);
            }

            final boolean[] isBrandsAllShown = {false};

            List<CheckBox> extraBrandCheckBoxes = new ArrayList<>();
            for (int i = 5; i < brandList.size(); i++) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(brandList.get(i));
                checkBox.setVisibility(View.GONE); // Initially hidden
                extraBrandCheckBoxes.add(checkBox);
                brandCheckBoxContainer.addView(checkBox);
            }

            brandToggleText.setOnClickListener(view -> {
                if (isBrandsAllShown[0]) {
                    // Hide extra brand checkboxes and change text
                    for (CheckBox checkBox : extraBrandCheckBoxes) {
                        checkBox.setVisibility(View.GONE);
                    }
                    brandToggleText.setText("Show All");
                } else {
                    // Show all brand checkboxes and change text
                    for (CheckBox checkBox : extraBrandCheckBoxes) {
                        checkBox.setVisibility(View.VISIBLE);
                    }
                    brandToggleText.setText("Show Less");
                }
                isBrandsAllShown[0] = !isBrandsAllShown[0];
            });

            for (int i = 0; i < Math.min(5, ingredientList.size()); i++) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(ingredientList.get(i));
                ingredientCheckBoxContainer.addView(checkBox);
            }

            final boolean[] isIngredientsAllShown = {false};

            List<CheckBox> extraIngredientCheckBoxes = new ArrayList<>();
            for (int i = 5; i < ingredientList.size(); i++) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(ingredientList.get(i));
                checkBox.setVisibility(View.GONE);
                extraIngredientCheckBoxes.add(checkBox);
                ingredientCheckBoxContainer.addView(checkBox);
            }

            ingredientToggleText.setOnClickListener(view -> {
                if (isIngredientsAllShown[0]) {
                    for (CheckBox checkBox : extraIngredientCheckBoxes) {
                        checkBox.setVisibility(View.GONE);
                    }
                    ingredientToggleText.setText("Show All");
                } else {
                    for (CheckBox checkBox : extraIngredientCheckBoxes) {
                        checkBox.setVisibility(View.VISIBLE);
                    }
                    ingredientToggleText.setText("Show Less");
                }
                isIngredientsAllShown[0] = !isIngredientsAllShown[0];
            });

            for (int i = 0; i < Math.min(5, categoryList.size()); i++) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(categoryList.get(i));
                categoryCheckBoxContainer.addView(checkBox);
            }

            final boolean[] isCategoriesAllShown = {false};

            List<CheckBox> extraCategoryCheckBoxes = new ArrayList<>();
            for (int i = 5; i < categoryList.size(); i++) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(categoryList.get(i));
                checkBox.setVisibility(View.GONE);
                extraCategoryCheckBoxes.add(checkBox);
                categoryCheckBoxContainer.addView(checkBox);
            }

            categoryToggleText.setOnClickListener(view -> {
                if (isCategoriesAllShown[0]) {
                    for (CheckBox checkBox : extraCategoryCheckBoxes) {
                        checkBox.setVisibility(View.GONE);
                    }
                    categoryToggleText.setText("Show All");
                } else {
                    for (CheckBox checkBox : extraCategoryCheckBoxes) {
                        checkBox.setVisibility(View.VISIBLE);
                    }
                    categoryToggleText.setText("Show Less");
                }
                isCategoriesAllShown[0] = !isCategoriesAllShown[0];
            });

            for (int i = 0; i < Math.min(5, productTypeList.size()); i++) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(productTypeList.get(i));
                productTypeCheckBoxContainer.addView(checkBox);
            }

            final boolean[] isProductTypesAllShown = {false};
            List<CheckBox> extraProductTypeCheckBoxes = new ArrayList<>();
            for (int i = 5; i < productTypeList.size(); i++) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(productTypeList.get(i));
                checkBox.setVisibility(View.GONE);
                extraProductTypeCheckBoxes.add(checkBox);
                productTypeCheckBoxContainer.addView(checkBox);
            }
            productTypeToggleText.setOnClickListener(view -> {
                if (isProductTypesAllShown[0]) {
                    for (CheckBox checkBox : extraProductTypeCheckBoxes) {
                        checkBox.setVisibility(View.GONE);
                    }
                    productTypeToggleText.setText("Show All");
                } else {
                    for (CheckBox checkBox : extraProductTypeCheckBoxes) {
                        checkBox.setVisibility(View.VISIBLE);
                    }
                    productTypeToggleText.setText("Show Less");
                }
                isProductTypesAllShown[0] = !isProductTypesAllShown[0];
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}