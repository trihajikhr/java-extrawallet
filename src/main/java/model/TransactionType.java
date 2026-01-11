package model;

public enum TransactionType {
    INCOME("In"),
    EXPANSE("Out");

    private final String label;

    TransactionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TransactionType fromString(String input) {
        if (input == null) return null;
        String val = input.trim();
        for (TransactionType type : values()) {
            if (type.name().equalsIgnoreCase(val) || type.label.equalsIgnoreCase(val)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown payment type: " + input);
    }
}