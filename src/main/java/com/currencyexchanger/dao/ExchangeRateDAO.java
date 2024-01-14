package com.currencyexchanger.dao;

import com.currencyexchanger.model.ExchangeRateModel;
import java.util.Optional;

public interface ExchangeRateDAO extends CrudDAO<ExchangeRateModel> {

    Optional<ExchangeRateModel> readeByCodes(String baseCode, String targetCode);
}