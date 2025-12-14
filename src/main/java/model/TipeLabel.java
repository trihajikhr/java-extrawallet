package model;

import javafx.scene.paint.Color;

public class TipeLabel {
    private int id;
    private String nama;
    private Color warna;

    public TipeLabel(int id, String nama, Color warna) {
        this.id = id;
        this.nama = nama;
        this.warna = warna;
    }

    public TipeLabel() {}

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
}