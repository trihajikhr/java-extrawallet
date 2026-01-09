package controller.transaction;

import controller.DashboardControl;
import controller.ExpensControl;
import controller.IncomeControl;
import controller.option.TransactionParent;
import dataflow.DataLoader;
import dataflow.DataManager;
import helper.Converter;
import helper.IOLogic;
import helper.MyPopup;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
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
import javafx.util.Duration;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class EditControl implements Initializable {
    // atribut vital
    private static final Logger log = LoggerFactory.getLogger(EditControl.class);
    private Stage stage;
    @FXML private AnchorPane rootPane;
    private Transaksi transOriginal;
    private Boolean isMultiple = false;
    private TransactionParent parent;

    // atribut pendukung
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean closing = false;
    private ObservableList<TipeLabel> tipeLabelList = FXCollections.observableArrayList();

    // atribut fxml
    @FXML private Spinner<Integer> amountEdit;
    @FXML private ComboBox<MataUang> mataUangCombo;
    @FXML private ComboBox<Akun> akunComboBox;
    @FXML private ComboBox<Kategori> categoryComboBox;
    @FXML private ComboBox<TipeLabel> tipeLabelCombo;
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
    @FXML
    public void showPopup() {
        rootPane.setOpacity(0);
        rootPane.setScaleX(0.8);
        rootPane.setScaleY(0.8);
        rootPane.setOpacity(0);

        FadeTransition fade = new FadeTransition(Duration.millis(200), rootPane);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(200), rootPane);
        scale.setFromX(0.8);
        scale.setFromY(0.8);
        scale.setToX(1);
        scale.setToY(1);

        ParallelTransition showAnim = new ParallelTransition(fade, scale);
        showAnim.setInterpolator(Interpolator.EASE_BOTH);
        showAnim.play();
    }
    @FXML
    private void closePopup() {
        if (closing) return;
        closing = true;
        if (stage == null) return;

        FadeTransition fade = new FadeTransition(Duration.millis(150), rootPane);
        fade.setFromValue(1);
        fade.setToValue(0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(150), rootPane);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(0.8);
        scale.setToY(0.8);

        ParallelTransition hideAnim = new ParallelTransition(fade, scale);
        hideAnim.setInterpolator(Interpolator.EASE_BOTH);

        hideAnim.setOnFinished(e -> stage.close());
        hideAnim.play();
    }
    public void setIsMultiple(Boolean val){
        this.isMultiple = val;
        isFormComplete();
        log.info("isMultiple: " + (isMultiple ? "true" : "false"));
    }
    public void setParent(TransactionParent parent) {
        this.parent = parent;
    }
    public Map<Transaksi, RecordCard> getParentRecordCards() {
        return parent.getRecordCardBoard();
    }

    // [1] >=== SCENE CONNECTION
    public ComboBox<TipeLabel> getTipeLabelCombo() {
        return tipeLabelCombo;
    }
    public ObservableList<TipeLabel> getTipeLabelList() {
        return tipeLabelList;
    }

    // [2] >=== DATA LOADER
    private void loadAllData() {
        DataLoader.getInstance().mataUangComboBoxLoader(mataUangCombo);
        DataLoader.getInstance().akunComboBoxLoader(akunComboBox);
        DataLoader.getInstance().kategoriComboBoxLoader(categoryComboBox);
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
        ArrayList<TipeLabel> dataTipelabel = DataManager.getInstance().getDataTipeLabel();
        tipeLabelList = FXCollections.observableArrayList(dataTipelabel);

        tipeLabelCombo.setItems(tipeLabelList);
        tipeLabelCombo.setCellFactory(list -> new ListCell<TipeLabel>() {
            @Override
            protected void updateItem(TipeLabel item, boolean empty) {
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
                                item.getWarna(),
                                new CornerRadii(8),
                                Insets.EMPTY
                        )
                ));

                // teks
                Label label = new Label(item.getNama());
                label.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

                // gabung
                HBox box = new HBox(10, iconBox, label);
                box.setAlignment(Pos.CENTER_LEFT);
                setGraphic(box);
            }
        });
        tipeLabelCombo.setButtonCell(tipeLabelCombo.getCellFactory().call(null));
    }
    public void prefilFromRecord(Transaksi trans) {
        amountEdit.getEditor().setText(Integer.toString(trans.getJumlah()));
        akunComboBox.setValue(trans.getAkun());
        categoryComboBox.setValue(trans.getKategori());
        tipeLabelCombo.setValue(trans.getTipelabel());
        dateEdit.setValue(trans.getTanggal());
        noteEdit.setText(
                Objects.requireNonNullElse(trans.getKeterangan(), "")
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

        if (isMultiple){
            submitButton.disableProperty().bind(formValid.not());
        } else {
            BooleanBinding isChanged = isChangedBinding();
            submitButton.disableProperty().bind(
                    formValid.and(isChanged).not()
            );
        }
    }
    private BooleanBinding isChangedBinding() {
        return Bindings.createBooleanBinding(() -> {
                    if (transOriginal == null) return false;

                    Transaksi current = new Transaksi(
                            transOriginal.getId(),
                            transOriginal.getTipeTransaksi(),
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
                mataUangCombo.setValue(newVal.getMataUang());
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
        if(isMultiple){
            List<Transaksi> selected = parent.getRecordCardBoard().entrySet().stream()
                    .filter(e -> e.getValue().getCheckList().isSelected())
                    .map(Map.Entry::getKey)
                    .toList();

            List<Transaksi> editedTransaksi = new ArrayList<>();

            for (Transaksi trans : selected) {
                // update tiap transaksi sesuai form
                trans.setJumlah(amountEdit.getValue());
                trans.setAkun(akunComboBox.getValue());
                trans.setKategori(categoryComboBox.getValue());
                trans.setTipelabel(tipeLabelCombo.getValue());
                trans.setTanggal(dateEdit.getValue());
                trans.setKeterangan(noteEdit.getText());
                trans.setPaymentType(paymentType.getValue());
                trans.setPaymentStatus(paymentStatus.getValue());

                editedTransaksi.add(trans);
            }

            DataManager.getInstance().modifyMultipleTransaksi(editedTransaksi);
            String page = DashboardControl.getInstance().getCurrentPage();
            DashboardControl.getInstance().loadPage(page);

        } else {
            String note = noteEdit.getText();
            note = (note == null || note.isBlank()) ? null : note;

            Transaksi transModified = new Transaksi(
                    transOriginal.getId(),
                    transOriginal.getTipeTransaksi(),
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
                DataManager.getInstance().modifyTransaksi(transModified);
                String page = DashboardControl.getInstance().getCurrentPage();
                DashboardControl.getInstance().loadPage(page);
            }
        }
        closePopup();
    }
}
