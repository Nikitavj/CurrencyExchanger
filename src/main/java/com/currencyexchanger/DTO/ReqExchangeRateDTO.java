package com.currencyexchanger.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqExchangeRateDTO extends ReqDTO{
    private String baseCurrencyCode;
    private String targetCurrncyCode;
    private BigDecimal rate;

    public ReqExchangeRateDTO(String baseCurrencyCode, String targetCurrncyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrncyCode = targetCurrncyCode;
    }
}
