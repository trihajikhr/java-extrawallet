package model;

public enum TipeTransaksi {
    IN("In"),
    OUT("Out");

    private final String label;

    TipeTransaksi(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TipeTransaksi fromString(String input) {
        for (TipeTransaksi t : values()) {
            if (t.label.equalsIgnoreCase(input)) return t;
        }
        throw new IllegalArgumentException("Unknown transaction type: " + input);
    }
}