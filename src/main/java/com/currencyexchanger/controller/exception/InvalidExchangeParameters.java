package com.currencyexchanger.controller.exception;

public class InvalidExchangeParameters extends Exception{
    public InvalidExchangeParameters(String message) {
        super(message);
    }
}
