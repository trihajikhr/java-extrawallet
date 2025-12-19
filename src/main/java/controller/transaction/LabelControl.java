package controller.transaction;

import dataflow.DataLoader;
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

    private TransactionControl parentTransaction;
    private TemplateControl parentTemplate;
    private TransactionControl grandParent;

    private Stage stage;
    private boolean closing = false;

    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showPopup();
        DataLoader.getInstance().warnaComboBoxLoader(colorComboBox);
        isTextFieldValid(labelName);
        isFormComplete();
    }

    // [0] >=== SCENE CONTROLLER
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

    // [1] >=== FORM VALIDATION & SUBMIT HANDLER
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

        TipeLabel tipeLabel = new TipeLabel(0, nama, warna);

        boolean result = DataManager.getInstance().addLabel(tipeLabel);
        if(parentTemplate != null && result) {
            parentTemplate.getTipeLabelList().add(tipeLabel);
            parentTemplate.getTipeLabel().getSelectionModel().select(tipeLabel);
            grandParent.getTipeLabelList().add(tipeLabel);
        } else if(result && parentTransaction != null) {
            parentTransaction.getTipeLabelList().add(tipeLabel);
            parentTransaction.getTipeLabelInOut().getSelectionModel().select(tipeLabel);
            parentTransaction.getTipeLabelTrans().getSelectionModel().select(tipeLabel);
        }
        closePopup();
    }

    // [2] >=== SCENE CONNECTION FUNCTION
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setParentTransaction(TransactionControl parent) {
        this.parentTransaction = parent;
    }
    public void setParentTemplate(TemplateControl parent, TransactionControl grandParent) {
        this.grandParent = grandParent;
        this.parentTemplate = parent;
    }
}