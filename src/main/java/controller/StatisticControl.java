package controller;

import dataflow.DataManager;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import model.TransactionType;
import model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class StatisticControl {
    private static final Logger log = LoggerFactory.getLogger(StatisticControl.class);
    private ArrayList<Transaction> dataTransaction = new ArrayList<>();
    @FXML private AreaChart mainStatistik;
    @FXML private Label labelTahun;

    @FXML
    public void initialize() {
        setLabelTahun();
        setGrafikArea();
    }

    private void setGrafikArea(){
        dataTransaction = DataManager.getInstance().getDataTransaksi();
        dataTransaction.sort(Comparator.comparing(Transaction::getDate));

        Map<Integer, Integer> monthlyDelta = new HashMap<>();
        for (int i = 1; i <= 12; i++) monthlyDelta.put(i, 0);

        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        for (Transaction t : dataTransaction) {
            if (t.getDate().getYear() != currentYear) continue;

            int month = t.getDate().getMonthValue();
            int delta = t.getTipeTransaksi() == TransactionType.INCOME ? t.getAmount() : -t.getAmount();
            monthlyDelta.put(month, monthlyDelta.get(month) + delta);
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Balance Growth");

        String[] bulan = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        int cumulative = 0;
        for (int i = 1; i <= Math.min(currentMonth, 12); i++) {
            cumulative += monthlyDelta.get(i);
            series.getData().add(new XYChart.Data<>(bulan[i-1], cumulative));
        }

        mainStatistik.getData().add(series);
    }

    private void setLabelTahun () {
        int currentYear = LocalDate.now().getYear();
        labelTahun.setText("From year: " + currentYear);

        labelTahun.setStyle("""
            -fx-text-fill: #9CA3AF;
            -fx-font-size: 12;
        """);
    }

}
