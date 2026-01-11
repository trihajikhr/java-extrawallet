package service;

import model.enums.TransactionType;
import model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class IncomeService extends AbstractTransactionService {
    private static final Logger log = LoggerFactory.getLogger(IncomeService.class);

    public IncomeService() {
        log.info("object incomeservice berhasil dibuat!");
    }

    private static class Holder {
        private static final IncomeService INSTANCE = new IncomeService();
    }

    public static IncomeService getInstance() {
        return IncomeService.Holder.INSTANCE;
    }

    @Override
    protected boolean isTargetType(Transaction t) {
        return t.getTransactionType() == TransactionType.INCOME;
    }

    @Override
    protected BigDecimal calculateSaldoDelta(BigDecimal oldAmount, BigDecimal newAmount) {
        return newAmount.subtract(oldAmount);
    }

    public BigDecimal incomeSumAfterFilter(List<Transaction> data) {
        return sumAfterFilter(data);
    }
}