package controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class TransactionControl {

    private Stage stage;

    // ini dipanggil dari main controller saat load FXML
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Button closeButton;

    @FXML
    private void closePopup() {
        if (stage != null) {
            stage.close();
        }
    }
}