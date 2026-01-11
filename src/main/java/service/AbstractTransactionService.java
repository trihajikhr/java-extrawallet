package service;

import dataflow.DataManager;
import helper.MyPopup;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public abstract class AbstractTransactionService implements TransactionService {

    private static final Logger log = LoggerFactory.getLogger(AbstractTransactionService.class);
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
        BigDecimal amount = trans.getAmount();
        String currency = trans.getAccount().getCurrencyType().getCode();

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
            if(trans.getCategory().equals(category)) {
                sum = sum.add(normalizeAmount(trans));
            }
        }
        return sum;
    }

    @Override
    public BigDecimal sumByAccount(List<Transaction> dataTransaction, Account account, LocalDate startDate, LocalDate endDate) {
        BigDecimal sum = BigDecimal.ZERO;
        for(Transaction trans : filterByDate(dataTransaction, startDate,endDate)) {
            if(trans.getAccount().equals(account)) {
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
    public Boolean updateSingleAkun(Account account, Transaction oldTrans, BigDecimal newAmount) {
        BigDecimal oldAmount = oldTrans.getAmount();

        BigDecimal delta = calculateSaldoDelta(oldAmount, newAmount);

        if (delta.compareTo(BigDecimal.ZERO) < 0 && delta.abs().compareTo(account.getBalance()) > 0) {
            MyPopup.showDanger("Saldo kurang!", "Saldo anda: " + account.getBalance());
            return false;
        }

        BigDecimal newSaldo = account.getBalance().add(delta);
        account.setBalance(newSaldo);

        return DataManager.getInstance().updateSaldoAkun(account, newSaldo);
    }

    @Override
    public Boolean updateMultipleAkun(List<Transaction> selected) {

        for (Transaction trans : selected) {
            Account account = trans.getAccount();
            BigDecimal jumlah = trans.getAmount();

            BigDecimal delta = calculateSaldoDelta(0, jumlah);

            if (delta.compareTo(BigDecimal.ZERO) < 0 && delta.abs().compareTo(account.getBalance()) > 0) {
                MyPopup.showDanger(
                        "Saldo kurang!",
                        "Account " + account.getName() + " saldo: " + account.getBalance()
                );
                return false;
            }

            BigDecimal newSaldo = account.getBalance().add(delta);

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

    protected abstract BigDecimal calculateSaldoDelta(BigDecimal oldAmount, BigDecimal newAmount);

    protected abstract boolean isTargetType(Transaction t);
}
