package com.example.lab11_ghannane;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductelecAdapter extends RecyclerView.Adapter<ProductFoodAdapter.ProductViewHolder>  {
    private List<Produit> productList;
    private Context context;
    private Map<String, Integer> quantities = new HashMap<>(); // id_produit -> quantity

    public ProductelecAdapter(Context context, List<Produit> productList) {
        this.context = context;
        this.productList = productList;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productDescription, quantityText, finalPrice, promotionDate, discountPercentage;
        Button btnAdd, btnRemove;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.finalPrice);
            productDescription = itemView.findViewById(R.id.productDescription);
            quantityText = itemView.findViewById(R.id.quantityText);
            finalPrice = itemView.findViewById(R.id.finalPrice);
            promotionDate = itemView.findViewById(R.id.promotionDate);
            discountPercentage = itemView.findViewById(R.id.discountPercentage);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }


    @Override
    public ProductFoodAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_product_food, parent, false);
        return new ProductFoodAdapter.ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductFoodAdapter.ProductViewHolder holder, int position) {
        Produit product = productList.get(position);
        holder.productName.setText(product.getNom());
        holder.productDescription.setText(product.getDescription());

        // Display the original price
        holder.productPrice.setText("Prix: " + product.getPrix() + " DH");

        // Set the final price based on the promotion
        double finalPrice = product.getPrixFinal();
        holder.finalPrice.setText("Prix: " + finalPrice + " DH");

        // Display promotion details if present
        if (product.getPrixReduction() > 0) {
            holder.promotionDate.setVisibility(View.VISIBLE);
            holder.discountPercentage.setVisibility(View.VISIBLE);
            holder.promotionDate.setText("Promo: " + product.getDatePromotion());

            // Format the discount percentage to 2 decimal places
            String formattedDiscount = String.format("%.2f", product.getReductionPercentage());
            holder.discountPercentage.setText("Discount: " + formattedDiscount + "%");
        } else {
            holder.promotionDate.setVisibility(View.GONE);
            holder.discountPercentage.setVisibility(View.GONE);
        }

        // Set image
        Glide.with(context)
                .load(product.getImageUrl())
                .into(holder.productImage);

        // Initialize quantity if not set
        if (!quantities.containsKey(product.getId_produit())) {
            quantities.put(product.getId_produit(), 0);
        }

        int quantity = quantities.get(product.getId_produit());
        holder.quantityText.setText(String.valueOf(quantity));

        holder.btnAdd.setOnClickListener(v -> {
            int current = quantities.get(product.getId_produit());
            current++;
            quantities.put(product.getId_produit(), current);
            holder.quantityText.setText(String.valueOf(current));
            PanierManager.getInstance().addProduct(product, current);
        });

        holder.btnRemove.setOnClickListener(v -> {
            int current = quantities.get(product.getId_produit());
            if (current > 0) {
                current--;
                quantities.put(product.getId_produit(), current);
                holder.quantityText.setText(String.valueOf(current));
                PanierManager.getInstance().addProduct(product, current);
            }
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

}