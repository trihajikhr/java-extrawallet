package model;

import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class Akun {
    private int id;
    private String nama;
    private Color warna;
    private Image icon;
    private String iconPath;
    private int jumlah;

    public Akun(int id, String nama, Color warna, Image image, String iconPath, int jumlah) {
        this.id = id;
        this.nama = nama;
        this.warna = warna;
        this.icon = image;
        this.iconPath = iconPath;
        this.jumlah = jumlah;
    }

    // TODO: hanya test, nanti refactor!
    public Akun(String nama, int jumlah, Image icon) {
        this.nama = nama;
        this.icon = icon;
        this.jumlah = jumlah;
    }

    public Akun() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}