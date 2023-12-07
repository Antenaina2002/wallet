package DAO;

import connection.dbConnection;
import models.accountModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class accountDAO implements crudOperator<accountModel> {

    private Connection connection;

    public accountDAO() {
        this.connection = dbConnection.get_connection();
    }

    @Override
    public List<accountModel> findAll() {
        String sql = "SELECT * FROM account;";
        List<accountModel> resultList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                resultList.add(new accountModel(
                        result.getInt("id"),
                        result.getString("user"),
                        result.getString("RIB"),
                        result.getInt("wallet")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    @Override
    public List<accountModel> saveAll(List<accountModel> toSave) {
        String sql = "INSERT INTO account (user, RIB, wallet) VALUES (?, ?, ?);";
        try (PreparedStatement prepared = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (accountModel account : toSave) {
                prepared.setString(1, account.getUser());
                prepared.setString(2, account.getRIB());
                prepared.setInt(3, account.getWallet());
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
    public accountModel save(accountModel toSave) {
        if (toSave.getId() != 0) {
            String sql = "UPDATE account SET user = ?, RIB = ?, wallet = ? WHERE id = ?;";
            try (PreparedStatement prepared = connection.prepareStatement(sql)) {
                prepared.setString(1, toSave.getUser());
                prepared.setString(2, toSave.getRIB());
                prepared.setInt(3, toSave.getWallet());
                prepared.setInt(4, toSave.getId());
                prepared.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String sql = "INSERT INTO account (user, RIB, wallet) VALUES (?, ?, ?);";
            try (PreparedStatement prepared = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                prepared.setString(1, toSave.getUser());
                prepared.setString(2, toSave.getRIB());
                prepared.setInt(3, toSave.getWallet());
                prepared.executeUpdate();
                ResultSet generatedKeys = prepared.getGeneratedKeys();
                if (generatedKeys.next()) {
                    toSave.setId(generatedKeys.getInt(1));
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
