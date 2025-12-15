package dataflow;

import java.util.ArrayList;
import java.util.Comparator;

import dataflow.basedata.AccountItem;
import dataflow.basedata.ColorItem;
import helper.Popup;
import javafx.scene.image.Image;
import model.MataUang;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataManager {
    private static final Logger log = LoggerFactory.getLogger(DataManager.class);
    private static DataManager instance;

    private ArrayList<Kategori> dataKategori;
    private ArrayList<Akun> dataAkun = new ArrayList<>();
    private ArrayList<Transaksi> dataTransaksi = new ArrayList<>();
    private ArrayList<TipeLabel> dataTipeLabel = new ArrayList<>();
    private ObservableList<String> dataPeymentType = FXCollections.observableArrayList();
    private ObservableList<String> dataStatusType = FXCollections.observableArrayList();
    private ObservableList<ColorItem> dataColor = FXCollections.observableArrayList();
    private ObservableList<AccountItem> dataAccountItem = FXCollections.observableArrayList();
    private ObservableList<MataUang> dataMataUang = FXCollections.observableArrayList();
    private Image[][] theImage;

    private DataManager() {}

    public void initBaseData() {
        dataPeymentType = DataSeeder.getInstance().seedTypeData();
        dataStatusType = DataSeeder.getInstance().seedStatusData();
        DataSeeder.getInstance().seedColor();
        DataSeeder.getInstance().seedAccountItem();
        DataSeeder.getInstance().seedCurrency();
        theImage = DataSeeder.getInstance().seedImageTransactionForm();
    }

    public void fetchDataDatabase() {
        dataAkun = Database.getInstance().fetchAkun();
        dataTipeLabel = Database.getInstance().fetchTipeLabel();
    }

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

    public ArrayList<TipeLabel> getDataTipeLabel() {
        return dataTipeLabel;
    }

    public Image[][] getImageTransactionForm() {
        return theImage;
    }

    public ObservableList<ColorItem> getDataColor() {
        return dataColor;
    }

    public ObservableList<AccountItem> getDataAccountItem() {
        return dataAccountItem;
    }

    public ObservableList<MataUang> getDataMataUang() {
        return dataMataUang;
    }

    // [1] >> =============== DATA AKUN =============== //
    public void addAkun (Akun data) {
        int newId = Database.getInstance().insertAkun(data);
        if(newId > 0) {
            data.setId(newId);
            dataAkun.add(data);
            log.info("akun baru [{}] berhasil dibuat!", data.getNama());
            Popup.showSucces("Akun baru berhasil dibuat!", "Selamat, akun " + data.getNama() + " berhasil dibuat!");
        } else {
            Popup.showDanger("Gagal!", "Terjadi kesalahan!");
        }
    }

    public ArrayList<Akun> getDataAkun() {
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
            trans.setId(newId);
            dataTransaksi.add(trans);
            log.info("transaksi berhasil ditambahkan!");
            Popup.showSucces("Operasi berhasil!", "Transaksi berhasil ditambahkan!");
        } else {
            Popup.showDanger("Gagal!", "Terjadi kesalahan!");
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

    public ArrayList<Kategori> getDataKategori() {
        return dataKategori;
    }

    public void setDataKategori() {
        dataKategori = new ArrayList<>(DataSeeder.getInstance().seedKategori());
    }

    // [6] >> =============== TIPE LABEL FUNCTION =============== //
    public boolean addLabel(TipeLabel tipelabel){
        int newId = Database.getInstance().insertTipeLabel(tipelabel);
        if(newId > 0) {
            tipelabel.setId(newId);
            dataTipeLabel.add(tipelabel);
            log.info("Label baru [{}] berhasil dibuat!", tipelabel.getNama());
            Popup.showSucces("Label baru berhasil dibuat!", "Label " + tipelabel.getNama() + " berhasil dibuat!");
            return true;
        } else {
            Popup.showDanger("Gagal!", "Terjadi kesalahan!");
            return false;
        }
    }
}