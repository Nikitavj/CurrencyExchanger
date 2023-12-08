package com.currencyexchanger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyModel {
    private int id;
    @JsonProperty("name")
    private String fullName;
    private String code;
    private String sign;
}
