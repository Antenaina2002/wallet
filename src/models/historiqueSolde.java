package models;

import java.sql.Timestamp;

public class historiqueSolde {
    private int id;
    private int idCompte;
    private double ancienSolde;
    private double nouveauSolde;
    private Timestamp dateHistorique;

    public historiqueSolde(int id, int idCompte, double ancienSolde, double nouveauSolde, Timestamp dateHistorique) {
        this.id = id;
        this.idCompte = idCompte;
        this.ancienSolde = ancienSolde;
        this.nouveauSolde = nouveauSolde;
        this.dateHistorique = dateHistorique;
    }

    public int getId() {
        return id;
    }

    public int getIdCompte() {
        return idCompte;
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

    public void setIdCompte(int idCompte) {
        this.idCompte = idCompte;
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
