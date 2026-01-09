package service;

import dataflow.DataManager;
import helper.MyPopup;
import model.*;

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

    @Override
    public Boolean updateSingleAkun(Akun akun, Transaksi oldTrans, int newJumlah) {
        TipeTransaksi tipe = oldTrans.getTipeTransaksi();
        int oldJumlah = oldTrans.getJumlah();
        int selisih = 0;

        switch (tipe) {
            case IN -> selisih = newJumlah - oldJumlah;
            case OUT -> selisih = oldJumlah - newJumlah;
        }

        if (tipe == TipeTransaksi.OUT && selisih > akun.getJumlah()) {
            MyPopup.showDanger("Saldo kurang!", "Saldo anda: " + akun.getJumlah());
            return false;
        }

        int newSaldo = akun.getJumlah() + selisih;
        akun.setJumlah(newSaldo);

        return DataManager.getInstance().updateSaldoAkun(akun, newSaldo);
    }

    @Override
    public Boolean updateMultipleAkun(List<Transaksi> selected) {
        for (Transaksi trans : selected) {
            Akun akun = trans.getAkun();
            int jumlah = trans.getJumlah();
            int newSaldo;

            if (trans.getTipeTransaksi() == TipeTransaksi.IN) {
                newSaldo = akun.getJumlah() + jumlah;
            } else {
                if (jumlah > akun.getJumlah()) {
                    MyPopup.showDanger("Saldo kurang!", "Akun " + akun.getNama() + " saldo: " + akun.getJumlah());
                    return false;
                }
                newSaldo = akun.getJumlah() - jumlah;
            }

            boolean result = DataManager.getInstance().updateSaldoAkun(akun, newSaldo);
            if (!result) {
                MyPopup.showDanger("Gagal update saldo untuk akun: " + akun.getNama(), "Coba lagi nanti.");
                return false;
            }

            akun.setJumlah(newSaldo);
        }

        return true;
    }

    protected abstract boolean isTargetType(Transaksi t);
}
