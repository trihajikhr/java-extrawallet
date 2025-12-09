package dataflow;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

// import package
import model.Kategori;
import model.Pengeluaran;
import model.Pemasukan;
import model.Transaksi;

public class DataManager {
    private static DataManager instance;
    private ArrayList<Transaksi> dataTransaksi = Database.getInstance().fetchTransaksi();
    private ArrayList<Kategori> dataKategori = Database.getInstance().fetchKategori();

    public static DataManager getInstance() {
        if(instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void sortingAscTanggal() {
        this.dataTransaksi.sort(Comparator.comparing(Transaksi::getTanggalSet));
    }

    public void sortingDscTanggal() {
        this.dataTransaksi.sort(Comparator.comparing(Transaksi::getTanggalSet).reversed());
    }

    public void sortingJumlahAscending(){
        this.dataTransaksi.sort(Comparator.comparing(Transaksi::getJumlah));
    }

    public void sortingJumlahDescending() {
        this.dataTransaksi.sort(Comparator.comparing(Transaksi::getJumlah).reversed());
    }

    // return copy biar data asli aman
    public ArrayList<Transaksi> getAllTransaksi() {
        return new ArrayList<>(dataTransaksi);
    }

    public ArrayList<Kategori> getAllKategori() {
        return new ArrayList<>(dataKategori);
    }

    // akses data asli
    public ArrayList<Transaksi> coreTransaksi(){
        return dataTransaksi;
    }

    public ArrayList<Kategori> coreKategori() {
        return dataKategori;
    }

    public ArrayList<Pemasukan> getPemasukan() {
        ArrayList<Pemasukan> inList = new ArrayList<>();
        for (Transaksi t : dataTransaksi) {
            if (t instanceof Pemasukan) inList.add((Pemasukan) t);
        }
        return inList;
    }

    public ArrayList<Pengeluaran> getPengeluaran() {
        ArrayList<Pengeluaran> outList = new ArrayList<>();
        for (Transaksi t : dataTransaksi) {
            if (t instanceof Pengeluaran) outList.add((Pengeluaran) t);
        }
        return outList;
    }

    public void addTransaksi(Transaksi t) {
        dataTransaksi.add(t);
        Database.getInstance().insertTransaksi(t); // kalau mau langsung save
    }

    public void removeTransaksi(Transaksi t) {
        dataTransaksi.remove(t);
        Database.getInstance().deleteTransaksi(t.getId());
    }

    public int getTotalSaldo() {
        int total = 0;
        for(Transaksi t : dataTransaksi) {
            total += t instanceof Pemasukan ? t.getJumlah() : -t.getJumlah();
        }
        return total;
    }

    public int getTotalPemasukan() {
        int total = 0;
        for(Transaksi t : dataTransaksi) {
            total += t instanceof Pemasukan ? t.getJumlah() : 0;
        }
        return total;
    }

    public int getTotalPengeluaran() {
        int total = 0;
        for(Transaksi t : dataTransaksi) {
            total += t instanceof Pengeluaran ? t.getJumlah() : 0;
        }
        return total;
    }
}