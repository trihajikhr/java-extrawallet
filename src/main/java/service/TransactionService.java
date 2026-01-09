package service;

import model.Akun;
import model.Kategori;
import model.TipeLabel;
import model.Transaksi;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    BigDecimal sumAfterFilter(
            List<Transaksi> dataTransaksi
    );

    List<Transaksi> filterByDate(
            List<Transaksi> dataTransaksi,
            LocalDate startDate,
            LocalDate endDate
    );

    BigDecimal sumBetween(
            List<Transaksi> dataTransaksi,
            LocalDate startDate,
            LocalDate endDate
    );

    BigDecimal sumByCategory(
            List<Transaksi> dataTransaksi,
            Kategori kategori,
            LocalDate startDate,
            LocalDate endDate
    );

    BigDecimal sumByAccount(
            List<Transaksi> dataTransaksi,
            Akun akun,
            LocalDate startDate,
            LocalDate endDate
    );

    BigDecimal sumByLabel(
            List<Transaksi> dataTransaksi,
            TipeLabel label,
            LocalDate startDate,
            LocalDate endDate
    );

    Boolean updateSingleAkun(Akun akun, Transaksi oldTrans, int newJumlah);

    Boolean updateMultipleAkun(
            List<Transaksi> selected
    );
}