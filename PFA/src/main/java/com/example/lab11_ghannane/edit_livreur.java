package com.example.lab11_ghannane;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class edit_livreur extends AppCompatActivity {

    private EditText editFirstName, editLastName, editAddress, editCommandCount;
    private Button btnUpdateLivreur;
    private FirebaseFirestore db;
    private String livreur_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_livreur);

        db = FirebaseFirestore.getInstance();

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editAddress = findViewById(R.id.editAddress);
        editCommandCount = findViewById(R.id.editCommandCount);
        btnUpdateLivreur = findViewById(R.id.btnUpdateLivreur);

        livreur_id = getIntent().getStringExtra("livreur_id");

        loadLivreurData();

        btnUpdateLivreur.setOnClickListener(v -> updateLivreur());
    }

    private void loadLivreurData() {
        db.collection("livreurs").document(livreur_id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Livreur livreur = documentSnapshot.toObject(Livreur.class);
                        if (livreur != null) {
                            livreur.setLivreur_id(livreur_id); // Make sure the id is set
                            editFirstName.setText(livreur.getFirstName());
                            editLastName.setText(livreur.getLastName());
                            editAddress.setText(livreur.getAddress());
                            editCommandCount.setText(String.valueOf(livreur.getCommandCount()));
                        }
                    } else {
                        Toast.makeText(edit_livreur.this, "Livreur not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(edit_livreur.this, "Error loading data", Toast.LENGTH_SHORT).show());
    }

    private void updateLivreur() {
        String updatedFirstName = editFirstName.getText().toString().trim();
        String updatedLastName = editLastName.getText().toString().trim();
        String updatedAddress = editAddress.getText().toString().trim();
        int updatedCommandCount = Integer.parseInt(editCommandCount.getText().toString().trim());

        Livreur updatedLivreur = new Livreur(updatedFirstName, updatedLastName, updatedAddress, updatedCommandCount);
        updatedLivreur.setLivreur_id(livreur_id); // Set the id here as well

        db.collection("livreurs").document(livreur_id)
                .set(updatedLivreur)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(edit_livreur.this, "Livreur updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(edit_livreur.this, "Error updating livreur", Toast.LENGTH_SHORT).show());
    }
}