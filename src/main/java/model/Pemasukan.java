package model;

import java.time.LocalDateTime;

public class Pemasukan extends Transaksi {
    public Pemasukan(int id, String tipe, int jumlah, Akun account, Kategori kategori, TipeLabel label, LocalDateTime tanggalSet, String keterangan, String metodeTransaksi, String status) {
        super(id, tipe, jumlah, account, kategori, label, tanggalSet, keterangan, metodeTransaksi, status);
    }

    public Pemasukan(int id, String tipe, int jumlah, Kategori kategori, LocalDateTime tanggalSet) {
        super(id, tipe, jumlah, kategori, tanggalSet);
    }

    public Pemasukan() {
    }
}