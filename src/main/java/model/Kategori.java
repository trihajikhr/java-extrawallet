package model;

public class Kategori {
    private int id;
    private String tipe; // IN/OUT
    private String label;

    public Kategori(int id, String tipe, String label) {
        this.id = id;
        this.tipe = tipe;
        this.label = label;
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
}
