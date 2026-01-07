package app;

import controller.DashboardControl;
import dataflow.DataManager;
import dataflow.DataSeeder;
import helper.MyPopup;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;

// package database
import dataflow.Database;
import service.CurrencyApiClient;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // tampilan sementara progress indicator
        ProgressIndicator progress = new ProgressIndicator();
        progress.setPrefSize(100, 100);
        Scene loadingScene = new Scene(new StackPane(progress), 400, 300);
        stage.setScene(loadingScene);
        stage.show();

        // Background task load data & dashboard
        Task<FXMLLoader> loadTask = new Task<>() {
            @Override
            protected FXMLLoader call() throws Exception {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
                Parent root = loader.load(); // load dulu → root

                // Load stylesheet
                root.getStylesheets().add(
                        getClass().getResource("/stylesheet/auto-card.css").toExternalForm()
                );

                // Setup data / database
                CurrencyApiClient.getInstance();
                Database.getInstance();
                DataManager.getInstance().setDataKategori();
                DataManager.getInstance().initBaseData();
                DataManager.getInstance().fetchDataDatabase();
                DataSeeder.getInstance().seedDatabaseKategori();
                DataSeeder.getInstance().seedDatabaseCurrency();

                return loader;
            }
        };

        loadTask.setOnSucceeded(e -> {
            try {
                FXMLLoader loader = loadTask.getValue();
                Scene dashboardScene = new Scene(loader.getRoot(), 1200, 800);
                stage.setScene(dashboardScene);

                // load halaman default
                DashboardControl ctrl = loader.getController();
                ctrl.loadPage("home");

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


    public static void main(String[] args) {
        launch();
    }
}