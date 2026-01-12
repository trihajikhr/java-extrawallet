package model.extended;

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
import model.Transaction;
import model.enums.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class RecordCard {

    private static final Logger log = LoggerFactory.getLogger(RecordCard.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");

    // all node atribute
    private HBox cardWrapper;
    private HBox basicInfoWrapper;
    private HBox dateStatusWrapper;
    private CheckBox checkList;
    private Label categoryLabel;
    private Label paymentTypeLabel;
    private Label descriptionLabel;
    private Label accountLabel;
    private Label labelTypeLabel;
    private Label totalAmountLabel;
    private Label transactionDateLabel;
    private ImageView categoryIcon;
    private ImageView paymentStatusIcon;

    // atribute helper
    private Transaction transaction;

    // [0] >=== CONSTRUCTOR
    public RecordCard(Transaction trans) {
        this.transaction = trans;

        // [0] parent node
        this.cardWrapper = createParentNode(trans);
        cardWrapper.getStyleClass().add("record-card");

        // [1] checklist:
        this.checkList = createCheckBoxNode(trans);

        // [2] icon dengan stackpane:
        StackPane iconStack = createIconStackNode(trans);

        // [3] vbox untuk label, category, dan keterangan
        VBox infoDasar = createBasicInfoNode(trans);

        // [4] menampilkan badges nama account
        HBox infoAkun = createAccountInfoNode(trans);

        // [5] menampilkan badges nama label
        HBox infoLabel = createLabelInfoNode(trans);

        // [6] spacer
        Region spacerRight = createSpacerRightNode(trans);

        // [7] menampilkan info harga, tanggal, dan keterangan lain
        VBox infoTransaksi = createTransactionInfoNode(trans);

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
    private HBox createParentNode(Transaction trans) {
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
    private CheckBox createCheckBoxNode(Transaction trans) {
        CheckBox result = new CheckBox();
        return result;
    }
    private StackPane createIconStackNode(Transaction trans) {
        Circle bgCircle = new Circle(20, trans.getCategory().getColor());
        this.categoryIcon = new ImageView(trans.getCategory().getIcon());
        this.categoryIcon.setFitWidth(24);
        this.categoryIcon.setFitHeight(24);

        Circle clip = new Circle(12, 12, 12);
        this.categoryIcon.setClip(clip);

        StackPane result = new StackPane(bgCircle, this.categoryIcon);
        return result;
    }
    private VBox createBasicInfoNode(Transaction trans) {
        VBox result = new VBox(5);
        result.setAlignment(Pos.CENTER_LEFT);
        result.setPrefWidth(400);
        result.setMinWidth(300);
        result.setMaxWidth(500);
        this.categoryLabel = new Label(trans.getCategory().getName());
        this.categoryLabel.setStyle("""
            -fx-text-fill: #000000;
            -fx-font-size: 18px;
            -fx-font-weight: bold;
            """
        );
        result.getChildren().add(this.categoryLabel);

        this.basicInfoWrapper = new HBox(5);

        String paymentTypeLabel = "";
        this.paymentTypeLabel = null;
        if(trans.getPaymentType() != null)  {
            this.paymentTypeLabel = new Label();
            paymentTypeLabel = trans.getPaymentType().getLabel();
            this.paymentTypeLabel.setText(paymentTypeLabel);
            this.paymentTypeLabel.setStyle("-fx-text-fill: #000000");
        }

        String tempKeterangan = "";
        this.descriptionLabel = null;
        if(trans.getDescription() != null) {
            this.descriptionLabel = new Label();
            tempKeterangan = trans.getDescription();
            this.descriptionLabel.setText(tempKeterangan);
            this.descriptionLabel.setStyle("-fx-text-fill: #6B7280");
        }

        Line lineSeparator = null;
        if(this.paymentTypeLabel != null && this.descriptionLabel != null) {
            lineSeparator = createLineSeparatorHelper();
            this.basicInfoWrapper.getChildren().addAll(this.paymentTypeLabel, lineSeparator, this.descriptionLabel);
        }  else {
            if(this.paymentTypeLabel != null) {
                this.basicInfoWrapper.getChildren().add(this.paymentTypeLabel);
            } else if(this.descriptionLabel != null) {
                this.basicInfoWrapper.getChildren().add(this.descriptionLabel);
            }
        }

        result.getChildren().add(this.basicInfoWrapper);
        return result;
    }
    private HBox createAccountInfoNode(Transaction trans) {
        HBox result = new HBox(15);
        result.setPrefWidth(300);
        result.setMinWidth(200);
        result.setMaxWidth(800);
        HBox.setHgrow(result, Priority.ALWAYS);
        result.setAlignment(Pos.CENTER_LEFT);
        this.accountLabel = new Label(trans.getAccount().getName());
        String warnaHex = Converter.colorToHex(trans.getAccount().getColor());
        this.accountLabel.setStyle("""
                -fx-background-color: %s;
                -fx-font-size: 11;
                -fx-text-fill: white;
                -fx-padding: 2 5 2 5;
                -fx-background-radius: 4;
                -fx-font-weight: bold;
            """.formatted(warnaHex));
        result.getChildren().add(this.accountLabel);
        return result;
    }
    private HBox createLabelInfoNode(Transaction trans) {
        HBox result = new HBox(15);
        result.setPrefWidth(100);
        result.setMinWidth(100);
        result.setMaxWidth(300);

        HBox.setHgrow(result, Priority.ALWAYS);
        result.setAlignment(Pos.CENTER_LEFT);
        if(trans.getLabelType() != null) {
            this.labelTypeLabel = new Label(trans.getLabelType().getName());
            String warnaHex = Converter.colorToHex(trans.getLabelType().getColor());
            this.labelTypeLabel.setStyle("""
                -fx-background-color: %s;
                -fx-font-size: 11;
                -fx-text-fill: white;
                -fx-padding: 2 5 2 5;
                -fx-background-radius: 4;
                -fx-font-weight: bold;
            """.formatted(warnaHex));
            result.getChildren().add(this.labelTypeLabel);
            return result;
        } else {
            return result;
        }
    }
    private Region createSpacerRightNode(Transaction trans) {
        Region result = new Region();
        HBox.setHgrow(result, Priority.ALWAYS);
        result.setMaxWidth(50);
        return result;
    }
    private VBox createTransactionInfoNode(Transaction trans) {
        VBox result = new VBox(5);
        result.setPrefWidth(250);
        result.setMaxWidth(250);
        result.setAlignment(Pos.CENTER_RIGHT);
        String formatJumlah = Converter.numberFormatter(trans.getAmount().toPlainString());

        if(trans.getTransactionType() == TransactionType.INCOME){
            this.totalAmountLabel = new Label(trans.getAccount().getCurrencyType().getSymbol() + " " + formatJumlah);
            this.totalAmountLabel.setStyle(
                    """
                    -fx-text-fill: #01AA71;
                    -fx-font-size: 18px;
                    -fx-font-weight: bold;
                    """
            );
        } else if(trans.getTransactionType() == TransactionType.EXPANSE) {
            this.totalAmountLabel = new Label("-" + trans.getAccount().getCurrencyType().getSymbol() + " " + formatJumlah);
            this.totalAmountLabel.setStyle(
                    """
                    -fx-text-fill: #F92222;
                    -fx-font-size: 18px;
                    -fx-font-weight: bold;
                    """
            );
        }

        result.getChildren().add(this.totalAmountLabel);

        this.dateStatusWrapper = new HBox(5);
        this.dateStatusWrapper.setAlignment(Pos.CENTER_RIGHT);
        this.transactionDateLabel = new Label(trans.getDate().format(formatter));
        this.transactionDateLabel.setStyle("-fx-text-fill: #6B7280");

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
            this.dateStatusWrapper.getChildren().addAll(this.transactionDateLabel, lineSeparator, this.paymentStatusIcon);
        } else {
            this.dateStatusWrapper.getChildren().add(this.transactionDateLabel);
        }
        result.getChildren().add(this.dateStatusWrapper);
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

    // [4] >=== CARD LISTENER JIKA DIKLIK
    public void setOnCardClick(Consumer<Transaction> onClick) {
        if (onClick == null) return;

        cardWrapper.setOnMouseClicked(e -> {
            if (e.getClickCount() != 1) return;

            // Kalau yang diklik checkbox (atau child-nya), stop! Abaikan!
            if (e.getTarget() instanceof CheckBox) return;

            onClick.accept(transaction);
        });
    }

    // [5] >=== SETTER & GETTER
    public HBox getCardWrapper() {
        return cardWrapper;
    }

    public void setCardWrapper(HBox cardWrapper) {
        this.cardWrapper = cardWrapper;
    }

    public HBox getBasicInfoWrapper() {
        return basicInfoWrapper;
    }

    public void setBasicInfoWrapper(HBox basicInfoWrapper) {
        this.basicInfoWrapper = basicInfoWrapper;
    }

    public HBox getDateStatusWrapper() {
        return dateStatusWrapper;
    }

    public void setDateStatusWrapper(HBox dateStatusWrapper) {
        this.dateStatusWrapper = dateStatusWrapper;
    }

    public CheckBox getCheckList() {
        return checkList;
    }

    public void setCheckList(CheckBox checkList) {
        this.checkList = checkList;
    }

    public Label getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(Label categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public Label getPaymentTypeLabel() {
        return paymentTypeLabel;
    }

    public void setPaymentTypeLabel(Label paymentTypeLabel) {
        this.paymentTypeLabel = paymentTypeLabel;
    }

    public Label getDescriptionLabel() {
        return descriptionLabel;
    }

    public void setDescriptionLabel(Label descriptionLabel) {
        this.descriptionLabel = descriptionLabel;
    }

    public Label getAccountLabel() {
        return accountLabel;
    }

    public void setAccountLabel(Label accountLabel) {
        this.accountLabel = accountLabel;
    }

    public Label getLabelTypeLabel() {
        return labelTypeLabel;
    }

    public void setLabelTypeLabel(Label labelTypeLabel) {
        this.labelTypeLabel = labelTypeLabel;
    }

    public Label getTotalAmountLabel() {
        return totalAmountLabel;
    }

    public void setTotalAmountLabel(Label totalAmountLabel) {
        this.totalAmountLabel = totalAmountLabel;
    }

    public Label getTransactionDateLabel() {
        return transactionDateLabel;
    }

    public void setTransactionDateLabel(Label transactionDateLabel) {
        this.transactionDateLabel = transactionDateLabel;
    }

    public ImageView getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(ImageView categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public ImageView getPaymentStatusIcon() {
        return paymentStatusIcon;
    }

    public void setPaymentStatusIcon(ImageView paymentStatusIcon) {
        this.paymentStatusIcon = paymentStatusIcon;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}