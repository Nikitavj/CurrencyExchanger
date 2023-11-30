package com.currencyexchanger.repository;

import com.currencyexchanger.control.error.ErrorMessages;
import com.currencyexchanger.control.exception.NotFoundCurrencyException;
import com.currencyexchanger.control.exception.NotFoundExchangeRateException;
import com.currencyexchanger.model.Currency;
import com.currencyexchanger.model.ExchangeRate;
import com.currencyexchanger.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Read {

    private static String SELECT_ALL_CURRENCIES = "SELECT * FROM currencies;";
    private static String SELECT_CURRENCY = "SELECT * FROM currencies WHERE Code = ?";
    private static String SELECT_ALL_EXCHANGE_RATES = "SELECT * FROM exchangerates;";
    private static String SELECT_EXCHANGE_RATE = "SELECT * FROM exchangerates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";

    public static Currency getCurrency(String codeCurrencies) throws SQLException, NotFoundCurrencyException {
        Currency currency = null;

        Connection connection = DBUtils.getConnect();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CURRENCY);
        preparedStatement.setString(1, codeCurrencies);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String code = resultSet.getString("Code");
            String fullname = resultSet.getString("Fullname");
            String sign = resultSet.getString("Sign");
            currency = new Currency(id, code, fullname, sign);
        }
        if (currency == null) throw new NotFoundCurrencyException(ErrorMessages.CURRENCY_GET_404.getMessage());
        return currency;
    }

    public static Currency getCurrencyID(int currencyID) throws SQLException, NotFoundCurrencyException {
        Currency currency = null;

        Connection connection = DBUtils.getConnect();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM currencies WHERE id = ?");
        preparedStatement.setInt(1, currencyID);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String code = resultSet.getString("Code");
            String fullname = resultSet.getString("Fullname");
            String sign = resultSet.getString("Sign");
            currency = new Currency(id, code, fullname, sign);
        }
        if (currency == null) throw new NotFoundCurrencyException(ErrorMessages.CURRENCY_GET_404.getMessage());
        return currency;
    }

    public static List<Currency> getCurrencies() throws SQLException {
        List<Currency> dtoList = new ArrayList<>();

        Connection connection = DBUtils.getConnect();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CURRENCIES);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String code = resultSet.getString("Code");
            String fullname = resultSet.getString("Fullname");
            String sign = resultSet.getString("Sign");
            Currency currency = new Currency(id, code, fullname, sign);
            dtoList.add(currency);
        }
        return dtoList;
    }

    public static List<ExchangeRate> getExchangeRates() throws SQLException {
        List<ExchangeRate> list = new ArrayList<>();

        Connection connection = DBUtils.getConnect();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_EXCHANGE_RATES);
        ResultSet resultSet = preparedStatement.executeQuery();

        try {
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int baseCurrencyId = resultSet.getInt(2);
                int targetCurrencyId = resultSet.getInt(3);
                double rate = resultSet.getDouble(4);
                Currency baseCurrency = getCurrencyID(baseCurrencyId);
                Currency targetCurrency = getCurrencyID(targetCurrencyId);
                ExchangeRate exchangeRate = new ExchangeRate(id, baseCurrency, targetCurrency, rate);
                list.add(exchangeRate);
            }
        } catch (NotFoundCurrencyException e) {
            throw new SQLException();
        }
        return list;
    }

    public static ExchangeRate getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) throws SQLException, NotFoundExchangeRateException {
        ExchangeRate exchangeRate = null;
        Currency baseCurrency = null;
        Currency targetCurrency = null;

        try {
            baseCurrency = getCurrency(baseCurrencyCode);
            targetCurrency = getCurrency(targetCurrencyCode);
        } catch (NotFoundCurrencyException e) {
            throw new NotFoundExchangeRateException(ErrorMessages.EXCHANGE_RATE_GET_404.getMessage());
        }

        Connection connection = DBUtils.getConnect();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EXCHANGE_RATE);
        preparedStatement.setInt(1, baseCurrency.getId());
        preparedStatement.setInt(2, targetCurrency.getId());
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            double rate = resultSet.getDouble(4);
            exchangeRate = new ExchangeRate(id, baseCurrency, targetCurrency, rate);
        }
        if (exchangeRate == null) throw new NotFoundExchangeRateException(ErrorMessages.EXCHANGE_RATE_GET_404.getMessage());
        return exchangeRate;
    }
}
