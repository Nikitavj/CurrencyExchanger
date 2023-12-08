package com.currencyexchanger.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestExchangeDTO {
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal amount;
}
