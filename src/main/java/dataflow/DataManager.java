package dataflow;

import java.util.ArrayList;
import java.util.Comparator;

// import package
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import model.*;
import helper.Converter;

public class DataManager {
    private static DataManager instance;

    private ArrayList<Kategori> dataKategori;
    private ArrayList<Akun> dataAkun;
    private ArrayList<Transaksi> dataTransaksi;
    private ArrayList<TipeLabel> dataTipeLabel;

    public static DataManager getInstance() {
        if(instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    // [1] >> =============== DATA AKUN =============== //
    public void addAkun (Akun data) {
        dataAkun.add(data);
    }

    // [1] >> =============== DATA TRANSAKSI =============== //
    public void sortingAscTanggal() {
        this.dataTransaksi.sort(Comparator.comparing(Transaksi::getTanggal));
    }

    public void sortingDscTanggal() {
        this.dataTransaksi.sort(Comparator.comparing(Transaksi::getTanggal).reversed());
    }

    public void sortingJumlahAscending(){
        this.dataTransaksi.sort(Comparator.comparing(Transaksi::getJumlah));
    }

    public void sortingJumlahDescending() {
        this.dataTransaksi.sort(Comparator.comparing(Transaksi::getJumlah).reversed());
    }

    public ArrayList<Transaksi> copyDataTransaksi() {
        return new ArrayList<>(dataTransaksi);
    }

    public ArrayList<Transaksi> coreDataTransaksi(){
        return dataTransaksi;
    }

    public void removeTransaksi(int id) {
        Database.getInstance().deleteTransaksi(id);
    }


    // [2] >> =============== DATA PEMASUKAN =============== //
    public ArrayList<Pemasukan> getPemasukan() {
        ArrayList<Pemasukan> inList = new ArrayList<>();
        for (Transaksi t : dataTransaksi) {
            if (t instanceof Pemasukan) inList.add((Pemasukan) t);
        }
        return inList;
    }

    public void addTransaksi(Transaksi t) {
        Database.getInstance().insertTransaksi(t);
    }

    // [3] >> =============== DATA PENGELUARAN =============== //
    public ArrayList<Pengeluaran> getPengeluaran() {
        ArrayList<Pengeluaran> outList = new ArrayList<>();
        for (Transaksi t : dataTransaksi) {
            if (t instanceof Pengeluaran) outList.add((Pengeluaran) t);
        }
        return outList;
    }

    // [4] >> =============== DATA TOTAL JUMLAH =============== //
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

    // [5] >> =============== KATEGORI FUNCTION =============== //
    public ArrayList<Kategori> copyDataKategori() {
        return new ArrayList<>(dataKategori);
    }

    public ArrayList<Kategori> coreDataKategori() {
        return dataKategori;
    }

    public void setDataKategori(ObservableList<Kategori> data) {
        dataKategori = new ArrayList<>(DataSeeder.getInstance().seedArrayKategori());
    }

    // [6] >> =============== TIPE LABEL FUNCTION =============== //
    public void addLabel(String nama, Color warna){
        int newId = Database.getInstance().insertTipeLabel(nama, Converter.getInstance().colorToHex(warna));
        if(newId > 0) {
            dataTipeLabel.add(new TipeLabel(newId, nama, warna));
        }
    }
}