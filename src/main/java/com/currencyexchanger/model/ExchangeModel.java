package com.currencyexchanger.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.ROUND_FLOOR;


public class ExchangeModel {

    private CurrencyModel baseCurrencyModel;
    private CurrencyModel targetCurrencyModel;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;

    public ExchangeModel() {}

    public ExchangeModel(CurrencyModel baseCurrencyModel, CurrencyModel targetCurrencyModel, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        this.baseCurrencyModel = baseCurrencyModel;
        this.targetCurrencyModel = targetCurrencyModel;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public ExchangeModel(CurrencyModel baseCurrencyModel, CurrencyModel targetCurrencyModel, BigDecimal amount) {
        this.baseCurrencyModel = baseCurrencyModel;
        this.targetCurrencyModel = targetCurrencyModel;
        this.amount = amount;
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
        this.rate = rate.setScale(6, ROUND_FLOOR);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount.setScale(2, ROUND_FLOOR);
    }
}
