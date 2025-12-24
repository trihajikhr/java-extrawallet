package controller;

import dataflow.DataManager;
import helper.Converter;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class IncomeControl implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(IncomeControl.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
    DateTimeFormatter formatterNow = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy");
    HashMap<Transaksi, HBox> recordCardBoard = new HashMap<>();

    @FXML private VBox recordPanel;

    // label tampilan
    @FXML private Label labelTotalRecords;
    @FXML private Label labelTanggalSekarang;

    // combobox filter
    @FXML private ComboBox<String> comboBoxSort;
    @FXML private ComboBox<Akun> comboBoxAccount;
    @FXML private ComboBox<Kategori> comboBoxCategories;
    @FXML private ComboBox<TipeLabel> comboBoxLabel;
    @FXML private ComboBox<MataUang> comboBoxCurrencies;
    @FXML private ComboBox<PaymentType> comboBoxType;
    @FXML private ComboBox<PaymentStatus> comboBoxStates;
    Set<Akun> setAkun;
    Set<Kategori> setKategori;
    Set<TipeLabel> setTipeLabel;
    Set<MataUang> setMataUang;
    Set<PaymentType> setPaymentType;
    Set<PaymentStatus> setPaymentStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("panel record income berhasil terbuka");
        initAll();
        fetchTransactionData();
    }

    private void initAll() {
        setDateNow();
    }

    // [1] >=== FIRST INIT
    private void setDateNow() {
        LocalDate dateNow = LocalDate.now();
        String dateStringNow = dateNow.format(formatterNow);
        labelTanggalSekarang.setText(dateStringNow);
        labelTanggalSekarang.setStyle("""
            -fx-text-fill: #01aa71;
            -fx-font-size: 12px;
            -fx-font-weight: bold;        
        """);
    }

    // [2] >=== CARDBOARD UI/UX & HANDLER
    private HBox createTransaction(Transaksi income) {
        HBox transList = new HBox(20);
        transList.setAlignment(Pos.CENTER_LEFT);
        transList.setPrefHeight(65);
        transList.setStyle("""
            -fx-padding: 0 20;
            -fx-background-color: white;
            -fx-border-radius: 12;
            -fx-background-radius: 4;
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
        infoDasar.setPrefWidth(400);
        infoDasar.setPrefWidth(400);
        infoDasar.setMaxWidth(400);
        Label namaKategori = new Label(income.getKategori().getNama());
        namaKategori.setStyle("""
            -fx-text-fill: #000000;
            -fx-font-size: 18px;
            -fx-font-weight: bold;
            """
        );
        infoDasar.getChildren().add(namaKategori);

        HBox infoDasarHelper = new HBox(5);
        String paymentTypeLabel = income.getPaymentType() == null ? "-" : income.getPaymentType().getLabel();
        Label metodeBayar = new Label(paymentTypeLabel);
        metodeBayar.setStyle("-fx-text-fill: #000000");
        infoDasarHelper.getChildren().add(metodeBayar);
        Label keterangan = new Label(income.getKeterangan());
        keterangan.setStyle("-fx-text-fill: #6B7280");
        infoDasarHelper.getChildren().add(keterangan);
        infoDasar.getChildren().add(infoDasarHelper);
        transList.getChildren().add(infoDasar);

//        // Spacer sebelum bagian tengah
//        Region spacerLeft = new Region();
//        spacerLeft.setPrefWidth(100);
//        spacerLeft.setMinWidth(100);
//        spacerLeft.setMaxWidth(100);
//        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
//        transList.getChildren().add(spacerLeft);

        // [4] menampilkan bank dan tipelabel pilihan user
        HBox infoDukung = new HBox(15);
        infoDukung.setPrefWidth(300);
        infoDukung.setMinWidth(300);
        infoDukung.setMaxWidth(300);
        HBox.setHgrow(infoDukung, Priority.ALWAYS);
        infoDukung.setAlignment(Pos.CENTER_LEFT);
        Label namaAkun = new Label(income.getAkun().getNama());
        String warnaHex = Converter.colorToHex(income.getAkun().getWarna());
        namaAkun.setStyle("""
                -fx-background-color: %s;
                -fx-text-fill: white;
                -fx-padding: 4 10 4 10;
                -fx-background-radius: 4;
                -fx-font-weight: bold;
            """.formatted(warnaHex));
        infoDukung.getChildren().add(namaAkun);

        HBox infoLabel = new HBox(15);
        infoLabel.setPrefWidth(100);
        infoLabel.setMinWidth(100);
        infoLabel.setMaxWidth(300);
        HBox.setHgrow(infoLabel, Priority.ALWAYS);
        infoLabel.setAlignment(Pos.CENTER_LEFT);
        if(income.getTipelabel() != null) {
            Label namaTipeLabel = new Label(income.getTipelabel().getNama());
            warnaHex = Converter.colorToHex(income.getTipelabel().getWarna());
            namaTipeLabel.setStyle("""
                -fx-background-color: %s;
                -fx-text-fill: white;
                -fx-padding: 4 10 4 10;
                -fx-background-radius: 4;
                -fx-font-weight: bold;
            """.formatted(warnaHex));
            infoLabel.getChildren().add(namaTipeLabel);
            transList.getChildren().addAll(infoDukung, infoLabel);
        } else {
            transList.getChildren().add(infoDukung);
        }

        // Spacer sebelum bagian kanan
        Region spacerRight = new Region();
        HBox.setHgrow(spacerRight, Priority.ALWAYS);
        transList.getChildren().add(spacerRight);

        // [5] menampilkan harga dan tanggal
        VBox infoTransaksi = new VBox(5);
        infoTransaksi.setPrefWidth(250);
        infoTransaksi.setMaxWidth(250);
        infoTransaksi.setAlignment(Pos.CENTER_RIGHT);
        Label harga = new Label("-" + income.getAkun().getMataUang().getSimbol() + " " + Integer.toString(income.getJumlah()));
        harga.setStyle(
                """
                -fx-text-fill: #F92222;
                -fx-font-size: 18px;
                -fx-font-weight: bold;
                """
        );
        infoTransaksi.getChildren().add(harga);

        HBox tanggalDanStatus = new HBox(5);
        tanggalDanStatus.setAlignment(Pos.CENTER_RIGHT);
        Label tanggal = new Label(income.getTanggal().format(formatter));
        tanggal.setStyle("-fx-text-fill: #6B7280");

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
            iconStatus.setFitWidth(20);
            iconStatus.setFitHeight(20);

            Line separator = new Line();
            separator.setStartX(0);
            separator.setStartY(0);
            separator.setEndX(0); // tetap di X yang sama
            separator.setEndY(20);
            separator.setStroke(Color.web("#E5E7EB"));
            separator.setStrokeWidth(1);
            tanggalDanStatus.getChildren().addAll(tanggal, separator, iconStatus);
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
            recordCardBoard.put(in, createTransaction(in));
        }

        for(HBox card : recordCardBoard.values()) {
            recordPanel.getChildren().add(card);
        }
    }

    // [3] >=== COMBOBOX DATA INIT
    private void initComboBoxSort() {

    }

    // [3] >=== FILTER HANDLER
    @FXML
    private void categoryComboBoxListener() {

    }

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