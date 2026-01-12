package model;

import model.enums.PaymentStatus;
import model.enums.PaymentType;
import model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Transaction {
    private int id;
    private TransactionType transactionType;
    private BigDecimal amount;
    private Account account;
    private Category category;
    private LabelType labelType;
    private LocalDate date;
    private String description;
    private PaymentType paymentType;
    private PaymentStatus paymentStatus;

    // full field constructor
    public Transaction(int id, TransactionType transactionType, BigDecimal amount, Account account, Category category, LabelType labelType, LocalDate date, String description, PaymentType paymentType, PaymentStatus paymentStatus) {
        this.id = id;
        this.transactionType = transactionType;
        this.amount = amount;
        this.account = account;
        this.category = category;
        this.labelType = labelType;
        this.date = date;
        this.description = description;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
    }

    // mid field constructor
    public Transaction(int id, TransactionType transactionType, BigDecimal amount, Category category, LocalDate date) {
        this.id = id;
        this.transactionType = transactionType;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // Default constructor for manual field assignment
    public Transaction() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    public Boolean isSameState(Transaction t) {
        if (t == null) return false;

        return Objects.equals(amount, t.amount) &&
                transactionType == t.transactionType &&
                Objects.equals(account, t.account) &&
                Objects.equals(category, t.category) &&
                Objects.equals(labelType, t.labelType) &&
                Objects.equals(date, t.date) &&
                Objects.equals(description, t.description) &&
                paymentType == t.paymentType &&
                paymentStatus == t.paymentStatus;
    }

    public void showData() {
        System.out.println("ID              : " + this.id);
        System.out.println("TransactionType : " + this.transactionType.getLabel());
        System.out.println("Jumlah          : " + this.amount);
        System.out.println("Account         : " + this.account.getName());
        System.out.println("Category        : " + this.category.getName());
        System.out.println("LabelType       : " + (this.labelType != null ? this.labelType.getName() : ""));
        System.out.println("Keterangan      : " + (this.description != null ? this.description : ""));
        System.out.println("PaymentType     : " + (this.paymentType != null ? this.paymentType.getLabel() : ""));
        System.out.println("PaymentStatus   : " + (this.paymentStatus != null ? this.paymentStatus.getLabel() : ""));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LabelType getLabelType() {
        return labelType;
    }

    public void setLabelType(LabelType labelType) {
        this.labelType = labelType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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