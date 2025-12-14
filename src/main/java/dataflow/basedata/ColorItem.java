package dataflow.basedata;

import javafx.scene.paint.Color;

public class ColorItem {
    private final String label;
    private final Color warna;

    public ColorItem(String label, Color warna) {
        this.label = label;
        this.warna = warna;
    }

    public String getLabel() { return label; }
    public Color getWarna() { return warna; }
}