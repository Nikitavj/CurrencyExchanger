package com.currencyexchanger.exception;

public class DatabaseException extends RuntimeException{

    public DatabaseException() {
        super("База данных недоступна");
    }
}
