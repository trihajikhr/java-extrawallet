package model;

import javafx.scene.paint.Color;

public class Account {
    private int id;
    private String label;
    private Color warna;
    private Image image;
    private int jumlah;

    public Account(int id, String label, Color warna, Image image, int jumlah) {
        this.id = id;
        this.label = label;
        this.warna = warna;
        this.image = image;
        this.jumlah = jumlah;
    }

    public Account() {}

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

    public Color getWarna() {
        return warna;
    }

    public void setWarna(Color warna) {
        this.warna = warna;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}