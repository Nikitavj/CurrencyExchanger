package com.currencyexchanger.DTO;

import java.math.BigDecimal;

public class RequestExchangeDTO {
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal amount;

    public RequestExchangeDTO(String baseCurrency, String targetCurrency, BigDecimal amount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.amount = amount;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
