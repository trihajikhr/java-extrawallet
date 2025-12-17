package model;

import java.time.LocalDate;

public class Transaksi {
    protected int id;
    protected String tipe; // [IN/OUT]
    protected int jumlah;
    protected Akun akun;
    protected Kategori kategori;
    protected TipeLabel tipelabel;
    protected LocalDate tanggal;
    protected String keterangan;
    protected String metodeTransaksi;
    protected String status;

    // contructor full spek
    public Transaksi(int id, String tipe, int jumlah, Akun akun, Kategori kategori, TipeLabel tipelabel, LocalDate tanggal, String keterangan, String metodeTransaksi, String status) {
        this.id = id;
        this.tipe = tipe;
        this.jumlah = jumlah;
        this.akun = akun;
        this.kategori = kategori;
        this.tipelabel = tipelabel;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.metodeTransaksi = metodeTransaksi;
        this.status = status;
    }

    // constructor hanya spek wajib

    public Transaksi(int id, String tipe, int jumlah, Kategori kategori, LocalDate tanggal) {
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

    public TipeLabel getTipelabel() {
        return tipelabel;
    }

    public void setTipelabel(TipeLabel tipelabel) {
        this.tipelabel = tipelabel;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
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