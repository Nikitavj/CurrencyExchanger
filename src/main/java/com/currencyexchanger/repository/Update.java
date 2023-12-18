package com.currencyexchanger.repository;

import com.currencyexchanger.DTO.RequestExchangeRateDTO;
import com.currencyexchanger.controller.exception.DatabaseException;
import com.currencyexchanger.controller.exception.NotFoundExchangeRateException;
import com.currencyexchanger.model.ExchangeRateModel;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

class Update extends CRUD {
    private static final String UPDATE_EXCHANGE_RATE = "UPDATE exchangerates SET rate = ? WHERE ID = ?";

    protected static Optional<ExchangeRateModel> updateExchangeRate(RequestExchangeRateDTO request) throws NotFoundExchangeRateException, DatabaseException {

        try {
            ExchangeRateModel exchangeRateModel = Read.getExchangeRate(request)
                    .orElseThrow(NotFoundExchangeRateException::new);

            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EXCHANGE_RATE);
            preparedStatement.setBigDecimal(1, request.getRate());
            preparedStatement.setInt(2, exchangeRateModel.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException();
        }

        return Read.getExchangeRate(request);
    }
}
