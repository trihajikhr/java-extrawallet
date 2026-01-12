package model;

public class Currency {
    private int id;
    private String code;        // ISO code: IDR, USD, EUR
    private String name;        // Rupiah, US Dollar
    private String symbol;      // Rp, $, €
    private int fractionDigits;        // 0 = IDR, 2 = USD

    // full field constructor
    public Currency(int id, String code, String name, String symbol, int fractionDigits) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.fractionDigits = fractionDigits;
    }

    // Default constructor for manual field assignment
    public Currency() {}

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

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
