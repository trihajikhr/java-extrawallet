package model;

import model.enums.*;
import java.math.BigDecimal;

public class Template {
    private int id;
    private TransactionType transactionType;
    private String name;
    private BigDecimal amount;
    private Account account;
    private Category category;
    private LabelType labelType;
    private String description;
    private PaymentType paymentType;
    private PaymentStatus paymentStatus;

    // full field constructor
    public Template(int id, TransactionType transactionType, String name, BigDecimal amount, Account account, Category category, LabelType labelType, String description, PaymentType paymentType, PaymentStatus paymentStatus) {
        this.id = id;
        this.transactionType = transactionType;
        this.name = name;
        this.amount = amount;
        this.account = account;
        this.category = category;
        this.labelType = labelType;
        this.description = description;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
    }

    // mid field constructor
    public Template(int id, String name, Account account, TransactionType transactionType, BigDecimal amount) {
        this.id = id;
        this.name = name;
        this.account = account;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    // Default constructor for manual field assignment
    public Template() {}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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