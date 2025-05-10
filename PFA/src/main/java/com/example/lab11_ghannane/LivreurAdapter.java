package com.example.lab11_ghannane;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class LivreurAdapter extends RecyclerView.Adapter<LivreurAdapter.LivreurViewHolder>{
    private List<Livreur> livreurs;
    private manage_livreur context;

    public LivreurAdapter(manage_livreur context) {
        this.context = context;
        livreurs = new ArrayList<>();
    }

    public void setLivreurs(List<Livreur> livreurs) {
        this.livreurs = livreurs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LivreurViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_livreur, parent, false);
        return new LivreurViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LivreurViewHolder holder, int position) {
        Livreur livreur = livreurs.get(position);
        holder.tvFullName.setText(livreur.getFirstName() + " " + livreur.getLastName());
        holder.tvAddress.setText(livreur.getAddress());
        holder.tvCommandCount.setText("Nombre de commandes: " + livreur.getCommandCount());

        holder.btnEditLivreur.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), edit_livreur.class);
            intent.putExtra("livreur_id", livreur.getLivreur_id());  // Pass the ID correctly
            view.getContext().startActivity(intent);
        });

        holder.btnDeleteLivreur.setOnClickListener(view -> deleteLivreur(livreur.getLivreur_id()));
    }

    @Override
    public int getItemCount() {
        return livreurs.size();
    }

    public class LivreurViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullName, tvAddress, tvCommandCount;
        View btnEditLivreur, btnDeleteLivreur;

        public LivreurViewHolder(View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvCommandCount = itemView.findViewById(R.id.tvCommandCount);
            btnEditLivreur = itemView.findViewById(R.id.btnEditLivreur);
            btnDeleteLivreur = itemView.findViewById(R.id.btnDeleteLivreur);
        }
    }

    private void deleteLivreur(String livreur_id) {
        new android.app.AlertDialog.Builder(context)
                .setTitle("Confirmation")
                .setMessage("Êtes-vous sûr de vouloir supprimer ce livreur ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("livreurs")
                            .document(livreur_id)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(context, "Livreur supprimé", Toast.LENGTH_SHORT).show();
                                context.loadLivreurs();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(context, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            );
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}