package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

// package database
import dataflow.Database;

public class App extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(
                getClass().getResource("/stylesheet/auto-card.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.setTitle("Extra Wallet");

        // buat instance
        Database.getInstance();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}