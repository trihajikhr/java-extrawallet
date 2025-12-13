package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import model.Account;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeControl implements Initializable {

    @FXML private FlowPane accountPane;
    private final ArrayList<Account> accounts = new ArrayList<>();

    @FXML
    private void addAccountCard() {
        accountPane.getChildren().clear(); // optional, tapi sering perlu

        for (Account acc : accounts) {
            accountPane.getChildren().add(new AccountCard(acc));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        accounts.add(new Account(
                "Cash",
                1_250_000,
                new Image(getClass().getResource("/account-type/cash.png").toString())
        ));

        accounts.add(new Account(
                "Bank BCA",
                5_300_000,
                new Image(getClass().getResource("/account-type/credit.png").toString())
        ));

        addAccountCard();
        System.out.println("Hello world");
    }
}