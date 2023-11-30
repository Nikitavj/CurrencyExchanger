package com.currencyexchanger.servise;

import com.currencyexchanger.model.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeDTO {

    private Currency baseCurrency;
    private Currency targetCurrency;
    private double rate;
    private double amount;
    private double convertedAmount;

    public ExchangeDTO() {}

    public ExchangeDTO(Currency baseCurrency, Currency targetCurrency, double rate, double amount, double convertedAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        BigDecimal bd = new BigDecimal(rate);
        BigDecimal result = bd.setScale(6, RoundingMode.HALF_UP);
        this.rate = result.doubleValue();
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(double convertedAmount) {
        BigDecimal bd = new BigDecimal(convertedAmount);
        BigDecimal result = bd.setScale(2, RoundingMode.HALF_UP);
        this.convertedAmount = result.doubleValue();
    }
}
