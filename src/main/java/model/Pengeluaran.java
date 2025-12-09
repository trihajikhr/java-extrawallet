package model;

import java.time.LocalDateTime;

public class Pengeluaran extends Transaksi {
    public Pengeluaran(int id, String tipe, int jumlah, int idKategori, LocalDateTime tanggalSet, LocalDateTime tanggalBuat, String keterangan) {
        super(id, tipe, jumlah, idKategori, tanggalSet, tanggalBuat, keterangan);
    }

    public Pengeluaran(int id, String tipe, int jumlah, int idKategori, LocalDateTime tanggalSet, LocalDateTime tanggalBuat) {
        super(id, tipe, jumlah, idKategori, tanggalSet, tanggalBuat);
    }

    public Pengeluaran() {
    }
}