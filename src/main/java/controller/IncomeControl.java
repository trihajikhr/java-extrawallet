package controller;

import dataflow.DataManager;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.PaymentStatus;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import model.Transaksi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class IncomeControl implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(IncomeControl.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
    @FXML private VBox recordPanel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("panel record income berhasil terbuka");
        fetchTransactionData();
    }

    private HBox createTransaction(Transaksi income) {
        HBox transList = new HBox(20);
        transList.setAlignment(Pos.CENTER_LEFT);
        transList.setPrefHeight(65);
        transList.setStyle("""
            -fx-padding: 0 20;
            -fx-background-color: white;
            -fx-border-radius: 12;
            -fx-background-radius: 12;
            -fx-border-color: transparent transparent #E5E7EB transparent;
            -fx-border-width: 0 0 1 0;
        """);

        // [1] checklist:
        CheckBox checklist = new CheckBox();
        transList.getChildren().add(checklist);

        // [2] icon dengan stackpane:
        Circle bgCircle = new Circle(20, income.getKategori().getWarna());
        ImageView kategoriIcon = new ImageView(income.getKategori().getIcon());
        kategoriIcon.setFitWidth(24);
        kategoriIcon.setFitHeight(24);

        Circle clip = new Circle(12, 12, 12);
        kategoriIcon.setClip(clip);

        StackPane iconStack = new StackPane(bgCircle, kategoriIcon);
        transList.getChildren().add(iconStack);

        // [3] vbox untuk label, kategori, dan keterangan
        VBox infoDasar = new VBox(5);
        infoDasar.setAlignment(Pos.CENTER_LEFT);
        Label namaKategori = new Label(income.getKategori().getNama());
        namaKategori.setStyle("-fx-text-fill: #000000");
        infoDasar.getChildren().add(namaKategori);

        HBox infoDasarHelper = new HBox(5);
        String paymentTypeLabel = income.getPaymentType() == null ? "-" : income.getPaymentType().getLabel();
        Label metodeBayar = new Label(paymentTypeLabel);
        metodeBayar.setStyle("-fx-text-fill: #000000");
        infoDasarHelper.getChildren().add(metodeBayar);
        Label keterangan = new Label(income.getKeterangan());
        keterangan.setStyle("-fx-text-fill: #000000");
        infoDasarHelper.getChildren().add(keterangan);
        infoDasar.getChildren().add(infoDasarHelper);
        transList.getChildren().add(infoDasar);

        // Spacer sebelum bagian tengah
        Region spacerLeft = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        transList.getChildren().add(spacerLeft);

        // [4] menampilkan bank dan tipelabel pilihan user
        HBox infoDukung = new HBox(15);
        HBox.setHgrow(infoDukung, Priority.ALWAYS);
        infoDukung.setAlignment(Pos.CENTER);
        Label namaAkun = new Label(income.getAkun().getNama());
        namaAkun.setStyle("-fx-text-fill: #000000");
        infoDukung.getChildren().add(namaAkun);

        Label namaTipeLabel = new Label(income.getTipelabel().getNama());
        namaTipeLabel.setStyle("-fx-text-fill: #000000");
        infoDukung.getChildren().add(namaTipeLabel);
        transList.getChildren().add(infoDukung);

        // Spacer sebelum bagian kanan
        Region spacerRight = new Region();
        HBox.setHgrow(spacerRight, Priority.ALWAYS);
        transList.getChildren().add(spacerRight);

        // [5] menampilkan harga dan tanggal
        VBox infoTransaksi = new VBox(5);
        infoTransaksi.setAlignment(Pos.CENTER_RIGHT);
        Label harga = new Label(income.getAkun().getMataUang().getSimbol() + " " + Integer.toString(income.getJumlah()));
        harga.setStyle("-fx-text-fill: #000000");
        infoTransaksi.getChildren().add(harga);

        HBox tanggalDanStatus = new HBox(5);
        tanggalDanStatus.setAlignment(Pos.CENTER_RIGHT);
        Label tanggal = new Label(income.getTanggal().format(formatter));
        tanggal.setStyle("-fx-text-fill: #000000");

        // kondisional icon payment
        ImageView iconStatus = null;
        // menentukan status icon
        Image newImage = null;
        if(income.getPaymentStatus() != null) {
            if (income.getPaymentStatus() == PaymentStatus.RECONCILED) {
                newImage = new Image(Objects.requireNonNull(getClass().getResource("/icons/reconciled.png")).toString());
            } else if (income.getPaymentStatus() == PaymentStatus.CLEARED) {
                newImage = new Image(Objects.requireNonNull(getClass().getResource("/icons/cleared.png")).toString());
            } else if (income.getPaymentStatus() == PaymentStatus.UNCLEARED) {
                newImage = new Image(Objects.requireNonNull(getClass().getResource("/icons/uncleared.png")).toString());
            }

            iconStatus = new ImageView(newImage);
            iconStatus.setFitWidth(25);
            iconStatus.setFitHeight(25);
            tanggalDanStatus.getChildren().addAll(tanggal, iconStatus);
        } else {
            tanggalDanStatus.getChildren().add(tanggal);
        }
        infoTransaksi.getChildren().add(tanggalDanStatus);
        transList.getChildren().add(infoTransaksi);

        return transList;
    }

    private void fetchTransactionData() {
        log.info("report income berhasil terbuka");
        ArrayList<Transaksi> incomeTransaction = DataManager.getInstance().getDataTransaksiPemasukan();

        for(Transaksi in : incomeTransaction) {
            recordPanel.getChildren().add(createTransaction(in));
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