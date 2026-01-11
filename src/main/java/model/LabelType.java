package model;

import javafx.scene.paint.Color;

public class LabelType {
    private int id;
    private String name;
    private Color color;

    public LabelType(int id, String name, Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public LabelType() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}