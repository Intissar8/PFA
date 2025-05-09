package com.example.lab11_ghannane;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class AddProduct extends AppCompatActivity {

    private EditText etNom, etPrix, etQuantite, etDescription, etImageUrl;
    private Spinner spinnerCategory;
    private ImageView ivPreview;
    private Button btnAddProduct;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        etNom = findViewById(R.id.etNom);
        etPrix = findViewById(R.id.etPrix);
        etQuantite = findViewById(R.id.etQuantite);
        etDescription = findViewById(R.id.etDescription);
        etImageUrl = findViewById(R.id.etImageUrl);
        ivPreview = findViewById(R.id.ivPreview);
        spinnerCategory = findViewById(R.id.spCategorie);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        db = FirebaseFirestore.getInstance();

        // Set up Spinner for category selection
        String[] categories = {"Alimentation", "Electronique", "Vêtement", "Hygiène", "Cadeaux"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);


        // Load image preview when URL is typed
        etImageUrl.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String url = etImageUrl.getText().toString().trim();
                if (!url.isEmpty()) {
                    Glide.with(this).load(url).into(ivPreview);
                }
            }
        });

        btnAddProduct.setOnClickListener(v -> uploadProduct());
    }

    private void uploadProduct() {
        String nom = etNom.getText().toString();
        String prix = etPrix.getText().toString();
        String quantite = etQuantite.getText().toString();
        String description = etDescription.getText().toString();
        String imageUrl = etImageUrl.getText().toString().trim();
        String categorie = spinnerCategory.getSelectedItem().toString();

        if (nom.isEmpty() || prix.isEmpty() || quantite.isEmpty() || description.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Ajout du produit...");
        dialog.show();

        HashMap<String, Object> produit = new HashMap<>();
        produit.put("nom", nom);
        produit.put("prix", Double.parseDouble(prix));
        produit.put("quantite", Integer.parseInt(quantite));
        produit.put("description", description);
        produit.put("category", categorie);
        produit.put("imageUrl", imageUrl);

        db.collection("Produit").add(produit)
                .addOnSuccessListener(documentReference -> {
                    dialog.dismiss();
                    Toast.makeText(this, "Produit ajouté avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(this, "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}