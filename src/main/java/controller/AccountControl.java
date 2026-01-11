package controller;

import dataflow.DataLoader;
import dataflow.DataManager;
import dataflow.basedata.AccountItem;
import dataflow.basedata.ColorItem;
import helper.MyPopup;
import model.Account;
import model.MataUang;
import helper.IOLogic;
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
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountControl implements Initializable {
    // logger
    private static final Logger log = LoggerFactory.getLogger(AccountControl.class);

    private Stage stage;
    private boolean closing = false;

    @FXML private AnchorPane rootPane;

    @FXML private ComboBox<AccountItem> accountComboBox;
    @FXML private ComboBox<ColorItem> colorComboBox;
    @FXML private ComboBox<MataUang> currencyComboBox;

    @FXML private TextField accountName;

    @FXML private Spinner<Integer> amountSpinner;

    @FXML private Button submitButton;

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
        PopupUtils.showPopup(rootPane, stage);
    }
    @FXML
    private void closePopup() {
        if (closing) return;
        closing = true;
        PopupUtils.closePopup(rootPane, stage);
    }

    // [3] >=== INIT DATA
    private void initComboBoxColor() {
        DataLoader.warnaComboBoxLoader(colorComboBox);
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

        Account accountBaru = new Account(
                0,
                name,
                warna.getWarna(),
                accountItem.getIcon(),
                accountItem.getIconPath(),
                jumlah,
                currencyItem
        );

        DataManager.getInstance().addAccount(accountBaru);
        closePopup();
    }
    private boolean uniqueNameValidation(String name) {
        for(Account account : DataManager.getInstance().getDataAkun()) {
            if(name.equalsIgnoreCase(account.getName())){
                return false;
            }
        }
        return true;
    }
}