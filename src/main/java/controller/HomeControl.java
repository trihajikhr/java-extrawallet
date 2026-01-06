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
import service.CurrencyApiClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class HomeControl implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(HomeControl.class);
    private static final String BASE_CURRENCY = "IDR";

    private ArrayList<Transaksi> dataTransaksi;
    private ArrayList<Transaksi> dataBulanExpense = new ArrayList<>();
    private ArrayList<Transaksi> dataBulanIncome = new ArrayList<>();
    private ArrayList<Transaksi> latestTransaction =  new ArrayList<>();
    private ArrayList<Akun> dataAkun;

    @FXML private VBox latestTransactionPanel;

    @FXML LineChart<String, Number> grafikLine;

    @FXML private Label incomeLabel;
    @FXML private Label expenseLabel;
    @FXML private Label balanceLabel;
    @FXML private Label persenIncome;
    @FXML private Label persenExpense;
    @FXML private Label persenBalance;

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
    private BigDecimal normalizeAmount(Transaksi trans) {
        BigDecimal amount = BigDecimal.valueOf(trans.getJumlah());
        String currency = trans.getAkun().getMataUang().getKode();
        if (currency.equalsIgnoreCase(BASE_CURRENCY)) {
            return amount;
        }
        BigDecimal converted = CurrencyApiClient.getInstance().convert(amount, currency, BASE_CURRENCY);
        return converted;
    }
    private void allLabelSet() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(30);
        LocalDate previousDate = today.minusDays(60);

        BigDecimal previousTotalIncome = BigDecimal.ZERO;
        BigDecimal previousTotalExpense = BigDecimal.ZERO;
        BigDecimal previousTotalBalance = BigDecimal.ZERO;

        List<Transaksi> previousDays = dataTransaksi.stream()
                .filter(t ->
                        !t.getTanggal().isBefore(previousDate) &&
                                !t.getTanggal().isAfter(startDate)
                )
                .toList();

        BigDecimal lastTotalIncome = BigDecimal.ZERO;
        BigDecimal lastTotalExpense = BigDecimal.ZERO;
        BigDecimal lastTotalBalance = BigDecimal.ZERO;

        List<Transaksi> last30Days = dataTransaksi.stream()
                .filter(t ->
                        !t.getTanggal().isBefore(startDate) &&
                                !t.getTanggal().isAfter(today)
                )
                .toList();

        for(Transaksi trans : previousDays) {
            if(trans.getTipeTransaksi() == TipeTransaksi.IN) {
                previousTotalIncome = previousTotalIncome.add(normalizeAmount(trans));
            } else if(trans.getTipeTransaksi() == TipeTransaksi.OUT) {
                previousTotalExpense = previousTotalExpense.add(normalizeAmount(trans));
            }
        }

        for(Transaksi trans : last30Days) {
            if(trans.getTipeTransaksi() == TipeTransaksi.IN) {
                lastTotalIncome = lastTotalIncome.add(normalizeAmount(trans));
            } else if(trans.getTipeTransaksi() == TipeTransaksi.OUT) {
                lastTotalExpense = lastTotalExpense.add(normalizeAmount(trans));
            }
        }

        // label all set
        setLabel(incomeLabel, lastTotalIncome);
        setLabel(expenseLabel, lastTotalExpense);

        lastTotalBalance = lastTotalIncome.subtract(lastTotalExpense);
        setLabel(balanceLabel, lastTotalBalance);

        previousTotalBalance = previousTotalIncome.subtract(previousTotalExpense);

        // persentasi set
        setPercentageBadge(persenIncome, lastTotalIncome, previousTotalIncome);
        setPercentageBadge(persenExpense, lastTotalExpense, previousTotalExpense);
        setPercentageBadge(persenBalance, lastTotalBalance, previousTotalBalance);
    }
    private void setPercentageBadge(Label badge, BigDecimal current, BigDecimal previous) {

        // base style (badge shape)
        String baseStyle = """
        -fx-padding: 3 8 3 8;
        -fx-background-radius: 999;
        -fx-font-size: 11;
        -fx-font-weight: bold;
    """;

        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            if (current.compareTo(BigDecimal.ZERO) == 0) {
                badge.setText("0%");
                badge.setStyle(baseStyle +
                        "-fx-text-fill: #6A7282;" +
                        "-fx-background-color: rgba(156,163,175,0.25);"
                );
            } else {
                badge.setText("New");
                badge.setStyle(baseStyle +
                        "-fx-text-fill: #01AA71;" +
                        "-fx-background-color: rgba(1,170,113,0.15);"
                );
            }
            return;
        }

        BigDecimal percent = current.subtract(previous)
                .multiply(BigDecimal.valueOf(100))
                .divide(previous, 0, RoundingMode.HALF_UP); // 0 decimal


        int sign = percent.signum();

        if (sign > 0) {
            badge.setText("↑ +" + percent + "%");
            badge.setStyle(baseStyle +
                    "-fx-text-fill: #01AA71;" +
                    "-fx-background-color: rgba(1,170,113,0.15);"
            );
        } else if (sign < 0) {
            badge.setText("↓ -" + percent.abs() + "%");
            badge.setStyle(baseStyle +
                    "-fx-text-fill: #F92222;" +
                    "-fx-background-color: rgba(249,34,34,0.15);"
            );
        } else {
            badge.setText("0%");
            badge.setStyle(baseStyle +
                    "-fx-text-fill: #6A7282;" +
                    "-fx-background-color: rgba(156,163,175,0.25);"
            );
        }
    }


    private void setLabel(Label label, BigDecimal value) {
        label.setText("IDR " + Converter.numberFormatter(value.toPlainString()));
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
        List<Transaksi> latest10 =
                dataTransaksi.stream()
                        .sorted(
                                Comparator
                                        .comparing(Transaksi::getTanggal).reversed()
                                        .thenComparing(Transaksi::getId, Comparator.reverseOrder())
                        )
                        .limit(batasData)
                        .toList();

        for (Transaksi trans: latest10) {
            latestTransactionPanel.getChildren().add(createRecordCard(trans).getCardWrapper());
        }
    }
}