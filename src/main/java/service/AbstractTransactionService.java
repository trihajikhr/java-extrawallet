package service;

import model.Akun;
import model.Kategori;
import model.TipeLabel;
import model.Transaksi;

import java.time.LocalDate;
import java.util.List;

public abstract class AbstractTransactionService implements TransactionService {
    protected final List<Transaksi> dataTransaksi;

    protected AbstractTransactionService(List<Transaksi> data){
        this.dataTransaksi = data;
    }

    @Override
    public List<Transaksi> filterByDate(LocalDate startDate, LocalDate endDate) {
        return dataTransaksi.stream()
                .filter(this::isTargetType)
                .filter(t ->
                        !t.getTanggal().isBefore(startDate) &&
                                !t.getTanggal().isAfter(endDate)
                )
                .toList();
    }

    @Override
    public int sumBetween(LocalDate startDate, LocalDate endDate) {
        int sum = 0;
        for(Transaksi t : filterByDate(startDate, endDate)) {
            sum += t.getJumlah();
        }
        return sum;
    }

    @Override
    public int sumByCategory(Kategori kategori, LocalDate startDate, LocalDate endDate) {
        int sum = 0;
        for(Transaksi t : filterByDate(startDate, endDate)){
            if(t.getKategori().equals(kategori)) {
                sum += t.getJumlah();
            }
        }
        return sum;
    }

    @Override
    public int sumByAccount(Akun akun, LocalDate startDate, LocalDate endDate) {
        int sum = 0;
        for(Transaksi t : filterByDate(startDate,endDate)) {
            if(t.getAkun().equals(akun)) {
                sum += t.getJumlah();
            }
        }
        return sum;
    }

    @Override
    public int sumByLabel(TipeLabel label, LocalDate startDate, LocalDate endDate) {
        int sum = 0;
        for(Transaksi t : filterByDate(startDate,endDate)) {
            if(t.getTipelabel().equals(label)) {
                sum += t.getJumlah();
            }
        }
        return sum;
    }

    protected abstract boolean isTargetType(Transaksi t);
}
