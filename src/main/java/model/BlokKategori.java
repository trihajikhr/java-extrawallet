package model;

import java.math.BigDecimal;

public class BlokKategori{
    private Category category;
    private int totalUsed;
    private BigDecimal totalAmount;

    public BlokKategori(Category category, int counter, BigDecimal totalAmount) {
        this.category = category;
        this.totalUsed = counter;
        this.totalAmount = totalAmount;
    }

    public BlokKategori(){}

    public Category getKategori() {
        return category;
    }

    public void setKategori(Category category) {
        this.category = category;
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