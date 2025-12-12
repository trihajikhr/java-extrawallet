package model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Kategori {
    private int id;
    private String tipe; // IN/OUT
    private Color warna;
    private String label;
    private Image gambar; // icon putih

    public Kategori(int id, String tipe, Color warna, String label, Image gambar) {
        this.id = id;
        this.tipe = tipe;
        this.warna = warna;
        this.label = label;
        this.gambar = gambar;
    }

    public Kategori(){}

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

    public Color getWarna() {
        return warna;
    }

    public void setWarna(Color warna) {
        this.warna = warna;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Image getGambar() {
        return gambar;
    }

    public void setGambar(Image gambar) {
        this.gambar = gambar;
    }
}