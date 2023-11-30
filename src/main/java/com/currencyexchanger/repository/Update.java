package com.currencyexchanger.repository;

import com.currencyexchanger.control.exception.NotFoundExchangeRateException;
import com.currencyexchanger.model.ExchangeRate;
import com.currencyexchanger.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Update {
    private static final String UPDATE_EXCHANGE_RATE = "UPDATE exchangerates SET rate = ? WHERE ID = ?;";

    public static ExchangeRate updateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, double rate) throws SQLException, NotFoundExchangeRateException {
        ExchangeRate exchangeRate = Read.getExchangeRate(baseCurrencyCode, targetCurrencyCode);

        Connection connection = DBUtils.getConnect();
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EXCHANGE_RATE);
        preparedStatement.setDouble(1, rate);
        preparedStatement.setInt(2, exchangeRate.getId());
        preparedStatement.executeUpdate();
        return Read.getExchangeRate(baseCurrencyCode, targetCurrencyCode);
    }
}
