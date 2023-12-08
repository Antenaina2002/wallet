package DAO;

import connection.dbConnection;
import models.devise;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class deviseDAO implements crudOperator<devise> {

    private Connection connection;

    public deviseDAO() {
        this.connection = dbConnection.get_connection();
    }

    @Override
    public List<devise> findAll() {
        String sql = "SELECT * FROM devise;";
        List<devise> resultList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                resultList.add(new devise(
                        result.getInt("id"),
                        result.getString("nom"),
                        result.getString("code")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    @Override
    public List<devise> saveAll(List<devise> toSave) {
        String sql = "INSERT INTO devise (nom, code) VALUES (?, ?);";
        try (PreparedStatement prepared = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (devise currency : toSave) {
                prepared.setString(1, currency.getName());
                prepared.setString(2, currency.getCode());
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
    public devise save(devise toSave) {
        if (toSave.getId() != 0) {
            updateCurrency(toSave);
        } else {
            insertCurrency(toSave);
        }
        return toSave;
    }

    private void insertCurrency(devise currency) {
        String sql = "INSERT INTO devise (nom, code) VALUES (?, ?);";
        try (PreparedStatement prepared = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            prepared.setString(1, currency.getName());
            prepared.setString(2, currency.getCode());
            prepared.executeUpdate();

            ResultSet generatedKeys = prepared.getGeneratedKeys();
            if (generatedKeys.next()) {
                currency.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Save failed, no generated ID.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateCurrency(devise currency) {
        String sql = "UPDATE devise SET nom = ?, code = ? WHERE id = ?;";
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.setString(1, currency.getName());
            prepared.setString(2, currency.getCode());
            prepared.setInt(3, currency.getId());
            prepared.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
