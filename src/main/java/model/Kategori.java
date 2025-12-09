package model;

public class Kategori {
    private int id;
    private String label;

    public Kategori(int id, String label){
        this.id = id;
        this.label = label;
    }

    public Kategori() {}

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
}
