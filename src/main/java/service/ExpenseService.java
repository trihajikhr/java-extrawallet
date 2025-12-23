package service;

import model.TipeTransaksi;
import model.Transaksi;
import java.util.List;

public class ExpenseService extends AbstractTransactionService {

    public ExpenseService(List<Transaksi> data) {
        super(data);
    }

    @Override
    protected boolean isTargetType(Transaksi t) {
        return t.getTipeTransaksi() == TipeTransaksi.OUT;
    }
}