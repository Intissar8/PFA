package com.example.lab11_ghannane;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PanierAdapter extends RecyclerView.Adapter<PanierAdapter.ViewHolder> {
    private Map<String, Produit> produits;
    private Map<String, Integer> quantities;
    private List<String> productIds;

    public PanierAdapter(Map<String, Produit> produits, Map<String, Integer> quantities) {
        this.produits = produits;
        this.quantities = quantities;
        this.productIds = new ArrayList<>(produits.keySet());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_panier, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String id = productIds.get(position);
        Produit p = produits.get(id);
        int qte = quantities.get(id);

        holder.nameText.setText(p.getNom());
        holder.quantityPriceText.setText(qte + " x " + p.getPrix() + " DH");
        double itemTotal = qte * p.getPrix();
        holder.itemTotalText.setText("= " + itemTotal + " DH");

        Glide.with(holder.itemView.getContext())
                .load(p.getImageUrl())
                .into(holder.productImage);

        // Delete button logic
        holder.deleteButton.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                String productId = productIds.get(currentPosition);
                produits.remove(productId);
                quantities.remove(productId);
                productIds.remove(currentPosition);
                notifyItemRemoved(currentPosition);
                notifyItemRangeChanged(currentPosition, getItemCount());
                refresh(); // This will update productIds and call notifyDataSetChanged
                if (PanierManager.getInstance().getAdapter() != null) {
                    PanierManager.getInstance().getAdapter().refresh();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productIds.size();
    }

    public void refresh() {
        this.productIds = new ArrayList<>(produits.keySet());
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, quantityPriceText, itemTotalText;
        ImageView productImage, deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            quantityPriceText = itemView.findViewById(R.id.quantityPriceText);
            itemTotalText = itemView.findViewById(R.id.itemTotalText);
            productImage = itemView.findViewById(R.id.productImage);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}