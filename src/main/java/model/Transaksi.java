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
    protected LocalDateTime tanggalBuat;
    protected String keterangan;

    // contructor full
    public Transaksi(int id, String tipe, int jumlah, Kategori kategori, LocalDateTime tanggalSet, LocalDateTime tanggalBuat, String keterangan) {
        this.id = id;
        this.tipe = tipe;
        this.jumlah = jumlah;
        this.kategori = kategori;
        this.tanggalSet = tanggalSet;
        this.tanggalBuat = tanggalBuat;
        this.keterangan = keterangan;
    }

    // conostructor tanpa keterangan
    public Transaksi(int id, String tipe, int jumlah, Kategori kategori, LocalDateTime tanggalSet, LocalDateTime tanggalBuat) {
        this.id = id;
        this.tipe = tipe;
        this.jumlah = jumlah;
        this.kategori = kategori;
        this.tanggalSet = tanggalSet;
        this.tanggalBuat = tanggalBuat;
    }

    // contructor hanya buat objek
    public Transaksi(){}

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

    public int getIdKategori() {
        return kategori.getId();
    }

    public void setIdKategori(int idKategori) {
        this.kategori.setId(idKategori);
    }

    public LocalDateTime getTanggalSet() {
        return tanggalSet;
    }

    public void setTanggalSet(LocalDateTime tanggalSet) {
        this.tanggalSet = tanggalSet;
    }

    public LocalDateTime getTanggalBuat() {
        return tanggalBuat;
    }

    public void setTanggalBuat(LocalDateTime tanggalBuat) {
        this.tanggalBuat = tanggalBuat;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}