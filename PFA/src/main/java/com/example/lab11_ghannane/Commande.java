package com.example.lab11_ghannane;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Commande {
    private String documentId;
    private String address;
    private Timestamp dateCommande;
    private String username;
    private double total;
    private String status;

    public Commande() {
        // Required empty constructor for Firestore
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    public Commande(String address, Timestamp dateCommande, String username, double total, String status) {
        this.address = address;
        this.dateCommande = dateCommande;
        this.username = username;
        this.total = total;
        this.status = status;
    }

    // Getter methods
    public String getAddress() {
        return address;
    }

    public Timestamp getDateCommande() {
        return dateCommande;
    }

    public String getUsername() {
        return username;
    }

    public double getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    // You can add a method to convert the Timestamp to String if needed
    public String getFormattedDateCommande() {
        if (dateCommande != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
            Date date = dateCommande.toDate();
            return sdf.format(date); // Format the date as a readable string
        }
        return "No Date";
    }
}