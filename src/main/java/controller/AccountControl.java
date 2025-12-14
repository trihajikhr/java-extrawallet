package controller;

import dataflow.DataManager;
import dataflow.DataSeeder;
import dataflow.basedata.AccountItem;
import dataflow.basedata.ColorItem;
import dataflow.basedata.CurrencyItem;
import helper.IOLogic;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
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
    private ComboBox<CurrencyItem> currencyComboBox;

    @FXML
    private TextField accountText;

    @FXML
    private Spinner<Integer> accountSpinner;

    @FXML
    private Button submitButton;

    // DIPANGGIL dari controller lain
    public void setStage(Stage stage) {
        this.stage = stage;
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

    private void buttonIsSubmitted() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("Popup account terbuka!");
        colorComboBox.setItems(DataManager.getInstance().getDataColor());
        accountComboBox.setItems(DataManager.getInstance().getDataAccountItem());
        currencyComboBox.setItems(DataManager.getInstance().getDataCurrency());

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
            protected void updateItem(CurrencyItem c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null
                        ? null
//                        : c.getCode() + " — " + c.getName());
                          : c.getCode());
            }
        });
        currencyComboBox.setButtonCell(currencyComboBox.getCellFactory().call(null));

        IOLogic.makeIntegerOnly(accountSpinner, 0, 2_147_483_647, 0);
    }
}