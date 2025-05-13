package com.example.lab11_ghannane;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.List;

public class ProductFoodAdapter extends RecyclerView.Adapter<ProductFoodAdapter.ProductViewHolder>  {
    private List<Produit> productList;
    private Context context;

    public ProductFoodAdapter(Context context, List<Produit> productList) {
        this.context = context;
        this.productList = productList;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productDescription;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productDescription = itemView.findViewById(R.id.productDescription);
        }
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_product_food, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Produit product = productList.get(position);
        holder.productName.setText(product.getNom());

        // Set the description
        holder.productDescription.setText(product.getDescription());

        // If there is a reduction
        if (product.getPrixReduction() > 0 && product.getPrixReduction() > 0) {
            double finalPrice = product.getPrixFinal();
            double percentage = product.getReductionPercentage();
            holder.productPrice.setText("Promo: " + finalPrice + " DH (-" + (int)percentage + "%)");
        } else {
            holder.productPrice.setText("Prix: " + product.getPrix() + " DH");
        }

        Glide.with(context)
                .load(product.getImageUrl())
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}