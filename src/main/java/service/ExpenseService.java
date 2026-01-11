package service;

import model.enums.TransactionType;
import model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class ExpenseService extends AbstractTransactionService {
    private static final Logger log = LoggerFactory.getLogger(ExpenseService.class);

    public ExpenseService() {
        log.info("object expenseservice berhasil dibuat!");
    }

    private static class Holder {
        private static final ExpenseService INSTANCE = new ExpenseService();
    }

    public static ExpenseService getInstance() {
        return ExpenseService.Holder.INSTANCE;
    }

    @Override
    protected boolean isTargetType(Transaction t) {
        return t.getTransactionType() == TransactionType.EXPANSE;
    }

    @Override
    protected BigDecimal calculateSaldoDelta(BigDecimal oldAmount, BigDecimal newAmount) {
        return oldAmount.subtract(newAmount);
    }

    public BigDecimal expenseSumAfterFilter(List<Transaction> data) {
        return sumAfterFilter(data);
    }

}