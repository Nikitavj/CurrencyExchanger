package com.currencyexchanger.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCurrencyDTO extends ReqDTO{
    private String name;
    private String code;
    private String sign;
    private int id;

    public ReqCurrencyDTO(String code) {
        this.code = code;
    }

    public ReqCurrencyDTO(int id) {
        this.id = id;
    }

    public ReqCurrencyDTO(String name, String code, String sign) {
        this.name = name;
        this.code = code;
        this.sign = sign;
    }
}
