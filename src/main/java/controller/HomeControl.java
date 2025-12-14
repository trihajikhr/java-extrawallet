package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import model.Akun;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeControl implements Initializable {

    @FXML private FlowPane accountPane;
    private final ArrayList<Akun> accounts = new ArrayList<>();

    @FXML
    private void addAccountCard() {
        accountPane.getChildren().clear(); // optional, tapi sering perlu

        for (Akun acc : accounts) {
            accountPane.getChildren().add(new AccountCard(acc));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        accounts.add(new Akun(
                "Cash",
                1_250_000,
                new Image(getClass().getResource("/account-type/cash.png").toString())
        ));

        accounts.add(new Akun(
                "Bank BCA",
                5_300_000,
                new Image(getClass().getResource("/account-type/credit.png").toString())
        ));

        addAccountCard();
        System.out.println("Hello world");
    }
}