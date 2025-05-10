package com.example.lab11_ghannane;

public class Produit {
    private String id_produit;
    private String nom;
    private String description;
    private String category;
    private double prix;
    private int quantite;
    private String imageUrl;
    private Double prixReduction; // To store the discount price, if any
    private String datePromotion; // To store the promotion period

    // Empty constructor required for Firestore
    public Produit() {
    }

    // Constructor to create a new product
    public Produit(String id_produit, String nom, double prix, int quantite, String description,
                   String category, String imageUrl, String datePromotion, double prixReduction) {
        this.id_produit = id_produit;
        this.nom = nom;
        this.prix = prix;
        this.quantite = quantite;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.datePromotion = datePromotion;
        this.prixReduction = prixReduction;
    }

    // Getters and Setters
    public String getId_produit() {
        return id_produit;
    }

    public void setId_produit(String id_produit) {
        this.id_produit = id_produit;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getDatePromotion() {
        return datePromotion;
    }

    public void setDatePromotion(String datePromotion) {
        this.datePromotion = datePromotion;
    }

    public double getPrixReduction() {
        return prixReduction;
    }

    public void setPrixReduction(double prixReduction) {
        this.prixReduction = prixReduction;
    }

    // Method to calculate the reduced price
    public double getPrixFinal() {
        if (prixReduction > 0) {
            return prix - prixReduction;
        }
        return prix;
    }

    // Method to get the discount percentage
    public double getReductionPercentage() {
        if (prix > 0 && prixReduction > 0) {
            return (prixReduction / prix) * 100;
        }
        return 0;
    }
}
