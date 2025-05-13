package com.example.lab11_ghannane;

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
}