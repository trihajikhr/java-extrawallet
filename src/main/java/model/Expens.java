package model;

import java.time.LocalDateTime;

public class Expens {
    protected int idPengeluaran;
    protected LocalDateTime tanggal;
    protected int idKategori;
    protected int jumlah;

    public Expens(int idPengeluaran, LocalDateTime tanggal, int idKategori, int jumlah) {
        this.idPengeluaran = idPengeluaran;
        this.tanggal = tanggal;
        this.idKategori = idKategori;
        this.jumlah = jumlah;
    }
}