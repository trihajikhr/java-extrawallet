package model;

public enum PaymentStatus {
    RECONCILED("Reconciled"),
    CLEARED("Cleared"),
    UNCLEARED("Uncleared");

    private final String label;

    PaymentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static PaymentStatus fromString(String input) {
        if (input == null) return null;
        String val = input.trim();
        for (PaymentStatus type : values()) {
            if (type.name().equalsIgnoreCase(val) || type.label.equalsIgnoreCase(val)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown payment type: " + input);
    }
}