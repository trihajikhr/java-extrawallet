package navigation;

import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Sidebar implements Initializable {

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
            Logger.getLogger(Sidebar.class.getName()).log(Level.SEVERE, null, e);
        }

        corePane.setCenter(root);
    }

}

