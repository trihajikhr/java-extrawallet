package helper;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PopupController {

    @FXML private AnchorPane root;
    @FXML private Label titleLabel;
    @FXML private Label messageLabel;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        playOpenAnimation();
    }

    public void setContent(String title, String message) {
        titleLabel.setText(title);
        messageLabel.setText(message);
    }

    private void playOpenAnimation() {
        FadeTransition fade = new FadeTransition(Duration.millis(200), root);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(200), root);
        scale.setFromX(0.9);
        scale.setFromY(0.9);
        scale.setToX(1);
        scale.setToY(1);

        fade.play();
        scale.play();
    }

    @FXML
    private void close() {
        FadeTransition fade = new FadeTransition(Duration.millis(150), root);
        fade.setFromValue(1);
        fade.setToValue(0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(150), root);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(0.9);
        scale.setToY(0.9);

        fade.setOnFinished(e -> stage.close());

        fade.play();
        scale.play();
    }
}