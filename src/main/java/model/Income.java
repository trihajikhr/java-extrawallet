package model;

import java.time.LocalDateTime;

public class Income extends Transaction{
    public Income(int id, LocalDateTime tanggal, int idKategori, int jumlah, String keterangan) {
        super(id, tanggal, idKategori, jumlah, keterangan);
    }

    public Income(int id, LocalDateTime tanggal, int idKategori, int jumlah) {
        super(id, tanggal, idKategori, jumlah);
    }

    public Income() {
    }
}