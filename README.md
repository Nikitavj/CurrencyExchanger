# Currency Exchanger API

# Overview
REST API для описания валют и обменных курсов. Позволяет просматривать и редактировать списки валют и обменных курсов, и совершать расчёт конвертации произвольных сумм из одной валюты в другую.

Проект реализован в соответствии с ТЗ: https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/

Проект представляет  серверное приложение с реализацией архитектурного подхода REST API. 
Приложение построено на принципе MVC (Model Vive Control), сервлеты образуют слой контроллера, 
который принимает  REST запросы пользователя, проводит валидацию и запрашивает данные у модели, 
после чего возвращает ответ в JSON формате.

Модель принимает объект запроса от контроллера, производит вычисления обменного курса, 
и возвращает объект результата расчета. 
Для расчета обратного курса или получения прямого курса модель обращается к 
DAO (Data Access Object) с помощью объекта запроса.

DAO реализует доступ к базе данных через CRUD интерфейс, 
в данном приложении есть возможность читать, вносить и редактировать записи. 
В качестве  хранилища данных используется встраиваемая реляционная DB SQLite.

# Technologies / tools used:
- JDBC
- Java Servlets
- SQLite
- Postman
- Maven
- VDS(Debian 11)
- Tomcat 10

# Installation
1. Собрать c помощью Maven war артефакт приложения.
2. Развернуть war артефакт в Tomcat.

# Usage
1. Импортировать файл запросов в Postman https://github.com/Nikitavj/CurrencyExchanger/blob/master/CurrencyExchanger.postman_collection.json

2. Выполнить запросы по URL приложения:

GET /currencies
```[
    {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },   
    {
        "id": 0,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    }
]
```

GET /currency/EUR
```{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```

POST /currencies?name=Euro&code=EUR&sign=€
```{
    "id": 0,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
}
```

GET /exchangeRates
```[
    {
        "id": 0,
        "baseCurrency": {
            "id": 0,
            "name": "United States dollar",
            "code": "USD",
            "sign": "$"
        },
        "targetCurrency": {
            "id": 1,
            "name": "Euro",
            "code": "EUR",
            "sign": "€"
        },
        "rate": 0.99
    }
]
```

GET /exchangeRate/USDRUB
```{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```

POST /exchangeRates/USDRUB?baseCurrencyCode=USD&targetCurrencyCode=EUR&rate=0.99
```{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```

PATCH /exchangeRate/USDRUB поле формы (x-www-form-urlencoded) - rate=0.99
```{
    "id": 0,
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Euro",
        "code": "EUR",
        "sign": "€"
    },
    "rate": 0.99
}
```

###   Обмен валюты

GET /exchange?from=USD&to=AUD&amount=10
```{
    "baseCurrency": {
        "id": 0,
        "name": "United States dollar",
        "code": "USD",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 1,
        "name": "Australian dollar",
        "code": "AUD",
        "sign": "A€"
    },
    "rate": 1.45,
    "amount": 10.00
    "convertedAmount": 14.50
}
```
