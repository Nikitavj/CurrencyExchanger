package com.currencyexchanger.exception;

public class InvalidParametersException extends Exception{

    public InvalidParametersException(String message) {
        super("Отсутствует параметр " + message);
    }

    public InvalidParametersException()  {
        super("Отсутствует параметр");
    }
}
