package service;

import dataflow.DataManager;
import helper.MyPopup;
import model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public abstract class AbstractTransactionService implements TransactionService {
    protected static final String BASE_CURRENCY = "IDR";
    protected AbstractTransactionService(){}

    @Override
    public List<Transaction> filterByDate(List<Transaction> dataTransaction, LocalDate startDate, LocalDate endDate) {
        return dataTransaction.stream()
                .filter(this::isTargetType)
                .filter(t ->
                        !t.getDate().isBefore(startDate) &&
                                !t.getDate().isAfter(endDate)
                )
                .toList();
    }

    protected BigDecimal normalizeAmount(Transaction trans) {
        BigDecimal amount = BigDecimal.valueOf(trans.getAmount());
        String currency = trans.getAkun().getCurrencyType().getKode();

        if (currency.equalsIgnoreCase(BASE_CURRENCY)) {
            return amount;
        }

        BigDecimal converted = CurrencyApiClient.getInstance().convert(amount, currency, BASE_CURRENCY);

        return converted;
    }

    @Override
    public BigDecimal sumBetween(List<Transaction> dataTransaction, LocalDate startDate, LocalDate endDate) {
        BigDecimal sum = BigDecimal.ZERO;
        for(Transaction trans : filterByDate(dataTransaction, startDate, endDate)) {
            sum = sum.add(normalizeAmount(trans));
        }
        return sum;
    }

    @Override
    public BigDecimal sumByCategory(List<Transaction> dataTransaction, Category category, LocalDate startDate, LocalDate endDate) {
        BigDecimal sum = BigDecimal.ZERO;
        for(Transaction trans : filterByDate(dataTransaction, startDate, endDate)){
            if(trans.getKategori().equals(category)) {
                sum = sum.add(normalizeAmount(trans));
            }
        }
        return sum;
    }

    @Override
    public BigDecimal sumByAccount(List<Transaction> dataTransaction, Account account, LocalDate startDate, LocalDate endDate) {
        BigDecimal sum = BigDecimal.ZERO;
        for(Transaction trans : filterByDate(dataTransaction, startDate,endDate)) {
            if(trans.getAkun().equals(account)) {
                sum = sum.add(normalizeAmount(trans));
            }
        }
        return sum;
    }

    @Override
    public BigDecimal sumByLabel(List<Transaction> dataTransaction, LabelType label, LocalDate startDate, LocalDate endDate) {
        BigDecimal sum = BigDecimal.ZERO;
        for(Transaction trans : filterByDate(dataTransaction, startDate,endDate)) {
            if(trans.getLabelType().equals(label)) {
                sum = sum.add(normalizeAmount(trans));
            }
        }
        return sum;
    }

    @Override
    public BigDecimal sumAfterFilter(List<Transaction> dataTransaction) {
        BigDecimal sum = BigDecimal.ZERO; // mulai dari 0
        for(Transaction trans : dataTransaction) {
            sum = sum.add(normalizeAmount(trans));
        }
        return sum;
    }

    @Override
    public Boolean updateSingleAkun(Account account, Transaction oldTrans, int newJumlah) {
        int oldJumlah = oldTrans.getAmount();

        int delta = calculateSaldoDelta(oldJumlah, newJumlah);

        if (delta < 0 && Math.abs(delta) > account.getBalance()) {
            MyPopup.showDanger("Saldo kurang!", "Saldo anda: " + account.getBalance());
            return false;
        }

        int newSaldo = account.getBalance() + delta;
        account.setBalance(newSaldo);

        return DataManager.getInstance().updateSaldoAkun(account, newSaldo);
    }

    @Override
    public Boolean updateMultipleAkun(List<Transaction> selected) {

        for (Transaction trans : selected) {
            Account account = trans.getAkun();
            int jumlah = trans.getAmount();

            int delta = calculateSaldoDelta(0, jumlah);

            if (delta < 0 && Math.abs(delta) > account.getBalance()) {
                MyPopup.showDanger(
                        "Saldo kurang!",
                        "Account " + account.getName() + " saldo: " + account.getBalance()
                );
                return false;
            }

            int newSaldo = account.getBalance() + delta;

            boolean result = DataManager.getInstance().updateSaldoAkun(account, newSaldo);
            if (!result) {
                MyPopup.showDanger(
                        "Gagal update saldo untuk account: " + account.getName(),
                        "Coba lagi nanti."
                );
                return false;
            }

            account.setBalance(newSaldo);
        }

        return true;
    }

    protected abstract int calculateSaldoDelta(int oldJumlah, int newJumlah);

    protected abstract boolean isTargetType(Transaction t);
}
