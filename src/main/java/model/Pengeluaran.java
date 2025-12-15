package model;

import java.time.LocalDate;

public class Pengeluaran extends Transaksi {
    public Pengeluaran(int id, String tipe, int jumlah, Akun account, Kategori kategori, TipeLabel label, LocalDate tanggalSet, String keterangan, String metodeTransaksi, String status) {
        super(id, tipe, jumlah, account, kategori, label, tanggalSet, keterangan, metodeTransaksi, status);
    }

    public Pengeluaran(int id, String tipe, int jumlah, Kategori kategori, LocalDate tanggalSet) {
        super(id, tipe, jumlah, kategori, tanggalSet);
    }

    public Pengeluaran() {
    }
}