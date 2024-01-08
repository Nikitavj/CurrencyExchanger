package com.currencyexchanger.dao;

import com.currencyexchanger.DTO.ReqDTO;
import com.currencyexchanger.DTO.ReqExchangeRateDTO;
import com.currencyexchanger.model.ExchangeModel;
import com.currencyexchanger.model.ExchangeRateModel;

import java.sql.SQLException;
import java.util.Optional;

public interface ExchangeRateDAO extends CrudDAO<ExchangeRateModel, ReqExchangeRateDTO> {

    public Optional<ExchangeRateModel> readeByCodes(ReqExchangeRateDTO req) throws SQLException;
}