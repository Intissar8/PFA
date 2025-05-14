package com.example.lab11_ghannane;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

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
        holder.dateCommandeText.setText("Date: " + commande.getFormattedDateCommande());
        holder.usernameText.setText("Username: " + commande.getUsername());
        holder.totalText.setText("Total: " + commande.getTotal());

        // Set up the Spinner for status
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                holder.itemView.getContext(),
                R.array.status_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.statusSpinner.setAdapter(adapter);

        // Set the current status in the Spinner
        int statusPosition = adapter.getPosition(commande.getStatus());
        holder.statusSpinner.setSelection(statusPosition);

        // Handle status selection
        holder.statusSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String newStatus = parentView.getItemAtPosition(position).toString();
                // Update status in Firestore
                updateCommandeStatus(commande, newStatus, holder.itemView.getContext());
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    @Override
    public int getItemCount() {
        return commandes.size();
    }

    private void updateCommandeStatus(Commande commande, String newStatus, android.content.Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Make sure that we are updating the correct document
        String documentId = commande.getDocumentId();  // Assuming username is used as the document ID
        Log.d("CommandeAdapter", "Updating status for document ID: " + documentId);

        db.collection("Commande").document(documentId) // Document path
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    // Use the context from the ViewHolder to show a Toast
                    Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT).show();
                    Log.d("CommandeAdapter", "Status successfully updated to: " + newStatus);
                })
                .addOnFailureListener(e -> {
                    // Use the context from the ViewHolder to show a Toast
                    Toast.makeText(context, "Error updating status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("CommandeAdapter", "Error updating status", e);  // Log error details for debugging
                });
    }

    public static class CommandeViewHolder extends RecyclerView.ViewHolder {

        TextView addressText, dateCommandeText, usernameText, totalText;
        Spinner statusSpinner;

        public CommandeViewHolder(View itemView) {
            super(itemView);
            addressText = itemView.findViewById(R.id.addressText);
            dateCommandeText = itemView.findViewById(R.id.dateCommandeText);
            usernameText = itemView.findViewById(R.id.usernameText);
            totalText = itemView.findViewById(R.id.totalText);
            statusSpinner = itemView.findViewById(R.id.statusSpinner);
        }
    }
}
