package model;

import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class Account {
    private int id;
    private String label;
    private Color warna;
    private Image icon;
    private int jumlah;

    public Account(int id, String label, Color warna, Image image, int jumlah) {
        this.id = id;
        this.label = label;
        this.warna = warna;
        this.icon = image;
        this.jumlah = jumlah;
    }

    // TODO: hanya test, nanti refactor!
    public Account(String label, int jumlah, Image icon) {
        this.label = label;
        this.icon = icon;
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

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}