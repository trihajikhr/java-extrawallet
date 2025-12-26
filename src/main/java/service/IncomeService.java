package service;

import model.TipeTransaksi;
import model.Transaksi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class IncomeService extends AbstractTransactionService {
    private static final Logger log = LoggerFactory.getLogger(IncomeService.class);
    private static IncomeService instance;
    private final String BASE_CURRENCY = "IDR";

    public IncomeService() {}

    private static class Holder {
        private static final IncomeService INSTANCE = new IncomeService();
    }

    public static IncomeService getInstance() {
        log.info("object incomeservice berhasil dibuat!");
        return IncomeService.Holder.INSTANCE;
    }

    @Override
    protected boolean isTargetType(Transaksi t) {
        return t.getTipeTransaksi() == TipeTransaksi.IN;
    }

    public BigDecimal incomeSumAfterFilter(List<Transaksi> incomeData) {
        BigDecimal sum = BigDecimal.ZERO; // mulai dari 0
        for(Transaksi income : incomeData) {
            if(income.getAkun().getMataUang().getKode().equalsIgnoreCase(BASE_CURRENCY)) {
                BigDecimal amount = BigDecimal.valueOf(income.getJumlah());
                sum = sum.add(amount);
            } else {
                String incomeKode = income.getAkun().getMataUang().getKode();
                BigDecimal amount = BigDecimal.valueOf(CurrencyApiClient.getInstance().convert(income.getJumlah(), incomeKode, BASE_CURRENCY));
                sum = sum.add(amount);
            }
        }
        return sum;
    }
}