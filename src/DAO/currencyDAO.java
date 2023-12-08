package DAO;

import connection.dbConnection;
import models.currencyModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class currencyDAO implements crudOperator<currencyModel> {

    private Connection connection;

    public currencyDAO() {
        this.connection = dbConnection.get_connection();
    }

    @Override
    public List<currencyModel> findAll() {
        String sql = "SELECT * FROM currency;";
        List<currencyModel> resultList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                resultList.add(new currencyModel(
                        result.getString("name")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    @Override
    public List<currencyModel> saveAll(List<currencyModel> toSave) {
        String sql = "INSERT INTO currency (name) VALUES (?);";
        try (PreparedStatement prepared = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (currencyModel currency : toSave) {
                prepared.setString(1, currency.getName());
                prepared.addBatch();
            }
            prepared.executeBatch();
            ResultSet generatedKeys = prepared.getGeneratedKeys();
            while (generatedKeys.next()) {
                // Assurez-vous d'avoir la logique appropriée pour votre modèle
                toSave.forEach(currency -> {
                    try {
                        currency.setName(generatedKeys.getString(1));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toSave;
    }


    @Override
    public currencyModel save(currencyModel toSave) {
        if (toSave.getName() != null && !toSave.getName().isEmpty()) {
            String sql = "UPDATE currency SET name = ? WHERE name = ?;";
            try (PreparedStatement prepared = connection.prepareStatement(sql)) {
                prepared.setString(1, toSave.getName());
                prepared.setString(2, toSave.getName());
                prepared.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String sql = "INSERT INTO currency (name) VALUES (?);";
            try (PreparedStatement prepared = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                prepared.setString(1, toSave.getName());
                prepared.executeUpdate();
                ResultSet generatedKeys = prepared.getGeneratedKeys();
                if (generatedKeys.next()) {
                } else {
                    throw new SQLException("Save failed, no generated ID.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return toSave;
    }
}
