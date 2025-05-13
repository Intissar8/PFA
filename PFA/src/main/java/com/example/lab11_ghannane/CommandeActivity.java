package com.example.lab11_ghannane;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class CommandeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView commandesRecyclerView;
    private CommandeAdapter commandeAdapter;
    private List<Commande> commandeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);

        db = FirebaseFirestore.getInstance();
        commandesRecyclerView = findViewById(R.id.commandesRecyclerView);
        commandeList = new ArrayList<>();
        commandeAdapter = new CommandeAdapter(commandeList);
        commandesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commandesRecyclerView.setAdapter(commandeAdapter);

        // Fetch all Commandes data
        fetchAllCommandes();
    }

    private void fetchAllCommandes() {
        db.collection("Commande")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Commande commande = document.toObject(Commande.class);
                            commandeList.add(commande);
                        }
                        commandeAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(CommandeActivity.this, "No commandes found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CommandeActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                });
    }
}