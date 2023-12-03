package com.currencyexchanger.DTO;



public class RequestCurrencyDTO {

    private String code;
    private int id;

    public RequestCurrencyDTO(String code) {
        this.code = code;
    }

    public RequestCurrencyDTO(int id) {
        this.id = id;
    }

    public RequestCurrencyDTO() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
