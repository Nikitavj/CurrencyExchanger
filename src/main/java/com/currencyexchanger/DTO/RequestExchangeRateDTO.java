package com.currencyexchanger.DTO;

import java.math.BigDecimal;

public class RequestExchangeRateDTO {

    private String baseCurrency;
    private String targetCurrncy;
    private BigDecimal rate;

    public RequestExchangeRateDTO(String baseCurrency, String targetCurrncy, BigDecimal rate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrncy = targetCurrncy;
        this.rate = rate;
    }

    public RequestExchangeRateDTO(String baseCurrency, String targetCurrncy) {
        this.baseCurrency = baseCurrency;
        this.targetCurrncy = targetCurrncy;
    }

    public RequestExchangeRateDTO() {}

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getTargetCurrncy() {
        return targetCurrncy;
    }

    public void setTargetCurrncy(String targetCurrncy) {
        this.targetCurrncy = targetCurrncy;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
