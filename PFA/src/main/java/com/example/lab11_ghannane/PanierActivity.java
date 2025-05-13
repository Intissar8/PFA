package com.example.lab11_ghannane;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Date;

public class PanierActivity extends AppCompatActivity {
    private RecyclerView panierRecyclerView;
    private TextView totalText;
    private PanierAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);

        panierRecyclerView = findViewById(R.id.panierRecyclerView);
        totalText = findViewById(R.id.totalText);

        panierRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get data from PanierManager
        Map<String, Produit> produits = PanierManager.getInstance().getProduits();
        Map<String, Integer> quantities = PanierManager.getInstance().getQuantities();

        // Set up adapter and assign it to PanierManager
        adapter = new PanierAdapter(produits, quantities);
        PanierManager.getInstance().setAdapter(adapter);
        panierRecyclerView.setAdapter(adapter);

        // Display total
        updateTotal();

        Button commanderButton = findViewById(R.id.commanderButton);
        commanderButton.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Get user's address from users collection
            db.collection("users").document(user.getUid())
                    .get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String address = documentSnapshot.getString("address");
                            String username = documentSnapshot.getString("username");

                            createCommande(user.getUid(), address, username);
                        } else {
                            Toast.makeText(this, "Adresse introuvable", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "Erreur lors de la récupération de l'adresse", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void updateTotal() {
        double total = 0;
        Map<String, Produit> produits = PanierManager.getInstance().getProduits();
        Map<String, Integer> quantities = PanierManager.getInstance().getQuantities();

        for (String id : produits.keySet()) {
            Produit p = produits.get(id);
            int qte = quantities.get(id);
            total += qte * p.getPrix();
        }

        totalText.setText("Total: " + String.format("%.2f", total) + " DH");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when activity resumes
        if (adapter != null) {
            adapter.refresh(); // Refresh the product list
        }
        updateTotal(); // Update the total price
    }

    private void createCommande(String userId, String address, String username) {
        Map<String, Produit> produits = PanierManager.getInstance().getProduits();
        Map<String, Integer> quantities = PanierManager.getInstance().getQuantities();
        List<Map<String, Object>> productList = new ArrayList<>();

        double total = 0;
        for (String id : produits.keySet()) {
            Produit p = produits.get(id);
            int qte = quantities.get(id);
            double itemTotal = p.getPrix() * qte;
            total += itemTotal;

            Map<String, Object> productMap = new HashMap<>();
            productMap.put("id_produit", id);
            productMap.put("nom", p.getNom());
            productMap.put("prix", p.getPrix());
            productMap.put("quantite", qte);
            productList.add(productMap);
        }

        Map<String, Object> commandeData = new HashMap<>();
        commandeData.put("status", "Order Received");
        commandeData.put("userId", userId);
        commandeData.put("address", address);
        commandeData.put("username", username);
        commandeData.put("total", total);
        commandeData.put("dateCommande", new Date());
        commandeData.put("produits", productList);

        FirebaseFirestore.getInstance().collection("Commande")
                .add(commandeData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Commande enregistrée avec succès", Toast.LENGTH_SHORT).show();
                    PanierManager.getInstance().clearPanier(); // Clear panier
                    startActivity(new Intent(this, delivery_status.class));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Échec de la commande", Toast.LENGTH_SHORT).show();
                });
    }
}