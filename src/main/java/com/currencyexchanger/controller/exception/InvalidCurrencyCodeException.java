package com.currencyexchanger.controller.exception;


public class InvalidCurrencyCodeException extends Exception {

    public InvalidCurrencyCodeException(String message) {
        super("Код валюты " + message + " не существует");
    }

    public InvalidCurrencyCodeException() {
        super("Код валюты не существует");
    }
}
