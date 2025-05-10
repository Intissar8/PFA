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
    private EditText editTextDatePromotion, editTextPrixReduction; // New fields
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
        editTextDatePromotion = findViewById(R.id.editTextDatePromotion);     // New
        editTextPrixReduction = findViewById(R.id.editTextPrixReduction);     // New
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonUpdateProduct = findViewById(R.id.buttonUpdateProduct);

        // Spinner setup
        String[] categories = {"Alimentation", "Electronique", "Vêtement", "Hygiène", "Cadeaux"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Get product ID from Intent
        productId = getIntent().getStringExtra("id_produit");

        // Load data
        loadProductData();

        // Handle update button
        buttonUpdateProduct.setOnClickListener(v -> updateProductInFirestore());
    }

    private void loadProductData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference productRef = db.collection("Produit").document(productId);

        productRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Produit produit = documentSnapshot.toObject(Produit.class);

                if (produit != null) {
                    editTextName.setText(produit.getNom());
                    editTextDescription.setText(produit.getDescription());
                    editTextPrice.setText(String.valueOf(produit.getPrix()));
                    editTextQuantity.setText(String.valueOf(produit.getQuantite()));
                    editTextImageUrl.setText(produit.getImageUrl());
                    Glide.with(this).load(produit.getImageUrl()).into(imageViewProduct);

                    // Set category
                    int spinnerPosition = getCategoryPosition(produit.getCategory(), new String[]{"Alimentation", "Electronique", "Vêtement", "Hygiène", "Cadeaux"});
                    spinnerCategory.setSelection(spinnerPosition);

                    // Set promotion fields if available
                    editTextDatePromotion.setText(produit.getDatePromotion() != null ? produit.getDatePromotion() : "");
                    editTextPrixReduction.setText(String.valueOf(produit.getPrixReduction()));
                }
            } else {
                Toast.makeText(this, "Produit introuvable", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Erreur de chargement", Toast.LENGTH_SHORT).show()
        );
    }

    private int getCategoryPosition(String category, String[] categories) {
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(category)) return i;
        }
        return 0;
    }

    private void updateProductInFirestore() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String priceStr = editTextPrice.getText().toString().trim();
        String quantityStr = editTextQuantity.getText().toString().trim();
        String imageUrl = editTextImageUrl.getText().toString().trim();
        String datePromotion = editTextDatePromotion.getText().toString().trim();
        String prixReductionStr = editTextPrixReduction.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        double prixReduction = prixReductionStr.isEmpty() ? 0.0 : Double.parseDouble(prixReductionStr);

        Glide.with(this).load(imageUrl).into(imageViewProduct);

        Map<String, Object> updatedProduct = new HashMap<>();
        updatedProduct.put("nom", name);
        updatedProduct.put("description", description);
        updatedProduct.put("category", category);
        updatedProduct.put("prix", Double.parseDouble(priceStr));
        updatedProduct.put("quantite", Integer.parseInt(quantityStr));
        updatedProduct.put("imageUrl", imageUrl);
        updatedProduct.put("datePromotion", datePromotion);
        updatedProduct.put("prixReduction", prixReduction);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference productRef = db.collection("Produit").document(productId);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Mise à jour du produit...");
        dialog.show();

        productRef.update(updatedProduct)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                    Toast.makeText(this, "Produit mis à jour avec succès", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, manage_produit.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                });
    }
}