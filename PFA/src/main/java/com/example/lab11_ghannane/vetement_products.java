package com.example.lab11_ghannane;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class vetement_products extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductFoodAdapter adapter;
    private List<Produit> productList;          // Full list from Firestore
    private List<Produit> filteredProductList;  // List filtered based on search
    private FirebaseFirestore db;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimentaire_products);

        searchBar = findViewById(R.id.searchBar);

        ImageButton btnPanier = findViewById(R.id.btnPanier);
        btnPanier.setOnClickListener(v -> startActivity(new Intent(this, PanierActivity.class)));

        recyclerView = findViewById(R.id.recyclerProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        filteredProductList = new ArrayList<>();
        adapter = new ProductFoodAdapter(this, filteredProductList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadAlimentaireProducts();

        // Add search text listener
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {  }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.second_color));
        }
    }

    private void loadAlimentaireProducts() {
        db.collection("Produit")
                .whereEqualTo("category", "Vêtement")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Produit produit = doc.toObject(Produit.class);
                        if (produit.getCategory().equals("Vêtement")) {
                            productList.add(produit);
                        }
                    }
                    // Initially show all products
                    filteredProductList.clear();
                    filteredProductList.addAll(productList);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching products", e));
    }

    // Filter method to update filteredProductList and notify adapter
    private void filterProducts(String query) {
        filteredProductList.clear();

        if (query.isEmpty()) {
            filteredProductList.addAll(productList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Produit p : productList) {
                if (p.getNom().toLowerCase().contains(lowerCaseQuery)) {
                    filteredProductList.add(p);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}