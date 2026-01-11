package controller.transaction;

import controller.PopupUtils;
import dataflow.DataLoader;
import dataflow.DataManager;
import dataflow.basedata.ColorItem;
import helper.IOLogic;
import helper.MyPopup;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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
    private EditControl parentEdit;

    private Stage stage;
    private boolean closing = false;

    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DataLoader.warnaComboBoxLoader(colorComboBox);
        IOLogic.isTextFieldValid(labelName,20);
        isFormComplete();
        showPopup();
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
    @FXML
    private void handleSubmitAction() {
        String nama = IOLogic.normalizeSpaces(labelName.getText());

        if(!uniqueNameValidation(nama)) {
            MyPopup.showDanger("Duplikasi Nama!", "Nama sudah digunakan!");
            labelName.clear();
            return;
        }

        Color warna = colorComboBox.getValue().getWarna();

        TipeLabel tipeLabel = new TipeLabel(0, nama, warna);

        boolean result = DataManager.getInstance().addLabel(tipeLabel);
        if(parentTemplate != null && result) {
            parentTemplate.getTipeLabelObservable().add(tipeLabel);
            parentTemplate.getTipeLabel().getSelectionModel().select(tipeLabel);
            grandParent.getTipeLabelList().add(tipeLabel);
        } else if(result && parentTransaction != null) {
            parentTransaction.getTipeLabelList().add(tipeLabel);
            parentTransaction.getTipeLabelInOut().getSelectionModel().select(tipeLabel);
            parentTransaction.getTipeLabelTrans().getSelectionModel().select(tipeLabel);
        } else if(result && parentEdit != null) {
            parentEdit.getTipeLabelList().add(tipeLabel);
            parentEdit.getTipeLabelCombo().getSelectionModel().select(tipeLabel);
        }
        closePopup();
    }

    private boolean uniqueNameValidation(String name) {
        for(TipeLabel tipeLabel : DataManager.getInstance().getDataTipeLabel()) {
            if(name.equalsIgnoreCase(tipeLabel.getNama())) {
                return false;
            }
        }
        return true;
    }

    // [2] >=== SCENE CONNECTION FUNCTION
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setParentTransaction(TransactionControl parent) {
        this.parentTransaction = parent;
    }
    public void setParentEditRecord(EditControl parent) {
        this.parentEdit = parent;
    }
    public void setParentTemplate(TemplateControl parent, TransactionControl grandParent) {
        this.grandParent = grandParent;
        this.parentTemplate = parent;
    }
}