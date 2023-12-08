package com.currencyexchanger.repository;

import com.currencyexchanger.DTO.RequestCurrenciesDTO;
import com.currencyexchanger.DTO.RequestCurrencyDTO;
import com.currencyexchanger.DTO.RequestExchangeRateDTO;
import com.currencyexchanger.controller.exception.DatabaseException;
import com.currencyexchanger.controller.exception.NotFoundCurrencyException;
import com.currencyexchanger.controller.exception.NotFoundExchangeRateException;
import com.currencyexchanger.model.CurrencyModel;
import com.currencyexchanger.model.ExchangeRateModel;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.Optional;


public class JDBCRepsitory {

    public static Optional<CurrencyModel> readCurrency(RequestCurrencyDTO requestCurrencyDTO) throws NotFoundCurrencyException, DatabaseException {
        return Read.getCurrencyCode(requestCurrencyDTO.getCode());
    }

    public static List<CurrencyModel> readCurrencies () throws DatabaseException {
        return Read.getCurrencies();
    }

    public static Optional<ExchangeRateModel> readExchangeRate(RequestExchangeRateDTO requestDTO) throws NotFoundExchangeRateException, DatabaseException {
        return Read.getExchangeRate(requestDTO);
    }

    public static List<ExchangeRateModel> readExchangeRates() throws DatabaseException {
        return Read.getExchangeRates();
    }

    public static Optional<CurrencyModel> createCurrency(RequestCurrenciesDTO requestDTO) throws FileAlreadyExistsException, NoSuchFieldException, DatabaseException {
       return Create.addCurrency(requestDTO.getCode(), requestDTO.getName(), requestDTO.getSign());
    }

    public static Optional<ExchangeRateModel> createExchangeRate(RequestExchangeRateDTO requestDTO) throws NotFoundCurrencyException, FileAlreadyExistsException, DatabaseException {
        return Create.addExchangeRate(requestDTO);
    }

    public static Optional<ExchangeRateModel> updateExchangeRate(RequestExchangeRateDTO requestDTO) throws NotFoundExchangeRateException, DatabaseException {
        return Update.updateExchangeRate(requestDTO);
    }
}
