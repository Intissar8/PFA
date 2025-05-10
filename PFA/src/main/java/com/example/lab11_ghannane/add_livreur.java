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

public class add_livreur extends AppCompatActivity {

    private EditText etFirstName, etLastName, etAddress, etCommandCount;
    private Button btnAddLivreur;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_livreur);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAddress = findViewById(R.id.etAddress);
        etCommandCount = findViewById(R.id.etCommandCount);
        btnAddLivreur = findViewById(R.id.btnAddLivreur);
        db = FirebaseFirestore.getInstance();

        btnAddLivreur.setOnClickListener(view -> {
            addLivreur();
        });
    }

    private void addLivreur() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        int commandCount = Integer.parseInt(etCommandCount.getText().toString().trim());

        Livreur livreur = new Livreur(firstName, lastName, address, commandCount);

        // Add livreur to Firestore
        db.collection("livreurs")
                .add(livreur)
                .addOnSuccessListener(documentReference -> {
                    String id = documentReference.getId();
                    db.collection("livreurs").document(id).update("livreur_id", id);
                    Toast.makeText(add_livreur.this, "Livreur ajouté", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(add_livreur.this, "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}