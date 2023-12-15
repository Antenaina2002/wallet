package models;

import java.sql.Timestamp;

public class historiqueSolde {
    private int id;
    private int compte; // Identifiant du compte lié
    private double ancienSolde; // Solde avant la modification
    private double nouveauSolde; // Solde après la modification
    private Timestamp dateHistorique; // Date de la modification

    public historiqueSolde(int id, int compte, double ancienSolde, double nouveauSolde, Timestamp dateHistorique) {
        this.id = id;
        this.compte = compte;
        this.ancienSolde = ancienSolde;
        this.nouveauSolde = nouveauSolde;
        this.dateHistorique = dateHistorique;
    }

    public int getId() {
        return id;
    }

    public int getCompte() {
        return compte;
    }

    public double getAncienSolde() {
        return ancienSolde;
    }

    public double getNouveauSolde() {
        return nouveauSolde;
    }

    public Timestamp getDateHistorique() {
        return dateHistorique;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCompte(int compte) {
        this.compte = compte;
    }

    public void setAncienSolde(double ancienSolde) {
        this.ancienSolde = ancienSolde;
    }

    public void setNouveauSolde(double nouveauSolde) {
        this.nouveauSolde = nouveauSolde;
    }

    public void setDateHistorique(Timestamp dateHistorique) {
        this.dateHistorique = dateHistorique;
    }

}
