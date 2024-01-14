package com.currencyexchanger.controller;

import com.currencyexchanger.exception.InvalidCurrencyCodeException;
import com.currencyexchanger.exception.InvalidParametersException;
import com.currencyexchanger.exception.InvalidRateCodeException;

import java.util.*;
import java.util.stream.Collectors;

public class Validator {
    private static final int POSITION_BASE_CODE = 3;
    private static final int POSITION_TARGET_CODE = 6;
    private static final int LENGTH_RATE_CODE = 6;
    private static Set<String> codes;

    static {
        Set<Currency> codesCurrensy = Currency.getAvailableCurrencies();
        codes = codesCurrensy.stream().
                map(Currency::getCurrencyCode).
                collect(Collectors.toSet());
    }

    public static void validateCurrencyCode(String codeCurrency) {

        Optional<String> code = codes.stream().filter(
                s -> s.equals(codeCurrency)).findFirst();
        code.orElseThrow(() -> new InvalidCurrencyCodeException(codeCurrency));
    }

    public static void validateRateCode(String rateCode) {

        if (rateCode.length() != LENGTH_RATE_CODE) {
            throw new InvalidRateCodeException();
        }
        String baseCode = rateCode.substring(0, POSITION_BASE_CODE);
        String targetCode = rateCode.substring(
                POSITION_BASE_CODE,
                POSITION_TARGET_CODE
        );

        if (baseCode.equals(targetCode)) {
            throw new InvalidRateCodeException();
        }

        validateCurrencyCode(baseCode);
        validateCurrencyCode(targetCode);
    }

    public static void validateParameters(List<String> parameters) {

        for (String param : parameters) {
            if (param == null || param.equals("")) {
                throw new InvalidParametersException();
            }
        }
    }

    public static void validateParameter(String param1) {

        if (param1 == null || param1.equals("")) {
            throw new InvalidParametersException();
        }
    }

    public static void validateExchangeParameters(
            String from,
            String to,
            String stringAmount) {

        if (from == null || from.equals("")) {
            throw new InvalidParametersException("Base Currency");
        }
        if (to == null || to.equals("")) {
            throw new InvalidParametersException("Target Currency");
        }
        if (stringAmount == null || stringAmount.equals("")) {
            throw new InvalidParametersException("Amount");
        }

        validateCurrencyCode(from);
        validateCurrencyCode(to);
    }
}
