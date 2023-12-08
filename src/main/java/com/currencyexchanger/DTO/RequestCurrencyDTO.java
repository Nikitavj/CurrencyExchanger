package com.currencyexchanger.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCurrencyDTO {
    private String code;
    private int id;

    public RequestCurrencyDTO(String code) {
        this.code = code;
    }
}
