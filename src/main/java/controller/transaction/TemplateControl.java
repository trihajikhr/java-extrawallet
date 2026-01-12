package controller.transaction;

import controller.PopupUtils;
import dataflow.DataLoader;
import dataflow.DataManager;
import helper.Converter;
import helper.IOLogic;
import helper.MyPopup;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import model.enums.PaymentStatus;
import model.enums.PaymentType;
import model.enums.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class TemplateControl implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(TemplateControl.class);
    private Stage stage;
    private boolean closing = false;
    IntegerProperty valueChoosen = new SimpleIntegerProperty(0);

    private double xOffset = 0;
    private double yOffset = 0;

    private TransactionControl parentTransaction;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField nameText;

    // button blok switcher
    @FXML private Button incomeBtn, expenseBtn;
    @FXML private Label incomeLabel, expenseLabel;
    @FXML private Image[][] theImage;
    @FXML private ImageView incomeImg;
    @FXML private ImageView expenseImg;

    @FXML
    private Spinner<BigDecimal> spinnerAmount;

    @FXML
    private ComboBox<Currency> mataUangComboBox;

    @FXML
    private ComboBox<Account> akunComboBox;

    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private ComboBox<LabelType> tipeLabelComboBox;
    private ObservableList<LabelType> labelTypeObservable = FXCollections.observableArrayList();

    @FXML
    private TextField noteText;

    // payment type & status
    @FXML private ComboBox<PaymentType> paymentTypeComboBox;
    @FXML private ComboBox<PaymentStatus>  paymentStatusComboBox;

    @FXML
    private Button submitButton;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        log.info("template popup terbuka");
        showPopup();

        // init function
        initButtonImage();
        initPayment();
        initDataComboBox();
        activateIncome();
        initButtons();

        // isform complete
        isFormComplete();

        // helper function
        textFieldHandler();

        // listenter
        mataUangListener();

        // tipelabel loader
        loadTipeLabelComboBox();

        // uncategorized
        IOLogic.makeIntegerOnlyBlankInitial(spinnerAmount, BigDecimal.ZERO, new BigDecimal("1_000_000_000_000"));
        mataUangComboBox.setMouseTransparent(true);
        mataUangComboBox.setFocusTraversable(false);
        mataUangComboBox.getStyleClass().add("locked");
    }

    // [0] >=== SCENE CONTROLLER
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

    // [1] >=== SCENE CONNECTION FUNCTION
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setParentTransaction(TransactionControl parent) {
        this.parentTransaction = parent;
    }
    public ComboBox<LabelType> getTipeLabel() {
        return tipeLabelComboBox;
    }
    public ObservableList<LabelType> getTipeLabelObservable() {
        return labelTypeObservable;
    }

    // [2] >=== INIT FUNCTION
    private void initButtons() {
        incomeBtn.getStyleClass().add("choice-btn");
        expenseBtn.getStyleClass().add("choice-btn");

        incomeBtn.setOnAction(e -> activateIncome());
        expenseBtn.setOnAction(e -> {
            select(1, expenseBtn, expenseImg, expenseLabel, "#F92222");
            updateCategoryCombo("OUT");
        });
    }
    private void initPayment() {
        paymentTypeComboBox.setItems(DataManager.getInstance().getDataPaymentType());
        paymentStatusComboBox.setItems(DataManager.getInstance().getDataPaymentStatus());

        Converter.bindEnumComboBox(paymentTypeComboBox, PaymentType::getLabel);
        Converter.bindEnumComboBox(paymentStatusComboBox, PaymentStatus::getLabel);
    }
    private void activateIncome() {
        select(0, incomeBtn, incomeImg, incomeLabel, "#01AA71");
        updateCategoryCombo("IN");
    }
    private void activateExpense() {
        select(0, expenseBtn, expenseImg, expenseLabel, "#F92222");
        updateCategoryCombo("OUT");
    }
    private void initDataComboBox() {
        DataLoader.kategoriComboBoxLoader(categoryComboBox);
        DataLoader.akunComboBoxLoader(akunComboBox);
        DataLoader.mataUangComboBoxLoader(mataUangComboBox);

    }
    private void initButtonImage() {
        theImage = DataManager.getInstance().getImageTransactionForm();
    }
    private void loadTipeLabelComboBox(){
        ArrayList<LabelType> dataTipelabel = DataManager.getInstance().getDataTipeLabel();
        labelTypeObservable = FXCollections.observableArrayList(dataTipelabel);

        tipeLabelComboBox.setItems(labelTypeObservable);
        tipeLabelComboBox.setCellFactory(list -> new ListCell<LabelType>() {
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
        tipeLabelComboBox.setButtonCell(tipeLabelComboBox.getCellFactory().call(null));
    }

    // [3] >=== HELPER FUNCTION
    private void textFieldHandler() {
        IOLogic.isTextFieldValid(nameText, 20);
        IOLogic.isTextFieldValid(noteText, 50);
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
    private void select(int index, Button btn, ImageView img, Label lbl, String color) {
        clearSelection();
        valueChoosen.setValue(index + 1);
        // kasih warna ke tombol
        btn.setStyle("-selected-color: " + color + ";");
        btn.getStyleClass().add("choice-btn-selected");

        // label & icon jadi putih
        lbl.setStyle("-fx-text-fill: white;");
        img.setImage(theImage[index][0]);
        System.out.println("user memilih: " + (valueChoosen.getValue() == 1 ? "income" : valueChoosen.getValue() == 2 ? "expense" : "transfer"));
    }
    private void clearSelection() {
        // button
        incomeBtn.getStyleClass().remove("choice-btn-selected");
        incomeBtn.setStyle("");

        expenseBtn.getStyleClass().remove("choice-btn-selected");
        expenseBtn.setStyle("");

        // label
        incomeLabel.setStyle("-fx-text-fill: black;");
        expenseLabel.setStyle("-fx-text-fill: black;");

        // reset icon ke hitam
        incomeImg.setImage(theImage[0][1]);
        expenseImg.setImage(theImage[1][1]);
    }

    // [4] >=== FORM VALIDATION
    private void isFormComplete() {
        BooleanBinding valueChoosenValid = valueChoosen.isNotEqualTo(0);
        BooleanBinding nameValid =
                Bindings.createBooleanBinding(
                        () -> !nameText.getText().trim().isEmpty(),
                        nameText.textProperty()
                );

        BooleanBinding amountValid =
                Bindings.createBooleanBinding(
                        () -> {
                            BigDecimal value = spinnerAmount.getValue();
                            return value != null && value.compareTo(BigDecimal.ZERO) > 0;
                        },
                        spinnerAmount.valueProperty()
                );

        BooleanBinding mataUangValid = mataUangComboBox.valueProperty().isNotNull();
        BooleanBinding akunValid = akunComboBox.valueProperty().isNotNull();
        BooleanBinding kategoriValid = categoryComboBox.valueProperty().isNotNull();

        BooleanBinding formValid =
                valueChoosenValid
                        .and(nameValid)
                        .and(amountValid)
                        .and(mataUangValid)
                        .and(akunValid)
                        .and(kategoriValid);
        submitButton.disableProperty().bind(formValid.not());
    }

    // [5] >=== BUTTON ADD LABEL & SUBMIT HANDLER & PREFILL FUNCTION
    @FXML
    private void addLabelOnTemplate(ActionEvent evt) {
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
            ctrl.setParentTemplate(this, parentTransaction);

            stage.showAndWait();

        } catch (IOException e) {
            log.error("gagal membuka panel tambah label!", e);
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
        }
    }
    @FXML
    private void submitHandler() {
        TransactionType tipe = valueChoosen.getValue() == 1 ? TransactionType.INCOME : TransactionType.EXPANSE;
        String nama = IOLogic.normalizeSpaces(nameText.getText());

        if(!uniqueNameValidation(nama)){
            MyPopup.showDanger("Duplikasi Nama!", "Nama sudah digunakan!");
            nameText.clear();
            return;
        }

        BigDecimal jumlah = spinnerAmount.getValue();
        Account dataAccount = akunComboBox.getValue();
        Category dataCategory = categoryComboBox.getValue();
        LabelType dataLabel = tipeLabelComboBox.getValue();
        String keterangan = IOLogic.normalizeSpaces(noteText.getText());
        PaymentType payment = paymentTypeComboBox.getValue();
        PaymentStatus status = paymentStatusComboBox.getValue();

        Template newData = new Template(
                0,
                tipe,
                nama,
                jumlah,
                dataAccount,
                dataCategory,
                dataLabel,
                keterangan,
                payment,
                status
        );

        boolean result = DataManager.getInstance().addTemplate(newData);
        if(result && parentTransaction != null) {
            parentTransaction.getTemplateList().add(newData);
            parentTransaction.getTempleteComboBox().getSelectionModel().select(newData);
        }

        closePopup();
    }
    private boolean uniqueNameValidation(String nama) {
        for(Template temp : DataManager.getInstance().getDataTemplate()) {
            if(nama.equalsIgnoreCase(temp.getName())){
                return false;
            }
        }
        return true;
    }
    public void prefillFromTransaksi(Transaction trans) {
        TransactionType tipe = trans.getTransactionType();
        if(tipe == TransactionType.INCOME) {
            activateIncome();
        } else if(tipe == TransactionType.EXPANSE){
            activateExpense();
        }

        spinnerAmount.getEditor().setText(trans.getAmount().toPlainString());
        akunComboBox.setValue(trans.getAccount());
        categoryComboBox.setValue(trans.getCategory());

        tipeLabelComboBox.setValue(trans.getLabelType());
        noteText.setText(
                Objects.requireNonNullElse(trans.getDescription(), "")
        );
        paymentTypeComboBox.setValue(trans.getPaymentType());
        paymentStatusComboBox.setValue(trans.getPaymentStatus());
    }

    // [6] >=== LISTENER
    private void mataUangListener() {
        akunComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mataUangComboBox.setValue(newVal.getCurrencyType());
            }
        });
    }
}