package com.currencyexchanger.repository;

import com.currencyexchanger.DTO.RequestExchangeRateDTO;
import com.currencyexchanger.controller.exception.NotFoundCurrencyException;
import com.currencyexchanger.controller.exception.NotFoundExchangeRateException;
import com.currencyexchanger.model.CurrencyModel;
import com.currencyexchanger.model.ExchangeRateModel;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class Read extends CRUD{

    private static String SELECT_ALL_CURRENCIES = "SELECT * FROM currencies;";
    private static String SELECT_CURRENCY = "SELECT * FROM currencies WHERE Code = ?";
    private static String SELECT_ALL_EXCHANGE_RATES = "SELECT * FROM exchangerates;";
    private static String SELECT_EXCHANGE_RATE = "SELECT * FROM exchangerates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";

    protected static CurrencyModel getCurrencyCode(String codeCurrencies) throws SQLException, NotFoundCurrencyException {
        CurrencyModel currencyModel = null;

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CURRENCY);
        preparedStatement.setString(1, codeCurrencies);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String code = resultSet.getString("Code");
            String fullname = resultSet.getString("Fullname");
            String sign = resultSet.getString("Sign");
            currencyModel = new CurrencyModel(id, code, fullname, sign);
        }
        if (currencyModel == null) throw new NotFoundCurrencyException();
        return currencyModel;
    }

    protected static CurrencyModel getCurrencyID(int currencyID) throws SQLException, NotFoundCurrencyException {
        CurrencyModel currencyModel = null;

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM currencies WHERE id = ?");
        preparedStatement.setInt(1, currencyID);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String code = resultSet.getString("Code");
            String fullname = resultSet.getString("Fullname");
            String sign = resultSet.getString("Sign");
            currencyModel = new CurrencyModel(id, code, fullname, sign);
        }
        if (currencyModel == null) throw new NotFoundCurrencyException();
        return currencyModel;
    }

    protected static List<CurrencyModel> getCurrencies() throws SQLException {
        List<CurrencyModel> dtoList = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CURRENCIES);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String code = resultSet.getString("Code");
            String fullname = resultSet.getString("Fullname");
            String sign = resultSet.getString("Sign");
            CurrencyModel currencyModel = new CurrencyModel(id, code, fullname, sign);
            dtoList.add(currencyModel);
        }
        return dtoList;
    }

    protected static List<ExchangeRateModel> getExchangeRates() throws SQLException {
        List<ExchangeRateModel> list = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_EXCHANGE_RATES);
        ResultSet resultSet = preparedStatement.executeQuery();

        try {
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int baseCurrencyId = resultSet.getInt(2);
                int targetCurrencyId = resultSet.getInt(3);
                BigDecimal rate = resultSet.getBigDecimal(4);
                CurrencyModel baseCurrencyModel = getCurrencyID(baseCurrencyId);
                CurrencyModel targetCurrencyModel = getCurrencyID(targetCurrencyId);
                ExchangeRateModel exchangeRateModel = new ExchangeRateModel(id, baseCurrencyModel, targetCurrencyModel, rate);
                list.add(exchangeRateModel);
            }
        } catch (NotFoundCurrencyException e) {
            throw new SQLException();
        }
        return list;
    }

    protected static ExchangeRateModel getExchangeRate(RequestExchangeRateDTO requestExchangeRateDTO) throws SQLException, NotFoundExchangeRateException {
        ExchangeRateModel exchangeRateModel = null;
        CurrencyModel baseCurrencyModel = null;
        CurrencyModel targetCurrencyModel = null;

        try {
            baseCurrencyModel = getCurrencyCode(requestExchangeRateDTO.getBaseCurrency());
            targetCurrencyModel = getCurrencyCode(requestExchangeRateDTO.getTargetCurrncy());

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EXCHANGE_RATE);
            preparedStatement.setInt(1, baseCurrencyModel.getId());
            preparedStatement.setInt(2, targetCurrencyModel.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                BigDecimal rate = resultSet.getBigDecimal(4);
                exchangeRateModel = new ExchangeRateModel(id, baseCurrencyModel, targetCurrencyModel, rate);
            }

            if (exchangeRateModel == null) throw new NotFoundCurrencyException();
        } catch (NotFoundCurrencyException e) {
            throw new NotFoundExchangeRateException();
        }
        return exchangeRateModel;
    }
}
