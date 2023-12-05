package com.currencyexchanger.repository;

import com.currencyexchanger.DTO.RequestCurrenciesDTO;
import com.currencyexchanger.DTO.RequestCurrencyDTO;
import com.currencyexchanger.DTO.RequestExchangeRateDTO;
import com.currencyexchanger.controller.exception.NotFoundCurrencyException;
import com.currencyexchanger.controller.exception.NotFoundExchangeRateException;
import com.currencyexchanger.model.CurrencyModel;
import com.currencyexchanger.model.ExchangeRateModel;

import java.nio.file.FileAlreadyExistsException;
import java.sql.SQLException;
import java.util.List;


 public class JDBCRepsitory {

    public static CurrencyModel readCurrency(RequestCurrencyDTO requestCurrencyDTO) throws SQLException, NotFoundCurrencyException {
        return Read.getCurrencyCode(requestCurrencyDTO.getCode());
    }

    public static List<CurrencyModel> readCurrencies () throws SQLException {
        return Read.getCurrencies();
    }

    public static ExchangeRateModel readExchangeRate(RequestExchangeRateDTO requestDTO) throws SQLException, NotFoundExchangeRateException {
        return Read.getExchangeRate(requestDTO);
    }

    public static List<ExchangeRateModel> readExchangeRates() throws SQLException, NotFoundExchangeRateException {
        return Read.getExchangeRates();
    }

    public static CurrencyModel createCurrency(RequestCurrenciesDTO requestDTO) throws FileAlreadyExistsException, NoSuchFieldException {
       return Create.addCurrency(requestDTO.getCode(), requestDTO.getName(), requestDTO.getSign());
    }

    public static ExchangeRateModel createExchangeRate(RequestExchangeRateDTO requestDTO) throws NotFoundCurrencyException, FileAlreadyExistsException {
        return Create.addExchangeRate(requestDTO);
    }

    public static ExchangeRateModel updateExchangeRate(RequestExchangeRateDTO requestDTO) throws SQLException, NotFoundExchangeRateException {
        return Update.updateExchangeRate(requestDTO);
    }


}
