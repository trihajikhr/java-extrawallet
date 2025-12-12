package model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Account {
    private int id;
    private String name;
    private Color color;
    private Image image;
    private int amount;

    public Account(int id, String name, Image image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Account() {}

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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}

