package controller;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class Navigation implements Initializable {

    @FXML
    private BorderPane corePane;

    @FXML
    private void home(MouseEvent event){
        loadPage("home");
    }

    @FXML
    private void page1(MouseEvent event){
        loadPage("page1");
    }

    @FXML
    private void page2(MouseEvent event){
        loadPage("page2");
    }

    @FXML
    private void page3(MouseEvent event){
        loadPage("page3");
    }

    @FXML
    private void page4(MouseEvent event){
        loadPage("page4");
    }

    @FXML
    private void page5(MouseEvent event){
        loadPage("page5");
    }

    @FXML
    private void page6(MouseEvent event){
        loadPage("page6");
    }

//    @FXML
//    private void addTransaction(MouseEvent event) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transaction.fxml"));
//        Parent root = loader.load();
//
//        Stage popup = new Stage();
//        popup.initStyle(javafx.stage.StageStyle.UNDECORATED);
//        popup.setResizable(false); // <-- penting kalau mau bener2 fix size
//        popup.setScene(new Scene(root));
//        // popup.initModality(Modality.WINDOW_MODAL);
//
//        // Supaya popup nempel ke window utama
//        popup.initOwner(((Node)event.getSource()).getScene().getWindow());
//
//        // popup.setTitle("Tambah Transaksi");
//        popup.show();
//        // playPopupAnimation(root);
//    }

    @FXML
    private void addTransaction(MouseEvent event) throws IOException {
        PopupUtils.showPopup("/fxml/transaction.fxml", (Node) event.getSource());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        loadPage("home");
    }

    @FXML
    private void loadPage(String page) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(("/fxml/" + page + ".fxml")));
        } catch (IOException e){
            Logger.getLogger(Navigation.class.getName()).log(Level.SEVERE, null, e);
        }

        corePane.setCenter(root);
    }

}