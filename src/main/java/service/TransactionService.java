package service;

import model.Account;
import model.Category;
import model.LabelType;
import model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    BigDecimal sumAfterFilter(
            List<Transaction> dataTransaction
    );

    List<Transaction> filterByDate(
            List<Transaction> dataTransaction,
            LocalDate startDate,
            LocalDate endDate
    );

    BigDecimal sumBetween(
            List<Transaction> dataTransaction,
            LocalDate startDate,
            LocalDate endDate
    );

    BigDecimal sumByCategory(
            List<Transaction> dataTransaction,
            Category category,
            LocalDate startDate,
            LocalDate endDate
    );

    BigDecimal sumByAccount(
            List<Transaction> dataTransaction,
            Account account,
            LocalDate startDate,
            LocalDate endDate
    );

    BigDecimal sumByLabel(
            List<Transaction> dataTransaction,
            LabelType label,
            LocalDate startDate,
            LocalDate endDate
    );

    Boolean updateSingleAkun(
            Account account,
            Transaction oldTrans,
            BigDecimal newAmount
    );

    Boolean updateMultipleAkun(
            List<Transaction> selected
    );
}