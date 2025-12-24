package controller.option;

public enum SortOption {
    TIME_NEWEST("Time (newest first)"),
    TIME_OLDEST("Time (oldest first)"),
    AMOUNT_HIGHEST("Amount (highest first)"),
    AMOUNT_LOWEST("Amount (lowest first)");

    private final String label;

    SortOption(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}