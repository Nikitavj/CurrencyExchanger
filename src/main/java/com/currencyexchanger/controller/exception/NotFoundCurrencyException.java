package com.currencyexchanger.controller.exception;

public class NotFoundCurrencyException extends Exception{

    public NotFoundCurrencyException(String message) {
        super(message);
    }
    public NotFoundCurrencyException() {}
}
