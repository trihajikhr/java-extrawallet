package model;

import javafx.scene.paint.Color;

public class Label {
    private int id;
    private String label;
    private Color color;

    public Label(int id, String label, Color color) {
        this.id = id;
        this.label = label;
        this.color = color;
    }

    public Label() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}