package controllers;

import controllers.PopupUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Transaction implements PopupUtils.PopupClosable {

    private Stage popupStage;

    @FXML
    private Parent rootPane; // fx:id="rootPane" pada FXML

    @Override
    public void setPopupStage(Stage stage) {
        this.popupStage = stage;
    }

    @FXML
    private void closePopup(MouseEvent e) {
        PopupUtils.playCloseAnimation(rootPane, popupStage);
    }

    // dragable pop up
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void initialize() {
        // klik awal: ambil offset
        rootPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        // geser: update posisi stage
        rootPane.setOnMouseDragged(event -> {
            if (popupStage != null) {
                popupStage.setX(event.getScreenX() - xOffset);
                popupStage.setY(event.getScreenY() - yOffset);
            }
        });
    }
}