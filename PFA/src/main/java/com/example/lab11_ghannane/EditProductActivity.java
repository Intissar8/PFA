package com.example.lab11_ghannane;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditProductActivity extends AppCompatActivity {

    private ImageView imageViewProduct;
    private EditText editTextImageUrl, editTextName, editTextDescription, editTextPrice, editTextQuantity;
    private Spinner spinnerCategory;
    private Button buttonUpdateProduct;

    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Bind views
        imageViewProduct = findViewById(R.id.imageViewProduct);
        editTextImageUrl = findViewById(R.id.editTextImageUrl);
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonUpdateProduct = findViewById(R.id.buttonUpdateProduct);

        // Set up Spinner for category selection
        String[] categories = {"Alimentation", "Electronique", "Vêtement", "Hygiène", "Cadeaux"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Get product ID from Intent
        productId = getIntent().getStringExtra("id_produit");

        // Load product data from Firestore
        loadProductData();

        // Set up "Update Product" button
        buttonUpdateProduct.setOnClickListener(v -> updateProductInFirestore());
    }

    private void loadProductData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference productRef = db.collection("Produit").document(productId);

        productRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve product data from Firestore
                Produit produit = documentSnapshot.toObject(Produit.class);

                // Fill fields with existing product data
                if (produit != null) {
                    editTextName.setText(produit.getNom());
                    editTextDescription.setText(produit.getDescription());
                    editTextPrice.setText(String.valueOf(produit.getPrix()));
                    editTextQuantity.setText(String.valueOf(produit.getQuantite()));

                    // Load the current image using Glide
                    Glide.with(EditProductActivity.this)
                            .load(produit.getImageUrl())
                            .into(imageViewProduct);

                    // Set the image URL in the EditText
                    editTextImageUrl.setText(produit.getImageUrl());

                    // Set the category in the spinner
                    String[] categories = {"Alimentation", "Electronique", "Vêtement", "Hygiène", "Cadeaux"};
                    int spinnerPosition = getCategoryPosition(produit.getCategory(), categories);
                    spinnerCategory.setSelection(spinnerPosition);
                }
            } else {
                Toast.makeText(EditProductActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(EditProductActivity.this, "Error loading product data", Toast.LENGTH_SHORT).show();
        });
    }

    private int getCategoryPosition(String category, String[] categories) {
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(category)) {
                return i;
            }
        }
        return 0; // Default to the first category if not found
    }

    private void updateProductInFirestore() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String priceStr = editTextPrice.getText().toString().trim();
        String quantityStr = editTextQuantity.getText().toString().trim();
        String imageUrl = editTextImageUrl.getText().toString().trim();

        if (imageUrl.isEmpty()) {
            Toast.makeText(this, "Please enter an image URL", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the image preview
        Glide.with(this).load(imageUrl).into(imageViewProduct);

        Map<String, Object> updatedProduct = new HashMap<>();
        updatedProduct.put("nom", name);
        updatedProduct.put("description", description);
        updatedProduct.put("category", category);
        updatedProduct.put("prix", Double.parseDouble(priceStr));
        updatedProduct.put("quantite", Integer.parseInt(quantityStr));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference productRef = db.collection("Produit").document(productId);

        productRef.update(updatedProduct)
                .addOnSuccessListener(unused -> {
                    // Show success toast
                    Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();

                    // Navigate to AddProductActivity
                    Intent intent = new Intent(EditProductActivity.this, manage_produit.class);
                    startActivity(intent);

                    // Optionally, finish the current activity to prevent going back to it
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Show failure toast
                    Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show();
                });
    }
}