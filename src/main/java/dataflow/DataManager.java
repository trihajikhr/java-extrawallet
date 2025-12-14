package dataflow;

import java.util.ArrayList;
import java.util.Comparator;

import dataflow.basedata.AccountItem;
import dataflow.basedata.ColorItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import model.*;

public class DataManager {
    private static DataManager instance;

    private ArrayList<Kategori> dataKategori;
    private ArrayList<Akun> dataAkun;
    private ArrayList<Transaksi> dataTransaksi;
    private ArrayList<TipeLabel> dataTipeLabel;
    private ObservableList<String> dataPeymentType = FXCollections.observableArrayList();
    private ObservableList<String> dataStatusType = FXCollections.observableArrayList();
    private ObservableList<ColorItem> dataColor = FXCollections.observableArrayList();
    private ObservableList<AccountItem> dataAccountItem = FXCollections.observableArrayList();

    private DataManager() {}

    public ObservableList<String> getDataPeymentType() {
        return dataPeymentType;
    }

    public ObservableList<String> getDataStatusType() {
        return dataStatusType;
    }

    public static DataManager getInstance() {
        if(instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public ArrayList<TipeLabel> coreDataTipeLabel() {
        return dataTipeLabel;
    }

    public void initBaseData() {
        dataPeymentType = DataSeeder.getInstance().seedTypeData();
        dataStatusType = DataSeeder.getInstance().seedStatusData();
        DataSeeder.getInstance().colorSeeder();
        DataSeeder.getInstance().accountItemSeeder();
    }

    public ObservableList<ColorItem> getDataColor() {
        return dataColor;
    }

    public ObservableList<AccountItem> getDataAccountItem() {
        return dataAccountItem;
    }

    // [1] >> =============== DATA AKUN =============== //
    public void addAkun (Akun data) {
        dataAkun.add(data);
    }

    public ArrayList<Akun> coreDataAkun() {
        return dataAkun;
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

    public void addTransaksi(Transaksi trans) {
        int newId = Database.getInstance().insertTransaksi(trans);
        if(newId > 0) {
            dataTransaksi.add(trans);
        }
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

    public void setDataKategori() {
        dataKategori = new ArrayList<>(DataSeeder.getInstance().seedArrayKategori());
    }

    // [6] >> =============== TIPE LABEL FUNCTION =============== //
    public void addLabel(TipeLabel tipelabel){
        int newId = Database.getInstance().insertTipeLabel(tipelabel);
        if(newId > 0) {
            dataTipeLabel.add(tipelabel);
        }
    }
}