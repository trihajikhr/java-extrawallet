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
import model.LabelType;
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

        Color warna = colorComboBox.getValue().getColor();

        LabelType labelType = new LabelType(0, nama, warna);

        boolean result = DataManager.getInstance().addLabel(labelType);
        if(parentTemplate != null && result) {
            parentTemplate.getTipeLabelObservable().add(labelType);
            parentTemplate.getTipeLabel().getSelectionModel().select(labelType);
            grandParent.getTipeLabelList().add(labelType);
        } else if(result && parentTransaction != null) {
            parentTransaction.getTipeLabelList().add(labelType);
            parentTransaction.getTipeLabelInOut().getSelectionModel().select(labelType);
            parentTransaction.getTipeLabelTrans().getSelectionModel().select(labelType);
        } else if(result && parentEdit != null) {
            parentEdit.getTipeLabelList().add(labelType);
            parentEdit.getTipeLabelCombo().getSelectionModel().select(labelType);
        }
        closePopup();
    }

    private boolean uniqueNameValidation(String name) {
        for(LabelType labelType : DataManager.getInstance().getDataTipeLabel()) {
            if(name.equalsIgnoreCase(labelType.getName())) {
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