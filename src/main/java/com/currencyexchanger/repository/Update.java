package com.currencyexchanger.repository;

import com.currencyexchanger.DTO.RequestExchangeRateDTO;
import com.currencyexchanger.controller.exception.NotFoundExchangeRateException;
import com.currencyexchanger.model.ExchangeRateModel;

import java.sql.PreparedStatement;
import java.sql.SQLException;

class Update extends CRUD {
    private static final String UPDATE_EXCHANGE_RATE = "UPDATE exchangerates SET rate = ? WHERE ID = ?;";

    protected static ExchangeRateModel updateExchangeRate(RequestExchangeRateDTO request) throws SQLException, NotFoundExchangeRateException {
        ExchangeRateModel exchangeRateModel = Read.getExchangeRate(request);
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EXCHANGE_RATE);
        preparedStatement.setBigDecimal(1, request.getRate());
        preparedStatement.setInt(2, exchangeRateModel.getId());
        preparedStatement.executeUpdate();
        return Read.getExchangeRate(request);
    }
}
