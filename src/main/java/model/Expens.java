package model;

import java.time.LocalDateTime;

public class Expens extends Transaction{
    public Expens(int id, LocalDateTime tanggal, int idKategori, int jumlah, String keterangan) {
        super(id, tanggal, idKategori, jumlah, keterangan);
    }

    public Expens(int id, LocalDateTime tanggal, int idKategori, int jumlah) {
        super(id, tanggal, idKategori, jumlah);
    }

    public Expens() {
    }
}