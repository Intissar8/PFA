package com.example.lab11_ghannane;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Configuration des boutons de navigation pour les catégories
        setupCategoryNavigation();

        // Configuration du bouton du panier
        //setupCartButton();
       // setupAddToCartButtons();

        // Configuration des boutons d'ajout au panier (décommenter si applicable)
        // setupAddToCartButtons();

        // Configuration de la barre de navigation du bas (décommenter si nécessaire)
        setupBottomNavigation();
    }

    private void setupCategoryNavigation() {
        // Trouver les vues des catégories par leur ID
        View foodCategory = findViewById(R.id.foodCategory);
        View hygieneCategory = findViewById(R.id.hygieneCategory);
        View electronicsCategory = findViewById(R.id.electronicsCategory);
        View giftsCategory = findViewById(R.id.giftsCategory);
        View clothingCategory = findViewById(R.id.clothingCategory);
        View offreCategory = findViewById(R.id.offreCategory);

        // Configurer les listeners pour chaque catégorie
        if (foodCategory != null) {
            foodCategory.setOnClickListener(v -> startActivity(new Intent(Home.this, alimentaire_products.class)));
        }

        if (hygieneCategory != null) {
           // hygieneCategory.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Hygiene.class)));
        }

        if (electronicsCategory != null) {
           electronicsCategory.setOnClickListener(v -> startActivity(new Intent(Home.this, electronique_products.class)));
        }

        if (giftsCategory != null) {
          //  giftsCategory.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Cadeau.class)));
        }

        if (clothingCategory != null) {
           // clothingCategory.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Vetement.class)));
        }

        if (offreCategory != null) {
           // offreCategory.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Offre.class)));
        }
    }

   /* private void setupCartButton() {
        ImageButton btnPanier = findViewById(R.id.btnPanier);
        if (btnPanier != null) {
            btnPanier.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            });
        }
    }*/

  /* private void setupAddToCartButtons() {
        // Exemple pour un bouton d'ajout au panier
        Button btnAddToCart = findViewById(R.id.btnAddToCart); // Adaptez l'ID selon votre layout
        if (btnAddToCart != null) {
            btnAddToCart.setOnClickListener(v -> {
                // Récupérer les informations du produit (à adapter selon votre structure)
                String productName = "Nom du produit"; // Remplacez par le nom réel du produit
                double price = 99.99; // Remplacez par le prix réel du produit
                int quantity = 1; // Ou récupérez la quantité depuis un TextView

                // Créer un nouvel élément de panier
                CartItem item = new CartItem(1, productName, price, quantity);

                // Ajouter au gestionnaire de panier
                CartManager.getInstance().addToCart(item);

                // Afficher un message de confirmation
                Toast.makeText(this, productName + " ajouté au panier", Toast.LENGTH_SHORT).show();
            });
        }
    }*/

    private void setupBottomNavigation() {
        LinearLayout homeNav = findViewById(R.id.home_nav);
        LinearLayout productsNav = findViewById(R.id.products_nav);
        LinearLayout ordersNav = findViewById(R.id.orders_nav);
        LinearLayout profileNav = findViewById(R.id.profile_nav);

        // Home navigation
        homeNav.setOnClickListener(view -> {
            // Navigate to Home activity (if necessary, you can call finish() to prevent it from stacking)
            Intent intent = new Intent(Home.this, Home.class);
            startActivity(intent);
            finish();
        });

        // Products navigation
        productsNav.setOnClickListener(view -> {
            // Navigate to ProductsActivity (ensure the right class is specified)
           // Intent intent = new Intent(Home.this, ProductsActivity.class);
           // startActivity(intent);

        });

        // Orders navigation
        ordersNav.setOnClickListener(view -> {
            // Navigate to OrdersActivity (ensure the right class is specified)
           Intent intent = new Intent(Home.this, delivery_status.class);
          startActivity(intent);

        });

        // Profile navigation
        profileNav.setOnClickListener(view -> {
            // Navigate to Profile activity from Home
            Intent intent = new Intent(Home.this, Profile.class);
            startActivity(intent);
        });
    }
}