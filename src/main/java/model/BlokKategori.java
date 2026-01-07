package model;

import java.math.BigDecimal;

public class BlokKategori{
    private Kategori kategori;
    private int totalUsed;
    private BigDecimal totalAmount;

    public BlokKategori(Kategori kategori, int counter, BigDecimal totalAmount) {
        this.kategori = kategori;
        this.totalUsed = counter;
        this.totalAmount = totalAmount;
    }

    public BlokKategori(){}

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public int getTotalUsed() {
        return totalUsed;
    }

    public void setTotalUsed(int totalUsed) {
        this.totalUsed = totalUsed;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}