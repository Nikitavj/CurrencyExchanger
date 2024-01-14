package com.currencyexchanger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyModel {
    private Integer id;
    @JsonProperty("name")
    private String fullName;
    private String code;
    private String sign;
}
