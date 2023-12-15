package models;

import java.sql.Timestamp;
import java.util.List;

public class historiqueSolde {
    private int id;
    private String nom;  // Changer de "name" à "nom"
    private double montantSolde;
    private Timestamp dateDerniereMiseAJourSolde;
    private List<transaction> transactions;
    private String devise;
    private String type;
}
