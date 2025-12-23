package service;

import model.TipeTransaksi;
import model.Transaksi;
import java.util.List;

public class IncomeService extends AbstractTransactionService {

    public IncomeService(List<Transaksi> data) {
        super(data);
    }

    @Override
    protected boolean isTargetType(Transaksi t) {
        return t.getTipeTransaksi() == TipeTransaksi.IN;
    }
}