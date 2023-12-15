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

}
