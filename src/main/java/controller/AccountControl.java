package controller;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
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

class AccountItem {
    private String label;
    private Image icon;
    private ObjectProperty<Color> warna = new SimpleObjectProperty<>(Color.WHITE);

    public AccountItem(String label, Image icon) {
        this.label = label;
        this.icon = icon;
    }

    public String getLabel() { return label; }
    public Image getIcon() { return icon; }
    public Color getWarna() { return warna.get(); }
    public void setWarna(Color c) { warna.set(c); }
    public ObjectProperty<Color> warnaProperty() { return warna; }
}

class ColorItem {
    private final String label;
    private final Color warna;

    public ColorItem(String label, Color warna) {
        this.label = label;
        this.warna = warna;
    }

    public String getLabel() { return label; }
    public Color getWarna() { return warna; }
}

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("Popup account terbuka!");

        colorComboBox.getItems().addAll(
            new ColorItem("Berry Red", Color.web("#D0006F")),
            new ColorItem("Red", Color.web("#FF0000")),
            new ColorItem("Orange", Color.web("#FF7F00")),
            new ColorItem("Yellow", Color.web("#FFD700")),
            new ColorItem("Olive Green", Color.web("#808000")),
            new ColorItem("Lime Green", Color.web("#32CD32")),
            new ColorItem("Mint Green", Color.web("#3EB489")),
            new ColorItem("Green", Color.web("#008000")),
            new ColorItem("Teal", Color.web("#008080")),
            new ColorItem("Sky Blue", Color.web("#87CEEB")),
            new ColorItem("Light Blue", Color.web("#ADD8E6")),
            new ColorItem("Blue", Color.web("#0000FF")),
            new ColorItem("Grape", Color.web("#6F2DA8")),
            new ColorItem("Violet", Color.web("#8A2BE2")),
            new ColorItem("Lavender", Color.web("#E6E6FA")),
            new ColorItem("Magenta", Color.web("#FF00FF")),
            new ColorItem("Salmon", Color.web("#FA8072")),
            new ColorItem("Charcoal", Color.web("#36454F")),
            new ColorItem("Grey", Color.web("#808080")),
            new ColorItem("Taupe", Color.web("#483C32"))
        );

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

        accountComboBox.getItems().addAll(
                new AccountItem(
                        "General",
                        new Image(getClass().getResource("/account-type/general.png").toString())
                ),
                new AccountItem(
                        "Cash",
                        new Image(getClass().getResource("/account-type/cash.png").toString())
                ),
                new AccountItem(
                        "Savings",
                        new Image(getClass().getResource("/account-type/savings.png").toString())
                ),
                new AccountItem(
                        "Credit",
                        new Image(getClass().getResource("/account-type/credit.png").toString())
                )
        );

        for (AccountItem item : accountComboBox.getItems()) {
            item.setWarna(Color.WHITE);
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

//                iconBox.setBackground(new Background(
//                        new BackgroundFill(
//                                item.getWarna(),
//                                new CornerRadii(8),
//                                Insets.EMPTY
//                        )
//                ));

//                iconBox.setBackground(new Background(
//                        new BackgroundFill(item.getWarna(), new CornerRadii(8), Insets.EMPTY)
//                ));

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
    }
}