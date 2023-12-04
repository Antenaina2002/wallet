package service;

import crudOperation.currencyCRUD;
import models.currencyModel;

import java.util.List;

public class currencyService {
    private final currencyCRUD currencyCRUD;

    public currencyService() {
        this.currencyCRUD = new currencyCRUD();
    }

    public List<currencyModel> getAllCurrency() {
        return currencyCRUD.findAll();
    }

    public currencyModel saveCurrency(currencyModel currency) {
        return currencyCRUD.save(currency);
    }

    public void updateCurrency(currencyModel currency) {
        currencyCRUD.update(currency);
    }
}
