package com.example.lab11_ghannane;

public class Review {
    private String id_review;
    private float rating;
    private String comment;
    private String timestamp;

    public Review() {
        // Required for Firestore deserialization
    }

    public Review(String id_review, float rating, String comment, String timestamp) {
        this.id_review = id_review;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getId_review() {
        return id_review;
    }

    public void setId_review(String id_review) {
        this.id_review = id_review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

