package service;

import java.util.Map;

public class ExchangeResponse {
    private String base;
    private String date;
    private Map<String, Double> rates;

    // getters
    public Map<String, Double> getRates() {
        return rates;
    }
}