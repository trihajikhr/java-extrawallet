package controller;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

// logger library
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardControl {

    // logger
    private static final Logger log = LoggerFactory.getLogger(DashboardControl.class);

    private boolean isExpanded = false;
    private final double expandedWidth = 200;
    private final double collapsedWidth = 55;
    private Label[] navLabel;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML private VBox navbarContainer;
    @FXML private Button toggleButton;
    @FXML private AnchorPane corePane;

    @FXML private HBox toggleHBox;
    @FXML private Label toggleIconLabel;
    @FXML private ImageView toggleIcon;

    @FXML private Image imgOpen;
    @FXML private Image imgClose;

    @FXML private Label item1Label;
    @FXML private Label item2Label;
    @FXML private Label item3Label;
    @FXML private Label item4Label;
    @FXML private Label item5Label;

    @FXML private void home(ActionEvent e){loadPage("home");}
    @FXML private void income(ActionEvent e){loadPage("income");}
    @FXML private void expense(ActionEvent e){loadPage("expense");}
    @FXML private void statistic(ActionEvent e){loadPage("statistic");}
    @FXML private void report(ActionEvent e){loadPage("report");}

    @FXML
    private void addTransaction(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transaction.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();

        // --- 1. PENGATURAN STYLE STAGE ---
        // Hapus StageStyle.UNDECORATED yang bertentangan
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);

        // --- 2. APLIKASI DROPSHADOW KE ROOT ---
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(5.0);
        dropShadow.setOffsetY(5.0);
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.4));
        root.setEffect(dropShadow);

        // --- 3. PENGATURAN SCENE (Harus SETELAH root dimuat dan diberi efek) ---
        Scene scene = new Scene(root);
        // Kesalahan Ada di sini: hanya menulis ".setFill(Color.TRANSPARENT);"
        scene.setFill(Color.TRANSPARENT); // Wajib agar area di sekitar shadow transparan
        stage.setScene(scene); // Set Scene harus dilakukan setelah scene dibuat

        stage.setMinWidth(750);
        stage.setMinHeight(650);
//        stage.setMaxWidth(800);
//        stage.setMaxHeight(700);

        // draggable pop up
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        // kasih akses stage ke controller
        TransactionControl ctrl = loader.getController();
        ctrl.setStage(stage);

        stage.showAndWait();
    }

    @FXML
    private void toggleNavbar() {
        isExpanded = !isExpanded;

        double targetWidth = isExpanded ? expandedWidth : collapsedWidth;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(200),
                        new KeyValue(navbarContainer.prefWidthProperty(), targetWidth)
                )
        );

        if (isExpanded) {
            // Setelah navbar selesai melebar → fade in label
            timeline.setOnFinished(e -> {
                fadeInTexts();
                toggleIcon.setImage(imgOpen);
            });

            timeline.play();

        } else {
            // Fade out dulu, baru animasi mengecil
            fadeOutTexts();

            // Biar animasi shrink tunggu sebentar supaya fade out selesai
            timeline.setDelay(Duration.millis(150));
            timeline.setOnFinished(e -> toggleIcon.setImage(imgClose));
            timeline.play();
        }
    }


    private void fadeInTexts() {
        for (Label text : navLabel) {
            text.setManaged(true);
            text.setVisible(true);
            text.setOpacity(0);

            FadeTransition ft = new FadeTransition(Duration.millis(150), text);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        }
    }

    private void fadeOutTexts() {
        for (Label text : navLabel) {
            FadeTransition ft = new FadeTransition(Duration.millis(150), text);
            ft.setFromValue(1);
            ft.setToValue(0);

            ft.setOnFinished(e -> {
                text.setVisible(false);
                text.setManaged(false);
            });

            ft.play();
        }
    }


    private void setTextsVisible(boolean visible) {
        for (Label text : navLabel) {
            text.setVisible(visible);
            text.setManaged(visible);
        }
    }

    @FXML
    public void initialize() {
        navLabel = new Label[]{item1Label, item2Label, item3Label, item4Label, item5Label};

        imgOpen  = new Image(getClass().getResource("/icons/left-arrow.png").toString());
        imgClose   = new Image(getClass().getResource("/icons/menu.png").toString());

        isExpanded = false;
        navbarContainer.setPrefWidth(collapsedWidth);
        setTextsVisible(false);
        toggleIcon.setImage(imgClose);
    }

    @FXML
    public void loadPage(String page) {
        try {
            FXMLLoader loadder = new FXMLLoader(getClass().getResource("/fxml/" + page + ".fxml"));
            Parent root = loadder.load();
            corePane.getChildren().setAll(root);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}