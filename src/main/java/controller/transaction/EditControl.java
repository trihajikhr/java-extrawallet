package controller.transaction;

import controller.DashboardControl;
import controller.PopupUtils;
import controller.option.TransactionParent;
import dataflow.DataLoader;
import dataflow.DataManager;
import helper.Converter;
import helper.IOLogic;
import helper.MyPopup;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import model.Currency;
import model.enums.PaymentStatus;
import model.enums.PaymentType;
import model.extended.RecordCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AbstractTransactionService;
import service.ExpenseService;
import service.IncomeService;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class EditControl implements Initializable {
    // atribut vital
    private static final Logger log = LoggerFactory.getLogger(EditControl.class);
    private Stage stage;
    @FXML private AnchorPane rootPane;
    private Transaction transOriginal;
    private Boolean isSingle = false;
    private TransactionParent parent;

    // atribut pendukung
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean closing = false;
    private ObservableList<LabelType> labelTypeList = FXCollections.observableArrayList();

    // atribut fxml
    @FXML private Spinner<Integer> amountEdit;
    @FXML private ComboBox<Currency> mataUangCombo;
    @FXML private ComboBox<Account> akunComboBox;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private ComboBox<LabelType> tipeLabelCombo;
    @FXML private DatePicker dateEdit;
    @FXML private TextField noteEdit;
    @FXML private ComboBox<PaymentType> paymentType;
    @FXML private ComboBox<PaymentStatus> paymentStatus;
    @FXML private Button submitButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadAllData();
        logicHandler();
        akunToMataUangListener();
        loadTipeLabelComboBox();
        isFormComplete();

        showPopup();
    }

    // [0] >=== SCENE CONTROLLER
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML private void showPopup() {
        PopupUtils.showPopup(rootPane, stage);
    }
    @FXML private void closePopup() {
        if (closing) return;
        closing = true;
        PopupUtils.closePopup(rootPane, stage);
    }
    public void setIsMultiple(Boolean val){
        this.isSingle = val;
        isFormComplete();
        log.info("isSingle: " + (isSingle ? "true" : "false"));
    }
    public void setParent(TransactionParent parent) {
        this.parent = parent;
    }
    public Map<Transaction, RecordCard> getParentRecordCards() {
        return parent.getRecordCardBoard();
    }

    // [1] >=== SCENE CONNECTION
    public ComboBox<LabelType> getTipeLabelCombo() {
        return tipeLabelCombo;
    }
    public ObservableList<LabelType> getTipeLabelList() {
        return labelTypeList;
    }

    // [2] >=== DATA LOADER
    private void loadAllData() {
        DataLoader.mataUangComboBoxLoader(mataUangCombo);
        DataLoader.akunComboBoxLoader(akunComboBox);
        DataLoader.kategoriComboBoxLoader(categoryComboBox);
        paymentType.setItems(DataManager.getInstance().getDataPaymentType());
        paymentStatus.setItems(DataManager.getInstance().getDataPaymentStatus());
        Converter.bindEnumComboBox(paymentType, PaymentType::getLabel);
        Converter.bindEnumComboBox(paymentStatus, PaymentStatus::getLabel);

        // lock combobox mata uang
        mataUangCombo.setMouseTransparent(true);
        mataUangCombo.setFocusTraversable(false);
        mataUangCombo.getStyleClass().add("locked");
    }
    private void loadTipeLabelComboBox(){
        ArrayList<LabelType> dataTipelabel = DataManager.getInstance().getDataTipeLabel();
        labelTypeList = FXCollections.observableArrayList(dataTipelabel);

        tipeLabelCombo.setItems(labelTypeList);
        tipeLabelCombo.setCellFactory(list -> new ListCell<LabelType>() {
            @Override
            protected void updateItem(LabelType item, boolean empty) {
                super.updateItem(item, empty);

                if(empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                // icon
                ImageView iconView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/icons/tagW.png")).toString()));
                iconView.setFitWidth(14);
                iconView.setFitHeight(14);
                iconView.setPreserveRatio(true);

                // background
                StackPane iconBox = new StackPane(iconView);
                iconBox.setPrefSize(28,28);;
                iconBox.setMaxSize(28,28);

                iconBox.setBackground(new Background(
                        new BackgroundFill(
                                item.getColor(),
                                new CornerRadii(8),
                                Insets.EMPTY
                        )
                ));

                // teks
                Label label = new Label(item.getName());
                label.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

                // gabung
                HBox box = new HBox(10, iconBox, label);
                box.setAlignment(Pos.CENTER_LEFT);
                setGraphic(box);
            }
        });
        tipeLabelCombo.setButtonCell(tipeLabelCombo.getCellFactory().call(null));
    }
    public void prefilFromRecord(Transaction trans) {
        amountEdit.getEditor().setText(Integer.toString(trans.getAmount()));
        akunComboBox.setValue(trans.getAccount());
        categoryComboBox.setValue(trans.getCategory());
        tipeLabelCombo.setValue(trans.getLabelType());
        dateEdit.setValue(trans.getDate());
        noteEdit.setText(
                Objects.requireNonNullElse(trans.getDescription(), "")
        );
        paymentType.setValue(trans.getPaymentType());
        paymentStatus.setValue(trans.getPaymentStatus());
        this.transOriginal = trans;
    }

    // [3] >=== LOGIC HANDLER & FORM VALIDATION
    private void logicHandler() {
        IOLogic.isTextFieldValid(noteEdit, 50);
        IOLogic.makeIntegerOnlyBlankInitial(amountEdit, 0, 2_147_483_647);
    }
    private void isFormComplete() {
        BooleanBinding amountValid =
                Bindings.createBooleanBinding(
                        () -> amountEdit.getValue() != null && amountEdit.getValue() > 0,
                        amountEdit.valueProperty()
                );

        BooleanBinding akunValid = akunComboBox.valueProperty().isNotNull();
        BooleanBinding kategoriValid = categoryComboBox.valueProperty().isNotNull();
        BooleanBinding dateValid = dateEdit.valueProperty().isNotNull();

        BooleanBinding formValid =
                amountValid
                        .and(akunValid)
                        .and(kategoriValid)
                        .and(dateValid);

        if (isSingle){
            BooleanBinding isChanged = isChangedBinding();
            submitButton.disableProperty().bind(
                    formValid.and(isChanged).not()
            );
        } else {
            submitButton.disableProperty().bind(formValid.not());
        }
    }
    private BooleanBinding isChangedBinding() {
        return Bindings.createBooleanBinding(() -> {
                    if (transOriginal == null) return false;

                    Transaction current = new Transaction(
                            transOriginal.getId(),
                            transOriginal.getTransactionType(),
                            amountEdit.getValue(),
                            akunComboBox.getValue(),
                            categoryComboBox.getValue(),
                            tipeLabelCombo.getValue(),
                            dateEdit.getValue(),
                            noteEdit.getText() == null || noteEdit.getText().isBlank()
                                    ? null : noteEdit.getText(),
                            paymentType.getValue(),
                            paymentStatus.getValue()
                    );

                    boolean changed = !current.isSameState(transOriginal);
                    if(changed){
                        log.info("EDIT: data tidak sama!");
                    } else {
                        log.info("EDIT: data sama!");
                    }

                    return changed;
                },
                amountEdit.valueProperty(),
                akunComboBox.valueProperty(),
                categoryComboBox.valueProperty(),
                tipeLabelCombo.valueProperty(),
                dateEdit.valueProperty(),
                noteEdit.textProperty(),
                paymentType.valueProperty(),
                paymentStatus.valueProperty()
        );
    }

    // [4] >=== LISTENER
    private void akunToMataUangListener() {
        akunComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mataUangCombo.setValue(newVal.getCurrencyType());
            }
        });
    }

    // [5] >=== BUTTON
    @FXML
    private void addLabelOnEdit(ActionEvent evt) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/label.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();

            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);

            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(10.0);
            dropShadow.setOffsetX(5.0);
            dropShadow.setOffsetY(5.0);
            dropShadow.setColor(Color.rgb(0, 0, 0, 0.4));
            root.setEffect(dropShadow);

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);

//        stage.setMinWidth(450);
//        stage.setMinHeight(560);
//        stage.setMaxWidth(800);
//        stage.setMaxHeight(700);

            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            LabelControl ctrl = loader.getController();
            ctrl.setStage(stage);
            ctrl.setParentEditRecord(this);

            stage.showAndWait();

        } catch (IOException e) {
            log.error("gagal membuka panel tambah label!", e);
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
        }
    }
    @FXML
    private void submitHandler(ActionEvent evt) {
        if(isSingle){

            AbstractTransactionService service =
                    resolveService(transOriginal);

            String note = noteEdit.getText();
            note = (note == null || note.isBlank()) ? null : note;

            Transaction transModified = new Transaction(
                    transOriginal.getId(),
                    transOriginal.getTransactionType(),
                    amountEdit.getValue(),
                    akunComboBox.getValue(),
                    categoryComboBox.getValue(),
                    tipeLabelCombo.getValue(),
                    dateEdit.getValue(),
                    note,
                    paymentType.getValue(),
                    paymentStatus.getValue()
            );

            Boolean isChanged = !transModified.isSameState(transOriginal);
            if(isChanged) {

                boolean saldoOk = service.updateSingleAkun(
                        transOriginal.getAccount(),   // account LAMA
                        transOriginal,             // transaksi LAMA
                        transModified.getAmount()  // jumlah BARU
                );

                if (!saldoOk) {
                    return; // popup sudah muncul di service
                }

                DataManager.getInstance().modifyTransaksi(transModified);
                String page = DashboardControl.getInstance().getCurrentPage();
                DashboardControl.getInstance().loadPage(page);
            }

        } else {
            List<Transaction> selected = parent.getRecordCardBoard().entrySet().stream()
                    .filter(e -> e.getValue().getCheckList().isSelected())
                    .map(Map.Entry::getKey)
                    .toList();

            // safety: minimal 1 data
            if (selected.isEmpty()) return;

            // safety: satu account saja
            if (selected.stream().map(Transaction::getAccount).distinct().count() > 1) {
                MyPopup.showDanger("Gagal", "Edit massal hanya boleh untuk satu account");
                return;
            }

            AbstractTransactionService service =
                    resolveService(selected.get(0));

            Account account = selected.get(0).getAccount();
            int saldoAwal = account.getBalance();

            List<Transaction> newList = new ArrayList<>();

            // === VALIDASI & HITUNG SALDO (SIMULASI) ===
            for (Transaction old : selected) {
                Transaction edited = new Transaction(
                        old.getId(),
                        old.getTransactionType(),
                        amountEdit.getValue(),
                        akunComboBox.getValue(),
                        categoryComboBox.getValue(),
                        tipeLabelCombo.getValue(),
                        dateEdit.getValue(),
                        noteEdit.getText(),
                        paymentType.getValue(),
                        paymentStatus.getValue()
                );

                boolean ok = service.updateSingleAkun(
                        account,
                        old,
                        edited.getAmount()
                );

                if (!ok) {
                    account.setBalance(saldoAwal);
                    DataManager.getInstance().updateSaldoAkun(account, saldoAwal);
                    return;
                }

                newList.add(edited);
            }

            DataManager.getInstance().modifyMultipleTransaksi(newList);

            String page = DashboardControl.getInstance().getCurrentPage();
            DashboardControl.getInstance().loadPage(page);
        }
        closePopup();
    }

    private AbstractTransactionService resolveService(Transaction t) {
        return switch (t.getTransactionType()) {
            case INCOME -> new IncomeService();
            case EXPANSE -> new ExpenseService();
        };
    }
}
