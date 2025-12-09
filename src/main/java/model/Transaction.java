package model;

import java.time.LocalDateTime;

public class Transaction {
    protected int id;
    protected LocalDateTime tanggal;
    protected int idKategori;
    protected int jumlah;
    protected String keterangan;

    // constructor lengkap
    public Transaction(int id, LocalDateTime tanggal, int idKategori, int jumlah, String keterangan) {
        this.id = id;
        this.tanggal = tanggal;
        this.idKategori = idKategori;
        this.jumlah = jumlah;
        this.keterangan = keterangan;
    }

    // construkctor tanpa keterangan
    public Transaction(int id, LocalDateTime tanggal, int idKategori, int jumlah) {
        this.id = id;
        this.tanggal = tanggal;
        this.idKategori = idKategori;
        this.jumlah = jumlah;
    }

    // objek kosong
    public Transaction() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }

    public int getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(int idKategori) {
        this.idKategori = idKategori;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}