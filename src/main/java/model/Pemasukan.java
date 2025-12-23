package model;

import java.time.LocalDate;

public class Pemasukan extends Transaksi {
    public Pemasukan(int id, TipeTransaksi tipeTransaksi, int jumlah, Akun akun, Kategori kategori, TipeLabel tipelabel, LocalDate tanggal, String keterangan, PaymentType paymentType, PaymentStatus paymentStatus) {
        super(id, tipeTransaksi, jumlah, akun, kategori, tipelabel, tanggal, keterangan, paymentType, paymentStatus);
    }

    public Pemasukan(int id, TipeTransaksi tipeTransaksi, int jumlah, Kategori kategori, LocalDate tanggal) {
        super(id, tipeTransaksi, jumlah, kategori, tanggal);
    }

    public Pemasukan() {
    }
}