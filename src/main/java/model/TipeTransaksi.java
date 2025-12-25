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

    public static TipeTransaksi  fromString(String input) {
        if (input == null) return null;
        String val = input.trim();
        for (TipeTransaksi type : values()) {
            if (type.name().equalsIgnoreCase(val) || type.label.equalsIgnoreCase(val)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown payment type: " + input);
    }
}