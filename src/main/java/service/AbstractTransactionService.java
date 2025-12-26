package service;

import dataflow.DataManager;
import model.Akun;
import model.Kategori;
import model.TipeLabel;
import model.Transaksi;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public abstract class AbstractTransactionService implements TransactionService {
    protected static final String BASE_CURRENCY = "IDR";
    protected AbstractTransactionService(){}

    @Override
    public List<Transaksi> filterByDate(List<Transaksi> dataTransaksi, LocalDate startDate, LocalDate endDate) {
        return dataTransaksi.stream()
                .filter(this::isTargetType)
                .filter(t ->
                        !t.getTanggal().isBefore(startDate) &&
                                !t.getTanggal().isAfter(endDate)
                )
                .toList();
    }

    protected BigDecimal normalizeAmount(Transaksi trans) {
        BigDecimal amount = BigDecimal.valueOf(trans.getJumlah());
        String currency = trans.getAkun().getMataUang().getKode();

        if (currency.equalsIgnoreCase(BASE_CURRENCY)) {
            return amount;
        }

        BigDecimal converted = CurrencyApiClient.getInstance().convert(amount, currency, BASE_CURRENCY);

        return converted;
    }

    @Override
    public BigDecimal sumBetween(List<Transaksi> dataTransaksi, LocalDate startDate, LocalDate endDate) {
        BigDecimal sum = BigDecimal.ZERO;
        for(Transaksi trans : filterByDate(dataTransaksi, startDate, endDate)) {
            sum = sum.add(normalizeAmount(trans));
        }
        return sum;
    }

    @Override
    public BigDecimal sumByCategory(List<Transaksi> dataTransaksi, Kategori kategori, LocalDate startDate, LocalDate endDate) {
        BigDecimal sum = BigDecimal.ZERO;
        for(Transaksi trans : filterByDate(dataTransaksi, startDate, endDate)){
            if(trans.getKategori().equals(kategori)) {
                sum = sum.add(normalizeAmount(trans));
            }
        }
        return sum;
    }

    @Override
    public BigDecimal sumByAccount(List<Transaksi> dataTransaksi, Akun akun, LocalDate startDate, LocalDate endDate) {
        BigDecimal sum = BigDecimal.ZERO;
        for(Transaksi trans : filterByDate(dataTransaksi, startDate,endDate)) {
            if(trans.getAkun().equals(akun)) {
                sum = sum.add(normalizeAmount(trans));
            }
        }
        return sum;
    }

    @Override
    public BigDecimal sumByLabel(List<Transaksi> dataTransaksi, TipeLabel label, LocalDate startDate, LocalDate endDate) {
        BigDecimal sum = BigDecimal.ZERO;
        for(Transaksi trans : filterByDate(dataTransaksi, startDate,endDate)) {
            if(trans.getTipelabel().equals(label)) {
                sum = sum.add(normalizeAmount(trans));
            }
        }
        return sum;
    }

    @Override
    public BigDecimal sumAfterFilter(List<Transaksi> dataTransaksi) {
        BigDecimal sum = BigDecimal.ZERO; // mulai dari 0
        for(Transaksi trans : dataTransaksi) {
            sum = sum.add(normalizeAmount(trans));
        }
        return sum;
    }

    protected abstract boolean isTargetType(Transaksi t);
}
