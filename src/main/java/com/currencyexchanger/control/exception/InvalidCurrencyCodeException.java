package com.currencyexchanger.control.exception;

public class InvalidCurrencyCodeException extends Exception{
    public InvalidCurrencyCodeException(String message) {
        super(message);
    }
}
