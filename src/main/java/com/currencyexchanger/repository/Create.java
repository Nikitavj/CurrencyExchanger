package com.currencyexchanger.repository;

import com.currencyexchanger.DTO.RequestExchangeRateDTO;
import com.currencyexchanger.controller.exception.DatabaseException;
import com.currencyexchanger.controller.exception.NotFoundCurrencyException;
import com.currencyexchanger.controller.exception.NotFoundExchangeRateException;
import com.currencyexchanger.model.CurrencyModel;
import com.currencyexchanger.model.ExchangeRateModel;
import java.nio.file.FileAlreadyExistsException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

class Create extends CRUD{
    private static final String ADD_CURRENCY = "INSERT INTO currencies (code, fullname, sign) VALUES (?, ?, ?);";
    private static final String ADD_EXCHANGE_RATE = "INSERT INTO exchangerates (BaseCurrencyId, TargetCurrencyId, rate) VALUES (?, ?, ?);";

    protected static Optional<CurrencyModel> addCurrency(String code, String name, String sign) throws NoSuchFieldException, FileAlreadyExistsException, DatabaseException {

        try {
        PreparedStatement preparedStatement = connection.prepareStatement(ADD_CURRENCY);
        preparedStatement.setString(1, code);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, sign);
        preparedStatement.executeUpdate();

        return Read.getCurrencyCode(code);

        } catch (NotFoundCurrencyException e) {
            throw new DatabaseException();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new FileAlreadyExistsException("Запись в БД уже существует");
            }
            if (e.getErrorCode() == 1048) {
                throw new NoSuchFieldException("Отсутсвует параметр в запросе");
            }
        }

        return Optional.empty();
    }

    protected static Optional<ExchangeRateModel> addExchangeRate(RequestExchangeRateDTO requestDTO) throws FileAlreadyExistsException, NotFoundCurrencyException, DatabaseException {

        try {
            CurrencyModel currencyModelBase = Read.getCurrencyCode(requestDTO.getBaseCurrency())
                    .orElseThrow(NotFoundCurrencyException::new);
            CurrencyModel currencyModelTarget = Read.getCurrencyCode(requestDTO.getTargetCurrncy())
                    .orElseThrow(NotFoundCurrencyException::new);

            PreparedStatement preparedStatement = connection.prepareStatement(ADD_EXCHANGE_RATE);
            preparedStatement.setInt(1, currencyModelBase.getId());
            preparedStatement.setInt(2, currencyModelTarget.getId());
            preparedStatement.setBigDecimal(3, requestDTO.getRate());
            preparedStatement.executeUpdate();

            return Read.getExchangeRate(requestDTO);

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) throw new FileAlreadyExistsException("Валютная пара с таким кодом уже существует");
        } catch (NotFoundExchangeRateException e) {
            throw new DatabaseException();
        }
        return Optional.empty();
    }
}
