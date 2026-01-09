package model;

import java.time.LocalDate;
import java.util.Objects;

public class Transaksi {
    protected int id;
    protected TipeTransaksi tipeTransaksi;
    protected int jumlah;
    protected Akun akun;
    protected Kategori kategori;
    protected TipeLabel tipelabel;
    protected LocalDate tanggal;
    protected String keterangan;
    protected PaymentType paymentType;
    protected PaymentStatus paymentStatus;

    // contructor full atribut
    public Transaksi(int id, TipeTransaksi tipeTransaksi, int jumlah, Akun akun, Kategori kategori, TipeLabel tipelabel, LocalDate tanggal, String keterangan, PaymentType paymentType, PaymentStatus paymentStatus) {
        this.id = id;
        this.tipeTransaksi = tipeTransaksi;
        this.jumlah = jumlah;
        this.akun = akun;
        this.kategori = kategori;
        this.tipelabel = tipelabel;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
    }

    // constructor hanya atribut wajib
    public Transaksi(int id, TipeTransaksi tipeTransaksi, int jumlah, Kategori kategori, LocalDate tanggal) {
        this.id = id;
        this.tipeTransaksi = tipeTransaksi;
        this.jumlah = jumlah;
        this.kategori = kategori;
        this.tanggal = tanggal;
    }

    public Transaksi() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaksi)) return false;
        Transaksi that = (Transaksi) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    public boolean isSameState(Transaksi t) {
        if (t == null) return false;

        return jumlah == t.jumlah &&
                tipeTransaksi == t.tipeTransaksi &&
                Objects.equals(akun, t.akun) &&
                Objects.equals(kategori, t.kategori) &&
                Objects.equals(tipelabel, t.tipelabel) &&
                Objects.equals(tanggal, t.tanggal) &&
                Objects.equals(keterangan, t.keterangan) &&
                paymentType == t.paymentType &&
                paymentStatus == t.paymentStatus;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipeTransaksi getTipeTransaksi() {
        return tipeTransaksi;
    }

    public void setTipeTransaksi(TipeTransaksi tipeTransaksi) {
        this.tipeTransaksi = tipeTransaksi;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public Akun getAkun() {
        return akun;
    }

    public void setAkun(Akun akun) {
        this.akun = akun;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public TipeLabel getTipelabel() {
        return tipelabel;
    }

    public void setTipelabel(TipeLabel tipelabel) {
        this.tipelabel = tipelabel;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}