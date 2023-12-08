package com.currencyexchanger.controller;

import com.currencyexchanger.controller.exception.InvalidCurrencyCodeException;
import com.currencyexchanger.controller.exception.InvalidParametersException;
import com.currencyexchanger.controller.exception.InvalidRateCodeException;

import java.util.Currency;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Validator {
    private static Set<String> codes;

    public static void validateCurrencyCode(String codeCurrency) throws InvalidCurrencyCodeException {

        if (codes == null) {
            Set<Currency> codesCurrensy = Currency.getAvailableCurrencies();
            codes = codesCurrensy.stream().map(Currency::getCurrencyCode).collect(Collectors.toSet());
        }
        Optional<String> code = codes.stream().filter(s -> s.equals(codeCurrency)).findFirst();
        code.orElseThrow(() -> new InvalidCurrencyCodeException(codeCurrency));
    }

    public static void validateRateCode(String rateCode) throws InvalidRateCodeException, InvalidCurrencyCodeException {

        if (rateCode.length() != 6) throw new InvalidRateCodeException();
        String baseCode = rateCode.substring(0, 3);
        String targetCode = rateCode.substring(3, 6);

        validateCurrencyCode(baseCode);
        validateCurrencyCode(targetCode);
    }

    public static void validateRatesParameter(String baseCurrencyCode, String targetCurrencyCode, String stringRate) throws InvalidParametersException {

        if (baseCurrencyCode == null || baseCurrencyCode.equals("")) throw new InvalidParametersException("baseCurrencyCode");
        if (targetCurrencyCode == null || targetCurrencyCode.equals("")) throw new InvalidParametersException("targetCurrencyCode");
        if (stringRate == null || stringRate.equals("")) throw new InvalidParametersException("stringRate");
    }

    public static void validateExchangeParameters(String from, String to, String stringAmount) throws InvalidParametersException, InvalidCurrencyCodeException {

        if (from == null || from.equals(""))throw new InvalidParametersException("from");
        if (to == null || to.equals(""))throw new InvalidParametersException("to");
        if (stringAmount == null || stringAmount.equals(""))throw new InvalidParametersException("stringAmount");
        validateCurrencyCode(from);
        validateCurrencyCode(to);
    }
}
