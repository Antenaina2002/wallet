package DAO;

import connection.dbConnection;
import models.historiqueSolde;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class historiqueSoldeDAO implements crudOperator<historiqueSolde> {

    private Connection connection;

    public historiqueSoldeDAO() {
        this.connection = dbConnection.get_connection();
    }

    @Override
    public List<historiqueSolde> findAll() {
        String sql = "SELECT * FROM historique_solde;";
        List<historiqueSolde> resultList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                resultList.add(new historiqueSolde(
                        result.getInt("id"),
                        result.getInt("compte"),
                        result.getDouble("ancien_solde"),
                        result.getDouble("nouveau_solde"),
                        result.getTimestamp("date_historique")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    @Override
    public List<historiqueSolde> saveAll(List<historiqueSolde> toSave) {
        String sql = "INSERT INTO historique_solde (compte, ancien_solde, nouveau_solde, date_historique) VALUES (?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (historiqueSolde historiqueSolde : toSave) {
                preparedStatement.setInt(1, historiqueSolde.getIdCompte());
                preparedStatement.setDouble(2, historiqueSolde.getAncienSolde());
                preparedStatement.setDouble(3, historiqueSolde.getNouveauSolde());
                preparedStatement.setTimestamp(4, historiqueSolde.getDateHistorique());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toSave;
    }

    @Override
    public historiqueSolde save(historiqueSolde historiqueSolde) {
        String sql = "INSERT INTO historique_solde (compte, ancien_solde, nouveau_solde, date_historique) VALUES (?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, historiqueSolde.getIdCompte());
            preparedStatement.setDouble(2, historiqueSolde.getAncienSolde());
            preparedStatement.setDouble(3, historiqueSolde.getNouveauSolde());
            preparedStatement.setTimestamp(4, historiqueSolde.getDateHistorique());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                historiqueSolde.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Save failed, no generated ID.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return historiqueSolde;
    }
    public static List<historiqueSolde> findByCompte(int compteId) throws SQLException {
        Connection connection = dbConnection.get_connection();

        String sql = "SELECT id, id_compte, ancien_solde, nouveau_solde, date_historique FROM historique_solde WHERE id_compte = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, compteId);

        ResultSet resultSet = preparedStatement.executeQuery();

        List<historiqueSolde> historiquesSolde = new ArrayList<>();
        while (resultSet.next()) {
            historiquesSolde.add(new historiqueSolde(
                    resultSet.getInt("id"),
                    resultSet.getInt("id_compte"),
                    resultSet.getDouble("ancien_solde"),
                    resultSet.getDouble("nouveau_solde"),
                    resultSet.getTimestamp("date_historique")
            ));
        }

        connection.close();
        return historiquesSolde;
    }

    public double getSolde(int compteId, Timestamp dateHeure) throws SQLException {
        List<historiqueSolde> historiquesSolde = historiqueSoldeDAO.findByCompte(compteId);
        // Initialise le solde à l'ancien solde du premier historique trouvé
        // (ou 0 si aucun historique n'est trouvé)
        double solde = historiquesSolde.isEmpty() ? 0 : historiquesSolde.get(0).getAncienSolde();
        // Parcours les historiques de solde et accumule les nouveaux soldes
        for (historiqueSolde historique : historiquesSolde) {
            if (historique.getDateHistorique().compareTo(dateHeure) <= 0) {
                solde += historique.getNouveauSolde();
            }
        }

        return solde;
    }


}
