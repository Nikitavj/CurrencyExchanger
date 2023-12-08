package com.currencyexchanger.controller.exception;

public class DatabaseException extends Exception{

    public DatabaseException() {
        super("База данных недоступна");
    }
}
