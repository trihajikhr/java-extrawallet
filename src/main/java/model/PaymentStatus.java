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
        for (PaymentStatus status : values()) {
            if (status.label.equalsIgnoreCase(input.trim())) return status;
        }
        throw new IllegalArgumentException("Unknown payment status: " + input);
    }
}