package dataflow;

import java.math.BigDecimal;
import java.util.*;
import dataflow.basedata.AccountItem;
import dataflow.basedata.ColorItem;
import helper.MyPopup;
import javafx.scene.image.Image;
import model.Currency;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import model.enums.PaymentStatus;
import model.enums.PaymentType;
import model.enums.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataManager {
    private static final Logger log = LoggerFactory.getLogger(DataManager.class);
    private static DataManager instance;

    private ArrayList<Category> dataCategory;
    private ArrayList<Account> accountData = new ArrayList<>();
    private ArrayList<Transaction> dataTransaction = new ArrayList<>();
    private ArrayList<LabelType> dataLabelType = new ArrayList<>();
    private ObservableList<PaymentType> dataPaymentType = FXCollections.observableArrayList();
    private ObservableList<PaymentStatus> dataPaymentStatus = FXCollections.observableArrayList();
    private ObservableList<ColorItem> dataColor = FXCollections.observableArrayList();
    private ObservableList<AccountItem> dataAccountItem = FXCollections.observableArrayList();
    private ObservableList<Currency> dataCurrency = FXCollections.observableArrayList();
    private ArrayList<Template> dataTemplate;
    private Map<PaymentStatus, Image> paymentStatusImage = new HashMap<>();
    private Image[][] theImage;

    private DataManager() {}

    public static DataManager getInstance() {
        if(instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void initBaseData() {
        dataPaymentType = DataSeeder.seedPaymentType();
        dataPaymentStatus = DataSeeder.seedPaymentStatus();
        DataSeeder.seedColor();
        DataSeeder.seedAccountItem();
        DataSeeder.seedCurrency();
        theImage = DataSeeder.seedImageTransactionForm();
        paymentStatusImage = DataSeeder.seedPaymentStatusImage();

    }

    public void setupDefaultAcount() {
        if(accountData.isEmpty()) {
            Account account = new Account(
                 1,
                 "General",
                  dataColor.get(1).getColor(),
                  dataAccountItem.get(0).getIcon(),
                  dataAccountItem.get(0).getIconPath(),
                   BigDecimal.ZERO,
                   dataCurrency.get(0)
            );

            accountData.add(account);
            addAccount(account, false);
            log.info("account default dibuat untuk penggunaan pertama user! account [general]");
        }
    }

    public void fetchDataDatabase() {
        accountData = Database.getInstance().fetchAkun();
        dataLabelType = Database.getInstance().fetchTipeLabel();
        dataTemplate = Database.getInstance().fetchTemplate();
        dataTransaction = Database.getInstance().fetchTransaction();
    }

    // [1] >=== RETURN DATA DATAMANAGER
    public ArrayList<Template> getDataTemplate() {
        return dataTemplate;
    }
    public ObservableList<PaymentType> getDataPaymentType() {
        return dataPaymentType;
    }
    public ObservableList<PaymentStatus> getDataPaymentStatus() {
        return dataPaymentStatus;
    }
    public ArrayList<LabelType> getDataTipeLabel() {
        return dataLabelType;
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

    public ObservableList<Currency> getDataMataUang() {
        return dataCurrency;
    }

    public ArrayList<Currency> getFilteredMataUang() {
        Set<Currency> filteredCurrency = new LinkedHashSet<>();
        for(Transaction trans : dataTransaction) {
            filteredCurrency.add(trans.getAccount().getCurrencyType());
        }

        return new ArrayList<>(filteredCurrency);
    }

    public Map<PaymentStatus, Image> getPaymentStatusImage() {
        return paymentStatusImage;
    }



    // [1] >=== DATA TRANSAKSI
    public ArrayList<Transaction> getDataTransaksi() {
        return dataTransaction;
    }
    public Boolean modifyTransaksi(Transaction trans) {
        Boolean result = Database.getInstance().updateTransaksi(trans);
        if(result) {
            for (Transaction t : DataManager.getInstance().getDataTransaksi()) {
                if (t.getId() == trans.getId()) {
                    t.setAmount(trans.getAmount());
                    t.setAccount(trans.getAccount());
                    t.setCategory(trans.getCategory());
                    t.setLabelType(trans.getLabelType());
                    t.setDate(trans.getDate());
                    t.setDescription(trans.getDescription());
                    t.setPaymentType(trans.getPaymentType());
                    t.setPaymentStatus(trans.getPaymentStatus());
                    break;
                }
            }

            log.info("transaksi berhasil diedit!");
            MyPopup.showsucces("Operasi berhasil!", "Transaction berhasil diedit!");
            return true;
        } else {
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
            return false;
        }
    }
    public Boolean modifyMultipleTransaksi(List<Transaction> selected){
        int counter = 0;
        for(Transaction trans : selected) {
            Boolean result = Database.getInstance().updateTransaksi(trans);
            if(result){
                counter++;
                for (Transaction t : DataManager.getInstance().getDataTransaksi()) {
                    if (t.getId() == trans.getId()) {
                        t.setAmount(trans.getAmount());
                        t.setAccount(trans.getAccount());
                        t.setCategory(trans.getCategory());
                        t.setLabelType(trans.getLabelType());
                        t.setDate(trans.getDate());
                        t.setDescription(trans.getDescription());
                        t.setPaymentType(trans.getPaymentType());
                        t.setPaymentStatus(trans.getPaymentStatus());
                        break;
                    }
                }
                log.info("transaksi " + counter + " berhasil diedit!");
            } else {
                log.error("GAGAL! transaksi " + counter + " gagal diedit!");
            }
        }

        if(counter == selected.size()) {
            MyPopup.showsucces("Operasi berhasil!", "Transaction berhasil diedit!");
            return true;
        } else {
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
            return false;
        }
    }
    public Boolean importTransaksiFromCSV(Transaction trans) {
        int newId = Database.getInstance().insertTransaksi(trans);
        if(newId > 0) {
            trans.setId(newId);
            dataTransaction.add(trans);
            return true;
        }
        return false;
    }

    public ArrayList<Transaction> getIncomeTransactionData() {
        ArrayList<Transaction> inList = new ArrayList<>();
        for(Transaction trans : dataTransaction) {
            if(trans.getTransactionType() == TransactionType.INCOME) {
                inList.add(trans);
            }
        }
        log.info("Total income transaction: {}", inList.size());
        return inList;
    }
    public ArrayList<Transaction> getExpenseTransactionData() {
        ArrayList<Transaction> outList = new ArrayList<>();
        for(Transaction trans : dataTransaction) {
            if(trans.getTransactionType() == TransactionType.EXPANSE) {
                outList.add(trans);
            }
        }
        log.info("Total expense transaction: {}", outList.size());
        return outList;
    }

    public void addTransaksi(Transaction trans) {
        int newId = Database.getInstance().insertTransaksi(trans);
        if(newId > 0) {
            trans.setId(newId);
            dataTransaction.add(trans);
            log.info("Transaction added successfully!");
            MyPopup.showsucces("Operation successful!", "Transaction added successfully!");
        } else {
            MyPopup.showDanger("Error", "An unexpected error occurred.");
        }
    }

    // [1] >> =============== DATA AKUN =============== //
    public ArrayList<Account> getDataAkun() {
        return accountData;
    }
    public void addAccount(Account data) {
        addAccount(data, true);
    }
    public void addAccount(Account newAccount, Boolean isGeneral) {
        int newId = Database.getInstance().insertAkun(newAccount);
        if (newId > 0) {
            newAccount.setId(newId);
            accountData.add(newAccount);
            log.info("New account [{}] created successfully!", newAccount.getName());

            if (isGeneral) {
                MyPopup.showsucces("Account created successfully!", "Congratulations, the account " + newAccount.getName() + " has been created!");
            }
        } else {
            if (isGeneral) {
                MyPopup.showDanger("Failed!", "An error occurred.");
            }
        }
    }

    public Boolean updateSaldoAkun(Account account, BigDecimal jumlah){
        Boolean result = Database.getInstance().updateSaldoAkun(account, jumlah);
        if(result) {
            log.info("saldo account diperbarui!");
        } else {
            log.error("saldo account GAGAL diperbarui!");
        }
        return result;
    }



    // [5] >> =============== KATEGORI FUNCTION =============== //
    public ArrayList<Category> getDataKategori() {
        return dataCategory;
    }

    public void setDataKategori() {
        dataCategory = new ArrayList<>(DataSeeder.seedKategori());
    }

    public ArrayList<Category> getFilteredCategory() {
        Set<Category> filteredCategory = new LinkedHashSet<>();
        for(Transaction trans : dataTransaction) {
            if(trans.getCategory() != null) {
                filteredCategory.add(trans.getCategory());
            }
        }

        return new ArrayList<>(filteredCategory);
    }

    // [6] >> =============== TIPE LABEL FUNCTION =============== //
    public boolean addLabel(LabelType tipelabel){
        int newId = Database.getInstance().insertTipeLabel(tipelabel);
        if(newId > 0) {
            tipelabel.setId(newId);
            dataLabelType.add(tipelabel);
            log.info("Label baru [{}] berhasil dibuat!", tipelabel.getName());
            MyPopup.showsucces("Label baru berhasil dibuat!", "Label " + tipelabel.getName() + " berhasil dibuat!");
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
            log.info("template {} berhasil ditambahkan!", temp.getName());
            MyPopup.showsucces("Template baru!", "Template " + temp.getName() + " berhasil ditambahkan!");
            return true;
        } else {
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
            return false;
        }
    }

    public ArrayList<LabelType> getFilteredLabel() {
        Set<LabelType> filteredLabel = new LinkedHashSet<>();
        for(Transaction trans : dataTransaction) {
            if(trans.getLabelType() != null) {
                filteredLabel.add(trans.getLabelType());
            }
        }

        return new ArrayList<>(filteredLabel);
    }
}