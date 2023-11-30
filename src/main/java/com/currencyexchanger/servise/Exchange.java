package com.currencyexchanger.servise;
import com.currencyexchanger.control.exception.NotFoundCurrencyException;
import com.currencyexchanger.control.exception.NotFoundExchangeRateException;
import com.currencyexchanger.model.ExchangeRate;
import com.currencyexchanger.repository.Read;

import java.sql.SQLException;

public class Exchange {

    public static ExchangeDTO getExchange(String baseCurrencyCode, String targetCurrencyCode, double amount) throws SQLException {
        ExchangeDTO exchangeDTO = null;

        exchangeDTO = getDirectExchangeRate(baseCurrencyCode, targetCurrencyCode, amount);
        if (exchangeDTO != null) return exchangeDTO;

        exchangeDTO = getReverseExchangeRate(targetCurrencyCode, baseCurrencyCode, amount);
        if(exchangeDTO != null) return exchangeDTO;

        exchangeDTO = getCalculatedExchangeRate(baseCurrencyCode, targetCurrencyCode, amount);
        if(exchangeDTO != null) return exchangeDTO;

        return exchangeDTO;
    }

    private static ExchangeDTO getDirectExchangeRate(String baseCurrencyCode, String targetCurrencyCode, double amount) throws SQLException {
        ExchangeRate exchangeRate = null;
        ExchangeDTO exchangeDTO = new ExchangeDTO();
        try {
            exchangeRate = Read.getExchangeRate(baseCurrencyCode, targetCurrencyCode);
            exchangeDTO.setBaseCurrency(Read.getCurrency(baseCurrencyCode));
            exchangeDTO.setTargetCurrency(Read.getCurrency(targetCurrencyCode));
            exchangeDTO.setRate(exchangeRate.getRate());
            exchangeDTO.setAmount(amount);
            exchangeDTO.setConvertedAmount(amount * exchangeRate.getRate());
        } catch (NotFoundExchangeRateException e) {
            return null;
        } catch (NotFoundCurrencyException e) {
            throw new SQLException();
        }
        return exchangeDTO;
    }

    private static ExchangeDTO getReverseExchangeRate(String targetCurrencyCode, String baseCurrencyCode, double amount) throws SQLException {
        ExchangeRate exchangeRate = null;
        ExchangeDTO exchangeDTO = new ExchangeDTO();
        try {
            exchangeRate = Read.getExchangeRate(targetCurrencyCode, baseCurrencyCode);
            exchangeDTO.setBaseCurrency(Read.getCurrency(baseCurrencyCode));
            exchangeDTO.setTargetCurrency(Read.getCurrency(targetCurrencyCode));
            exchangeDTO.setRate(1 / exchangeRate.getRate());
            exchangeDTO.setAmount(amount);
            exchangeDTO.setConvertedAmount(amount * (1 / exchangeRate.getRate()));
        } catch (NotFoundExchangeRateException e) {
            return null;
        } catch (NotFoundCurrencyException e) {
            throw new SQLException();
        }
        return exchangeDTO;
    }

    private static ExchangeDTO getCalculatedExchangeRate(String baseCurrencyCode, String targetCurrencyCode, double amount) throws SQLException {
        ExchangeDTO exchangeDTO = new ExchangeDTO();
        ExchangeRate baseExchangeRate = null;
        try {
            baseExchangeRate = Read.getExchangeRate("USD", baseCurrencyCode);
            ExchangeRate targetExchangeRate = Read.getExchangeRate("USD", targetCurrencyCode);
            double rate = targetExchangeRate.getRate() / baseExchangeRate.getRate();
            double convertedAmount = amount * rate;
            exchangeDTO.setBaseCurrency(Read.getCurrency(baseCurrencyCode));
            exchangeDTO.setTargetCurrency(Read.getCurrency(targetCurrencyCode));
            exchangeDTO.setRate(rate);
            exchangeDTO.setAmount(amount);
            exchangeDTO.setConvertedAmount(convertedAmount);
        } catch (NotFoundExchangeRateException e) {
            return null;
        } catch (NotFoundCurrencyException e) {
            throw new SQLException();
        }
        return exchangeDTO;
    }
}
