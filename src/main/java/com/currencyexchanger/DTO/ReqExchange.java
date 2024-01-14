package com.currencyexchanger.DTO;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReqExchange {
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal amount;
}
