package com.example.lab11_ghannane;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab11_ghannane.Produit;

import java.util.List;

public class ProduitAdapter extends RecyclerView.Adapter<ProduitAdapter.ProduitViewHolder> {

    private List<Produit> produitList;
    private OnProduitClickListener listener;

    public interface OnProduitClickListener {
        void onEditClick(Produit produit);
        void onDeleteClick(Produit produit);
    }

    public ProduitAdapter(List<Produit> produitList, OnProduitClickListener listener) {
        this.produitList = produitList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProduitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_produit, parent, false);
        return new ProduitViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProduitViewHolder holder, int position) {
        try {
            Produit produit = produitList.get(position);

            holder.produitName.setText(produit.getNom());
            holder.produitPrice.setText("Prix: " + produit.getPrix() + " MAD");
            holder.produitQuantity.setText("Quantité: " + produit.getQuantite());
            holder.produitCategory.setText("Catégorie: " + produit.getCategory());
            holder.produitDescription.setText(produit.getDescription());

            // Load image using Glide
            Glide.with(holder.itemView.getContext())
                    .load(produit.getImageUrl())
                    .into(holder.produitImage);

            // Handle promotion display
            if (produit.getPrixReduction() > 0 && produit.getPrixReduction() > 0) {
                double prixFinal = produit.getPrixFinal();
                double percentage = produit.getReductionPercentage();

                holder.promotionInfo.setVisibility(View.VISIBLE);
                holder.promotionInfo.setText("Promotion: -" + String.format("%.0f", percentage) + "% → " + prixFinal + " MAD");

                if (produit.getDatePromotion() != null && !produit.getDatePromotion().isEmpty()) {
                    holder.promotionDate.setVisibility(View.VISIBLE);
                    holder.promotionDate.setText("Période: " + produit.getDatePromotion());
                } else {
                    holder.promotionDate.setVisibility(View.GONE);
                }
            } else {
                holder.promotionInfo.setVisibility(View.GONE);
                holder.promotionDate.setVisibility(View.GONE);
            }

            // Edit and delete actions
            holder.itemView.findViewById(R.id.btnEdit).setOnClickListener(v -> {
                if (listener != null) listener.onEditClick(produit);
            });

            holder.itemView.findViewById(R.id.btnDelete).setOnClickListener(v -> {
                if (listener != null) listener.onDeleteClick(produit);
            });

        } catch (Exception e) {
            Log.e("ProduitAdapter", "Error binding product: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return produitList.size();
    }

    public static class ProduitViewHolder extends RecyclerView.ViewHolder {
        TextView produitName, produitPrice, produitQuantity, produitCategory, produitDescription, promotionInfo, promotionDate;
        ImageView produitImage;

        public ProduitViewHolder(View itemView) {
            super(itemView);
            produitName = itemView.findViewById(R.id.produitName);
            produitPrice = itemView.findViewById(R.id.produitPrice);
            produitQuantity = itemView.findViewById(R.id.produitQuantity);
            produitCategory = itemView.findViewById(R.id.produitCategory);
            produitDescription = itemView.findViewById(R.id.produitDescription);
            produitImage = itemView.findViewById(R.id.produitImage);
            promotionInfo = itemView.findViewById(R.id.promotionInfo);
            promotionDate = itemView.findViewById(R.id.promotionDate);
        }
    }
}