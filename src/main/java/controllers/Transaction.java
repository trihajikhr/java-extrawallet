package controllers;

import controllers.PopupUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Transaction implements PopupUtils.PopupClosable {

    private Stage popupStage;

    @FXML
    private Parent rootPane; // fx:id="root" pada FXML

    @Override
    public void setPopupStage(Stage stage) {
        this.popupStage = stage;
    }

    @FXML
    private void closePopup(MouseEvent e) {
        PopupUtils.playCloseAnimation(rootPane, popupStage);
    }
}