package model;

import javafx.scene.image.Image;

public class Kategori {
    private int id;
    private String tipe;
    private String label;
    private Image gambar;

    public Kategori(int id, String tipe, String label, Image gambar) {
        this.id = id;
        this.tipe = tipe;
        this.label = label;
        this.gambar = gambar;
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

    public Image getGambar() {
        return gambar;
    }

    public void setGambar(Image gambar) {
        this.gambar = gambar;
    }
}