package service;

import dataflow.DataManager;
import model.Akun;
import model.Kategori;
import model.TipeLabel;
import model.Transaksi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ExpensManager implements TransactionService {

    private static final Logger log = LoggerFactory.getLogger(ExpensManager.class);
    private final List<Transaksi> data;

    @Override
    public List<Transaksi> filterByDate(LocalDate startDate, LocalDate endDate) {
        return DataManager.getInstance().getDataTransaksi().stream()
                .filter(t ->
                        !t.getTanggal().isBefore(startDate) &&
                                !t.getTanggal().isAfter(endDate)
                )
                .toList();
    }

    @Override
    public int sumBetween(LocalDate startDate, LocalDate endDate) {
        return filterByDate(startDate, endDate).stream()
                .mapToInt(Transaksi::getJumlah)
                .sum();
    }

    @Override
    public int sumByCategory(Kategori kategori, LocalDate startDate, LocalDate endDate) {
        int sum = 0;
        for(Transaksi trans : filterByDate(startDate, endDate)) {
            if(trans.getKategori().equals(kategori)) {
                sum += trans.getJumlah();
            }
        }
        return sum;
    }

    @Override
    public int sumByAccount(Akun akun, LocalDate startDate, LocalDate endDate) {
        int sum = 0;
        for(Transaksi trans : filterByDate(startDate, endDate)) {
            if(trans.getAkun().equals(akun)) {
                sum += trans.getJumlah();
            }
        }
        return sum;
    }

    @Override
    public int sumByLabel(TipeLabel label, LocalDate startDate, LocalDate endDate) {
        int sum = 0;
        for(Transaksi trans : filterByDate(startDate, endDate)) {
            if(trans.getTipelabel().equals(label)) {
                sum += trans.getJumlah();
            }
        }
        return sum;
    }
}
