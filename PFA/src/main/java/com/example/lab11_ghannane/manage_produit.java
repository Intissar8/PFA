package com.example.lab11_ghannane;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class manage_produit extends AppCompatActivity implements ProduitAdapter.OnProduitClickListener {

    private RecyclerView recyclerView;
    private ProduitAdapter adapter;
    private List<Produit> produitList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_produit);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.rvProduit);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        produitList = new ArrayList<>();
        adapter = new ProduitAdapter(produitList, this);  // Pass 'this' as the listener
        recyclerView.setAdapter(adapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Load products from Firestore
        loadProducts();

        // Set up FAB to add new product
        findViewById(R.id.fabAddProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddProductActivity to add a new product
                Intent intent = new Intent(manage_produit.this, AddProduct.class);
                startActivity(intent);
            }
        });
    }

    private void loadProducts() {
        db.collection("Produit")  // Your collection name in Firestore
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot snapshot, FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(manage_produit.this, "Error loading products", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        produitList.clear();  // Clear the current list before adding new products
                        for (DocumentSnapshot doc : snapshot) {
                            Produit produit = doc.toObject(Produit.class);  // Map Firestore document to Produit object
                            Log.d("Product", "Category: " + produit.getCategory());  // Log category value to check
                            produit.setId_produit(doc.getId());  // Set the document ID for each product
                            produitList.add(produit);
                        }
                        adapter.notifyDataSetChanged();  // Notify the adapter that the data has changed
                    }
                });
    }

    @Override
    public void onEditClick(Produit produit) {
        // Navigate to EditProductActivity to edit the product details
        Intent intent = new Intent(this, EditProductActivity.class);
        intent.putExtra("id_produit", produit.getId_produit());  // Pass the product ID to the edit screen
        startActivityForResult(intent, 1);  // Start activity for result to refresh after editing
    }

    @Override
    public void onDeleteClick(Produit produit) {
        // Show a confirmation dialog to delete the product
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteProduitFromFirestore(produit);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteProduitFromFirestore(Produit produit) {
        db.collection("Produit").document(produit.getId_produit())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Remove the product from the list and update the RecyclerView
                    produitList.remove(produit);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(manage_produit.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(manage_produit.this, "Failed to delete product", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // After editing a product, reload the products list
            loadProducts();
        }
    }
}