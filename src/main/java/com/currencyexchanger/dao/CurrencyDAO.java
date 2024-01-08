package com.currencyexchanger.dao;

import com.currencyexchanger.DTO.ReqCurrencyDTO;
import com.currencyexchanger.DTO.ReqDTO;
import com.currencyexchanger.model.CurrencyModel;

import java.sql.SQLException;
import java.util.Optional;

public interface CurrencyDAO extends CrudDAO<CurrencyModel, ReqCurrencyDTO>{

    public Optional<CurrencyModel> readeByCode(ReqCurrencyDTO req) throws SQLException;

}
