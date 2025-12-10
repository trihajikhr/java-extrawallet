package model;

import java.time.LocalDateTime;

public class Pemasukan extends Transaksi {
    public Pemasukan(int id, String tipe, int jumlah, Kategori kategori, LocalDateTime tanggalSet, LocalDateTime tanggalBuat, String keterangan) {
        super(id, tipe, jumlah, kategori, tanggalSet, tanggalBuat, keterangan);
    }

    public Pemasukan(int id, String tipe, int jumlah, Kategori kategori, LocalDateTime tanggalSet, LocalDateTime tanggalBuat) {
        super(id, tipe, jumlah, kategori, tanggalSet, tanggalBuat);
    }

    public Pemasukan() {
    }
}