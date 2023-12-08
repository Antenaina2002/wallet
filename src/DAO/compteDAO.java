package DAO;

import connection.dbConnection;
import models.compte;
import models.transactionModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class compteDAO implements crudOperator<compte> {
    private Connection connection;

    public compteDAO() {
        this.connection = dbConnection.get_connection();
    }

    @Override
    public List<compte> findAll() {
        String sql = "SELECT * FROM compte;";
        List<compte> resultList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                int compteId = result.getInt("id");
                List<transactionModel> transactions = findTransactionsByCompteId(compteId);

                resultList.add(new compte(
                        compteId,
                        result.getString("nom"),
                        result.getDouble("solde_montant"),
                        result.getString("solde_date_maj"),
                        transactions,
                        result.getString("devise"),
                        result.getString("type")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    @Override
    public List<compte> saveAll(List<compte> toSave) {
        String sql = "INSERT INTO compte (nom, solde_montant, solde_date_maj, devise, type) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement prepared = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (models.compte compte : toSave) {
                prepared.setString(1, compte.getNom());
                prepared.setDouble(2, compte.getMontantSolde());
                prepared.setString(3, compte.getDateDerniereMiseAJourSolde());
                prepared.setString(4, compte.getDevise());
                prepared.setString(5, compte.getType());
                prepared.addBatch();
            }

            prepared.executeBatch();

            ResultSet generatedKeys = prepared.getGeneratedKeys();
            int i = 0;
            while (generatedKeys.next()) {
                toSave.get(i).setId(generatedKeys.getInt(1));
                i++;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toSave;
    }

    @Override
    public compte save(compte toSave) {
        if (toSave.getId() != 0) {
            updateCompte(toSave);
        } else {
            insertCompte(toSave);
        }
        return toSave;
    }

    private void insertCompte(compte compte) {
        String sql = "INSERT INTO compte (nom, solde_montant, solde_date_maj, devise, type) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement prepared = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            prepared.setString(1, compte.getNom());
            prepared.setDouble(2, compte.getMontantSolde());
            prepared.setString(3, compte.getDateDerniereMiseAJourSolde());
            prepared.setString(4, compte.getDevise());
            prepared.setString(5, compte.getType());

            prepared.executeUpdate();

            ResultSet generatedKeys = prepared.getGeneratedKeys();
            if (generatedKeys.next()) {
                compte.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Save failed, no generated ID.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateCompte(compte compte) {
        String sql = "UPDATE compte SET nom = ?, solde_montant = ?, solde_date_maj = ?, devise = ?, type = ? WHERE id = ?;";
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.setString(1, compte.getNom());
            prepared.setDouble(2, compte.getMontantSolde());
            prepared.setString(3, compte.getDateDerniereMiseAJourSolde());
            prepared.setString(4, compte.getDevise());
            prepared.setString(5, compte.getType());
            prepared.setInt(6, compte.getId());

            prepared.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<transactionModel> findTransactionsByCompteId(int compteId) {
        // Implémenter la récupération des transactions associées à un compte
        // Utilisez une requête SQL pour récupérer les transactions pour un compte donné
        return new ArrayList<>();  // Remplacez par la liste réelle des transactions
    }
}
