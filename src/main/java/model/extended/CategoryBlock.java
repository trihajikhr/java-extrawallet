package model.extended;

import model.Category;

import java.math.BigDecimal;

public class CategoryBlock {
    private Category category;
    private int totalUsed;
    private BigDecimal totalAmount;

    public CategoryBlock(Category category, int counter, BigDecimal totalAmount) {
        this.category = category;
        this.totalUsed = counter;
        this.totalAmount = totalAmount;
    }

    public CategoryBlock(){}

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
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