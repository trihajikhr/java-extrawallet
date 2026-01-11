package model;

public class Template {
    private int id;
    private TransactionType transactionType;
    private String nama;
    private int jumlah;
    private Account account;
    private Category category;
    private LabelType labelType;
    private String keterangan;
    private PaymentType paymentType;
    private PaymentStatus paymentStatus;

    // constructor full atribut
    public Template(int id, TransactionType transactionType, String nama, int jumlah, Account account, Category category, LabelType labelType, String keterangan, PaymentType paymentType, PaymentStatus paymentStatus) {
        this.id = id;
        this.transactionType = transactionType;
        this.nama = nama;
        this.jumlah = jumlah;
        this.account = account;
        this.category = category;
        this.labelType = labelType;
        this.keterangan = keterangan;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
    }

    // constructor atribut wajib
    public Template(int id, String nama, Account account, TransactionType transactionType, int jumlah) {
        this.id = id;
        this.nama = nama;
        this.account = account;
        this.transactionType = transactionType;
        this.jumlah = jumlah;
    }

    // constructor custom
    public Template() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransactionType getTipeTransaksi() {
        return transactionType;
    }

    public void setTipeTransaksi(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public Account getAkun() {
        return account;
    }

    public void setAkun(Account account) {
        this.account = account;
    }

    public Category getKategori() {
        return category;
    }

    public void setKategori(Category category) {
        this.category = category;
    }

    public LabelType getTipeLabel() {
        return labelType;
    }

    public void setTipeLabel(LabelType labelType) {
        this.labelType = labelType;
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