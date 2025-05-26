package com.example.lab11_ghannane;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;


public class delivery_status extends AppCompatActivity {
    private ImageView iconStep1, iconStep2, iconStep3;
    private TextView tvCurrentStatus, tvUserName, tvAddress, tvDate, tvTotal;
    private Button btnReview;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_status);

        // Initialize views
        iconStep1 = findViewById(R.id.iconStep1);
        iconStep2 = findViewById(R.id.iconStep2);
        iconStep3 = findViewById(R.id.iconStep3);
        tvCurrentStatus = findViewById(R.id.tvCurrentStatus);
        tvUserName = findViewById(R.id.tvUserName);
        tvAddress = findViewById(R.id.tvAddress);
        tvDate = findViewById(R.id.tvDate);
        tvTotal = findViewById(R.id.tvTotal);
        btnReview = findViewById(R.id.btnReview);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        String userId = auth.getCurrentUser().getUid();
        fetchLatestCommande(userId);

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(delivery_status.this, AddReview.class);
                startActivity(intent);
            }
        });
    }

    private void fetchLatestCommande(String userId) {
        CollectionReference commandesRef = db.collection("Commande");

        commandesRef.whereEqualTo("userId", userId)
                .orderBy("dateCommande", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot commandeDoc = queryDocumentSnapshots.getDocuments().get(0);

                        String status = commandeDoc.getString("status");
                        Timestamp timestamp = commandeDoc.getTimestamp("dateCommande");
                        String dateCommande = timestamp != null ? new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(timestamp.toDate()) : "N/A";
                        Double total = commandeDoc.getDouble("total");

                        tvCurrentStatus.setText("Current Status: " + status);
                        tvDate.setText("Date: " + dateCommande);
                        tvTotal.setText("Total: " + (total != null ? String.format("%.2f", total) + " MAD" : "N/A"));

                        updateStatusIcons(status);

                        // Now fetch user info from "users" collection
                        fetchUserInfo(userId);
                    } else {
                        Toast.makeText(this, "No commandes found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void fetchUserInfo(String userId) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("username");
                        String address = documentSnapshot.getString("address");

                        tvUserName.setText("Client: " + (name != null ? name : "N/A"));
                        tvAddress.setText("Address: " + (address != null ? address : "N/A"));
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error loading user info: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateStatusIcons(String status) {
        // Reset all icons to gray
        iconStep1.setColorFilter(getResources().getColor(R.color.gray));
        iconStep2.setColorFilter(getResources().getColor(R.color.gray));
        iconStep3.setColorFilter(getResources().getColor(R.color.gray));

        if (status.equals("Order Received")) {
            iconStep1.setColorFilter(getResources().getColor(R.color.blue));
        } else if (status.equals("In Transit")) {
            iconStep1.setColorFilter(getResources().getColor(R.color.blue));
            iconStep2.setColorFilter(getResources().getColor(R.color.blue));
        } else if (status.equals("Delivered")) {
            iconStep1.setColorFilter(getResources().getColor(R.color.blue));
            iconStep2.setColorFilter(getResources().getColor(R.color.blue));
            iconStep3.setColorFilter(getResources().getColor(R.color.blue));
        }
    }
}