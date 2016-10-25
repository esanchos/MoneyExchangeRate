package com.earaujo.app.moneyexchangerate;

/**
 * Created by Eduardo on 01/07/2016.
 */
public class RateData {
    private String cCode;
    private Double rRate;

    public RateData(String code, Double name) {
        cCode = code;
        rRate = name;
    }

    public String getcCode() {
        return cCode;
    }

    public Double getcRate() {
        return rRate;
    }
}
