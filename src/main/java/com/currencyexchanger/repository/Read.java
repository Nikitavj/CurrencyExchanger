package com.currencyexchanger.repository;

import com.currencyexchanger.DTO.RequestExchangeRateDTO;
import com.currencyexchanger.controller.exception.DatabaseException;
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
import java.util.Optional;

import static java.math.BigDecimal.ROUND_FLOOR;

class Read extends CRUD{

    private static String SELECT_ALL_CURRENCIES = "SELECT * FROM currencies;";
    private static String SELECT_CURRENCY = "SELECT * FROM currencies WHERE Code = ?";
    private static String SELECT_ALL_EXCHANGE_RATES = "SELECT * FROM exchangerates;";
    private static String SELECT_EXCHANGE_RATE = "SELECT * FROM exchangerates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";

    protected static Optional<CurrencyModel> getCurrencyCode(String codeCurrencies) throws NotFoundCurrencyException, DatabaseException {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CURRENCY);
            preparedStatement.setString(1, codeCurrencies);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String code = resultSet.getString("Code");
                String fullname = resultSet.getString("Fullname");
                String sign = resultSet.getString("Sign");

                CurrencyModel currencyModel = new CurrencyModel(id, fullname, code, sign);
                return Optional.of(currencyModel);
            }

            throw new NotFoundCurrencyException();

        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    protected static Optional<CurrencyModel> getCurrencyID(int currencyID) throws NotFoundCurrencyException, DatabaseException {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM currencies WHERE id = ?");
            preparedStatement.setInt(1, currencyID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String code = resultSet.getString("Code");
                String fullname = resultSet.getString("Fullname");
                String sign = resultSet.getString("Sign");

                CurrencyModel currencyModel = new CurrencyModel(id, fullname, code, sign);
                return Optional.of(currencyModel);
            }

            throw new NotFoundCurrencyException();

        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    protected static List<CurrencyModel> getCurrencies() throws DatabaseException {
        List<CurrencyModel> dtoList = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CURRENCIES);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String code = resultSet.getString("Code");
                String fullname = resultSet.getString("Fullname");
                String sign = resultSet.getString("Sign");

                CurrencyModel currencyModel = new CurrencyModel(id, fullname, code, sign);
                dtoList.add(currencyModel);
            }

        } catch (SQLException e) {
            throw new DatabaseException();
        }
        return dtoList;
    }

    protected static List<ExchangeRateModel> getExchangeRates() throws DatabaseException {
        List<ExchangeRateModel> list = new ArrayList<>();

        try {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_EXCHANGE_RATES);
        ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int baseCurrencyId = resultSet.getInt(2);
                int targetCurrencyId = resultSet.getInt(3);
                BigDecimal rate = resultSet.getBigDecimal(4).setScale(6, ROUND_FLOOR);

                CurrencyModel baseCurrencyModel = getCurrencyID(baseCurrencyId)
                        .orElseThrow(NotFoundCurrencyException::new);
                CurrencyModel targetCurrencyModel = getCurrencyID(targetCurrencyId)
                        .orElseThrow(NotFoundCurrencyException::new);

                ExchangeRateModel exchangeRateModel = new ExchangeRateModel(id, baseCurrencyModel, targetCurrencyModel, rate);
                list.add(exchangeRateModel);
            }

        } catch (NotFoundCurrencyException | SQLException e) {
            throw new DatabaseException();
        }
        return list;
    }

    protected static Optional<ExchangeRateModel> getExchangeRate(RequestExchangeRateDTO requestExchangeRateDTO) throws NotFoundExchangeRateException, DatabaseException {

        try {
            CurrencyModel baseCurrencyModel = getCurrencyCode(requestExchangeRateDTO.getBaseCurrency())
                    .orElseThrow(NotFoundCurrencyException::new);
            CurrencyModel targetCurrencyModel = getCurrencyCode(requestExchangeRateDTO.getTargetCurrncy())
                    .orElseThrow(NotFoundCurrencyException::new);

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EXCHANGE_RATE);
            preparedStatement.setInt(1, baseCurrencyModel.getId());
            preparedStatement.setInt(2, targetCurrencyModel.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                BigDecimal rate = resultSet.getBigDecimal(4).setScale(6, ROUND_FLOOR);;

                ExchangeRateModel exchangeRateModel = new ExchangeRateModel(id, baseCurrencyModel, targetCurrencyModel, rate);
                return Optional.of(exchangeRateModel);
            }

        } catch (NotFoundCurrencyException e) {
            throw new NotFoundExchangeRateException();

        } catch (SQLException e) {
            throw new DatabaseException();
        }
        return Optional.empty();
    }
}
