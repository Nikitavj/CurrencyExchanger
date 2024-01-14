package com.currencyexchanger.dao;

import com.currencyexchanger.model.CurrencyModel;

import java.util.Optional;

public interface CurrencyDAO extends CrudDAO<CurrencyModel> {

    Optional<CurrencyModel> readeByCode(String code);
}
