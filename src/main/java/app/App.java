package app;

import controller.DashboardControl;
import dataflow.DataManager;
import dataflow.DataSeeder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

// package database
import dataflow.Database;
import service.CurrencyApiClient;

public class App extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(
                getClass().getResource("/stylesheet/auto-card.css").toExternalForm()
        );

        // currencyClient api loader
        CurrencyApiClient.getInstance();

        stage.setScene(scene);
        stage.setTitle("Extra Wallet");

        // buat instance [MENJALANKAN PROGRAM SETUP AWAL DISINI!]
        Database.getInstance();
        DataManager.getInstance().setDataKategori();
        DataManager.getInstance().initBaseData();
        DataManager.getInstance().fetchDataDatabase();
        DataSeeder.getInstance().seedDatabaseKategori();
        DataSeeder.getInstance().seedDatabaseCurrency();

        // load tampilan & setting default menu
        stage.show();

        DashboardControl ctrl = loader.getController();
        ctrl.loadPage("home");
    }

    public static void main(String[] args) {
        launch();
    }
}