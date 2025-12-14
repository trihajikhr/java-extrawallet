package controller;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Akun;

public class AccountCard extends HBox {

    public AccountCard(Akun account) {
        setSpacing(10);
        setPadding(new Insets(10));
        getStyleClass().add("account-card");

        ImageView icon = new ImageView(account.getIcon());
        icon.setFitWidth(32);
        icon.setFitHeight(32);

        Label name = new Label(account.getNama());
        name.getStyleClass().add("account-name");

        Label balance = new Label(formatCurrency(account.getJumlah()));
        balance.getStyleClass().add("account-balance");

        VBox textBox = new VBox(name, balance);
        textBox.setSpacing(4);

        getChildren().addAll(icon, textBox);

        getStyleClass().add("account-card");
        name.getStyleClass().add("account-name");
        balance.getStyleClass().add("account-balance");
    }

    private String formatCurrency(double value) {
        return "Rp " + String.format("%,.0f", value);
    }
}
