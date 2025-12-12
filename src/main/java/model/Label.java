package model;

import javafx.scene.paint.Color;

public class Label {
    private int id;
    private String name;
    Color color;

    public Label(int id, String name, Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Label() {}

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
