package com.example.lab11_ghannane;

public class Livreur {
    private String livreur_id;
    private String firstName;
    private String lastName;
    private String address;
    private int commandCount;

    public Livreur() {}

    public Livreur(String firstName, String lastName, String address, int commandCount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.commandCount = commandCount;
    }

    public String getLivreur_id() {
        return livreur_id;
    }

    public void setLivreur_id(String livreur_id) {
        this.livreur_id = livreur_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCommandCount() {
        return commandCount;
    }

    public void setCommandCount(int commandCount) {
        this.commandCount = commandCount;
    }
}