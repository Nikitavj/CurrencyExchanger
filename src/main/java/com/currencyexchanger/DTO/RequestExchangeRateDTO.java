package com.currencyexchanger.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestExchangeRateDTO {
    private String baseCurrency;
    private String targetCurrncy;
    private BigDecimal rate;

    public RequestExchangeRateDTO(String baseCurrency, String targetCurrncy) {
        this.baseCurrency = baseCurrency;
        this.targetCurrncy = targetCurrncy;
    }
}
