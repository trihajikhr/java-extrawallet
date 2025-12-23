package controller;

import controller.transaction.TransactionControl;
import dataflow.DataManager;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.*;
import model.Pemasukan;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class IncomeControl implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(IncomeControl.class);
    @FXML private VBox recordPanel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("panel record income berhasil terbuka");
        fetchTransactionData();
    }

    private HBox createTransaction(Pemasukan income) {
        HBox transList = new HBox(10);
        transList.setPrefHeight(65);


        transList.setStyle("""
            -fx-padding: 0 20;
            -fx-background-color: white;
            -fx-border-color: #E5E7EB;
            -fx-border-radius: 12;
            -fx-background-radius: 12;
        """);

        CheckBox checklist = new CheckBox();
        Label kategoriLabel = new Label(income.getKategori().getNama());
        transList.getChildren().add(checklist);
        transList.getChildren().add(kategoriLabel);
        return transList;
    }

    private void fetchTransactionData() {
        log.info("DATA TERPANGGIL GUYS");
        ArrayList<Pemasukan> incomeTransaction = DataManager.getInstance().getDataTransaksiPemasukan();

        log.info("Data BERHASIL DI FETCHING");
        for(Pemasukan in : incomeTransaction) {
            recordPanel.getChildren().add(createTransaction(in));
            log.info("memasukan data baru! Ke record Panel!");
        }
    }

//    public void loadDataFromDatabase() {
//        if (transactionContainer == null) return;
//
//        // Bersihkan kontainer agar tidak terjadi duplikasi visual
//        transactionContainer.getChildren().clear();
//        transactionContainer.setSpacing(10);
//
//        // Menggunakan coreDataTransaksi() sesuai isi DataManager milikmu
//        ArrayList<Transaksi> daftarTransaksi = DataManager.getInstance().coreDataTransaksi();
//
//        if (daftarTransaksi != null && !daftarTransaksi.isEmpty()) {
//            for (Transaksi t : daftarTransaksi) {
//                transactionContainer.getChildren().add(createRow(t));
//            }
//        } else {
//            // Opsional: Tampilkan pesan jika data kosong
//            Label emptyLabel = new Label("Belum ada data transaksi.");
//            emptyLabel.setStyle("-fx-text-fill: #95a5a6; -fx-font-style: italic;");
//            transactionContainer.getChildren().add(emptyLabel);
//        }
//    }
//
//    /**
//     * Membuat baris (row) untuk setiap transaksi
//     */
//    private HBox createRow(Transaksi t) {
//        HBox row = new HBox(15);
//        row.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 12; " +
//                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2); " +
//                "-fx-alignment: center-left;");
//
//        // Bagian Teks: Kategori & Keterangan
//        VBox info = new VBox(3);
//        String namaKat = (t.getKategori() != null) ? t.getKategori().getNama() : "Umum";
//        Label kategori = new Label(namaKat);
//        kategori.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
//
//        Label desc = new Label(t.getKeterangan() != null && !t.getKeterangan().isEmpty() ? t.getKeterangan() : "-");
//        desc.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");
//        info.getChildren().addAll(kategori, desc);
//
//        // Spacer untuk mendorong nominal ke kanan
//        Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//
//        // Bagian Nominal: Warna Emerald untuk IN, Merah untuk OUT
//        boolean isIncome = t.getTipe().equals("IN");
//        String color = isIncome ? "#01AA71" : "#E74C3C";
//        String sign = isIncome ? "+" : "-";
//
//        Label harga = new Label(sign + " IDR " + String.format("%,d", t.getJumlah()));
//        harga.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: " + color + ";");
//
//        row.getChildren().addAll(info, spacer, harga);
//        return row;
//    }

    /**
     * Membuka popup transaksi
     */
//    @FXML
//    private void handleAdd() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transaction.fxml"));
//            Parent root = loader.load();
//            Stage stage = new Stage();
//            stage.initStyle(StageStyle.TRANSPARENT);
//            stage.initModality(Modality.APPLICATION_MODAL);
//
//            TransactionControl ctrl = loader.getController();
//            ctrl.setStage(stage);
//
//            Scene scene = new Scene(root);
//            scene.setFill(Color.TRANSPARENT);
//            stage.setScene(scene);
//
//            // Animasi muncul jika ada
//            ctrl.showPopup();
//
//            stage.showAndWait();
//
//            // Refresh tampilan secara otomatis setelah popup ditutup
//            loadDataFromDatabase();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            showNotification("Error", "Gagal memuat form transaksi.");
//        }
//    }

    // WAJIB ADA: Mencegah Error "Namespace" di FXML meskipun belum ada logikanya
    @FXML
    private void handleEdit() {
        showNotification("Info", "Fitur Edit akan segera hadir.");
    }

    @FXML
    private void handleDelete() {
        showNotification("Info", "Fitur Delete akan segera hadir.");
    }

    private void showNotification(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}