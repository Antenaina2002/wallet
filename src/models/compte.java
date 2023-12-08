package models;

import java.sql.Timestamp;
import java.util.List;

public class compte {
    private int id;
    private String nom;  // Changer de "name" à "nom"
    private double montantSolde;
    private Timestamp dateDerniereMiseAJourSolde;
    private List<transaction> transactions;
    private String devise;
    private String type;

    public compte(int id, String nom, double montantSolde, Timestamp dateDerniereMiseAJourSolde, List<transaction> transactions, String devise, String type) {
        this.id = id;
        this.nom = nom;
        this.montantSolde = montantSolde;
        this.dateDerniereMiseAJourSolde = dateDerniereMiseAJourSolde;
        this.transactions = transactions;
        this.devise = devise;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public double getMontantSolde() {
        return montantSolde;
    }

    public Timestamp getDateDerniereMiseAJourSolde() {
        return dateDerniereMiseAJourSolde;
    }

    public List<transaction> getTransactions() {
        return transactions;
    }

    public String getDevise() {
        return devise;
    }

    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMontantSolde(double montantSolde) {
        this.montantSolde = montantSolde;
    }

    public void setDateDerniereMiseAJourSolde(Timestamp dateDerniereMiseAJourSolde) {
        this.dateDerniereMiseAJourSolde = dateDerniereMiseAJourSolde;
    }

    public void setTransactions(List<transaction> transactions) {
        this.transactions = transactions;
    }
}
