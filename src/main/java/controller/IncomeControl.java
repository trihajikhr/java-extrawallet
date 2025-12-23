package controller;

import controller.transaction.TransactionControl;
import dataflow.DataManager;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import model.Pemasukan;
import model.Transaksi;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class IncomeControl implements Initializable {

    @FXML private VBox recordPanel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recordPanel.getChildren().add(
                createTransaction("🍕", "Food & Drinks", "CASH", "di toleransi",
                        "-IDR 24,000.00", "09:40 PM")
        );

        recordPanel.getChildren().add(
                createTransaction("🚕", "Transport", "E-WALLET", "",
                        "-IDR 15,000.00", "08:10 AM")
        );
    }

    private HBox createTransaction(Pemasukan income) {

        HBox root = new HBox(20);
        root.setPrefHeight(85);
        root.setStyle("""
            -fx-padding: 0 20;
            -fx-background-color: white;
            -fx-border-color: #E5E7EB;
            -fx-border-radius: 12;
            -fx-background-radius: 12;
        """);

        CheckBox checkBox = new CheckBox();

        // ICON
        Circle circle = new Circle(25);
        circle.setFill(javafx.scene.paint.Color.web("#E8F6F1"));
        circle.setStroke(javafx.scene.paint.Color.web("#01AA71"));

        Label icon = new Label(emoji);
        icon.setStyle("-fx-font-size: 20;");

        StackPane iconPane = new StackPane(circle, icon);

        // TEXT
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#2C3E50"));

        Label paymentLabel = new Label(payment);
        paymentLabel.setStyle("""
            -fx-background-color: #01AA71;
            -fx-text-fill: white;
            -fx-padding: 2 10;
            -fx-background-radius: 10;
            -fx-font-size: 10;
            -fx-font-weight: bold;
        """);

        Label noteLabel = new Label(note);
        noteLabel.setStyle("-fx-font-size: 12;");
        noteLabel.setTextFill(javafx.scene.paint.Color.web("#95A5A6"));

        HBox infoRow = new HBox(8, paymentLabel, noteLabel);
        VBox textBox = new VBox(6, titleLabel, infoRow);

        // SPACER
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // AMOUNT
        Label amountLabel = new Label(amount);
        amountLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        amountLabel.setTextFill(javafx.scene.paint.Color.web("#E74C3C"));

        Label timeLabel = new Label(time);
        timeLabel.setStyle("-fx-font-size: 12;");
        timeLabel.setTextFill(javafx.scene.paint.Color.web("#BDC3C7"));

        VBox rightBox = new VBox(amountLabel, timeLabel);
        rightBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        root.getChildren().addAll(
                checkBox,
                iconPane,
                textBox,
                spacer,
                rightBox
        );

        return root;
    }

    private void fetchTransacctionData() {
        ArrayList<Pemasukan> incomeTransaction = DataManager.getInstance().getDataTransaksiPemasukan();

        for(Pemasukan in : incomeTransaction) {
            createTransaction(in);
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