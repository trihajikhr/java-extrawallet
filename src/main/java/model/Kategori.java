package model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Kategori {
    private int id;
    private String tipe;
    private String label;
    private Image icon;
    private Color warna;

    public Kategori(int id, String tipe, String label, Image gambar, Color warna) {
        this.id = id;
        this.tipe = tipe;
        this.label = label;
        this.icon = gambar;
        this.warna = warna;
    }

    public Kategori() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public Color getWarna() {
        return warna;
    }

    public void setWarna(Color warna) {
        this.warna = warna;
    }
}