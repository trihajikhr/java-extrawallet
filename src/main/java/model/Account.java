package model;

import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class Account {
    private int id;
    private String name;
    private Color color;
    private Image icon;
    private String iconPath;
    private int balance;
    private MataUang currencyType;

    public Account(int id, String name, Color color, Image image, String iconPath, int balance, MataUang currencyType) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.icon = image;
        this.iconPath = iconPath;
        this.balance = balance;
        this.currencyType = currencyType;
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public MataUang getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(MataUang currencyType) {
        this.currencyType = currencyType;
    }
}