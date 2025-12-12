package model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Account {
    private int id;
    private String name;
    private Color color;
    private int idAccountType;
    private int amount;

    public Account(int id, String name, Color color, int idAccountType, int amount) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.idAccountType = idAccountType;
        this.amount = amount;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getIdAccountType() {
        return idAccountType;
    }

    public void setIdAccountType(int idAccountType) {
        this.idAccountType = idAccountType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

