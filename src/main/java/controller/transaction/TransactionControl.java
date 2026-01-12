package controller.transaction;

import controller.DashboardControl;
import controller.PopupUtils;
import dataflow.DataLoader;
import dataflow.DataManager;
import helper.Converter;
import helper.IOLogic;
import helper.MyPopup;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;
import model.*;
import model.enums.PaymentStatus;
import model.enums.PaymentType;
import model.enums.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TransactionControl implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(TransactionControl.class);

    private Stage stage;
    IntegerProperty valueChoosen = new SimpleIntegerProperty(0);
    private boolean closing = false;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML private AnchorPane rootPane;

    @FXML private Button incomeBtn_inout;
    @FXML private Button expenseBtn_inout;
    @FXML private Button transferBtn_inout;

    @FXML private Button incomeBtn_trans;
    @FXML private Button expenseBtn_trans;
    @FXML private Button transferBtn_trans;

    @FXML private ImageView incomeImg;
    @FXML private ImageView expenseImg;
    @FXML private ImageView transferImg_inout;
    @FXML private ImageView transferImg_trans;

    @FXML private Label incomeLbl;
    @FXML private Label expenseLbl;
    @FXML private Label transferLbl_inout;
    @FXML private Label transferLbl_trans;

    @FXML private GridPane inoutForm;
    @FXML private GridPane transForm;

    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private ComboBox<PaymentType> paymentType_inout, paymentType_trans;
    @FXML private ComboBox<PaymentStatus> paymentStatus_inout, paymentStatus_trans;
    @FXML private ComboBox<Account> akunComboBox_inout, akunComboBox_from, akunComboBox_to;

    // combobox tipeLabel
    private ObservableList<LabelType> labelTypeList = FXCollections.observableArrayList();
    @FXML private ComboBox<LabelType> tipeLabel_inout;
    @FXML private ComboBox<LabelType> tipeLabel_trans;

    // template
    private ObservableList<Template> dataTemplateList = FXCollections.observableArrayList();
    @FXML private ComboBox<Template> templateComboBox_inout, templateComboBox_trans;

    private final ObjectProperty<PaymentType> selectedPaymentType =
            new SimpleObjectProperty<>();

    private final ObjectProperty<PaymentStatus> selectedPaymentStatus =
            new SimpleObjectProperty<>();

    private final StringProperty noteState = new SimpleStringProperty();
    @FXML private TextField note_inout, note_trans;

    @FXML private Image[][] theImage;

    // combobox mata uang
    @FXML private ComboBox<Currency> mataUangCombo_inout, mataUangCombo_from, mataUangCombo_to;

    // spinner
    @FXML Spinner<BigDecimal> spinner_inout, spinner_from, spinner_to;

    // tanggal
    @FXML DatePicker date_inout, date_trans;

    // checkbox
    @FXML CheckBox checkTemplate_inout, checkTemplate_trans;

    // submit button + add temlate
    @FXML Button submit_inout;
    @FXML Button submit_trans;
    @FXML Button addMultipleRecord_inout;
    @FXML Button addMultipleRecord_trans;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("Transaction pop up terbuka");

        // isformcomplete function
        isInOutFormComplete();
        isTransFormComplete();

        // beberapa fungsi init
        initPaymentData();
        initMessageBinding();
        initDate();
        initDataComboBox();
        initTipeLabelList();
        initTemplate();
        initButtonImage();
        initButtons();
        clearSelection(1); // default: semua putih, teks hitam, icon hitam
        activateIncome();

        // disable combobox
        mataUangCombo_inout.setMouseTransparent(true);
        mataUangCombo_inout.setFocusTraversable(false);
        mataUangCombo_from.setMouseTransparent(true);
        mataUangCombo_from.setFocusTraversable(false);
        mataUangCombo_to.setMouseTransparent(true);
        mataUangCombo_to.setFocusTraversable(false);
        mataUangCombo_inout.getStyleClass().add("locked");
        mataUangCombo_from.getStyleClass().add("locked");
        mataUangCombo_to.getStyleClass().add("locked");

        // default scene ke inout layer
        inoutForm.setVisible(true);
        transForm.setVisible(false);

        // helper
        spinnerLogicHandler();
        checkTemplate_trans.setDisable(true);
        IOLogic.isTextFieldValid(note_inout, 50);
        IOLogic.isTextFieldValid(note_trans, 50);

        // listener
        akunToMataUangListener();
        templateInOutListener();
        templateTransListener();

        // scene controller
        showPopup();
    }

    // [0] >=== SCENE CONTROLLER
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    public void showPopup() {
        PopupUtils.showPopup(rootPane, stage);
    }
    @FXML
    private void closePopup() {
        if (closing) return;
        closing = true;
       PopupUtils.closePopup(rootPane, stage);
    }
    public int getValueChoosen() {
        return valueChoosen.getValue();
    }

    // [1] >=== INIT FUNCTION
    private void initButtonImage() {
        theImage = DataManager.getInstance().getImageTransactionForm();
    }
    private void initPaymentData() {
        paymentType_inout.setItems(DataManager.getInstance().getDataPaymentType());
        paymentStatus_inout.setItems(DataManager.getInstance().getDataPaymentStatus());

        Converter.bindEnumComboBox(paymentType_inout, PaymentType::getLabel);
        Converter.bindEnumComboBox(paymentStatus_inout, PaymentStatus::getLabel);

        paymentType_trans.setItems(DataManager.getInstance().getDataPaymentType());
        paymentStatus_trans.setItems(DataManager.getInstance().getDataPaymentStatus());

        Converter.bindEnumComboBox(paymentType_trans, PaymentType::getLabel);
        Converter.bindEnumComboBox(paymentStatus_trans, PaymentStatus::getLabel);

        paymentType_inout.valueProperty().bindBidirectional(selectedPaymentType);
        paymentType_trans.valueProperty().bindBidirectional(selectedPaymentType);

        paymentStatus_inout.valueProperty().bindBidirectional(selectedPaymentStatus);
        paymentStatus_trans.valueProperty().bindBidirectional(selectedPaymentStatus);
    }
    private void initTemplate() {
        dataTemplateList = FXCollections.observableArrayList(
                DataManager.getInstance().getDataTemplate()
        );

        templateComboBox_inout.setItems(dataTemplateList);
        templateComboBox_inout.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Template temp, boolean empty) {
                super.updateItem(temp, empty);
                setText(empty || temp == null ? null : temp.getName());
            }
        });
        templateComboBox_inout.setButtonCell(
                templateComboBox_inout.getCellFactory().call(null)
        );

        templateComboBox_trans.setItems(dataTemplateList);
        templateComboBox_trans.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Template temp, boolean empty) {
                super.updateItem(temp, empty);
                setText(empty || temp == null ? null : temp.getName());
            }
        });
        templateComboBox_trans.setButtonCell(
                templateComboBox_trans.getCellFactory().call(null)
        );
    }
    private void initTipeLabelList() {
        ArrayList<LabelType> data = DataManager.getInstance().getDataTipeLabel();
        labelTypeList = FXCollections.observableArrayList(data);
        tipeLabel_inout.setItems(labelTypeList);
        tipeLabel_trans.setItems(labelTypeList);
    }
    private void initButtons() {

        // Set class dasar
        incomeBtn_inout.getStyleClass().add("choice-btn");
        expenseBtn_inout.getStyleClass().add("choice-btn");
        transferBtn_inout.getStyleClass().add("choice-btn");

        // incomeBtn_1.setOnAction(e -> select(1,0, incomeBtn_1, incomeImg, incomeLbl, "#01AA71"));
        // expenseBtn_1.setOnAction(e -> select(1,1, expenseBtn_1, expenseImg, expenseLbl, "#F92222"));

        incomeBtn_inout.setOnAction(e -> activateIncome());
//        incomeBtn_1.setOnAction(e -> {
//            select(1,0, incomeBtn_1, incomeImg, incomeLbl, "#01AA71");
//            updateCategoryCombo("IN");
//        });
        expenseBtn_inout.setOnAction(e -> activateExpense());
//            select(1,1, expenseBtn_inout, expenseImg, expenseLbl, "#F92222");
//            updateCategoryCombo("OUT");
//        });
        transferBtn_inout.setOnAction(e -> select(1,2, transferBtn_trans, transferImg_trans, transferLbl_trans, "#0176FE"));

        incomeBtn_trans.setOnAction(e -> select(2,0, incomeBtn_inout, incomeImg, incomeLbl, "#01AA71"));
        expenseBtn_trans.setOnAction(e -> select(2,1, expenseBtn_inout, expenseImg, expenseLbl, "#F92222"));
        transferBtn_trans.setOnAction(e -> select(2,2, transferBtn_trans, transferImg_trans, transferLbl_trans, "#0176FE"));
    }
    private void initDate() {
        date_inout.setValue(LocalDate.now());
        date_trans.setValue(LocalDate.now());
    }
    private void initMessageBinding() {
        note_inout.textProperty().bindBidirectional(noteState);
        note_trans.textProperty().bindBidirectional(noteState);
    }
    private void initDataComboBox() {
        DataLoader.akunComboBoxLoader(akunComboBox_inout);
        DataLoader.akunComboBoxLoader(akunComboBox_from);
        DataLoader.akunComboBoxLoader(akunComboBox_to);

        DataLoader.kategoriComboBoxLoader(categoryComboBox);
        DataLoader.tipeLabelComboBoxLoader(tipeLabel_inout);
        DataLoader.tipeLabelComboBoxLoader(tipeLabel_trans);

        DataLoader.mataUangComboBoxLoader(mataUangCombo_inout);
        DataLoader.mataUangComboBoxLoader(mataUangCombo_from);
        DataLoader.mataUangComboBoxLoader(mataUangCombo_to);
    }
    private void activateIncome() {
        select(1, 0, incomeBtn_inout, incomeImg, incomeLbl, "#01AA71");
        updateCategoryCombo("IN");
    }
    private void activateExpense() {
        select(1, 1, expenseBtn_inout, expenseImg, expenseLbl, "#F92222");
        updateCategoryCombo("OUT");
    }

    // [2] >=== FORM VALIDATION & SUBMIT HANDLER
    private void isInOutFormComplete() {
        BooleanBinding valueChoosenValid = valueChoosen.isNotEqualTo(0);
        BooleanBinding amountValid =
                Bindings.createBooleanBinding(
                        () -> {
                            BigDecimal value = spinner_inout.getValue();
                            return value != null && value.compareTo(BigDecimal.ZERO) > 0;
                        },
                        spinner_inout.valueProperty()
                );
        BooleanBinding akunValid = akunComboBox_inout.valueProperty().isNotNull();
        BooleanBinding kategoriValid = categoryComboBox.valueProperty().isNotNull();
        BooleanBinding dateValid = date_inout.valueProperty().isNotNull();
        BooleanBinding mataUangValid = mataUangCombo_inout.valueProperty().isNotNull();

        BooleanBinding formValid =
                valueChoosenValid
                        .and(amountValid)
                        .and(akunValid)
                        .and(kategoriValid)
                        .and(dateValid)
                        .and(mataUangValid);

        submit_inout.disableProperty().bind(formValid.not());
        addMultipleRecord_inout.disableProperty().bind(formValid.not());

        // listener
        formValid.addListener((obs, oldV, newV) ->
                System.out.println("FORM VALID = " + newV)
        );
    }
    private void isTransFormComplete() {
        BooleanBinding valueChoosenValid = valueChoosen.isNotEqualTo(0);
        BooleanBinding akunComboFrom = akunComboBox_from.valueProperty().isNotNull();
        BooleanBinding akunComboTo = akunComboBox_to.valueProperty().isNotNull();

        BooleanBinding amountFromValid =
                Bindings.createBooleanBinding(
                        () -> {
                            BigDecimal value = spinner_from.getValue();
                            return value != null && value.compareTo(BigDecimal.ZERO) > 0;
                        },
                        spinner_from.valueProperty()
                );

        BooleanBinding amountToValid =
                Bindings.createBooleanBinding(
                        () -> {
                            BigDecimal value = spinner_to.getValue();
                            return value != null && value.compareTo(BigDecimal.ZERO) > 0;
                        },
                        spinner_to.valueProperty()
                );

        BooleanBinding mataUangFrom = mataUangCombo_from.valueProperty().isNotNull();
        BooleanBinding mataUangTo = mataUangCombo_to.valueProperty().isNotNull();
        BooleanBinding dateValid = date_trans.valueProperty().isNotNull();

        BooleanBinding formValid =
                valueChoosenValid
                        .and(akunComboFrom)
                        .and(akunComboTo)
                        .and(amountFromValid)
                        .and(amountToValid)
                        .and(mataUangFrom)
                        .and(mataUangTo)
                        .and(dateValid);

        submit_trans.disableProperty().bind(formValid.not());
        addMultipleRecord_trans.disableProperty().bind(formValid.not());

        // listener
        formValid.addListener((obs, oldV, newV) ->
                System.out.println("FORM VALID = " + newV)
        );
    }
    @FXML
    private void inoutSubmitHandler(boolean closeAfterSubmit) {
        TransactionType tipe = getValueChoosen() == 1 ? TransactionType.INCOME : TransactionType.EXPANSE;
        BigDecimal jumlah = spinner_inout.getValue();
        Account account = akunComboBox_inout.getValue();
        Category category = categoryComboBox.getValue();
        LabelType labelType = tipeLabel_inout.getValue();
        LocalDate tanggal = date_inout.getValue();
        String keterangan = IOLogic.normalizeSpaces(note_inout.getText());
        PaymentType payment = paymentType_inout.getValue();
        PaymentStatus status = paymentStatus_inout.getValue();

        if(tipe == TransactionType.EXPANSE && jumlah.compareTo(account.getBalance()) > 0){
            MyPopup.showDanger("Saldo kurang!", "Saldo anda: " + account.getBalance());
            return;
        }

        BigDecimal newSaldo = BigDecimal.ZERO;
        Boolean result = false;
        if(tipe == TransactionType.INCOME) {
            newSaldo = account.getBalance().add(jumlah);
            result = DataManager.getInstance().updateSaldoAkun(account, newSaldo);
            if(result) {
                account.setBalance(newSaldo);
            }
        } else {
            newSaldo = account.getBalance().subtract(jumlah);
            result = DataManager.getInstance().updateSaldoAkun(account, newSaldo);
            if(result) {
                account.setBalance(newSaldo);
            }
        }

        if(!result) {
            return;
        }

        DataManager.getInstance().addTransaksi(new Transaction(
                0,
                tipe,
                jumlah,
                account,
                category,
                labelType,
                tanggal,
                keterangan,
                payment,
                status
        ));

        if(checkTemplate_inout.isSelected()){
            openTemplateSceneWithPrefill();
        }

        if(closeAfterSubmit) {
            closePopup();
            String page = DashboardControl.getInstance().getCurrentPage();
            DashboardControl.getInstance().loadPage(page);
        } else {
            clearInoutForm();
        }
    }
    private void openTemplateSceneWithPrefill() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/template.fxml"));
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

            TemplateControl ctrl = loader.getController();
            ctrl.setStage(stage);
            ctrl.setParentTransaction(this);

            Transaction draft = buildDraftTransaksi();
            ctrl.prefillFromTransaksi(draft);

            stage.showAndWait();

        } catch (IOException e) {
            log.error("gagal membuka panel template!", e);
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
        }
    }
    @FXML
    private void transSubmitHandler(boolean closeAfterSubmit) {
        Account fromAccount = akunComboBox_from.getValue();
        Account toAccount = akunComboBox_to.getValue();
        BigDecimal fromJumlah = spinner_from.getValue();
        BigDecimal toJumlah = spinner_to.getValue();
        LabelType labelType = tipeLabel_trans.getValue();
        LocalDate tanggal = date_trans.getValue();
        String keterangan = IOLogic.normalizeSpaces(note_trans.getText());
        PaymentType payment = paymentType_trans.getValue();
        PaymentStatus status = paymentStatus_trans.getValue();

        if(fromJumlah.compareTo(fromAccount.getBalance()) > 0){
            MyPopup.showDanger("Saldo account kurang!", "Saldo anda: " + fromAccount.getBalance());
            return;
        }

        Boolean fromValid = false, toValid = false;
        BigDecimal newFromJumlah = fromAccount.getBalance().subtract(fromJumlah);
        BigDecimal newToJumlah = toAccount.getBalance().add(toJumlah);

        fromValid = DataManager.getInstance().updateSaldoAkun(fromAccount, newFromJumlah);
        toValid = DataManager.getInstance().updateSaldoAkun(fromAccount, newToJumlah);

        if(fromValid && toValid) {
            fromAccount.setBalance(newFromJumlah);
            toAccount.setBalance(newToJumlah);
        } else {
            return;
        }

        Category fromCategory = null;
        for(Category ktgr : DataManager.getInstance().getDataKategori()) {
            if(ktgr.getId() == 32) {
                fromCategory = ktgr;
                break;
            }
        }

        if(fromCategory == null) {
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
            return;
        }

        DataManager.getInstance().addTransaksi(new Transaction(
                0,
                TransactionType.EXPANSE,
                fromJumlah,
                fromAccount,
                fromCategory,
                labelType,
                tanggal,
                keterangan,
                payment,
                status
        ));

        Category toCategory = null;
        for(Category ktgr : DataManager.getInstance().getDataKategori()) {
            if(ktgr.getId() == 31) {
                toCategory = ktgr;
                break;
            }
        }

        if(toCategory == null) {
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
            return;
        }

        DataManager.getInstance().addTransaksi(new Transaction(
                0,
                TransactionType.INCOME,
                toJumlah,
                toAccount,
                toCategory,
                labelType,
                tanggal,
                keterangan,
                payment,
                status
        ));

        if(closeAfterSubmit) {
            closePopup();
        } else {
            clearTransForm();
        }
    }

    @FXML
    private void transSubmitButton() {
        transSubmitHandler(true);
    }

    @FXML
    private void transSubmitAndAnotherRecord() {
        transSubmitHandler(false);
    }

    @FXML
    private void inouSubmitButton() {
        inoutSubmitHandler(true);
    }
    @FXML
    private void inoutSubmitAndAnotherRecord() {
        inoutSubmitHandler(false);
    }
    private void clearInoutForm() {
        templateComboBox_inout.setValue(null);
        spinner_inout.getEditor().clear();
        akunComboBox_inout.setValue(null);
        categoryComboBox.setValue(null);
        tipeLabel_inout.setValue(null);
        date_inout.setValue(LocalDate.now());
        checkTemplate_inout.setSelected(false);
        note_inout.setText(null);
        paymentType_inout.setValue(null);
        paymentStatus_inout.setValue(null);
    }
    private void clearTransForm() {
        akunComboBox_from.setValue(null);
        akunComboBox_to.setValue(null);
        spinner_from.getEditor().clear();
        spinner_to.getEditor().clear();
        tipeLabel_trans.setValue(null);
        date_trans.setValue(LocalDate.now());
        note_trans.clear();
        paymentType_trans.setValue(null);
        paymentStatus_trans.setValue(null);
        templateComboBox_trans.setValue(null);
    }

    // [3] >=== BUTTON HANDLER
    @FXML
    private void addLabelOnTransaction(ActionEvent evt) {
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
            ctrl.setParentTransaction(this);

            stage.showAndWait();

        } catch (IOException e) {
            log.error("gagal membuka panel tambah label!", e);
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
        }
    }
    @FXML
    private void addTemplateOnTransaction(ActionEvent evt) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/template.fxml"));
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

            TemplateControl ctrl = loader.getController();
            ctrl.setStage(stage);
            ctrl.setParentTransaction(this);
            stage.showAndWait();

        } catch (IOException e) {
            log.error("gagal membuka panel template!", e);
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
        }
    }

    // [4] >=== CONNECTOR FUNCTION
    public ObservableList<LabelType> getTipeLabelList() {
        return labelTypeList;
    }
    public ComboBox<LabelType> getTipeLabelInOut() {
        return tipeLabel_inout;
    }
    public ComboBox<LabelType> getTipeLabelTrans() {
        return tipeLabel_trans;
    }
    public ObservableList<Template> getTemplateList() {
        return dataTemplateList;
    }
    public ComboBox<Template> getTempleteComboBox() {
        return templateComboBox_inout;
    }

    // [5] >=== HELPER FUNCTION
    private void spinnerLogicHandler() {
        IOLogic.makeIntegerOnlyBlankInitial(spinner_inout, BigDecimal.ZERO, new BigDecimal("1_000_000_000_000"));
        IOLogic.makeIntegerOnlyBlankInitial(spinner_from, BigDecimal.ZERO, new BigDecimal("1_000_000_000_000"));
        IOLogic.makeIntegerOnlyBlankInitial(spinner_to, BigDecimal.ZERO, new BigDecimal("1_000_000_000_000"));
    }
    private void showInOut() {
        inoutForm.setVisible(true);
        transForm.setVisible(false);

        inoutForm.toFront();
    }
    private void showTransfer() {
        transForm.setVisible(true);
        inoutForm.setVisible(false);

        transForm.toFront();
    }
    private void select(int layer, int index, Button btn, ImageView img, Label lbl, String color) {

        if(layer == 1) {
            if(index != 2) {
                clearSelection(layer);
            } else {
                clearSelection(2);
                showTransfer();
            }

            valueChoosen.setValue(index + 1);

            // kasih warna ke tombol
            btn.setStyle("-selected-color: " + color + ";");
            btn.getStyleClass().add("choice-btn-selected");

            // label & icon jadi putih
            lbl.setStyle("-fx-text-fill: white;");
            img.setImage(theImage[index][0]); // icon putih

        } else if(layer == 2) {
            if(index == 2) return;
            showInOut();
            clearSelection(1);
            valueChoosen.setValue(index + 1);

            // kasih warna ke tombol
            btn.setStyle("-selected-color: " + color + ";");
            btn.getStyleClass().add("choice-btn-selected");

            // label & icon jadi putih
            lbl.setStyle("-fx-text-fill: white;");
            img.setImage(theImage[index][0]); // icon putih
        }
        System.out.println("user memilih: " + (valueChoosen.getValue() == 1 ? "income" : valueChoosen.getValue() == 2 ? "expense" : "transfer"));
    }
    private void updateCategoryCombo(String type) {
        categoryComboBox.getSelectionModel().clearSelection();

        List<Category> filtered = DataManager.getInstance().getDataKategori().stream()
                .filter(k -> k.getType().equals(type))
                .toList();

        categoryComboBox.setItems(
                FXCollections.observableArrayList(filtered)
        );
    }
    private void clearSelection(int layer) {

        if(layer == 1){
            // reset button
            for (Button b : List.of(incomeBtn_inout, expenseBtn_inout, transferBtn_inout)) {
                b.getStyleClass().remove("choice-btn-selected");
                b.setStyle("");  // hilangkan selected-color
            }

            // reset label
            for (Label l : List.of(incomeLbl, expenseLbl, transferLbl_inout)) {
                l.setStyle("-fx-text-fill: black;");
            }

            // reset icon ke hitam
            incomeImg.setImage(theImage[0][1]);
            expenseImg.setImage(theImage[1][1]);
            transferImg_inout.setImage(theImage[2][1]);

        } else if(layer == 2) {
            // reset button
            for (Button b : List.of(incomeBtn_trans, expenseBtn_trans, transferBtn_trans)) {
                b.getStyleClass().remove("choice-btn-selected");
                b.setStyle("");  // hilangkan selected-color
            }

            transferLbl_trans.setStyle("-fx-text-fill: black;");
            transferImg_trans.setImage(DataManager.getInstance().getImageTransactionForm()[2][1]);
        }
    }
    private Transaction buildDraftTransaksi() {
        TransactionType tipe = getValueChoosen() == 1 ? TransactionType.INCOME : TransactionType.EXPANSE;
        BigDecimal jumlah = spinner_inout.getValue();
        Account account = akunComboBox_inout.getValue();
        Category category = categoryComboBox.getValue();
        LabelType labelType = tipeLabel_inout.getValue();
        LocalDate tanggal = date_inout.getValue();
        String keterangan = IOLogic.normalizeSpaces(note_inout.getText());
        PaymentType payment = paymentType_inout.getValue();
        PaymentStatus status = paymentStatus_inout.getValue();

        Transaction draft = new Transaction(
                0,
                tipe,
                jumlah,
                account,
                category,
                labelType,
                tanggal,
                keterangan,
                payment,
                status
        );

        return draft;
    }
    private void clearInoutFormForTemplater() {
        spinner_inout.getEditor().clear();
        akunComboBox_inout.setValue(null);
        categoryComboBox.setValue(null);
        tipeLabel_inout.setValue(null);
        date_inout.setValue(LocalDate.now());
        checkTemplate_inout.setSelected(false);
        note_inout.setText(null);
        paymentType_inout.setValue(null);
        paymentStatus_inout.setValue(null);
    }

    // [6] >=== LISTENER
    private void akunToMataUangListener() {
        akunComboBox_inout.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mataUangCombo_inout.setValue(newVal.getCurrencyType());
            }
        });
        akunComboBox_from.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mataUangCombo_from.setValue(newVal.getCurrencyType());
            }
        });
        akunComboBox_to.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mataUangCombo_to.setValue(newVal.getCurrencyType());
            }
        });
    }
    private void templateInOutListener() {
        templateComboBox_inout.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                clearInoutFormForTemplater();
                templateInOutDrafter(newVal);
            }
        });
    }
    private void templateTransListener() {
        templateComboBox_trans.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                showInOut();
                templateComboBox_inout.setValue(newVal);
            }
        });
    }
    private void templateInOutDrafter(Template temp) {
        if(temp.getTransactionType() == TransactionType.INCOME) {
            activateIncome();
        } else if (temp.getTransactionType() == TransactionType.EXPANSE) {
            activateExpense();
        }

        spinner_inout.getValueFactory().setValue(temp.getAmount());
        akunComboBox_inout.setValue(temp.getAccount());

        categoryComboBox.setValue(temp.getCategory());

        tipeLabel_inout.setValue(temp.getLabelType());
        note_inout.setText(temp.getDescription());
        paymentType_inout.setValue(temp.getPaymentType());
        paymentStatus_inout.setValue(temp.getPaymentStatus());
    }
}