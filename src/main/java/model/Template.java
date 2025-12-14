package model;

public class Template {
    private int id;
    private String tipe;
    private int jumlah;
    private Akun account;
    private Kategori kategori;
    private TipeLabel label;
    private String keterangan;
    private String metodeBayar;
    private String status;

    // constructor full atribut
    public Template(int id, String tipe, int jumlah, Akun account, Kategori kategori, TipeLabel label, String keterangan, String metodeBayar, String status) {
        this.id = id;
        this.tipe = tipe;
        this.jumlah = jumlah;
        this.account = account;
        this.kategori = kategori;
        this.label = label;
        this.keterangan = keterangan;
        this.metodeBayar = metodeBayar;
        this.status = status;
    }

    // constructor atribut wajib
    public Template(Akun account, int id, String tipe, int jumlah) {
        this.account = account;
        this.id = id;
        this.tipe = tipe;
        this.jumlah = jumlah;
    }

    // constructor custom
    public Template() {}
}