package com.currencyexchanger.repository;

import com.currencyexchanger.control.exception.NotFoundCurrencyException;
import com.currencyexchanger.control.exception.NotFoundExchangeRateException;
import com.currencyexchanger.model.Currency;
import com.currencyexchanger.model.ExchangeRate;
import com.currencyexchanger.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Create {
    private static final String ADD_CURRENCY = "INSERT INTO currencies (code, fullname, sign) VALUES (?, ?, ?);";
    private static final String ADD_EXCHANGE_RATE = "INSERT INTO exchangerates (BaseCurrencyId, TargetCurrencyId, rate) VALUES (?, ?, ?);";

    public static Currency addCurrency(String code, String name, String sign) throws SQLException {
        Currency currency = null;

        Connection connection = DBUtils.getConnect();
        PreparedStatement preparedStatement = connection.prepareStatement(ADD_CURRENCY);
        preparedStatement.setString(1, code);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, sign);
        preparedStatement.executeUpdate();

        try {
            currency = Read.getCurrency(code);
        } catch (NotFoundCurrencyException e) {
            throw new SQLException();
        }
        return currency;
    }

    public static ExchangeRate addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, double rate) throws SQLException {
        ExchangeRate exchangeRate = null;

        try {
            Currency currencyBase = Read.getCurrency(baseCurrencyCode);
            Currency currencyTarget = Read.getCurrency(targetCurrencyCode);
            Connection connection = DBUtils.getConnect();
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_EXCHANGE_RATE);
            preparedStatement.setInt(1, currencyBase.getId());
            preparedStatement.setInt(2, currencyTarget.getId());
            preparedStatement.setDouble(3, rate);
            preparedStatement.executeUpdate();
            exchangeRate = Read.getExchangeRate(baseCurrencyCode, targetCurrencyCode);
        } catch (NotFoundExchangeRateException e) {
            throw new SQLException();
        } catch (NotFoundCurrencyException e) {
            throw new SQLException();
        }

        return exchangeRate;
    }
}
