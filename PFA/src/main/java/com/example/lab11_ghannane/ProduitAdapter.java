package com.example.lab11_ghannane;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab11_ghannane.Produit;

import java.util.List;

public class ProduitAdapter extends RecyclerView.Adapter<ProduitAdapter.ProduitViewHolder> {

    private List<Produit> produitList;
    private OnProduitClickListener listener;

    // Constructor to accept both product list and the click listener
    public ProduitAdapter(List<Produit> produitList, OnProduitClickListener listener) {
        this.produitList = produitList;
        this.listener = listener;
    }

    @Override
    public ProduitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produit, parent, false);
        return new ProduitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProduitViewHolder holder, int position) {
        Produit produit = produitList.get(position);

        holder.produitName.setText(produit.getNom());
        holder.produitPrice.setText("Prix: " + produit.getPrix() + " MAD");
        holder.produitQuantity.setText("Quantité: " + produit.getQuantite());
        holder.produitCategory.setText("Catégorie: " + produit.getCategory());
        holder.produitDescription.setText(produit.getDescription());

        // Load the product image using Glide
        Glide.with(holder.itemView.getContext())
                .load(produit.getImageUrl())
                .into(holder.produitImage);

        // Set up click listeners for edit and delete buttons
        holder.itemView.findViewById(R.id.btnEdit).setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(produit); // Trigger edit action
            }
        });

        holder.itemView.findViewById(R.id.btnDelete).setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(produit); // Trigger delete action
            }
        });
    }

    @Override
    public int getItemCount() {
        return produitList.size();
    }

    // ViewHolder class
    public static class ProduitViewHolder extends RecyclerView.ViewHolder {
        TextView produitName, produitPrice, produitQuantity, produitCategory, produitDescription;
        ImageView produitImage;

        public ProduitViewHolder(View itemView) {
            super(itemView);
            produitName = itemView.findViewById(R.id.produitName);
            produitPrice = itemView.findViewById(R.id.produitPrice);
            produitQuantity = itemView.findViewById(R.id.produitQuantity);
            produitCategory = itemView.findViewById(R.id.produitCategory);
            produitDescription = itemView.findViewById(R.id.produitDescription);
            produitImage = itemView.findViewById(R.id.produitImage);
        }
    }

    // Interface to handle edit and delete actions
    public interface OnProduitClickListener {
        void onEditClick(Produit produit); // Method to handle edit action
        void onDeleteClick(Produit produit); // Method to handle delete action
    }
}