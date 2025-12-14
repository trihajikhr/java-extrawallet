package helper;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class PopUp {
    @FXML
    public static void tampilPopupBerhasil() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Berhasil");
        alert.setHeaderText("Popup JavaFX");
        alert.setContentText("Data berhasil ditambahkan!");

        alert.showAndWait(); // tampil dan tunggu sampai user close
    }

    @FXML
    public static void tampilPopupWarning(String note) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Gagal");
        alert.setHeaderText("Popup JavaFX");
        alert.setContentText(note);

        alert.showAndWait(); // tampil dan tunggu sampai user close
    }
}