package controller;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AccountItem {
    private String label;
    private Image icon;
    private Color warna;

    public AccountItem(String label, Color warna, Image icon) {
        this.label = label;
        this.icon = icon;
        this.warna = warna;
    }

    public String getLabel() { return label; }
    public Image getIcon() { return icon; }
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

        accountComboBox.getItems().addAll(
                new AccountItem(
                        "General",
                        Color.web("#6C5CE7"),
                        new Image(getClass().getResource("/account-type/general.png").toString())
                ),
                new AccountItem(
                        "Cash",
                        Color.web("#00B894"),
                        new Image(getClass().getResource("/account-type/cash.png").toString())
                ),
                new AccountItem(
                        "Savings",
                        Color.web("#0984E3"),
                        new Image(getClass().getResource("/account-type/savings.png").toString())
                ),
                new AccountItem(
                        "Credit",
                        Color.web("#D63031"),
                        new Image(getClass().getResource("/account-type/credit.png").toString())
                )
        );

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

                iconBox.setBackground(new Background(
                        new BackgroundFill(
                                item.getWarna(),
                                new CornerRadii(8),
                                Insets.EMPTY
                        )
                ));

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