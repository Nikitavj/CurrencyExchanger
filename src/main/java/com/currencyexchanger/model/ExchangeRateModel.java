package com.currencyexchanger.model;

import java.math.BigDecimal;

public class ExchangeRateModel {
    private int id;
    private CurrencyModel baseCurrencyModel;
    private CurrencyModel targetCurrencyModel;
    private BigDecimal rate;

    public ExchangeRateModel(int id, CurrencyModel baseCurrencyModel, CurrencyModel targetCurrencyModel, BigDecimal rate) {
        this.id = id;
        this.baseCurrencyModel = baseCurrencyModel;
        this.targetCurrencyModel = targetCurrencyModel;
        this.rate = rate;
    }

    public ExchangeRateModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CurrencyModel getBaseCurrency() {
        return baseCurrencyModel;
    }

    public void setBaseCurrency(CurrencyModel baseCurrencyModel) {
        this.baseCurrencyModel = baseCurrencyModel;
    }

    public CurrencyModel getTargetCurrency() {
        return targetCurrencyModel;
    }

    public void setTargetCurrency(CurrencyModel targetCurrencyModel) {
        this.targetCurrencyModel = targetCurrencyModel;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}






