package com.example.lab11_ghannane;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class manage_livreur extends AppCompatActivity {

    private RecyclerView rvLivreur;
    private FloatingActionButton fabAddLivreur;
    private LivreurAdapter livreurAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_livreur);

        rvLivreur = findViewById(R.id.rvLivreur);
        fabAddLivreur = findViewById(R.id.fabAddLivreur);
        db = FirebaseFirestore.getInstance();

        rvLivreur.setLayoutManager(new LinearLayoutManager(this));
        livreurAdapter = new LivreurAdapter(this);
        rvLivreur.setAdapter(livreurAdapter);

        loadLivreurs();

        fabAddLivreur.setOnClickListener(view -> startActivity(new Intent(manage_livreur.this, add_livreur.class)));
    }

    public void loadLivreurs() {
        db.collection("livreurs")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Livreur> livreurs = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Livreur livreur = document.toObject(Livreur.class);
                            livreur.setLivreur_id(document.getId()); // Set the ID
                            livreurs.add(livreur);
                        }
                        livreurAdapter.setLivreurs(livreurs);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLivreurs(); // Refresh list every time the activity becomes visible
    }
}