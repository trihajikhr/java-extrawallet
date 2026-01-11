package model;

import java.time.LocalDate;

public class Pengeluaran extends Transaction {
    public Pengeluaran(int id, TransactionType transactionType, int jumlah, Account account, Category category, LabelType tipelabel, LocalDate tanggal, String keterangan, PaymentType paymentType, PaymentStatus paymentStatus) {
        super(id, transactionType, jumlah, account, category, tipelabel, tanggal, keterangan, paymentType, paymentStatus);
    }

    public Pengeluaran(int id, TransactionType transactionType, int jumlah, Category category, LocalDate tanggal) {
        super(id, transactionType, jumlah, category, tanggal);
    }

    public Pengeluaran() {
    }
}