package model;

public class Currency {
    private int id;
    private String code;        // ISO code: IDR, USD, EUR
    private String name;        // Rupiah, US Dollar
    private String symbol;      // Rp, $, €
    private int decimal;        // 0 = IDR, 2 = USD

    // full field constructor
    public Currency(int id, String code, String name, String symbol, int decimal) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.decimal = decimal;
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

    public int getDecimal() {
        return decimal;
    }

    public void setDecimal(int decimal) {
        this.decimal = decimal;
    }
}
