package com.example.lab11_ghannane;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class PanierManager {
    private static PanierManager instance;
    private final Map<String, Produit> produits = new HashMap<>();
    private final Map<String, Integer> quantities = new HashMap<>();

    private PanierAdapter adapter;

    private PanierManager() {
        // private constructor
    }

    public static PanierManager getInstance() {
        if (instance == null) {
            instance = new PanierManager();
        }
        return instance;
    }

    public void setAdapter(PanierAdapter adapter) {
        this.adapter = adapter;
    }

    public PanierAdapter getAdapter() {
        return adapter;
    }

    public void addProduct(Produit produit, int quantity) {
        String id = produit.getId_produit();

        if (quantity > 0) {
            if (produits.containsKey(id)) {
                quantities.put(id, quantities.get(id) + quantity);
            } else {
                produits.put(id, produit);
                quantities.put(id, quantity);
            }
        } else {
            produits.remove(id);
            quantities.remove(id);
        }

        Log.d("PanierManager", "Produits: " + produits.toString());  // Log the produits map
        Log.d("PanierManager", "Quantities: " + quantities.toString());  // Log the quantities map

        if (adapter != null) {
            adapter.refresh(); // This ensures productIds get updated correctly
        }
    }

    public Map<String, Produit> getProduits() {
        return produits;
    }

    public Map<String, Integer> getQuantities() {
        return quantities;
    }

    public void clearPanier() {
        produits.clear();
        quantities.clear();
        if (adapter != null) {
            adapter.refresh();
        }
    }


}