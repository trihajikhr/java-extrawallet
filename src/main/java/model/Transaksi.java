package model;

import java.time.LocalDateTime;

public class Transaksi {
    protected int id;
    protected String tipe; // [IN/OUT/TRS]
    protected int jumlah;
    protected Akun account;
    protected Kategori kategori;
    protected TipeLabel label;
    protected LocalDateTime tanggal;
    protected String keterangan;
    protected String metodeTransaksi;
    protected String status;

    // contructor full spek
    public Transaksi(int id, String tipe, int jumlah, Akun account, Kategori kategori, TipeLabel label, LocalDateTime tanggal, String keterangan, String metodeTransaksi, String status) {
        this.id = id;
        this.tipe = tipe;
        this.jumlah = jumlah;
        this.account = account;
        this.kategori = kategori;
        this.label = label;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.metodeTransaksi = metodeTransaksi;
        this.status = status;
    }

    // constructor hanya spek wajib

    public Transaksi(int id, String tipe, int jumlah, Kategori kategori, LocalDateTime tanggal) {
        this.id = id;
        this.tipe = tipe;
        this.jumlah = jumlah;
        this.kategori = kategori;
        this.tanggal = tanggal;
    }

    public Transaksi() {}

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

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public Akun getAccount() {
        return account;
    }

    public void setAccount(Akun account) {
        this.account = account;
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

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getMetodeTransaksi() {
        return metodeTransaksi;
    }

    public void setMetodeTransaksi(String metodeTransaksi) {
        this.metodeTransaksi = metodeTransaksi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}