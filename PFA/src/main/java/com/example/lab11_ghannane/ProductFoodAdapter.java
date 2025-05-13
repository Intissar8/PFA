package com.example.lab11_ghannane;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProductFoodAdapter extends RecyclerView.Adapter<ProductFoodAdapter.ProductViewHolder>  {
    private List<Produit> productList;
    private Context context;
    private Map<String, Integer> quantities = new HashMap<>(); // id_produit -> quantity

    public ProductFoodAdapter(Context context, List<Produit> productList) {
        this.context = context;
        this.productList = productList;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productDescription, quantityText;
        Button btnAdd, btnRemove;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productDescription = itemView.findViewById(R.id.productDescription);
            quantityText = itemView.findViewById(R.id.quantityText);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnRemove = itemView.findViewById(R.id.btnRemove);
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
        holder.productDescription.setText(product.getDescription());
        holder.productPrice.setText("Prix: " + product.getPrix() + " DH");

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