package model;

public enum PaymentType {
    CASH("Cash"),
    DEBIT_CARD("Debit card"),
    CREDIT_CARD("Credit card"),
    TRANSFER("Transfer"),
    VOUCHER("Voucher"),
    MOBILE_PAYMENT("Mobile payment");

    private final String label;

    PaymentType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static PaymentType fromString(String input) {
        if (input == null) return null;
        String val = input.trim();
        for (PaymentType type : values()) {
            if (type.name().equalsIgnoreCase(val) || type.label.equalsIgnoreCase(val)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown payment type: " + input);
    }
}