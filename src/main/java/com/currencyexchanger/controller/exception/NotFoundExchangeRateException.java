package com.currencyexchanger.controller.exception;

public class NotFoundExchangeRateException extends Exception{
    public NotFoundExchangeRateException(String message) {
        super(message);
    }
}
