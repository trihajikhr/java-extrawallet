package dataflow;

import java.math.BigDecimal;
import java.util.*;

import dataflow.basedata.AccountItem;
import dataflow.basedata.ColorItem;
import helper.MyPopup;
import javafx.scene.image.Image;
import model.MataUang;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.CurrencyApiClient;

// TODO: rapikan struktur fungsi di file ini  (supaya lebih rapi)
// dan hapus beberapa fungsi tidak terpakai

public class DataManager {
    private static final Logger log = LoggerFactory.getLogger(DataManager.class);
    private static DataManager instance;

    private ArrayList<Kategori> dataKategori;
    private ArrayList<Akun> dataAkun = new ArrayList<>();
    private ArrayList<Transaksi> dataTransaksi = new ArrayList<>();
    private ArrayList<TipeLabel> dataTipeLabel = new ArrayList<>();
    private ObservableList<PaymentType> dataPaymentType = FXCollections.observableArrayList();
    private ObservableList<PaymentStatus> dataPaymentStatus = FXCollections.observableArrayList();
    private ObservableList<ColorItem> dataColor = FXCollections.observableArrayList();
    private ObservableList<AccountItem> dataAccountItem = FXCollections.observableArrayList();
    private ObservableList<MataUang> dataMataUang = FXCollections.observableArrayList();
    private ArrayList<Template> dataTemplate;
    private Map<PaymentStatus, Image> paymentStatusImage = new HashMap<>();
    private Image[][] theImage;

    private DataManager() {}

    public void initBaseData() {
        dataPaymentType = DataSeeder.getInstance().seedPaymentType();
        dataPaymentStatus = DataSeeder.getInstance().seedPaymentStatus();
        DataSeeder.getInstance().seedColor();
        DataSeeder.getInstance().seedAccountItem();
        DataSeeder.getInstance().seedCurrency();
        theImage = DataSeeder.getInstance().seedImageTransactionForm();
        paymentStatusImage = DataSeeder.getInstance().seedPaymentStatusImage();
    }

    public void fetchDataDatabase() {
        dataAkun = Database.getInstance().fetchAkun();
        dataTipeLabel = Database.getInstance().fetchTipeLabel();
        dataTemplate = Database.getInstance().fetchTemplate();
        dataTransaksi = Database.getInstance().fetchTransaksi();
    }

    public ArrayList<Template> getDataTemplate() {
        return dataTemplate;
    }

    public ObservableList<PaymentType> getDataPaymentType() {
        return dataPaymentType;
    }

    public ObservableList<PaymentStatus> getDataPaymentStatus() {
        return dataPaymentStatus;
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

    public ArrayList<MataUang> getFilteredMataUang() {
        Set<MataUang> filteredCurrency = new LinkedHashSet<>();
        for(Transaksi trans : dataTransaksi) {
            filteredCurrency.add(trans.getAkun().getMataUang());
        }

        return new ArrayList<>(filteredCurrency);
    }

    public Map<PaymentStatus, Image> getPaymentStatusImage() {
        return paymentStatusImage;
    }

    // [1] >> =============== DATA AKUN =============== //
    public void addAkun (Akun data) {
        int newId = Database.getInstance().insertAkun(data);
        if(newId > 0) {
            data.setId(newId);
            dataAkun.add(data);
            log.info("akun baru [{}] berhasil dibuat!", data.getNama());
            MyPopup.showSucces("Akun baru berhasil dibuat!", "Selamat, akun " + data.getNama() + " berhasil dibuat!");
        } else {
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
        }
    }

    public ArrayList<Akun> getDataAkun() {
        return dataAkun;
    }

    // [1] >> =============== DATA TRANSAKSI =============== //
    public ArrayList<Transaksi> getDataTransaksi() {
        return dataTransaksi;
    }
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
    public ArrayList<Transaksi> getDataTransaksiPemasukan() {
        ArrayList<Transaksi> inList = new ArrayList<>();
        for(Transaksi trans : dataTransaksi) {
            if(trans.getTipeTransaksi() == TipeTransaksi.IN) {
                inList.add(trans);
            }
        }
        log.info("Total income transaction: " + inList.size());
        return inList;
    }

    public void addTransaksi(Transaksi trans) {
        int newId = Database.getInstance().insertTransaksi(trans);
        if(newId > 0) {
            trans.setId(newId);
            dataTransaksi.add(trans);
            log.info("transaksi berhasil ditambahkan!");
            MyPopup.showSucces("Operasi berhasil!", "Transaksi berhasil ditambahkan!");
        } else {
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
        }
    }

    // [3] >> =============== DATA PENGELUARAN =============== //
    public ArrayList<Pengeluaran> getDataTransaksiPengeluaran() {
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

    public ArrayList<Kategori> getFilteredCategory() {
        Set<Kategori> filteredCategory = new LinkedHashSet<>();
        for(Transaksi trans : dataTransaksi) {
            if(trans.getKategori() != null) {
                filteredCategory.add(trans.getKategori());
            }
        }

        return new ArrayList<>(filteredCategory);
    }

    // [6] >> =============== TIPE LABEL FUNCTION =============== //
    public boolean addLabel(TipeLabel tipelabel){
        int newId = Database.getInstance().insertTipeLabel(tipelabel);
        if(newId > 0) {
            tipelabel.setId(newId);
            dataTipeLabel.add(tipelabel);
            log.info("Label baru [{}] berhasil dibuat!", tipelabel.getNama());
            MyPopup.showSucces("Label baru berhasil dibuat!", "Label " + tipelabel.getNama() + " berhasil dibuat!");
            return true;
        } else {
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
            return false;
        }
    }

    public boolean addTemplate(Template temp) {
        int newId = Database.getInstance().insertTemplate(temp);
        if(newId > 0) {
            temp.setId(newId);
            dataTemplate.add(temp);
            log.info("template {} berhasil ditambahkan!", temp.getNama());
            MyPopup.showSucces("Template baru!", "Template " + temp.getNama() + " berhasil ditambahkan!");
            return true;
        } else {
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
            return false;
        }
    }

    public ArrayList<TipeLabel> getFilteredLabel() {
        Set<TipeLabel> filteredLabel = new LinkedHashSet<>();
        for(Transaksi trans : dataTransaksi) {
            if(trans.getTipelabel() != null) {
                filteredLabel.add(trans.getTipelabel());
            }
        }

        return new ArrayList<>(filteredLabel);
    }
}