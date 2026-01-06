package controller;

import dataflow.DataManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import model.Akun;
import model.TipeTransaksi;
import model.Transaksi;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeControl implements Initializable {
    private ArrayList<Transaksi> dataTransaksi;
    private ArrayList<Transaksi> dataBulanExpense = new ArrayList<>();
    private ArrayList<Transaksi> dataBulanIncome = new ArrayList<>();
    private ArrayList<Akun> dataAkun;

    @FXML
    LineChart<String, Number> grafikLine;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        fetchData();
        generateChart();
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
}