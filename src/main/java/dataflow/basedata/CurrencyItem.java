package dataflow.basedata;

public class CurrencyItem {
    private String code;        // ISO code: IDR, USD, EUR
    private String name;        // Rupiah, US Dollar
    private String symbol;      // Rp, $, €
    private int fractionDigits; // 0 = IDR, 2 = USD

    public CurrencyItem(String code, String name, String symbol, int fractionDigits) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.fractionDigits = fractionDigits;
    }

    public CurrencyItem() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getFractionDigits() {
        return fractionDigits;
    }

    public void setFractionDigits(int fractionDigits) {
        this.fractionDigits = fractionDigits;
    }
}
