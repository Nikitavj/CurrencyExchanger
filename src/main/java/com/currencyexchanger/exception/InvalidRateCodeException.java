package com.currencyexchanger.exception;

public class InvalidRateCodeException extends RuntimeException {

    public InvalidRateCodeException() {
        super("Неправильно введен код обмена валют");
    }
}
