package DAO;

import connection.dbConnection;
import models.compte;
import models.transaction;

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
                List<transaction> transactions = findTransactionsByCompteId(compteId);

                resultList.add(new compte(
                        compteId,
                        result.getString("nom"),
                        result.getDouble("solde_montant"),
                        result.getTimestamp("solde_date_maj"),
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
            for (compte compte : toSave) {
                prepared.setString(1, compte.getNom());
                prepared.setDouble(2, compte.getMontantSolde());
                prepared.setTimestamp(3, compte.getDateDerniereMiseAJourSolde());
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

    public compte performTransaction(transaction transaction) {
        int compteId = transaction.getAccountId();
        compte compte = findCompteById(compteId);

        if (compte != null) {
            double montantTransaction = transaction.getAmount();
            String typeTransaction = transaction.getType();

            if ("Banque".equals(compte.getType())) {
                updateSolde(compte, montantTransaction, typeTransaction);
            } else {
                if ("Débit".equals(typeTransaction) && compte.getMontantSolde() < montantTransaction) {
                    throw new RuntimeException("Solde insuffisant pour effectuer la transaction.");
                }

                updateSolde(compte, montantTransaction, typeTransaction);
            }

            compte.setDateDerniereMiseAJourSolde(Timestamp.valueOf(transaction.getDate().toString()));

            List<transaction> transactions = compte.getTransactions();
            transactions.add(transaction);
            compte.setTransactions(transactions);

            updateCompte(compte);

            return compte;
        } else {
            throw new RuntimeException("Le compte avec l'ID " + compteId + " n'existe pas.");
        }
    }

    private void updateCompte(compte compte) {
        String sql = "UPDATE compte SET nom = ?, solde_montant = ?, solde_date_maj = ?, devise = ?, type = ? WHERE id = ?;";
        try (PreparedStatement prepared = connection.prepareStatement(sql)) {
            prepared.setString(1, compte.getNom());
            prepared.setDouble(2, compte.getMontantSolde());
            prepared.setTimestamp(3, compte.getDateDerniereMiseAJourSolde());
            prepared.setString(4, compte.getDevise());
            prepared.setString(5, compte.getType());
            prepared.setInt(6, compte.getId());

            prepared.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertCompte(compte compte) {
        String sql = "INSERT INTO compte (nom, solde_montant, solde_date_maj, devise, type) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement prepared = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            prepared.setString(1, compte.getNom());
            prepared.setDouble(2, compte.getMontantSolde());
            prepared.setTimestamp(3, compte.getDateDerniereMiseAJourSolde());
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

    private void updateSolde(compte compte, double montantTransaction, String typeTransaction) {
        if ("Débit".equals(typeTransaction)) {
            compte.setMontantSolde(compte.getMontantSolde() - montantTransaction);
        } else if ("Crédit".equals(typeTransaction)) {
            compte.setMontantSolde(compte.getMontantSolde() + montantTransaction);
        }
    }

    private compte findCompteById(int compteId) {
        String sql = "SELECT * FROM compte WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, compteId);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    int foundCompteId = result.getInt("id");
                    List<transaction> transactions = findTransactionsByCompteId(foundCompteId);

                    return new compte(
                            foundCompteId,
                            result.getString("nom"),
                            result.getDouble("solde_montant"),
                            result.getTimestamp("solde_date_maj"),
                            transactions,
                            result.getString("devise"),
                            result.getString("type")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private List<transaction> findTransactionsByCompteId(int compteId) {
        String sql = "SELECT * FROM transaction WHERE compte_id = ?;";
        List<transaction> resultList = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, compteId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    resultList.add(new transaction(
                            result.getInt("id"),
                            result.getString("label"),
                            result.getDouble("montant"),
                            result.getTimestamp("date_transaction"),
                            result.getString("type_transaction"),
                            result.getInt("compte_id")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultList;
    }

}
