package com.currencyexchanger.control.exception;

public class NotFoundExchangeRateException extends Exception{
    public NotFoundExchangeRateException(String message) {
        super(message);
    }
}
