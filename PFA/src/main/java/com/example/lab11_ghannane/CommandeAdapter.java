package com.example.lab11_ghannane;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class CommandeAdapter extends RecyclerView.Adapter<CommandeAdapter.CommandeViewHolder> {
    private List<Commande> commandes;

    public CommandeAdapter(List<Commande> commandes) {
        this.commandes = commandes;
    }

    @NonNull
    @Override
    public CommandeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_commande, parent, false);
        return new CommandeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommandeViewHolder holder, int position) {
        Commande commande = commandes.get(position);
        holder.addressText.setText("Address: " + commande.getAddress());
        holder.dateCommandeText.setText("Date: " + commande.getFormattedDateCommande()); // Use the formatted date
        holder.usernameText.setText("Username: " + commande.getUsername());
        holder.totalText.setText("Total: " + commande.getTotal());
        holder.statusText.setText("Status: " + commande.getStatus());
    }

    @Override
    public int getItemCount() {
        return commandes.size();
    }

    public static class CommandeViewHolder extends RecyclerView.ViewHolder {

        TextView addressText, dateCommandeText, usernameText, totalText, statusText;

        public CommandeViewHolder(View itemView) {
            super(itemView);
            addressText = itemView.findViewById(R.id.addressText);
            dateCommandeText = itemView.findViewById(R.id.dateCommandeText);
            usernameText = itemView.findViewById(R.id.usernameText);
            totalText = itemView.findViewById(R.id.totalText);
            statusText = itemView.findViewById(R.id.statusText);
        }
    }
}

