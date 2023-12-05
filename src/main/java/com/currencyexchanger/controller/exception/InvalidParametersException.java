package com.currencyexchanger.controller.exception;

public class InvalidParametersException extends Exception{

    public InvalidParametersException(String message) {
        super("Введен неверный параметр" + message);
    }

    public InvalidParametersException()  {
        super("Введен неверный параметр");
    }
}
