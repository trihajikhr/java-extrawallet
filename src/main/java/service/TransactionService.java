package service;

import model.Akun;
import model.Kategori;
import model.TipeLabel;
import model.Transaksi;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    List<Transaksi> filterByDate(
            LocalDate startDate,
            LocalDate endDate
    );

    int sumBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    int sumByCategory(
            Kategori kategori,
            LocalDate startDate,
            LocalDate endDate
    );

    int sumByAccount(
            Akun akun,
            LocalDate startDate,
            LocalDate endDate
    );

    int sumByLabel(
            TipeLabel label,
            LocalDate startDate,
            LocalDate endDate
    );
}