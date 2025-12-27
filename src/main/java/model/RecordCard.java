package model;

import dataflow.DataManager;
import helper.Converter;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

// TODO: sempurnakan constructor, dan atur setter dan getter, dan pikirkan soal listener

public class RecordCard {

    private static final Logger log = LoggerFactory.getLogger(RecordCard.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");

    private final Map<PaymentStatus, Image> statusIconData = new HashMap<>();

    // all node atribute
    private HBox cardWrapper;
    private HBox infoDasarWrapper;
    private HBox tanggalStatusWrapper;

    private CheckBox checkList;

    private Label labelKategori;
    private Label labelMetodeBayar;
    private Label labelKeterangan;
    private Label labelAkun;
    private Label labelTipeLabel;
    private Label labelJumlahTransaksi;
    private Label labelTanggalTransaksi;

    private ImageView kategoriIcon;
    private ImageView paymentStatusIcon;

    // [0] >=== CONSTRUCTOR
    public RecordCard(Transaksi trans) {
        // [0] parent node
        this.cardWrapper = createParentNode(trans);

        // [1] checklist:
        this.checkList = createCheckBoxNode(trans);

        // [2] icon dengan stackpane:
        StackPane iconStack = createIconStackNode(trans);

        // [3] vbox untuk label, kategori, dan keterangan
        VBox infoDasar = createInfoDasarNode(trans);

        // [4] menampilkan badges nama akun
        HBox infoAkun = createInfoAkunNode(trans);

        // [5] menampilkan badges nama label
        HBox infoLabel = createInfoLabelNode(trans);

        // [6] spacer
        Region spacerRight = createSpacerRightNode(trans);

        // [7] menampilkan info harga, tanggal, dan keterangan lain
        VBox infoTransaksi = createInfoTransaksiNode(trans);

       this.cardWrapper.getChildren().addAll(
               this.checkList,
               iconStack,
               infoDasar,
               infoAkun,
               infoLabel,
               spacerRight,
               infoTransaksi);
    }

    // [1] >=== MODULARISASI PEMBUATAN NODE
    private HBox createParentNode(Transaksi trans) {
        HBox result = new HBox(20);
        result.setAlignment(Pos.CENTER_LEFT);
        result.setPrefHeight(65);
        result.setStyle("""
            -fx-padding: 0 20;
            -fx-background-color: white;
            -fx-border-radius: 12;
            -fx-background-radius: 4;
            -fx-border-color: transparent transparent #E5E7EB transparent;
            -fx-border-width: 0 0 1 0;
        """);
        return result;
    }
    private CheckBox createCheckBoxNode(Transaksi trans) {
        CheckBox result = new CheckBox();
        return result;
    }
    private StackPane createIconStackNode(Transaksi trans) {
        Circle bgCircle = new Circle(20, trans.getKategori().getWarna());
        this.kategoriIcon = new ImageView(trans.getKategori().getIcon());
        this.kategoriIcon.setFitWidth(24);
        this.kategoriIcon.setFitHeight(24);

        Circle clip = new Circle(12, 12, 12);
        this.kategoriIcon.setClip(clip);

        StackPane result = new StackPane(bgCircle, this.kategoriIcon);
        return result;
    }
    private VBox createInfoDasarNode(Transaksi trans) {
        VBox result = new VBox(5);
        result.setAlignment(Pos.CENTER_LEFT);
        result.setPrefWidth(400);
        result.setMinWidth(300);
        result.setMaxWidth(500);
        this.labelKategori = new Label(trans.getKategori().getNama());
        this.labelKategori.setStyle("""
            -fx-text-fill: #000000;
            -fx-font-size: 18px;
            -fx-font-weight: bold;
            """
        );
        result.getChildren().add(this.labelKategori);

        this.infoDasarWrapper = new HBox(5);

        String paymentTypeLabel = "";
        this.labelMetodeBayar = null;
        if(trans.getPaymentType() != null)  {
            this.labelMetodeBayar = new Label();
            paymentTypeLabel = trans.getPaymentType().getLabel();
            this.labelMetodeBayar.setText(paymentTypeLabel);
            this.labelMetodeBayar.setStyle("-fx-text-fill: #000000");
        }

        String tempKeterangan = "";
        this.labelKeterangan = null;
        if(trans.getKeterangan() != null) {
            this.labelKeterangan = new Label();
            tempKeterangan = trans.getKeterangan();
            this.labelKeterangan.setText(tempKeterangan);
            this.labelKeterangan.setStyle("-fx-text-fill: #6B7280");
        }

        Line lineSeparator = null;
        if(this.labelMetodeBayar != null && this.labelKeterangan != null) {
            lineSeparator = createLineSeparatorHelper();
            this.infoDasarWrapper.getChildren().addAll(this.labelMetodeBayar, lineSeparator, this.labelKeterangan);
        }  else {
            if(this.labelMetodeBayar != null) {
                this.infoDasarWrapper.getChildren().add(this.labelMetodeBayar);
            } else if(this.labelKeterangan != null) {
                this.infoDasarWrapper.getChildren().add(this.labelKeterangan);
            }
        }

        result.getChildren().add(this.infoDasarWrapper);
        return result;
    }
    private HBox createInfoAkunNode(Transaksi trans) {
        HBox result = new HBox(15);
        result.setPrefWidth(300);
        result.setMinWidth(200);
        result.setMaxWidth(800);
        //result.setStyle("-fx-background-color: blue;");
        HBox.setHgrow(result, Priority.ALWAYS);
        result.setAlignment(Pos.CENTER_LEFT);
        this.labelAkun = new Label(trans.getAkun().getNama());
        String warnaHex = Converter.colorToHex(trans.getAkun().getWarna());
        this.labelAkun.setStyle("""
                -fx-background-color: %s;
                -fx-font-size: 11;
                -fx-text-fill: white;
                -fx-padding: 2 5 2 5;
                -fx-background-radius: 4;
                -fx-font-weight: bold;
            """.formatted(warnaHex));
        result.getChildren().add(this.labelAkun);
        return result;
    }
    private HBox createInfoLabelNode(Transaksi trans) {
        HBox result = new HBox(15);
        result.setPrefWidth(100);
        result.setMinWidth(100);
        result.setMaxWidth(300);
        //result.setStyle("-fx-background-color: red;");

        HBox.setHgrow(result, Priority.ALWAYS);
        result.setAlignment(Pos.CENTER_LEFT);
        if(trans.getTipelabel() != null) {
            this.labelTipeLabel = new Label(trans.getTipelabel().getNama());
            String warnaHex = Converter.colorToHex(trans.getTipelabel().getWarna());
            this.labelTipeLabel.setStyle("""
                -fx-background-color: %s;
                -fx-font-size: 11;
                -fx-text-fill: white;
                -fx-padding: 2 5 2 5;
                -fx-background-radius: 4;
                -fx-font-weight: bold;
            """.formatted(warnaHex));
            result.getChildren().add(this.labelTipeLabel);
            return result;
        } else {
            return result;
        }
    }
    private Region createSpacerRightNode(Transaksi trans) {
        Region result = new Region();
        HBox.setHgrow(result, Priority.ALWAYS);
        result.setMaxWidth(50);
        // result.setStyle("-fx-background-color: blue;");
        return result;
    }
    private VBox createInfoTransaksiNode(Transaksi trans) {
        VBox result = new VBox(5);
        result.setPrefWidth(250);
        result.setMaxWidth(250);
        result.setAlignment(Pos.CENTER_RIGHT);
        String formatJumlah = Converter.numberFormatter(Integer.toString(trans.getJumlah()));
        this.labelJumlahTransaksi = new Label(trans.getAkun().getMataUang().getSimbol() + " " + formatJumlah);
        this.labelJumlahTransaksi.setStyle(
                """
                -fx-text-fill: #01AA71;
                -fx-font-size: 18px;
                -fx-font-weight: bold;
                """
        );
        result.getChildren().add(this.labelJumlahTransaksi);

        this.tanggalStatusWrapper = new HBox(5);
        this.tanggalStatusWrapper.setAlignment(Pos.CENTER_RIGHT);
        this.labelTanggalTransaksi = new Label(trans.getTanggal().format(formatter));
        this.labelTanggalTransaksi.setStyle("-fx-text-fill: #6B7280");

        // kondisional icon payment
        this.paymentStatusIcon = null;
        Image statusImage = null;
        if(trans.getPaymentStatus() != null) {
            statusImage = DataManager.getInstance().getPaymentStatusImage().get(trans.getPaymentStatus());

            this.paymentStatusIcon = new ImageView(statusImage);
            this.paymentStatusIcon.setFitWidth(20);
            this.paymentStatusIcon.setFitHeight(20);

            Line lineSeparator = new Line();
            lineSeparator.setStartX(0);
            lineSeparator.setStartY(0);
            lineSeparator.setEndX(0); // tetap di X yang sama
            lineSeparator.setEndY(20);
            lineSeparator.setStroke(Color.web("#E5E7EB"));
            lineSeparator.setStrokeWidth(1);
            this.tanggalStatusWrapper.getChildren().addAll(this.labelTanggalTransaksi, lineSeparator, this.paymentStatusIcon);
        } else {
            this.tanggalStatusWrapper.getChildren().add(this.labelTanggalTransaksi);
        }
        result.getChildren().add(this.tanggalStatusWrapper);
        return result;
    }

    // [2] >=== HELPER FUNCTION
    private Line createLineSeparatorHelper() {
        Line lineSeparator = new Line();
        lineSeparator.setStartX(0);
        lineSeparator.setStartY(0);
        lineSeparator.setEndX(0); // tetap di X yang sama
        lineSeparator.setEndY(20);
        lineSeparator.setStroke(Color.web("#E5E7EB"));
        lineSeparator.setStrokeWidth(1);

        return lineSeparator;
    }

    // [3] >=== CHECKBOX LISTENER
    public void setCheckBoxListener(Consumer<Boolean> callback) {
        checkList.selectedProperty().addListener((obs, oldVal, newVal) -> {
            callback.accept(newVal);
        });
    }

    // [] >=== SETTER & GETTER

    public HBox getCardWrapper() {
        return cardWrapper;
    }

    public void setCardWrapper(HBox cardWrapper) {
        this.cardWrapper = cardWrapper;
    }

    public HBox getInfoDasarWrapper() {
        return infoDasarWrapper;
    }

    public void setInfoDasarWrapper(HBox infoDasarWrapper) {
        this.infoDasarWrapper = infoDasarWrapper;
    }

    public HBox getTanggalStatusWrapper() {
        return tanggalStatusWrapper;
    }

    public void setTanggalStatusWrapper(HBox tanggalStatusWrapper) {
        this.tanggalStatusWrapper = tanggalStatusWrapper;
    }

    public CheckBox getCheckList() {
        return checkList;
    }

    public void setCheckList(CheckBox checkList) {
        this.checkList = checkList;
    }

    public Label getLabelKategori() {
        return labelKategori;
    }

    public void setLabelKategori(Label labelKategori) {
        this.labelKategori = labelKategori;
    }

    public Label getLabelMetodeBayar() {
        return labelMetodeBayar;
    }

    public void setLabelMetodeBayar(Label labelMetodeBayar) {
        this.labelMetodeBayar = labelMetodeBayar;
    }

    public Label getLabelKeterangan() {
        return labelKeterangan;
    }

    public void setLabelKeterangan(Label labelKeterangan) {
        this.labelKeterangan = labelKeterangan;
    }

    public Label getLabelAkun() {
        return labelAkun;
    }

    public void setLabelAkun(Label labelAkun) {
        this.labelAkun = labelAkun;
    }

    public Label getLabelTipeLabel() {
        return labelTipeLabel;
    }

    public void setLabelTipeLabel(Label labelTipeLabel) {
        this.labelTipeLabel = labelTipeLabel;
    }

    public Label getLabelJumlahTransaksi() {
        return labelJumlahTransaksi;
    }

    public void setLabelJumlahTransaksi(Label labelJumlahTransaksi) {
        this.labelJumlahTransaksi = labelJumlahTransaksi;
    }

    public Label getLabelTanggalTransaksi() {
        return labelTanggalTransaksi;
    }

    public void setLabelTanggalTransaksi(Label labelTanggalTransaksi) {
        this.labelTanggalTransaksi = labelTanggalTransaksi;
    }

    public ImageView getKategoriIcon() {
        return kategoriIcon;
    }

    public void setKategoriIcon(ImageView kategoriIcon) {
        this.kategoriIcon = kategoriIcon;
    }

    public ImageView getPaymentStatusIcon() {
        return paymentStatusIcon;
    }

    public void setPaymentStatusIcon(ImageView paymentStatusIcon) {
        this.paymentStatusIcon = paymentStatusIcon;
    }
}