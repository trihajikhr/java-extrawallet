package model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Category {
    private int id;
    private String type;
    private String name;
    private Image icon;
    private String iconPath;
    private Color color;

    public Category(int id, String type, String name, Image icon, String iconPath, Color color) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.icon = icon;
        this.iconPath = iconPath;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}