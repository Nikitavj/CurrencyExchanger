package com.currencyexchanger.controller.exception;


public class InvalidCurrencyCodeException extends Exception {
    public InvalidCurrencyCodeException(String message) {
        super(message);
    }
}
