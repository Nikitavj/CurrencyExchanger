package com.currencyexchanger.exception;

public class InvalidRateCodeException extends Exception{

    public InvalidRateCodeException(String message) {
        super(message);
    }

    public InvalidRateCodeException() {
        super("Неправильно введен код обмена валют");
    }
}
