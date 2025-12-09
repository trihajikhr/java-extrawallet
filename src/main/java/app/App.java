package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

// package database
import dataflow.Database;

import java.sql.SQLException;

public class App extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Extra Wallet");

        // buat instace
        try {
            Database.getInstance();
        } catch (SQLException e) {

        }

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}