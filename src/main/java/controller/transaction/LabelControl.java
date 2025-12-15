package controller.transaction;

import dataflow.DataManager;
import dataflow.basedata.ColorItem;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.TipeLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class LabelControl implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(LabelControl.class);

    @FXML private TextField labelName;
    @FXML private ComboBox<ColorItem> colorComboBox;
    @FXML private Button submitButton;

    private Stage stage;
    private boolean closing = false;

    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadColorComboBox();
        isTextFieldValid(labelName);
        isFormComplete();
    }

    private void loadColorComboBox() {
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

        colorComboBox.setButtonCell(colorComboBox.getCellFactory().call(null));
    }

    private void isFormComplete() {
        BooleanBinding nameValid =
                Bindings.createBooleanBinding(
                        () -> !labelName.getText().trim().isEmpty(),
                        labelName.textProperty()
                );

        BooleanBinding colorValid = colorComboBox.valueProperty().isNotNull();

        BooleanBinding formValid =
                nameValid.and(colorValid);

        submitButton.disableProperty().bind(formValid.not());
    }

    private void isTextFieldValid(TextField theTextField) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().length() <= 20) {
                return change; // allow input
            } else {
                return null; // reject input
            }
        });

        theTextField.setTextFormatter(formatter);
    }

    @FXML
    private void handleSubmitAction() {
        String nama = labelName.getText();
        Color warna = colorComboBox.getValue().getWarna();

        TipeLabel tipeLabel = new TipeLabel(
                0,
                nama,
                warna
        );

        DataManager.getInstance().addLabel(tipeLabel);
        closePopup();
    }

    // DIPANGGIL dari controller pemanggil
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
}