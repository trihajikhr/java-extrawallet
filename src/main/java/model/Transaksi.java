package model;

import java.time.LocalDateTime;

public class Transaksi {
    protected int id;
    protected String tipe; // [IN/OUT/TRS]
    protected int jumlah;
    protected Account account;
    protected Kategori kategori;
    protected Label label;
    protected LocalDateTime tanggalSet;
    protected String keterangan;
    protected String metodeTransaksi;
    protected String status;

    public Transaksi(int id, String tipe, int jumlah, Account account, Kategori kategori, Label label, LocalDateTime tanggalSet, String keterangan, String metodeTransaksi, String status) {
        this.id = id;
        this.tipe = tipe;
        this.jumlah = jumlah;
        this.account = account;
        this.kategori = kategori;
        this.label = label;
        this.tanggalSet = tanggalSet;
        this.keterangan = keterangan;
        this.metodeTransaksi = metodeTransaksi;
        this.status = status;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public LocalDateTime getTanggalSet() {
        return tanggalSet;
    }

    public void setTanggalSet(LocalDateTime tanggalSet) {
        this.tanggalSet = tanggalSet;
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