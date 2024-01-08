package com.currencyexchanger.controller;

import com.currencyexchanger.exception.InvalidCurrencyCodeException;
import com.currencyexchanger.exception.InvalidParametersException;
import com.currencyexchanger.exception.InvalidRateCodeException;
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

        if (baseCode.equals(targetCode)) throw new InvalidRateCodeException();

        validateCurrencyCode(baseCode);
        validateCurrencyCode(targetCode);
    }

    public static void validateParameters(String param1, String param2, String param3) throws InvalidParametersException {

        if (param1 == null || param1.equals("")) throw new InvalidParametersException();
        if (param2 == null || param2.equals("")) throw new InvalidParametersException();
        if (param3 == null || param3.equals("")) throw new InvalidParametersException();
    }

    public static void validateParameter(String param1) throws InvalidParametersException {

        if (param1 == null || param1.equals("")) throw new InvalidParametersException();
    }

    public static void validateExchangeParameters(String from, String to, String stringAmount) throws InvalidParametersException, InvalidCurrencyCodeException {

        if (from == null || from.equals(""))throw new InvalidParametersException("Base Currency");
        if (to == null || to.equals(""))throw new InvalidParametersException("Target Currency");
        if (stringAmount == null || stringAmount.equals(""))throw new InvalidParametersException("Amount");
        validateCurrencyCode(from);
        validateCurrencyCode(to);
    }
}
