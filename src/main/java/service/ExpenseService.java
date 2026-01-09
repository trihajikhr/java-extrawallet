package service;

import model.Akun;
import model.TipeTransaksi;
import model.Transaksi;
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
    protected boolean isTargetType(Transaksi t) {
        return t.getTipeTransaksi() == TipeTransaksi.OUT;
    }

    public BigDecimal expenseSumAfterFilter(List<Transaksi> data) {
        return sumAfterFilter(data);
    }
}