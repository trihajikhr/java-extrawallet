package controller;

import controller.option.SortOption;
import controller.option.TransactionParent;
import controller.transaction.EditControl;
import dataflow.DataManager;
import helper.Converter;
import helper.MyPopup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.CurrencyApiClient;
import service.IncomeService;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IncomeControl implements Initializable, TransactionParent {
    private static final Logger log = LoggerFactory.getLogger(IncomeControl.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
    DateTimeFormatter formatterNow = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy");

    private double xOffset = 0;
    private double yOffset = 0;

    // data sumber kebenaran
    List<Transaksi> incomeTransaction  = new ArrayList<>();
    private final Map<Transaksi, RecordCard> recordCardBoard = new HashMap<>();
    private final List<CheckBox> visibleCheckBox = new ArrayList<>();

    @FXML private VBox recordPanel;
    @FXML private AnchorPane checkBoxIndicatorPanel;

    // label tampilan
    @FXML private Label labelTotalRecords;
    @FXML private Label labelTanggalSekarang;
    @FXML private Label labelTotalAmount;
    @FXML private Label labelSelectAll;
    private BigDecimal totalDefaultValue = BigDecimal.ZERO;
    private BigDecimal totalSelectedValue = BigDecimal.ZERO;

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

    // checkbox untuk select all
    @FXML private CheckBox checkBoxSelectAll;
    private int checkBoxSelectedCount = 0;
    private boolean isBulkChanging = false;
    private boolean isUpdatingFromSingleSelect = false;
    private boolean isAnyCheckBoxSelected = false;

    // bagian button
    @FXML private Button editButton;
    @FXML private Button exportButton;
    @FXML private Button deleteButton;
    private Map<String, Button> mainButtonList = new HashMap<>();

    // [0] >=== INIT FUNCTION & SCENE CONNECTOR
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("panel record income berhasil terbuka");
        fetchTransactionData();
        initAllComboBox();
        initBaseData();

        // listener
        checkBoxSelectAllListener();
        checkBoxSetupListeners();
        updateButtons();
    }
    private void initBaseData() {
        mainButtonInit();
        loadStyleForRecordParent();
        setDateNow();
        recordCounterLabelInit();
        defaultTotalAmountSetter(incomeTransaction);
        setFirstFilter();
    }
    @Override
    public Map<Transaksi, RecordCard> getRecordCardBoard() {
        return recordCardBoard;
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
    private void mainButtonInit() {
        mainButtonList.put("edit", editButton);
        mainButtonList.put("export", exportButton);
        mainButtonList.put("delete", deleteButton);
        mainButtonStyler(isAnyCheckBoxSelected);
    }
    private void loadStyleForRecordParent() {
        recordPanel.getStylesheets().add(
                getClass().getResource("/stylesheet/record-card.css").toExternalForm()
        );
    }
    private void setFirstFilter() {
        applyFilterAndSort();
    }
    private void defaultTotalAmountSetter(List<Transaksi> dataIncome) {
        totalDefaultValue = (IncomeService.getInstance().incomeSumAfterFilter(dataIncome));
        String stringForm = totalDefaultValue.toPlainString();
        String result = Converter.numberFormatter(stringForm);
        labelTotalAmount.setText("IDR " + result);
    }
    private void selectedAmountSetter(Boolean anySelected) {
        if(anySelected) {
            String stringForm = totalSelectedValue.toPlainString();
            String result = Converter.numberFormatter(stringForm);
            labelTotalAmount.setText("IDR " + result);
        } else {
            String stringForm = totalDefaultValue.toPlainString();
            String result = Converter.numberFormatter(stringForm);
            labelTotalAmount.setText("IDR " + result);
        }
    }
    private void resetSelectedAmount() {
        totalSelectedValue = BigDecimal.ZERO;
    }

    // [2] >=== CARDBOARD UI/UX & DATA FETCHING
    private RecordCard createRecordCard(Transaksi income) {
         RecordCard recordCard = new RecordCard(income);
         recordCard.setOnCardClick(this::openSingleEdit);
         visibleCheckBox.add(recordCard.getCheckList());
         return recordCard;
    }
    private void fetchTransactionData() {
        log.info("data income berhasil diambil dari datamanager");

//        recordCardBoard.clear();
//        recordPanel.getChildren().clear();
//        visibleCheckBox.clear();

        incomeTransaction = DataManager.getInstance().getDataTransaksiPemasukan();

        for(Transaksi in : incomeTransaction) {
            recordCardBoard.put(in, createRecordCard(in));
        }

        for(RecordCard card : recordCardBoard.values()) {
            recordPanel.getChildren().add(card.getCardWrapper());
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
        defaultTotalAmountSetter(result);

        refreshView(result);
        refreshVisibleCheckbox(result);
        resetSelectedAmount();
    }
    private Comparator<Transaksi> activeComparator() {
        SortOption sort = comboBoxSort.getValue();
        if (sort == null) {
            return Comparator.comparing(Transaksi::getTanggal).reversed()
                    .thenComparing(Comparator.comparing(Transaksi::getId).reversed());
        }

        Comparator<Transaksi> byIdDesc =
                Comparator.comparing(Transaksi::getId).reversed();

        switch (sort) {
            case TIME_NEWEST:
                return Comparator.comparing(Transaksi::getTanggal).reversed()
                        .thenComparing(byIdDesc);
            case TIME_OLDEST:
                return Comparator.comparing(Transaksi::getTanggal)
                        .thenComparing(Transaksi::getId);
            case AMOUNT_HIGHEST:
                return Comparator.comparing(Transaksi::getJumlah).reversed()
                        .thenComparing(byIdDesc);
            case AMOUNT_LOWEST:
                return Comparator.comparing(Transaksi::getJumlah)
                        .thenComparing(byIdDesc);
            default:
                return Comparator.comparing(Transaksi::getTanggal).reversed()
                        .thenComparing(byIdDesc);
        }
    }
    private void refreshView(List<Transaksi> data) {
        recordPanel.getChildren().clear();

        for (Transaksi trans : data) {
            recordPanel.getChildren().add(recordCardBoard.get(trans).getCardWrapper());
        }
    }

    // [6] >=== CHECKBOX LISTENER & HANDLER
    private void checkBoxSelectAllListener() {
        checkBoxSelectAll.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if(isUpdatingFromSingleSelect) return;
            if(isNowSelected) {
                applySelectAllCheckBox();
            } else {
                applyDeselectAllCheckBox();
            }
            selectedAmountSetter(false);
        });
    }
    private void refreshVisibleCheckbox(List<Transaksi> dataTransaksi){
        applyDeselectAllCheckBox();
        visibleCheckBox.clear();
        for(Transaksi trans : dataTransaksi) {
            visibleCheckBox.add(recordCardBoard.get(trans).getCheckList());
        }
    }
    private void applySelectAllCheckBox() {
        isBulkChanging = true;
        for (CheckBox cek : visibleCheckBox) {
            cek.setSelected(true);
        }
        checkBoxSelectedCount = visibleCheckBox.size();
        totalSelectedValue = totalDefaultValue;
        updateButtons();
        isBulkChanging = false;
    }
    private void applyDeselectAllCheckBox() {
        isBulkChanging = true;
        for (CheckBox cek : visibleCheckBox) {
            cek.setSelected(false);
        }
        checkBoxSelectedCount = 0;
        resetSelectedAmount();
        updateButtons();
        isBulkChanging = false;
    }
    private void checkBoxSetupListeners() {
        for (RecordCard rc : recordCardBoard.values()) {
            rc.setCheckBoxListener(isSelected -> {
                if (isBulkChanging) return; // skip update count saat bulk change

                if (isSelected) checkBoxSelectedCount++;
                else checkBoxSelectedCount--;

                if(isSelected) {
                    totalSelectedValue = totalSelectedValue.add(CurrencyApiClient.getInstance().convert(
                            BigDecimal.valueOf(rc.getTransaksi().getJumlah()),
                            rc.getTransaksi().getAkun().getMataUang().getKode(),
                            "IDR"
                    ));
                } else{
                    totalSelectedValue = totalSelectedValue.subtract(CurrencyApiClient.getInstance().convert(
                            BigDecimal.valueOf(rc.getTransaksi().getJumlah()),
                            rc.getTransaksi().getAkun().getMataUang().getKode(),
                            "IDR"
                    ));
                }

                updateButtons();
            });
        }
    }
    private void updateButtons() {
        boolean anySelected = checkBoxSelectedCount > 0;

        // System.out.println("checkbox sekarang: " + checkBoxSelectedCount);
        // System.out.println("default total: " + totalDefaultValue);
        // System.out.println("checked total: " + totalSelectedValue);

        if(anySelected != isAnyCheckBoxSelected) {
            System.out.println("mendeteksi perubahan TOOGLER");
            mainButtonStyler(anySelected);
            isAnyCheckBoxSelected = anySelected;
        }

        checkBoxIndicatorPanelSetter(anySelected);
        editButton.setDisable(!anySelected);
        exportButton.setDisable(!anySelected);
        deleteButton.setDisable(!anySelected);
    }
    private void checkBoxIndicatorPanelSetter(boolean result) {
        if(result) {
            checkBoxIndicatorPanel.setStyle("-fx-background-color: #FFF9DB;");

            String showLabel;
            isUpdatingFromSingleSelect = true; // guard start

            if(checkBoxSelectedCount == visibleCheckBox.size()) {
                checkBoxSelectAll.setSelected(true);
                showLabel = "Deselect all";
            } else {
                showLabel = "Select all, selected " + checkBoxSelectedCount;
                checkBoxSelectAll.setSelected(false);
            }

            isUpdatingFromSingleSelect = false; // guard end
            labelSelectAll.setText(showLabel);

        } else {
            isUpdatingFromSingleSelect = true; // guard start
            checkBoxIndicatorPanel.setStyle("-fx-background-color: #FFFFFF;");
            labelSelectAll.setText("Select all");
            checkBoxSelectAll.setSelected(false);
            isUpdatingFromSingleSelect = false; // guard start
        }

        selectedAmountSetter(result);
    }

    // [7] >=== TOMBOL EDIT UNTUK RECORDCARD
    private void openSingleEdit(Transaksi trans) {
        openSingleEdit(trans, false); // default true
    }
    @FXML
    private void openSingleEdit(Transaksi trans, Boolean isMultiple) {
        // setting stage ini mirip dengan fungsi addTransaction di class DashboardController!
        // komentas yang lebih lengkap ada disana!

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit-transaction.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();


            // undecorated windows
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);

            // dropshadow ke root
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(10.0);
            dropShadow.setOffsetX(5.0);
            dropShadow.setOffsetY(5.0);
            dropShadow.setColor(Color.rgb(0, 0, 0, 0.4));
            root.setEffect(dropShadow);

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);

            // stage.setMinWidth(750);
            // stage.setMinHeight(650);
            // stage.setMaxWidth(800);
            // stage.setMaxHeight(700);

            // draggable pop up
            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            // kasih akses stage ke controller
            EditControl ctrl = loader.getController();
            ctrl.setStage(stage);
            ctrl.setParent(this);
            if(isMultiple){
                ctrl.setIsMultiple(isMultiple);
            } else {
                ctrl.setIsMultiple(isMultiple);
                ctrl.prefilFromRecord(trans);
            }

            stage.showAndWait();
        } catch (IOException e) {
            log.error("gagal membuka jendela edit!", e);
        }
    }

    // [7] >=== 3 BUTTON PENGEDITAN & HELPER
    private void mainButtonStyler(Boolean value) {
        if(value) {
            mainButtonList.forEach((name, btn) -> {
                btn.getStyleClass().remove("unselect-button");
            });
            mainButtonList.forEach((name, btn) -> {
                btn.getStyleClass().add("selected-button-" + name);
            });
        } else {
            mainButtonList.forEach((name, btn) -> {
                btn.getStyleClass().remove("selected-button-" + name);
            });
            mainButtonList.forEach((name, btn) -> {
                btn.getStyleClass().add("unselect-button");
            });
        }
    }
    @FXML
    private void handleMultipleEdit() {
        openSingleEdit(null, true);
    }

    @FXML
    private void handleDelete() {
        MyPopup.showSucces("Coming Soon!", "dalam tahap pengembangan");
    }

    @FXML
    private void handleExport() {
        MyPopup.showSucces("Coming Soon!", "dalam tahap pengembangan");
    }

    private void showNotification(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}