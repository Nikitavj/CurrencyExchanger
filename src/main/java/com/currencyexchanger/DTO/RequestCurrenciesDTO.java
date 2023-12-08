package com.currencyexchanger.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCurrenciesDTO {
    private String name;
    private String code;
    private String sign;
}
