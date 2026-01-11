package controller;

import helper.Converter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Account;

public class AccountCard extends HBox {

    public AccountCard(Account account) {

        setSpacing(10);
        setPadding(new Insets(10));
        getStyleClass().add("account-card");

        // override warna
        setStyle(
                "-fx-background-color: " + Converter.colorToHex(account.getColor()) + ";"
        );

        ImageView icon = new ImageView(account.getIcon());
        icon.setFitWidth(32);
        icon.setFitHeight(32);

        Label name = new Label(account.getName());
        name.getStyleClass().add("account-name");

        Label balance = new Label(formatCurrency(account.getBalance()));
        balance.getStyleClass().add("account-balance");

        VBox textBox = new VBox(name, balance);
        textBox.setSpacing(4);

        getChildren().addAll(icon, textBox);
    }


    private String formatCurrency(double value) {
        return "Rp " + String.format("%,.0f", value);
    }
}
