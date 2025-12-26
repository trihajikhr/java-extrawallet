package controller;

import controller.option.SortOption;
import dataflow.DataManager;
import helper.Converter;
import helper.IOLogic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.geometry.Insets;
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
import service.CurrencyApiClient;
import service.IncomeService;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IncomeControl implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(IncomeControl.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
    DateTimeFormatter formatterNow = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy");

    // data sumber kebenaran
    List<Transaksi> incomeTransaction  = new ArrayList<>();
    private final Map<Transaksi, HBox> recordCardBoard = new HashMap<>();

    @FXML private VBox recordPanel;

    // label tampilan
    @FXML private Label labelTotalRecords;
    @FXML private Label labelTanggalSekarang;
    @FXML private Label labelTotalAmount;

    // combobox filter
    @FXML private ComboBox<SortOption> comboBoxSort;
    @FXML private MenuButton menuButtonAccount;
    @FXML private MenuButton menuButtonCategory;
    @FXML private MenuButton menuButtonLabel;
    @FXML private MenuButton menuButtonCurrencie;
    @FXML private MenuButton menuButtonPaymentType;
    @FXML private MenuButton menuButtonPaymentState;

    private final ObservableSet<Akun> selectedAccounts =
            FXCollections.observableSet(new LinkedHashSet<>());

    private final ObservableSet<Kategori> selectedCategories =
            FXCollections.observableSet(new LinkedHashSet<>());

    private final ObservableSet<TipeLabel> selectedLabels =
            FXCollections.observableSet(new LinkedHashSet<>());

    private final ObservableSet<MataUang> selectedCurrencies =
            FXCollections.observableSet(new LinkedHashSet<>());

    private final ObservableSet<PaymentType> selectedPaymentTypes =
            FXCollections.observableSet(new LinkedHashSet<>());

    private final ObservableSet<PaymentStatus> selectedPaymentStates =
            FXCollections.observableSet(new LinkedHashSet<>());


    // [0] >=== INIT FUNCTION
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("panel record income berhasil terbuka");
        fetchTransactionData();
        initAllComboBox();
        initBaseData();
    }

    private void initBaseData() {
        setDateNow();
        recordCounterLabelInit();
        totalIncomeAmountSetter(incomeTransaction);
    }

    // [1] >=== BASE INIT
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
    private void recordCounterLabelInit() {
        labelTotalRecords.setText("Found " + incomeTransaction.size() + " record");
    }
    private void totalIncomeAmountSetter(List<Transaksi> dataIncome) {
        BigDecimal value = (IncomeService.getInstance().incomeSumAfterFilter(dataIncome));
        String stringForm = value.toPlainString();
        String result = Converter.numberFormatter(stringForm);
        labelTotalAmount.setText("TOTAL: IDR " + result);
    }

    // [2] >=== CARDBOARD UI/UX & DATA FETCHING
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
        infoDasar.setMinWidth(300);
        infoDasar.setMaxWidth(500);
        Label namaKategori = new Label(income.getKategori().getNama());
        namaKategori.setStyle("""
            -fx-text-fill: #000000;
            -fx-font-size: 18px;
            -fx-font-weight: bold;
            """
        );
        infoDasar.getChildren().add(namaKategori);

        HBox infoDasarHelper = new HBox(5);

        String paymentTypeLabel = "";
        Label metodeBayar = null;
        if(income.getPaymentType() != null)  {
            metodeBayar = new Label();
            paymentTypeLabel = income.getPaymentType().getLabel();
            metodeBayar.setText(paymentTypeLabel);
            metodeBayar.setStyle("-fx-text-fill: #000000");
        }

        String tempKeterangan = "";
        Label keterangan = null;
        if(income.getKeterangan() != null) {
            keterangan = new Label();
            tempKeterangan = income.getKeterangan();
            keterangan.setText(tempKeterangan);
            keterangan.setStyle("-fx-text-fill: #6B7280");
        }

        Line separatorDasar = null;
        if(metodeBayar != null && keterangan != null) {
            separatorDasar = new Line();
            separatorDasar.setStartX(0);
            separatorDasar.setStartY(0);
            separatorDasar.setEndX(0); // tetap di X yang sama
            separatorDasar.setEndY(20);
            separatorDasar.setStroke(Color.web("#E5E7EB"));
            separatorDasar.setStrokeWidth(1);

            infoDasarHelper.getChildren().addAll(metodeBayar, separatorDasar, keterangan);
        }  else {
            if(metodeBayar != null) {
                infoDasarHelper.getChildren().add(metodeBayar);
            } else if(keterangan != null) {
                infoDasarHelper.getChildren().add(keterangan);
            }
        }

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
        infoDukung.setMinWidth(200);
        infoDukung.setMaxWidth(800);
        //infoDukung.setStyle("-fx-background-color: blue;");
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
        //infoLabel.setStyle("-fx-background-color: red;");

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
            transList.getChildren().addAll(infoDukung, infoLabel);
        }

        // Spacer sebelum bagian kanan
        Region spacerRight = new Region();
        HBox.setHgrow(spacerRight, Priority.ALWAYS);
        transList.getChildren().add(spacerRight);
        spacerRight.setMaxWidth(50);
        // spacerRight.setStyle("-fx-background-color: blue;");

        // [5] menampilkan harga dan tanggal
        VBox infoTransaksi = new VBox(5);
        infoTransaksi.setPrefWidth(250);
        infoTransaksi.setMaxWidth(250);
        infoTransaksi.setAlignment(Pos.CENTER_RIGHT);
        String formatJumlah = Converter.numberFormatter(Integer.toString(income.getJumlah()));
        Label harga = new Label(income.getAkun().getMataUang().getSimbol() + " " + formatJumlah);
        harga.setStyle(
                """
                -fx-text-fill: #01AA71;
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
        incomeTransaction = DataManager.getInstance().getDataTransaksiPemasukan();

        for(Transaksi in : incomeTransaction) {
            recordCardBoard.put(in, createTransaction(in));
        }

        for(HBox card : recordCardBoard.values()) {
            recordPanel.getChildren().add(card);
        }
    }

    // [3] >=== COMBOBOX DATA INIT
    private void initAllComboBox() {
        initComboBoxSort();
        initMenuButtonAccount();
        initMenuButtonCategory();
        initMenuButtonLabel();
        initMenuButtonCurrency();
        initMenuButtonPaymentType();
        initMenuButtonPaymentState();
    }
    private void initComboBoxSort() {
        ObservableList<SortOption> sortItems =
                FXCollections.observableArrayList(SortOption.values());
        comboBoxSort.setItems(sortItems);

        comboBoxSort.getSelectionModel().select(SortOption.TIME_NEWEST);

        comboBoxSort.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            applyFilterAndSort();
        });
    }
    private void initMenuButtonAccount() {
        menuButtonAccount.getItems().clear();
        menuButtonAccount.setText("Account");
        menuButtonAccount.setStyle("-fx-text-fill: #9CA3AF;"); // abu-abu
        List<Akun> dataAkun = DataManager.getInstance().getDataAkun();

        for (Akun akun : dataAkun) {
            // Icon
            ImageView iconView = new ImageView(akun.getIcon());
            iconView.setFitWidth(14);
            iconView.setFitHeight(14);
            iconView.setPreserveRatio(true);
            StackPane iconBox = new StackPane(iconView);
            iconBox.setPrefSize(28, 28);
            iconBox.setBackground(new Background(new BackgroundFill(
                    akun.getWarna(), new CornerRadii(8), Insets.EMPTY)));

            // Label
            Label label = new Label(akun.getNama());
            label.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

            // Centang
            Label checkMark = new Label("✓");
            checkMark.setTextFill(Color.GREEN);
            checkMark.setVisible(selectedAccounts.contains(akun));

            // Spacer
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // HBox wrapper
            HBox wrapper = new HBox(5, checkMark, iconBox, label, spacer);
            wrapper.setAlignment(Pos.CENTER_LEFT);
            wrapper.setPadding(new Insets(2, 5, 2, 5));
            wrapper.setMaxWidth(Double.MAX_VALUE); // biar expand
            wrapper.prefWidthProperty().bind(menuButtonAccount.widthProperty().subtract(2));

            // CustomMenuItem
            CustomMenuItem menuItem = new CustomMenuItem(wrapper);
            menuItem.setHideOnClick(false);

            // Klik seluruh area menuItem
            menuItem.setOnAction(e -> {
                boolean selected = !selectedAccounts.contains(akun);
                if (selected) selectedAccounts.add(akun);
                else selectedAccounts.remove(akun);

                checkMark.setVisible(selected);
                updateAccountMenuText();
                applyFilterAndSort();
            });

            menuButtonAccount.getItems().add(menuItem);
        }
    }
    private void updateAccountMenuText() {
        if (selectedAccounts.isEmpty()) {
            // prompt-like state
            menuButtonAccount.setText("Account");
            menuButtonAccount.setStyle("-fx-text-fill: #9CA3AF;"); // abu-abu
            return;
        }
        String text = selectedAccounts.stream()
                .map(Akun::getNama)
                .collect(Collectors.joining(", "));

        menuButtonAccount.setText(text);
        menuButtonAccount.setStyle("-fx-text-fill: -fx-text-base-color;");
    }
    private void initMenuButtonCategory() {
        menuButtonCategory.getItems().clear();
        menuButtonCategory.setText("Category");
        menuButtonCategory.setStyle("-fx-text-fill: #9CA3AF;"); // abu-abu
        List<Kategori> dataKategori = DataManager.getInstance().getFilteredCategory();

        for (Kategori kategori : dataKategori) {
            // Icon
            ImageView iconView = new ImageView(kategori.getIcon());
            iconView.setFitWidth(14);
            iconView.setFitHeight(14);
            iconView.setPreserveRatio(true);
            StackPane iconBox = new StackPane(iconView);
            iconBox.setPrefSize(28, 28);
            iconBox.setBackground(new Background(new BackgroundFill(
                    kategori.getWarna(), new CornerRadii(8), Insets.EMPTY)));

            // Label
            Label label = new Label(kategori.getNama());
            label.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

            // Centang
            Label checkMark = new Label("✓");
            checkMark.setTextFill(Color.GREEN);
            checkMark.setVisible(selectedCategories.contains(kategori));

            // Spacer
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // HBox wrapper
            HBox wrapper = new HBox(5, checkMark, iconBox, label, spacer);
            wrapper.setAlignment(Pos.CENTER_LEFT);
            wrapper.setPadding(new Insets(2, 5, 2, 5));
            wrapper.setMaxWidth(Double.MAX_VALUE); // biar expand
            wrapper.prefWidthProperty().bind(menuButtonCategory.widthProperty().subtract(2));

            // CustomMenuItem
            CustomMenuItem menuItem = new CustomMenuItem(wrapper);
            menuItem.setHideOnClick(false);

            // Klik seluruh area menuItem
            menuItem.setOnAction(e -> {
                boolean selected = !selectedCategories.contains(kategori);
                if (selected) selectedCategories.add(kategori);
                else selectedCategories.remove(kategori);

                checkMark.setVisible(selected);
                updateCategoriesMenuText();
                applyFilterAndSort();
            });

            menuButtonCategory.getItems().add(menuItem);
        }
    }
    private void updateCategoriesMenuText() {
        if (selectedCategories.isEmpty()) {
            // prompt-like state
            menuButtonCategory.setText("Categories");
            menuButtonCategory.setStyle("-fx-text-fill: #9CA3AF;"); // abu-abu
            return;
        }
        String text = selectedCategories.stream()
                .map(Kategori::getNama)
                .collect(Collectors.joining(", "));

        menuButtonCategory.setText(text);
        menuButtonCategory.setStyle("-fx-text-fill: -fx-text-base-color;");
    }
    private void initMenuButtonLabel() {
        menuButtonLabel.getItems().clear();
        menuButtonLabel.setText("Label");
        menuButtonLabel.setStyle("-fx-text-fill: #9CA3AF;"); // abu-abu
        List<TipeLabel> dataTipeLabel = DataManager.getInstance().getFilteredLabel();

        for (TipeLabel tipeLabel : dataTipeLabel) {
            // Icon
            ImageView iconView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/icons/tagW.png").toString())));
            iconView.setFitWidth(14);
            iconView.setFitHeight(14);
            iconView.setPreserveRatio(true);
            StackPane iconBox = new StackPane(iconView);
            iconBox.setPrefSize(28, 28);
            iconBox.setBackground(new Background(new BackgroundFill(
                    tipeLabel.getWarna(), new CornerRadii(8), Insets.EMPTY)));

            // Label
            Label label = new Label(tipeLabel.getNama());
            label.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

            // Centang
            Label checkMark = new Label("✓");
            checkMark.setTextFill(Color.GREEN);
            checkMark.setVisible(selectedCategories.contains(tipeLabel));

            // Spacer
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // HBox wrapper
            HBox wrapper = new HBox(5, checkMark, iconBox, label, spacer);
            wrapper.setAlignment(Pos.CENTER_LEFT);
            wrapper.setPadding(new Insets(2, 5, 2, 5));
            wrapper.setMaxWidth(Double.MAX_VALUE); // biar expand
            wrapper.prefWidthProperty().bind(menuButtonCategory.widthProperty().subtract(2));

            // CustomMenuItem
            CustomMenuItem menuItem = new CustomMenuItem(wrapper);
            menuItem.setHideOnClick(false);

            // Klik seluruh area menuItem
            menuItem.setOnAction(e -> {
                boolean selected = !selectedLabels.contains(tipeLabel);
                if (selected) selectedLabels.add(tipeLabel);
                else selectedLabels.remove(tipeLabel);

                checkMark.setVisible(selected);
                updateLabelMenuText();
                applyFilterAndSort();
            });

            menuButtonLabel.getItems().add(menuItem);
        }
    }
    private void updateLabelMenuText() {
        if (selectedLabels.isEmpty()) {
            // prompt-like state
            menuButtonLabel.setText("Labels");
            menuButtonLabel.setStyle("-fx-text-fill: #9CA3AF;"); // abu-abu
            return;
        }
        String text = selectedLabels.stream()
                .map(TipeLabel::getNama)
                .collect(Collectors.joining(", "));

        menuButtonLabel.setText(text);
        menuButtonLabel.setStyle("-fx-text-fill: -fx-text-base-color;");
    }
    private void initMenuButtonCurrency() {
        menuButtonCurrencie.getItems().clear();
        menuButtonCurrencie.setText("Currency");
        menuButtonCurrencie.setStyle("-fx-text-fill: #9CA3AF;"); // abu-abu
        List<MataUang> dataMataUang = DataManager.getInstance().getFilteredMataUang();

        for (MataUang mataUang : dataMataUang) {
            // Label teks mata uang
            Label label = new Label(mataUang.getNama());
            label.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

            // Centang
            Label checkMark = new Label("✓");
            checkMark.setTextFill(Color.GREEN);
            checkMark.setVisible(selectedCurrencies.contains(mataUang));

            // Spacer supaya full row clickable
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Wrapper HBox
            HBox wrapper = new HBox(5, checkMark, label, spacer);
            wrapper.setAlignment(Pos.CENTER_LEFT);
            wrapper.setPadding(new Insets(2, 5, 2, 5));
            wrapper.setMaxWidth(Double.MAX_VALUE);
            wrapper.prefWidthProperty().bind(menuButtonCurrencie.widthProperty().subtract(2));

            // CustomMenuItem
            CustomMenuItem menuItem = new CustomMenuItem(wrapper);
            menuItem.setHideOnClick(false);

            // Klik seluruh area menuItem
            menuItem.setOnAction(e -> {
                boolean selected = !selectedCurrencies.contains(mataUang);
                if (selected) selectedCurrencies.add(mataUang);
                else selectedCurrencies.remove(mataUang);

                checkMark.setVisible(selected);
                updateCurrencyMenuText();
                applyFilterAndSort();
            });

            menuButtonCurrencie.getItems().add(menuItem);
        }
    }
    private void updateCurrencyMenuText() {
        if (selectedCurrencies.isEmpty()) {
            menuButtonCurrencie.setText("Currency");
            menuButtonCurrencie.setStyle("-fx-text-fill: #9CA3AF;"); // abu-abu
            return;
        }
        String text = selectedCurrencies.stream()
                .map(MataUang::getNama)
                .collect(Collectors.joining(", "));
        menuButtonCurrencie.setText(text);
        menuButtonCurrencie.setStyle("-fx-text-fill: -fx-text-base-color;");
    }
    private void initMenuButtonPaymentType() {
        menuButtonPaymentType.getItems().clear();
        menuButtonPaymentType.setText("Type");
        menuButtonPaymentType.setStyle("-fx-text-fill: #9CA3AF;"); // abu-abu
        List<PaymentType> dataType = Arrays.asList(PaymentType.values());

        for (PaymentType paymentType : dataType) {
            Label label = new Label(paymentType.getLabel());
            label.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

            // Centang
            Label checkMark = new Label("✓");
            checkMark.setTextFill(Color.GREEN);
            checkMark.setVisible(selectedPaymentTypes.contains(paymentType));

            // Spacer supaya full row clickable
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Wrapper HBox
            HBox wrapper = new HBox(5, checkMark, label, spacer);
            wrapper.setAlignment(Pos.CENTER_LEFT);
            wrapper.setPadding(new Insets(2, 5, 2, 5));
            wrapper.setMaxWidth(Double.MAX_VALUE);
            wrapper.prefWidthProperty().bind(menuButtonPaymentType.widthProperty().subtract(2));

            // CustomMenuItem
            CustomMenuItem menuItem = new CustomMenuItem(wrapper);
            menuItem.setHideOnClick(false);

            // Klik seluruh area menuItem
            menuItem.setOnAction(e -> {
                boolean selected = !selectedPaymentTypes.contains(paymentType);
                if (selected) selectedPaymentTypes.add(paymentType);
                else selectedPaymentTypes.remove(paymentType);

                checkMark.setVisible(selected);
                updatePaymentTypeMenuText();
                applyFilterAndSort();
            });

            menuButtonPaymentType.getItems().add(menuItem);
        }
    }
    private void updatePaymentTypeMenuText() {
        if (selectedPaymentTypes.isEmpty()) {
            menuButtonPaymentType.setText("Type");
            menuButtonPaymentType.setStyle("-fx-text-fill: #9CA3AF;"); // abu-abu
            return;
        }
        String text = selectedPaymentTypes.stream()
                .map(PaymentType::getLabel)
                .collect(Collectors.joining(", "));
        menuButtonPaymentType.setText(text);
        menuButtonPaymentType.setStyle("-fx-text-fill: -fx-text-base-color;");
    }
    private void initMenuButtonPaymentState() {
        menuButtonPaymentState.getItems().clear();
        menuButtonPaymentState.setText("State");
        menuButtonPaymentState.setStyle("-fx-text-fill: #9CA3AF;"); // abu-abu
        List<PaymentStatus> dataStatus = Arrays.asList(PaymentStatus.values());

        for (PaymentStatus paymentStatus : dataStatus) {
            Label label = new Label(paymentStatus.getLabel());
            label.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

            // Centang
            Label checkMark = new Label("✓");
            checkMark.setTextFill(Color.GREEN);
            checkMark.setVisible(selectedPaymentStates.contains(paymentStatus));

            // Spacer supaya full row clickable
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Wrapper HBox
            HBox wrapper = new HBox(5, checkMark, label, spacer);
            wrapper.setAlignment(Pos.CENTER_LEFT);
            wrapper.setPadding(new Insets(2, 5, 2, 5));
            wrapper.setMaxWidth(Double.MAX_VALUE);
            wrapper.prefWidthProperty().bind(menuButtonPaymentState.widthProperty().subtract(2));

            // CustomMenuItem
            CustomMenuItem menuItem = new CustomMenuItem(wrapper);
            menuItem.setHideOnClick(false);

            // Klik seluruh area menuItem
            menuItem.setOnAction(e -> {
                boolean selected = !selectedPaymentStates.contains(paymentStatus);
                if (selected) selectedPaymentStates.add(paymentStatus);
                else selectedPaymentStates.remove(paymentStatus);

                checkMark.setVisible(selected);
                updatePaymentStateMenuText();
                applyFilterAndSort();
            });

            menuButtonPaymentState.getItems().add(menuItem);
        }
    }
    private void updatePaymentStateMenuText() {
        if (selectedPaymentStates.isEmpty()) {
            menuButtonPaymentState.setText("State");
            menuButtonPaymentState.setStyle("-fx-text-fill: #9CA3AF;"); // abu-abu
            return;
        }
        String text = selectedPaymentStates.stream()
                .map(PaymentStatus::getLabel)
                .collect(Collectors.joining(", "));
        menuButtonPaymentState.setText(text);
        menuButtonPaymentState.setStyle("-fx-text-fill: -fx-text-base-color;");
    }

    // [4] >=== FILTER LISTENER
    private Predicate<Transaksi> accountFilter() {
        return t ->
                selectedAccounts.isEmpty()
                        || selectedAccounts.contains(t.getAkun());
    }
    private Predicate<Transaksi> categoryFilter() {
        return t ->
                selectedCategories.isEmpty()
                        || selectedCategories.contains(t.getKategori());
    }
    private Predicate<Transaksi> labelFilter() {
        return t ->
                selectedLabels.isEmpty()
                        || selectedLabels.contains(t.getTipelabel());
    }
    private Predicate<Transaksi> currencyFilter() {
        return t ->
                selectedCurrencies.isEmpty()
                        || selectedCurrencies.contains(t.getAkun().getMataUang());
    }
    private Predicate<Transaksi> paymentTypeFilter() {
        return t ->
                selectedPaymentTypes.isEmpty()
                        || selectedPaymentTypes.contains(t.getPaymentType());
    }
    private Predicate<Transaksi> paymentStateFilter() {
        return t ->
                selectedPaymentStates.isEmpty()
                        || selectedPaymentStates.contains(t.getPaymentStatus());
    }

    // [5] >=== FILTER HANDLER
    private void applyFilterAndSort() {
        List<Transaksi> result = incomeTransaction.stream()
                .filter(accountFilter())
                .filter(categoryFilter())
                .filter(labelFilter())
                .filter(currencyFilter())
                .filter(paymentTypeFilter())
                .filter(paymentStateFilter())
                // filter lainnya...
                .sorted(activeComparator())  // sort sesuai pilihan user
                .toList();

        String recordCounter = "Found " + result.size() + " record";
        labelTotalRecords.setText(recordCounter);
        totalIncomeAmountSetter(result);
        refreshView(result);
    }
    private Comparator<Transaksi> activeComparator() {
        SortOption sort = comboBoxSort.getValue();
        if (sort == null) {
            return Comparator.comparing(Transaksi::getTanggal).reversed()
                    .thenComparing(Transaksi::getId); // default
        }

        switch (sort) {
            case TIME_NEWEST:
                return Comparator.comparing(Transaksi::getTanggal).reversed()
                        .thenComparing(Transaksi::getId);
            case TIME_OLDEST:
                return Comparator.comparing(Transaksi::getTanggal)
                        .thenComparing(Transaksi::getId);
            case AMOUNT_HIGHEST:
                return Comparator.comparing(Transaksi::getJumlah).reversed()
                        .thenComparing(Transaksi::getId);
            case AMOUNT_LOWEST:
                return Comparator.comparing(Transaksi::getJumlah)
                        .thenComparing(Transaksi::getId);
            default:
                return Comparator.comparing(Transaksi::getTanggal).reversed()
                        .thenComparing(Transaksi::getId);
        }
    }
    private void refreshView(List<Transaksi> data) {
        recordPanel.getChildren().clear();

        for (Transaksi trans : data) {
            recordPanel.getChildren().add(recordCardBoard.get(trans));
        }
    }

    // [6] >=== CONTROLLER LAINYA...
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