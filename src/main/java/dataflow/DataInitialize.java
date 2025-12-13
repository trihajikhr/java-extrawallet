package dataflow;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;

class AccountType {
    private Image icon;
    private Color warna;
    private String label;

    public AccountType(Image icon, Color warna, String label) {
        this.icon = icon;
        this.warna = warna;
        this.label = label;
    }

    public AccountType() {}

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public Color getWarna() {
        return warna;
    }

    public void setWarna(Color warna) {
        this.warna = warna;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

public class DataInitialize {
    private static DataInitialize instance;
    private ArrayList<AccountType> accountType = new ArrayList<AccountType>();

    private DataInitialize() {
        accountType.add(new AccountType(new Image(getClass().getResource("/account-type/general.png").toString()),
                Color.web("#84DA46"),
                "General"));
        accountType.add(new AccountType(new Image(getClass().getResource("/account-type/cash.png").toString()),
                Color.web("#2BB7FC"),
                "Cash"));
        accountType.add(new AccountType(new Image(getClass().getResource("/account-type/credit.png").toString()),
                Color.web("#F92222"),
                "Credit"));
        accountType.add(new AccountType(new Image(getClass().getResource("/account-type/credit.png").toString()),
                Color.web("#0176FE"),
                "Savings"));
    }

    public static DataInitialize getInstance() {
        if(instance == null) {
            instance = new DataInitialize();
        }
        return instance;
    }
}