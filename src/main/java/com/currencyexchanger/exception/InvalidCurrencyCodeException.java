package com.currencyexchanger.exception;

public class InvalidCurrencyCodeException extends RuntimeException {

    public InvalidCurrencyCodeException(String message) {
        super("Код валюты " + message + " не существует");
    }
}
