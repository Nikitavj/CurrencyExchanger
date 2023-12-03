package com.currencyexchanger.controller.exception;

public class InvalidCurrencyRatesParameters extends Exception{
    public InvalidCurrencyRatesParameters(String message) {
        super(message);
    }
}
