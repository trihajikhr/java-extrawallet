package controller;

import dataflow.DataManager;
import helper.Converter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.Akun;
import model.RecordCard;
import model.TipeTransaksi;
import model.Transaksi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class HomeControl implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(HomeControl.class);

    private ArrayList<Transaksi> dataTransaksi;
    private ArrayList<Transaksi> dataBulanExpense = new ArrayList<>();
    private ArrayList<Transaksi> dataBulanIncome = new ArrayList<>();
    private ArrayList<Transaksi> latestTransaction =  new ArrayList<>();
    private ArrayList<Akun> dataAkun;

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal totalBalance;

    @FXML private VBox latestTransactionPanel;

    @FXML LineChart<String, Number> grafikLine;

    @FXML private Label incomeLabel;
    @FXML private Label expenseLabel;
    @FXML private Label balanceLabel;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // style dasar
        latestTransactionPanel.setSpacing(5);

        fetchData();
        generateChart();
        generateLatestTransactionPanel();
        allLabelSet();
    }

    // [0] >=== INITIALISASI & DATA FETCHING
    private void fetchData() {
        dataTransaksi = DataManager.getInstance().getDataTransaksi();
        dataAkun = DataManager.getInstance().getDataAkun();
    }

    private void generateChart() {
        // BUG: TESTING SAJA!
        Akun akun = dataAkun.get(0);

        // matikan line bagian bawah
        grafikLine.getXAxis().setVisible(false);
        grafikLine.setHorizontalGridLinesVisible(false);
        grafikLine.setCreateSymbols(false);

        // clear data
        dataBulanIncome.clear();
        dataBulanExpense.clear();
        grafikLine.getData().clear();

        // getdata income & expense
        for(int i = 0; i < dataTransaksi.size() && dataBulanIncome.size() < 30; i++) {
            if(dataTransaksi.get(i).getAkun().equals(akun) && dataTransaksi.get(i).getTipeTransaksi() == TipeTransaksi.IN) {
                dataBulanIncome.add(dataTransaksi.get(i));
            }
        }

        for(int i = 0; i < dataTransaksi.size() && dataBulanExpense.size() < 30; i++) {
            if(dataTransaksi.get(i).getAkun().equals(akun) && dataTransaksi.get(i).getTipeTransaksi() == TipeTransaksi.OUT) {
                dataBulanExpense.add(dataTransaksi.get(i));
            }
        }

        // income
        XYChart.Series<String, Number> dataIncome = new XYChart.Series<>();
        dataIncome.setName("Income");

        for(int i = 0; i < dataBulanIncome.size(); i++) {
            dataIncome.getData().add(
                    new XYChart.Data<>(String.valueOf(i), dataBulanIncome.get(i).getJumlah()));
        }

        // expense
        XYChart.Series<String, Number> dataExpense = new XYChart.Series<>();
        dataExpense.setName("Expense");

        for(int i = 0; i < dataBulanExpense.size(); i++) {
            dataExpense.getData().add(
                    new XYChart.Data<>(String.valueOf(i), dataBulanExpense.get(i).getJumlah()));
        }

        // tampilkan
        grafikLine.getData().addAll(dataIncome, dataExpense);
    }

    // [] >=== LABEL SET
    private void allLabelSet() {
        totalIncome = DataManager.getInstance().getTotalIncome();
        String stringForm = totalIncome.toPlainString();
        String result = Converter.numberFormatter(stringForm);
        incomeLabel.setText("IDR " + result);

        totalExpense = DataManager.getInstance().getTotalExpense();
        stringForm = totalExpense.toPlainString();
        result = Converter.numberFormatter(stringForm);
        expenseLabel.setText("IDR " + result);

        totalBalance = totalIncome.subtract(totalExpense);
        stringForm = totalBalance.toPlainString();
        result = Converter.numberFormatter(stringForm);
        balanceLabel.setText("IDR " + result);
    }

    // [] >=== PANEL 15 TRANSAKSI TERAKHIR
    private RecordCard createRecordCard(Transaksi trans) {
        RecordCard recordCard = new RecordCard(trans);
        recordCard.getCheckList().setVisible(false);
        return recordCard;
    }
    private void generateLatestTransactionPanel(){
        log.info("data transaksi terbaru berhasil digenerate!");
        int batasData = 10;
        List<Transaksi> latest15 =
                dataTransaksi.stream()
                        .sorted(
                                Comparator
                                        .comparing(Transaksi::getTanggal).reversed()
                                        .thenComparing(Transaksi::getId, Comparator.reverseOrder())
                        )
                        .limit(batasData)
                        .toList();

        for (Transaksi trans: latest15) {
            latestTransactionPanel.getChildren().add(createRecordCard(trans).getCardWrapper());
        }
    }


}