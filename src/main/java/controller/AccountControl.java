package controller;

import dataflow.DataManager;
import dataflow.basedata.AccountItem;
import dataflow.basedata.ColorItem;
import helper.MyPopup;
import model.MataUang;
import helper.IOLogic;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

import model.Akun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountControl implements Initializable {
    // logger
    private static final Logger log = LoggerFactory.getLogger(AccountControl.class);

    private Stage stage;
    private boolean closing = false;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ComboBox<AccountItem> accountComboBox;

    @FXML
    private ComboBox<ColorItem> colorComboBox;

    @FXML
    private ComboBox<MataUang> currencyComboBox;

    @FXML
    private TextField accountName;

    @FXML
    private Spinner<Integer> amountSpinner;

    @FXML
    private Button submitButton;

    // [0] >=== INIT FUNCTION
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("Popup account terbuka!");
        showPopup();

        // combobox init
        initComboBoxColor();
        initComboBoxAccountItem();
        currencyComboBox.setItems(DataManager.getInstance().getDataMataUang());

        // validation init
        IOLogic.isTextFieldValid(accountName, 20);
        isFormComplete();
        IOLogic.makeIntegerOnlyBlankInitial(amountSpinner, 0, 2_147_483_647);
    }

    // [1] >=== CONNECTOR FUNCTION
    // DIPANGGIL dari controller lain
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // [2] >=== SCENE CONTROLLER FUUNCTION
    @FXML
    public void showPopup() {
        if (stage == null) return;

        rootPane.setOpacity(0);
        rootPane.setScaleX(0.6);
        rootPane.setScaleY(0.6);

        FadeTransition fade = new FadeTransition(Duration.millis(250), rootPane);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(250), rootPane);
        scale.setFromX(0.6);
        scale.setFromY(0.6);
        scale.setToX(1);
        scale.setToY(1);

        ParallelTransition pt = new ParallelTransition(fade, scale);
        pt.setInterpolator(Interpolator.EASE_OUT);
        pt.play();
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

    // [3] >=== INIT DATA
    private void initComboBoxColor() {
        colorComboBox.setItems(DataManager.getInstance().getDataColor());
        colorComboBox.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(ColorItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Circle circle = new Circle(8, item.getWarna());
                    Label label = new Label(item.getLabel());
                    label.setStyle("-fx-text-fill: black;");
                    HBox box = new HBox(8, circle, label);
                    box.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(box);
                }
            }
        });

        // biar yang dipilih juga tampil sama
        colorComboBox.setButtonCell(colorComboBox.getCellFactory().call(null));

        for (AccountItem item : accountComboBox.getItems()) {
            item.setWarna(Color.GREY);
        }

        colorComboBox.valueProperty().addListener((obs, oldColor, newColor) -> {
            if (newColor == null) return;

            for (AccountItem item : accountComboBox.getItems()) {
                item.setWarna(newColor.getWarna());
            }
        });
    }
    private void initComboBoxAccountItem() {
        accountComboBox.setItems(DataManager.getInstance().getDataAccountItem());
        accountComboBox.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(AccountItem item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                // ICON
                ImageView iconView = new ImageView(item.getIcon());
                iconView.setFitWidth(14);
                iconView.setFitHeight(14);
                iconView.setPreserveRatio(true);

                // KOTAK BACKGROUND ICON
                StackPane iconBox = new StackPane(iconView);
                iconBox.setPrefSize(28, 28);
                iconBox.setMaxSize(28, 28);

                // BIND background ke property warna
                iconBox.backgroundProperty().bind(
                        Bindings.createObjectBinding(
                                () -> new Background(new BackgroundFill(item.getWarna(), new CornerRadii(8), Insets.EMPTY)),
                                item.warnaProperty()
                        )
                );

                // TEKS
                Label label = new Label(item.getLabel());
                label.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

                // GABUNG
                HBox box = new HBox(10, iconBox, label);
                box.setAlignment(Pos.CENTER_LEFT);

                setGraphic(box);
            }
        });

        // INI PENTING BIAR YANG TERPILIH JUGA CANTIK
        accountComboBox.setButtonCell(accountComboBox.getCellFactory().call(null));

        currencyComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(MataUang c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null
                        ? null
//                        : c.getCode() + " — " + c.getName());
                        : c.getKode());
            }
        });
        currencyComboBox.setButtonCell(currencyComboBox.getCellFactory().call(null));
    }

    // [4] >=== SUBMIT HANDLER & VALIDATION
    private void isFormComplete() {
        BooleanBinding nameValid =
                Bindings.createBooleanBinding(
                        () -> !accountName.getText().trim().isEmpty(),
                        accountName.textProperty()
                );

        BooleanBinding accountValid = accountComboBox.valueProperty().isNotNull();
        BooleanBinding colorValid = colorComboBox.valueProperty().isNotNull();
        BooleanBinding currencyValid = currencyComboBox.valueProperty().isNotNull();
        BooleanBinding amountValid =
                Bindings.createBooleanBinding(
                        () -> amountSpinner.getValue() != null && amountSpinner.getValue() >= 0,
                        amountSpinner.valueProperty()
                );

        BooleanBinding formValid =
                nameValid
                        .and(accountValid)
                        .and(colorValid)
                        .and(currencyValid)
                        .and(amountValid);

        submitButton.disableProperty().bind(formValid.not());
    }
    @FXML
    private void handleSubmitAction() {
        String name = IOLogic.normalizeSpaces(accountName.getText());

        if(!uniqueNameValidation(name)){
            MyPopup.showDanger("Duplikasi Nama!", "Nama sudah digunakan!");
            accountName.clear();
            return;
        }

        ColorItem warna = colorComboBox.getValue();
        AccountItem accountItem = accountComboBox.getValue();
        int jumlah = amountSpinner.getValue();
        MataUang currencyItem = currencyComboBox.getValue();

        Akun akunBaru = new Akun(
                0,
                name,
                warna.getWarna(),
                accountItem.getIcon(),
                accountItem.getIconPath(),
                jumlah,
                currencyItem
        );

        DataManager.getInstance().addAkun(akunBaru);
        closePopup();
    }
    private boolean uniqueNameValidation(String name) {
        for(Akun akun : DataManager.getInstance().getDataAkun()) {
            if(name.equalsIgnoreCase(akun.getNama())){
                return false;
            }
        }
        return true;
    }
}