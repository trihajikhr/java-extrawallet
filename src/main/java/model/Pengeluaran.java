package model;

import java.time.LocalDateTime;

public class Pengeluaran extends Transaksi {
    public Pengeluaran(int id, String tipe, int jumlah, Account account, Kategori kategori, TipeLabel label, LocalDateTime tanggalSet, String keterangan, String metodeTransaksi, String status) {
        super(id, tipe, jumlah, account, kategori, label, tanggalSet, keterangan, metodeTransaksi, status);
    }

    public Pengeluaran(int id, String tipe, int jumlah, Kategori kategori, LocalDateTime tanggalSet) {
        super(id, tipe, jumlah, kategori, tanggalSet);
    }

    public Pengeluaran() {
    }
}