package com.currencyexchanger.exception;

public class NotFoundExchangeRateException extends Exception{

    public NotFoundExchangeRateException(String message) {
        super(message);
    }

    public NotFoundExchangeRateException() {
        super("Обменный курс не найден в БД");
    }
}
