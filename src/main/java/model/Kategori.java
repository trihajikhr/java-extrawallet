package model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Kategori {
    private int id;
    private String tipe;
    private String nama;
    private Image icon;
    private String iconPath;
    private Color warna;

    public Kategori(int id, String tipe, String nama, Image icon, String iconPath, Color warna) {
        this.id = id;
        this.tipe = tipe;
        this.nama = nama;
        this.icon = icon;
        this.iconPath = iconPath;
        this.warna = warna;
    }

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

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    public Color getWarna() {
        return warna;
    }

    public void setWarna(Color warna) {
        this.warna = warna;
    }
}