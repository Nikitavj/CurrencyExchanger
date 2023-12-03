package com.currencyexchanger.controller.exception;

public class InvalidRateCodeException extends Exception{
    public InvalidRateCodeException(String message) {
        super(message);
    }
}
