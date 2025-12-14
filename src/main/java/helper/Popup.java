package helper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Popup {

    private static double xOffset = 0;
    private static double yOffset = 0;

    public static void showSucces(String title, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Popup.class.getResource("/fxml/popup-succes.fxml")
            );
            AnchorPane root = loader.load();

            PopupController controller = loader.getController();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            scene.setFill(null);
            scene.getStylesheets().add(
                    Objects.requireNonNull(Popup.class.getResource("/stylesheet/popup-succes.css")).toExternalForm()
            );

            stage.setScene(scene);

            controller.setContent(title, message);
            controller.setStage(stage);

            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDanger(String title, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Popup.class.getResource("/fxml/popup-danger.fxml")
            );
            AnchorPane root = loader.load();

            PopupController controller = loader.getController();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            scene.setFill(null);
            scene.getStylesheets().add(
                    Objects.requireNonNull(Popup.class.getResource("/stylesheet/popup-danger.css")).toExternalForm()
            );

            stage.setScene(scene);

            controller.setContent(title, message);
            controller.setStage(stage);

            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}