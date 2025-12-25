package model;

public class Template {
    private int id;
    private TipeTransaksi tipe;
    private String nama;
    private int jumlah;
    private Akun akun;
    private Kategori kategori;
    private TipeLabel tipeLabel;
    private String keterangan;
    private PaymentType paymentType;
    private PaymentStatus paymentStatus;

    // constructor full atribut
    public Template(int id, TipeTransaksi tipe, String nama, int jumlah, Akun akun, Kategori kategori, TipeLabel tipeLabel, String keterangan, PaymentType paymentType, PaymentStatus paymentStatus) {
        this.id = id;
        this.tipe = tipe;
        this.nama = nama;
        this.jumlah = jumlah;
        this.akun = akun;
        this.kategori = kategori;
        this.tipeLabel = tipeLabel;
        this.keterangan = keterangan;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
    }

    // constructor atribut wajib
    public Template(int id, String nama, Akun akun, TipeTransaksi tipe, int jumlah) {
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

    public TipeTransaksi getTipe() {
        return tipe;
    }

    public void setTipe(TipeTransaksi tipe) {
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

    public TipeLabel getTipeLabel() {
        return tipeLabel;
    }

    public void setTipeLabel(TipeLabel tipeLabel) {
        this.tipeLabel = tipeLabel;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}