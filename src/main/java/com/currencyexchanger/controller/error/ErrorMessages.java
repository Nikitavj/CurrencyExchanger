package com.currencyexchanger.controller.error;

public enum ErrorMessages {
    CURRENCY_GET_404("Валюта не найдена"),
    EXCHANGE_RATE_GET_404("Обменный курс для пары не найден"),
    ERROR_DATABASE("База данных не доступна"),
    CURRENCIES_POST_400("Отсутствует нужное поле формы"),
    CURRENCIES_POST_409("Валюта с таким кодом уже существует"),
    CURRENCY_GET_400("Код валюты отсутствует в адресе. Пример: USD"),
    EXCHANGE_RATE_GET_400("Коды валют пары отсутствуют в адресе. Пример: USDRUB"),
    CURRENCIES_PATCH_400("Отсутствует нужное поле формы"),
    EXCHANGE_RATES_POST_409("Валютная пара с таким кодом уже существует"),
    EXCHANGE_RATES_POST_400("Отсутсвует нужное поле формы. Пример: baseCurrencyCode=USD&targetCurrencyCode=EUR&rate=0.99");


    private String message;
    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
