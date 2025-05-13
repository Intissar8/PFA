package com.example.lab11_ghannane;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

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


public class alimentaire_products extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductFoodAdapter adapter;
    private List<Produit> productList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alimentaire_products);


        recyclerView = findViewById(R.id.recyclerProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        adapter = new ProductFoodAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadAlimentaireProducts();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.second_color));
        }
    }

    private void loadAlimentaireProducts() {
        db.collection("Produit")
                .whereEqualTo("category", "Alimentation")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Produit produit = doc.toObject(Produit.class);

                        // Only show if prixReduction and datePromotion are null
                        if (produit.getPrixReduction() == 0 && (produit.getDatePromotion() == null || produit.getDatePromotion().isEmpty())) {
                            productList.add(produit);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching products", e);
                });
    }
}