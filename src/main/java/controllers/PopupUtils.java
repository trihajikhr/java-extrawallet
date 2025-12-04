package controllers;

import javafx.animation.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class PopupUtils {

    public interface PopupClosable {
        void setPopupStage(Stage stage);
    }

    public static void showPopup(String fxmlPath, Node owner) throws IOException {

        FXMLLoader loader = new FXMLLoader(PopupUtils.class.getResource(fxmlPath));
        Parent root = loader.load();

        Stage popup = new Stage();
        popup.initStyle(StageStyle.UNDECORATED);
        popup.setResizable(false);
        popup.initOwner(owner.getScene().getWindow());

        Scene scene = new Scene(root);
        popup.setScene(scene);

        Object controller = loader.getController();
        if (controller instanceof PopupClosable closable) {
            closable.setPopupStage(popup);
        }

        popup.show();
        playOpenAnimation(root);
    }

    public static void playOpenAnimation(Parent root) {
        root.setOpacity(0);
        root.setScaleX(0.92);
        root.setScaleY(0.92);

        FadeTransition fade = new FadeTransition(Duration.millis(160), root);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(160), root);
        scale.setFromX(0.92);
        scale.setFromY(0.92);
        scale.setToX(1);
        scale.setToY(1);

        new ParallelTransition(fade, scale).play();
    }

    public static void playCloseAnimation(Parent root, Stage popup) {
        FadeTransition fade = new FadeTransition(Duration.millis(150), root);
        fade.setFromValue(1);
        fade.setToValue(0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(150), root);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(0.92);
        scale.setToY(0.92);

        ParallelTransition pt = new ParallelTransition(fade, scale);
        pt.setOnFinished(e -> popup.close());
        pt.play();
    }
}