package controller.transaction;

import dataflow.DataLoader;
import dataflow.DataManager;
import helper.IOLogic;
import helper.Popup;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
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
import javafx.util.Duration;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
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

    @FXML private ComboBox<Kategori> categoryComboBox;
    @FXML private ComboBox<String> paymentType_inout, paymentType_trans;
    @FXML private ComboBox<String> paymentStatus_inout, paymentStatus_trans;
    @FXML private ComboBox<Akun> akunComboBox_inout, akunComboBox_from, akunComboBox_to;

    // combobox tipeLabel
    private ObservableList<TipeLabel> tipeLabelList = FXCollections.observableArrayList();
    @FXML private ComboBox<TipeLabel> tipeLabel_inout;
    @FXML private ComboBox<TipeLabel> tipeLabel_trans;

    // template
    private ObservableList<Template> dataTemplateList = FXCollections.observableArrayList();
    @FXML private ComboBox<Template> dataTemplateComboBox;

    private final ObjectProperty<String> selectedPaymentType =
            new SimpleObjectProperty<>();

    private final ObjectProperty<String> selectedPaymentStatus =
            new SimpleObjectProperty<>();

    private final StringProperty noteState = new SimpleStringProperty();
    @FXML private TextField note_inout, note_trans;

    @FXML private Image[][] theImage;

    // combobox mata uang
    @FXML private ComboBox<MataUang> mataUangCombo_inout, mataUangCombo_from, mataUangCombo_to;

    // spinner
    @FXML Spinner<Integer> spinner_inout, spinner_from, spinner_to;

    // tanggal
    @FXML DatePicker date_inout, date_trans;

    // submit button + add temlate
    @FXML Button submit_inout;
    @FXML Button submit_trans;
    @FXML Button addTemplate_inout;
    @FXML Button addTemplate_trans;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("Transaksi pop up terbuka");

        // isformcomplete function
        isInOutFormComplete();
        isTransFormComplete();

        // beberapa fungsi init
        initPaymentData();
        initMessageBinding();
        akunToMataUangListener();
        initDate();

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

        // load data combobox
        initDataComboBox();
        initTipeLabelList();
        spinnerLogicHandler();
        initTemplate();

        // load image
        theImage = DataManager.getInstance().getImageTransactionForm();

        inoutForm.setVisible(true);   // default
        transForm.setVisible(false);

        initButtons();
        clearSelection(1); // default: semua putih, teks hitam, icon hitam
        activateIncome();
        showPopup();
    }

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

    private void updateCategoryCombo(String type) {
        categoryComboBox.getSelectionModel().clearSelection();

        List<Kategori> filtered = DataManager.getInstance().getDataKategori().stream()
                .filter(k -> k.getTipe().equals(type))
                .toList();

        categoryComboBox.setItems(
                FXCollections.observableArrayList(filtered)
        );
    }

    // [1] >=== FUNGSI INIT
    private void initPaymentData() {
        paymentType_inout.setItems(DataManager.getInstance().getDataPeymentType());
        paymentType_trans.setItems(DataManager.getInstance().getDataPeymentType());

        paymentStatus_inout.setItems(DataManager.getInstance().getDataStatusType());
        paymentStatus_trans.setItems(DataManager.getInstance().getDataStatusType());

        paymentType_inout.valueProperty().bindBidirectional(selectedPaymentType);
        paymentType_trans.valueProperty().bindBidirectional(selectedPaymentType);

        paymentStatus_inout.valueProperty().bindBidirectional(selectedPaymentStatus);
        paymentStatus_trans.valueProperty().bindBidirectional(selectedPaymentStatus);
    }
    private void initTemplate() {
        dataTemplateList = FXCollections.observableArrayList(
                DataManager.getInstance().getDataTemplate()
        );
        dataTemplateComboBox.setItems(dataTemplateList);

        dataTemplateComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Template temp, boolean empty) {
                super.updateItem(temp, empty);
                setText(empty || temp == null ? null : temp.getNama());
            }
        });

        dataTemplateComboBox.setButtonCell(
                dataTemplateComboBox.getCellFactory().call(null)
        );
    }
    private void initTipeLabelList() {
        ArrayList<TipeLabel> data = DataManager.getInstance().getDataTipeLabel();
        tipeLabelList = FXCollections.observableArrayList(data);
        tipeLabel_inout.setItems(tipeLabelList);
        tipeLabel_trans.setItems(tipeLabelList);
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
        expenseBtn_inout.setOnAction(e -> {
            select(1,1, expenseBtn_inout, expenseImg, expenseLbl, "#F92222");
            updateCategoryCombo("OUT");
        });
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
        DataLoader.getInstance().akunComboBoxLoader(akunComboBox_inout);
        DataLoader.getInstance().akunComboBoxLoader(akunComboBox_from);
        DataLoader.getInstance().akunComboBoxLoader(akunComboBox_to);

        DataLoader.getInstance().kategoriComboBoxLoader(categoryComboBox);
        DataLoader.getInstance().tipeLabelComboBoxLoader(tipeLabel_inout);
        DataLoader.getInstance().tipeLabelComboBoxLoader(tipeLabel_trans);

        DataLoader.getInstance().mataUangComboBoxLoader(mataUangCombo_inout);
        DataLoader.getInstance().mataUangComboBoxLoader(mataUangCombo_from);
        DataLoader.getInstance().mataUangComboBoxLoader(mataUangCombo_to);
    }
    private void activateIncome() {
        select(1, 0, incomeBtn_inout, incomeImg, incomeLbl, "#01AA71");
        updateCategoryCombo("IN");
    }

    // [2] >=== FORM VALIDATION & SUBMIT HANDLER
    private void isInOutFormComplete() {
        BooleanBinding valueChoosenValid = valueChoosen.isNotEqualTo(0);
        BooleanBinding amountValid =
                Bindings.createBooleanBinding(
                        () -> spinner_inout.getValue() != null && spinner_inout.getValue() > 0,
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
        addTemplate_inout.disableProperty().bind(formValid.not());

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
                        () -> spinner_from.getValue() != null && spinner_from.getValue() > 0,
                        spinner_from.valueProperty()
                );

        BooleanBinding amountToValid =
                Bindings.createBooleanBinding(
                        () -> spinner_to.getValue() != null && spinner_to.getValue() > 0,
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
        addTemplate_trans.disableProperty().bind(formValid.not());

        // listener
        formValid.addListener((obs, oldV, newV) ->
                System.out.println("FORM VALID = " + newV)
        );
    }
    @FXML
    private void inoutSubmitHandler() {
        String tipe = getValueChoosen() == 1 ? "IN" : "OUT";
        int jumlah = spinner_inout.getValue();
        Akun akun = akunComboBox_inout.getValue();
        Kategori kategori = categoryComboBox.getValue();
        TipeLabel tipeLabel = tipeLabel_inout.getValue();
        LocalDate tanggal = date_inout.getValue();
        String keterangan = note_inout.getText();
        String payment = paymentType_inout.getValue();
        String status = paymentStatus_inout.getValue();

        DataManager.getInstance().addTransaksi(new Transaksi(
                0,
                tipe,
                jumlah,
                akun,
                kategori,
                tipeLabel,
                tanggal,
                keterangan,
                payment,
                status
        ));

        closePopup();
    }
    @FXML
    private void transSubmitHandler() {
        Akun fromAkun = akunComboBox_from.getValue();
        Akun toAkun = akunComboBox_to.getValue();
        int fromJumlah = spinner_from.getValue();
        int toJumlah = spinner_to.getValue();
        TipeLabel tipeLabel = tipeLabel_trans.getValue();
        LocalDate tanggal = date_trans.getValue();
        String keterangan = note_trans.getText();
        String payment = paymentType_trans.getValue();
        String status = paymentStatus_trans.getValue();

        Kategori fromKategori = null;
        for(Kategori ktgr : DataManager.getInstance().getDataKategori()) {
            if(ktgr.getId() == 32) {
                fromKategori = ktgr;
                break;
            }
        }

        if(fromKategori == null) {
            Popup.showDanger("Gagal!", "Terjadi kesalahan!");
            return;
        }

        DataManager.getInstance().addTransaksi(new Transaksi(
                0,
                "OUT",
                fromJumlah,
                fromAkun,
                fromKategori,
                tipeLabel,
                tanggal,
                keterangan,
                payment,
                status
        ));

        Kategori toKategori = null;
        for(Kategori ktgr : DataManager.getInstance().getDataKategori()) {
            if(ktgr.getId() == 31) {
                toKategori = ktgr;
                break;
            }
        }

        if(toKategori == null) {
            Popup.showDanger("Gagal!", "Terjadi kesalahan!");
            return;
        }

        DataManager.getInstance().addTransaksi(new Transaksi(
                0,
                "IN",
                toJumlah,
                toAkun,
                toKategori,
                tipeLabel,
                tanggal,
                keterangan,
                payment,
                status
        ));

        closePopup();
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
            Popup.showDanger("Gagal!", "Terjadi kesalahan!");
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
            Popup.showDanger("Gagal!", "Terjadi kesalahan!");
        }
    }

    // [4] >=== CONNECTOR FUNCTION
    public ObservableList<TipeLabel> getTipeLabelList() {
        return tipeLabelList;
    }
    public ComboBox<TipeLabel> getTipeLabelInOut() {
        return tipeLabel_inout;
    }
    public ComboBox<TipeLabel> getTipeLabelTrans() {
        return tipeLabel_trans;
    }
    public ObservableList<Template> getTemplateList() {
        return dataTemplateList;
    }
    public ComboBox<Template> getTempleteComboBox() {
        return dataTemplateComboBox;
    }

    // [5] >=== HELPER FUNCTION
    private void spinnerLogicHandler() {
        IOLogic.makeIntegerOnly(spinner_inout, 1, 2_147_483_647, 1);
        IOLogic.makeIntegerOnly(spinner_from, 1, 2_147_483_647, 1);
        IOLogic.makeIntegerOnly(spinner_to, 1, 2_147_483_647, 1);
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

    // [6] >===



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


    private void akunToMataUangListener() {
        akunComboBox_inout.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mataUangCombo_inout.setValue(newVal.getMataUang());
            }
        });
        akunComboBox_from.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mataUangCombo_from.setValue(newVal.getMataUang());
            }
        });
        akunComboBox_to.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mataUangCombo_to.setValue(newVal.getMataUang());
            }
        });
    }

    // [10] >=== submit button handler

    public int getValueChoosen() {
        return valueChoosen.getValue();
    }
}