package helper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class MyPopup {

    private static final Logger log = LoggerFactory.getLogger(MyPopup.class);
    private static double xOffset = 0;
    private static double yOffset = 0;

    public static void showsucces(String title, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MyPopup.class.getResource("/fxml/popup-succes.fxml")
            );
            AnchorPane root = loader.load();

            PopupController controller = loader.getController();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            scene.setFill(null);
            scene.getStylesheets().add(
                    Objects.requireNonNull(MyPopup.class.getResource("/stylesheet/popup-succes.css")).toExternalForm()
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
            log.error("gagal menampilkan popup sukses!", e);
        }
    }

    public static void showDanger(String title, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MyPopup.class.getResource("/fxml/popup-danger.fxml")
            );
            AnchorPane root = loader.load();

            PopupController controller = loader.getController();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            scene.setFill(null);
            scene.getStylesheets().add(
                    Objects.requireNonNull(MyPopup.class.getResource("/stylesheet/popup-danger.css")).toExternalForm()
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
            log.error("gagal menampilkan popup gagal!", e);
        }
    }
}