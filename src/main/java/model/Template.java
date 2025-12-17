package model;

public class Template {
    private int id;
    private String tipe;
    private String nama;
    private int jumlah;
    private Akun akun;
    private Kategori kategori;
    private TipeLabel label;
    private String keterangan;
    private String metodeBayar;
    private String status;

    // constructor full atribut
    public Template(int id, String tipe, String nama, int jumlah, Akun akun, Kategori kategori, TipeLabel label, String keterangan, String metodeBayar, String status) {
        this.id = id;
        this.tipe = tipe;
        this.nama = nama;
        this.jumlah = jumlah;
        this.akun = akun;
        this.kategori = kategori;
        this.label = label;
        this.keterangan = keterangan;
        this.metodeBayar = metodeBayar;
        this.status = status;
    }

    // constructor atribut wajib
    public Template(int id, String nama, Akun akun, String tipe, int jumlah) {
        this.id = id;
        this.nama = nama;
        this.akun = akun;
        this.tipe = tipe;
        this.jumlah = jumlah;
    }

    // constructor custom
    public Template() {}

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

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public Akun getAkun() {
        return akun;
    }

    public void setAkun(Akun akun) {
        this.akun = akun;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public TipeLabel getLabel() {
        return label;
    }

    public void setLabel(TipeLabel label) {
        this.label = label;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getMetodeBayar() {
        return metodeBayar;
    }

    public void setMetodeBayar(String metodeBayar) {
        this.metodeBayar = metodeBayar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}