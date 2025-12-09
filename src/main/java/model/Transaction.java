package model;

import java.time.LocalDateTime;

public class Transaction {
    protected int id;
    protected LocalDateTime tanggal;
    protected int idKategori;
    protected int jumlah;

    public Transaction(int id, LocalDateTime tanggal, int idKategori, int jumlah) {
        this.id = id;
        this.tanggal = tanggal;
        this.idKategori = idKategori;
        this.jumlah = jumlah;
    }
}