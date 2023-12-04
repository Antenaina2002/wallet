package service;

import crudOperation.AccountCRUD;
import models.accountModel;

import java.util.List;

public class accountService {
    private final AccountCRUD accountCRUD;

    public accountService() {
        this.accountCRUD = new AccountCRUD();
    }

    public List<accountModel> getAllAccounts() {
        return accountCRUD.findAll();
    }

    public accountModel saveAccount(accountModel account) {
        return accountCRUD.save(account);
    }

    public void updateAccount(accountModel account) {
        accountCRUD.update(account);
    }
}
