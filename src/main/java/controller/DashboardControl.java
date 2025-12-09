package controller;

import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.fxml.FXML;

public class DashboardControl {

    @FXML private AnchorPane corePane;

    @FXML private void home(ActionEvent e){loadPage("home");}
    @FXML private void income(ActionEvent e){loadPage("income");}
    @FXML private void expense(ActionEvent e){loadPage("expense");}
    @FXML private void statistic(ActionEvent e){loadPage("statistic");}

//    @FXML
//    private void addTransaction(MouseEvent event) throws IOException {
//        PopupUtils.showPopup("/fxml/transaction.fxml", (Node) event.getSource());
//    }

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