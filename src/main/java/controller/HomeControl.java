package controller;

import dataflow.DataManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import model.Akun;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeControl implements Initializable {

    @FXML
    private FlowPane accountPane;
    private ArrayList<Akun> dataAkun = new ArrayList<>();

    @FXML
    private void addAccountCard() {
        accountPane.getChildren().clear(); // optional, tapi sering perlu
        for (Akun acc : dataAkun) {
            accountPane.getChildren().add(new AccountCard(acc));
        }
    }

    private void initAllData() {
        dataAkun = DataManager.getInstance().getDataAkun();
        addAccountCard();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accountPane.setHgap(12); // jarak horizontal
        accountPane.setVgap(12); // jarak vertikal
        initAllData();
    }
}