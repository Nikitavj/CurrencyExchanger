package com.currencyexchanger.control;

import com.currencyexchanger.control.error.ErrorMessages;
import com.currencyexchanger.control.exception.InvalidCurrencyCodeException;
import com.currencyexchanger.control.exception.InvalidCurrencyRatesParameters;
import com.currencyexchanger.control.exception.InvalidExchangeParameters;
import com.currencyexchanger.control.exception.InvalidRateCodeException;

public class ValidateRequest {

    public static boolean validateCurrencyCode(String code) throws InvalidCurrencyCodeException {

        for (int i = 0; i < code.length(); i++) {
            char ch = Character.toUpperCase(code.charAt(i));
            if (code.length() != 3 ||
                Character.isDigit(ch) ||
                ch < 'A' || 'Z' < ch) throw new InvalidCurrencyCodeException("Валюта отсутствует в адресе. Пример: USD");
        }
        return true;
    }

    public static boolean validateRateCode(String rateCode) throws InvalidRateCodeException {

        if (rateCode.length() < 6) throw new InvalidRateCodeException(ErrorMessages.EXCHANGE_RATE_GET_400.getMessage());
        String baseCode = rateCode.substring(0, 3);
        String targetCode = rateCode.substring(3, 6);

        try {
            if (validateCurrencyCode(baseCode) & validateCurrencyCode(targetCode)) return true;
        } catch (InvalidCurrencyCodeException e) {
            throw new InvalidRateCodeException(ErrorMessages.EXCHANGE_RATE_GET_400.getMessage());
        }
        return false;
    }

    public static boolean validateRatesParameter(String baseCurrencyCode, String targetCurrencyCode, String stringRate) throws InvalidCurrencyRatesParameters {

        if (baseCurrencyCode == null || baseCurrencyCode.equals("") ||
            targetCurrencyCode == null || targetCurrencyCode.equals("") ||
            stringRate == null || stringRate.equals("")) {
            throw new InvalidCurrencyRatesParameters(ErrorMessages.EXCHANGE_RATES_POST_400.getMessage());
        }
        return true;
    }

    public static boolean validateExchangeParameters(String from, String to, String stringAmount) throws InvalidExchangeParameters {

        if (from == null || from.equals("") ||
            to == null || to.equals("") ||
            stringAmount == null || stringAmount.equals("")) {
            throw new InvalidExchangeParameters(ErrorMessages.CURRENCIES_POST_400.getMessage());
        }
        return true;
    }
}
