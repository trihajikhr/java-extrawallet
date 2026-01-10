package app;

import controller.DashboardControl;
import dataflow.DataManager;
import dataflow.DataSeeder;
import helper.MyPopup;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;

// package database
import dataflow.Database;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AppPaths;
import service.CurrencyApiClient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class App extends Application {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    @Override
    public void start(Stage stage) throws Exception {

        // Background task load data & dashboard
        Task<Void> loadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {

                updateProgress(0,100);

                Path logDir = Paths.get(System.getProperty("user.home"), ".extrawallet");
                Files.createDirectories(logDir);

                updateProgress(7, 100);

                Database.getInstance();
                updateProgress(15,100);
                CurrencyApiClient.getInstance();
                updateProgress(25, 100);
                DataManager.getInstance().setDataKategori();
                updateProgress(45, 100);
                DataManager.getInstance().initBaseData();
                updateProgress(65, 100);
                DataManager.getInstance().fetchDataDatabase();
                updateProgress(90, 100);
                DataSeeder.getInstance().seedDatabaseKategori();
                updateProgress(95, 100);
                DataSeeder.getInstance().seedDatabaseCurrency();

                // jika pertama kali download aplikasi, buat aku default supaya dahboard tidak crash!
                DataManager.getInstance().setupDefaultAcount();

                log.info("App Mode: {}", AppPaths.IS_DEV ? "DEV" : "PROD");
                log.info("App Root: {}", AppPaths.ROOT.toAbsolutePath());

                updateProgress(100, 100);

                return null;
            }
        };

        // panggil scene loader
        Stage loaderStage = new Stage();
        loaderStage.initStyle(StageStyle.UNDECORATED);
        loaderStage.initModality(Modality.APPLICATION_MODAL);
        loaderStage.setOnCloseRequest(e -> e.consume());
        loaderStage.setScene(createLoadingScene(loadTask));
        loaderStage.centerOnScreen();
        loaderStage.show();

        loadTask.setOnSucceeded(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
                Parent root = loader.load(); // load dulu → root

                // Load stylesheet
                root.getStylesheets().add(
                        getClass().getResource("/stylesheet/auto-card.css").toExternalForm()
                );

                DashboardControl ctrl = loader.getController();
                ctrl.loadPage("home");

                Scene dashboardScene = new Scene(loader.getRoot(), 1200, 800);
                stage.setScene(dashboardScene);

                stage.centerOnScreen();
                stage.show();
                loaderStage.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        loadTask.setOnFailed(e -> {
            loadTask.getException().printStackTrace();
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
        });

        Thread t = new Thread(loadTask);
        t.setName("Loader-Thread");
        t.start();
    }

    private Scene createLoadingScene(Task<Void> task) {
        // app icon
        HBox mainDesign = new HBox();
        mainDesign.setAlignment(Pos.CENTER);
        mainDesign.setSpacing(15);

        Image iconImg = new Image(Objects.requireNonNull(getClass().getResource("/app-icon/icon.png")).toString());
        ImageView icon = new ImageView(iconImg);
        icon.setFitHeight(75);
        icon.setFitWidth(75);

        VBox labelWrapper = new VBox();
        labelWrapper.setAlignment(Pos.CENTER_LEFT);
        labelWrapper.setSpacing(5);
        Label appName = new Label("Extra Wallet");
        appName.setStyle("""
            -fx-text-fill: black;
            -fx-font-size: 26px;
            -fx-font-weight: bold;
        """);

        Label slogan = new Label("Track smarter. Spend better.");
        slogan.setStyle("""
            -fx-font-size: 14px;
            -fx-text-fill: #6A7282;
            -fx-font-weight: bold;
        """);

        labelWrapper.getChildren().addAll(appName, slogan);
        mainDesign.getChildren().addAll(icon, labelWrapper);


        Label loadingLabel = new Label();
        loadingLabel.setStyle("""
            -fx-font-size: 12px;
            -fx-text-fill: #9CA3AF;
        """);
        // loadingLabel.setText("Preparing dashboard...");
        loadingLabel.textProperty().bind(
                task.progressProperty().multiply(100).asString("Preparing dashboard... %.0f%%")
        );


        ProgressBar bar = new ProgressBar();
        // tinggi dan lebar progres bar ada di css aja!
        bar.getStyleClass().add("progress-bar");
        bar.progressProperty().bind(task.progressProperty());

        VBox progresWrapper = new VBox(15, bar, loadingLabel);
        progresWrapper.setAlignment(Pos.CENTER);

        VBox contentWrapper = new VBox(60, mainDesign, progresWrapper);
        contentWrapper.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(contentWrapper);
        root.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/stylesheet/dashboard.css")
                ).toExternalForm()
        );

        return scene;
    }

    public static void main(String[] args) {
        launch();
    }
}