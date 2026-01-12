package dataflow.basedata;

import javafx.scene.paint.Color;

public class ColorItem {
    private final String label;
    private final Color color;

    public ColorItem(String label, Color color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() { return label; }
    public Color getColor() { return color; }
}