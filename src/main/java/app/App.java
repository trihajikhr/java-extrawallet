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
import service.CurrencyApiClient;

import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Background task load data & dashboard
        Task<FXMLLoader> loadTask = new Task<>() {
            @Override
            protected FXMLLoader call() throws Exception {

                updateProgress(0,100);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
                Parent root = loader.load(); // load dulu → root

                updateProgress(15,100);

                // Load stylesheet
                root.getStylesheets().add(
                        getClass().getResource("/stylesheet/auto-card.css").toExternalForm()
                );
                updateProgress(25, 100);

                // Setup data / database
                CurrencyApiClient.getInstance();
                Database.getInstance();
                updateProgress(45, 100);
                DataManager.getInstance().setDataKategori();
                DataManager.getInstance().initBaseData();
                updateProgress(65, 100);
                DataManager.getInstance().fetchDataDatabase();
                updateProgress(90, 100);
                DataSeeder.getInstance().seedDatabaseKategori();
                updateProgress(95, 100);
                DataSeeder.getInstance().seedDatabaseCurrency();
                updateProgress(100, 100);

                return loader;
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
                FXMLLoader loader = loadTask.getValue();
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

    private Scene createLoadingScene(Task<FXMLLoader> task) {
        // app icon
        HBox mainDesign = new HBox();
        mainDesign.setAlignment(Pos.CENTER);
        mainDesign.setSpacing(10);

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