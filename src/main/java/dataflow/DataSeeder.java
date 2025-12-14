package dataflow;

import dataflow.basedata.AccountItem;
import dataflow.basedata.ColorItem;
import dataflow.basedata.CurrencyItem;
import helper.Converter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Objects;

import model.Kategori;

public class DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private static DataSeeder instance;

    public DataSeeder() {}

    public static DataSeeder getInstance() {
        if(instance == null) {
            instance = new DataSeeder();
            log.info("objek data seeder berhasil dibuat!");
        }
        return instance;
    }

    public ObservableList<Kategori> seedArrayKategori() {
        ObservableList<Kategori> data = FXCollections.observableArrayList();

        try {
            data.addAll(
                    // IN
                    new Kategori(
                            1,
                            "IN",
                            "Salary",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/1_salary.png")).toString()),
                            "/category-icons/1_salary.png",
                            Color.web("#D0006F")
                    ),
                    new Kategori(
                            2,
                            "IN",
                            "Allowance",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/2_allowance.png")).toString()),
                            "/category-icons/2_allowance.png",
                            Color.web("#FF0000")
                    ),
                    new Kategori(
                            3,
                            "IN",
                            "Bonuses",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/3_bonuses.png")).toString()),
                            "/category-icons/3_bonuses.png",
                            Color.web("#FF7F00")
                    ),
                    new Kategori(
                            4,
                            "IN",
                            "Busines",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/4_business.png")).toString()),
                            "/category-icons/4_business.png",
                            Color.web("#FFD700")
                    ),
                    new Kategori(
                            5,
                            "IN",
                            "Freelance/project",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/5_freelance.png")).toString()),
                            "/category-icons/5_freelance.png",
                            Color.web("#808000")
                    ),
                    new Kategori(
                            6,
                            "IN",
                            "Sales",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/6_sales.png")).toString()),
                            "/category-icons/6_sales.png",
                            Color.web("#32CD32")
                    ),
                    new Kategori(
                            7,
                            "IN",
                            "Dividends",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/7_dividends.png")).toString()),
                            "/category-icons/7_dividends.png",
                            Color.web("#3EB489")
                    ),
                    new Kategori(
                            8,
                            "IN",
                            "Investment Gains",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/8_investment.png")).toString()),
                            "/category-icons/8_investment.png",
                            Color.web("#008000")
                    ),
                    new Kategori(
                            9,
                            "IN",
                            "Incoming Transfer",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/9_incoming-transfer.png")).toString()),
                            "/category-icons/9_incoming-transfer.png",
                            Color.web("#3EB489")
                    ),
                    new Kategori(
                            10,
                            "IN",
                            "Gift",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/10_gift.png")).toString()),
                            "/category-icons/10_gift.png",
                            Color.web("#008080")
                    ),
                    new Kategori(
                            11,
                            "IN",
                            "Cashback",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/11_cashback.png")).toString()),
                            "/category-icons/11_cashback.png",
                            Color.web("#87CEEB")
                    ),
                    new Kategori(
                            12,
                            "IN",
                            "Commission",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/12_commission.png")).toString()),
                            "/category-icons/12_commission.png",
                            Color.web("#ADD8E6")
                    ),
                    new Kategori(
                            13,
                            "IN",
                            "Royalty",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/13_royalty.png")).toString()),
                            "/category-icons/13_royalty.png",
                            Color.web("#0000FF")
                    ),
                    new Kategori(
                            14,
                            "IN",
                            "App Reward",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/14_app-reward.png")).toString()),
                            "/category-icons/14_app-reward.png",
                            Color.web("#6F2DA8")
                    ),
                    new Kategori(
                            15,
                            "IN",
                            "Others",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/15_others.png")).toString()),
                            "/category-icons/15_others.png",
                            Color.web("#62718a")
                    ),

                    // OUT
                    new Kategori(
                            16,
                            "OUT",
                            "Food & Beverages",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/16_food.png")).toString()),
                            "/category-icons/16_food.png",
                            Color.web("#D0006F")
                    ),
                    new Kategori(
                            17,
                            "OUT",
                            "Daily Shopping",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/17_shopping.png")).toString()),
                            "/category-icons/17_shopping.png",
                            Color.web("#FF0000")
                    ),
                    new Kategori(
                            18,
                            "OUT",
                            "Transportation",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/18_transportation.png")).toString()),
                            "/category-icons/18_transportation.png",
                            Color.web("#FF7F00")
                    ),
                    new Kategori(
                            19,
                            "OUT",
                            "Bills & Utilities",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/19_bills.png")).toString()),
                            "/category-icons/19_bills.png",
                            Color.web("#FFD700")
                    ),
                    new Kategori(
                            20,
                            "OUT",
                            "Personal Shopping",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/13_royalty.png")).toString()),
                            "/category-icons/13_royalty.png",
                            Color.web("#808000")
                    ),
                    new Kategori(
                            21,
                            "OUT",
                            "Gadgets & Electronics",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/20_gadgets.png")).toString()),
                            "/category-icons/20_gadgets.png",
                            Color.web("#32CD32")
                    ),
                    new Kategori(
                            22,
                            "OUT",
                            "Health",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/21_health.png")).toString()),
                            "/category-icons/21_health.png",
                            Color.web("#3EB489")
                    ),
                    new Kategori(
                            23,
                            "OUT",
                            "Entertainment & Lifestyle",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/22_entertainment.png")).toString()),
                            "/category-icons/22_entertainment.png",
                            Color.web("#008000")
                    ),
                    new Kategori(
                            24,
                            "OUT",
                            "Education & Courses",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/23_education.png")).toString()),
                            "/category-icons/23_education.png",
                            Color.web("#008080")
                    ),
                    new Kategori(
                            25,
                            "OUT",
                            "Financial Obligations",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/24_obligation.png")).toString()),
                            "/category-icons/24_obligation.png",
                            Color.web("#87CEEB")
                    ),
                    new Kategori(
                            26,
                            "OUT",
                            "Home & Appliances",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/25_home.png")).toString()),
                            "/category-icons/25_home.png",
                            Color.web("#ADD8E6")
                    ),
                    new Kategori(
                            27,
                            "OUT",
                            "Family",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/28_family.png")).toString()),
                            "/category-icons/28_family.png",
                            Color.web("#0000FF")
                    ),
                    new Kategori(
                            28,
                            "OUT",
                            "Gift",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/10_gift.png")).toString()),
                            "/category-icons/10_gift.png",
                            Color.web("#6F2DA8")
                    ),
                    new Kategori(
                            29,
                            "OUT",
                            "Donation",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/27_donation.png")).toString()),
                            "/category-icons/27_donation.png",
                            Color.web("#8A2BE2")
                    ),
                    new Kategori(
                            30,
                            "OUT",
                            "Others",
                            new Image(Objects.requireNonNull(getClass().getResource("/category-icons/15_others.png")).toString()),
                            "/category-icons/15_others.png",
                            Color.web("#62718a")
                    )
            );
            log.info("pembuatan data kategori berhasil!");
            return data;

        } catch (Exception e) {
            log.error("terjadi kesalahan: ", e);
            return null;
        }
    }

    public void seedDatabaseKategori() {
        Database dataConnect = Database.getInstance();
        String querySql = "INSERT OR IGNORE INTO kategori (id, tipe, nama, icon_path, warna) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stat = dataConnect.getConnection().prepareStatement(querySql)) {
            for(Kategori ktr : DataManager.getInstance().coreDataKategori()) {
                stat.setInt(1, ktr.getId());
                stat.setString(2, ktr.getTipe());
                stat.setString(3, ktr.getNama());
                stat.setString(4, ktr.getIconPath());
                stat.setString(5, Converter.getInstance().colorToHex(ktr.getWarna()));

                stat.executeUpdate();
            }
            log.info("data kategori berhasil di seed ke database!");

        } catch (SQLException e) {
            log.error("gagal seed data kategori: ", e);
        }
    }

    public ObservableList<String> seedTypeData () {
        ObservableList<String> data = FXCollections.observableArrayList();
        data.addAll(
            "Cash",
            "Debit card",
            "Credit card",
            "Transfer",
            "Voucher",
            "Mobile payment"
        );
        log.info("mengisi data typedata!");
        return data;
    }

    public ObservableList<String> seedStatusData () {
        ObservableList<String> data = FXCollections.observableArrayList();
        data.addAll(
            "Reconciled",
            "Cleared",
            "Uncleared"

        );
        log.info("mengisi data statusdata!");
        return data;
    }

    public void colorSeeder() {
        DataManager.getInstance().getDataColor().setAll(
                new ColorItem("Berry Red", Color.web("#D0006F")),
                new ColorItem("Red", Color.web("#FF0000")),
                new ColorItem("Orange", Color.web("#FF7F00")),
                new ColorItem("Yellow", Color.web("#FFD700")),
                new ColorItem("Olive Green", Color.web("#808000")),
                new ColorItem("Lime Green", Color.web("#32CD32")),
                new ColorItem("Mint Green", Color.web("#3EB489")),
                new ColorItem("Green", Color.web("#008000")),
                new ColorItem("Teal", Color.web("#008080")),
                new ColorItem("Sky Blue", Color.web("#87CEEB")),
                new ColorItem("Light Blue", Color.web("#ADD8E6")),
                new ColorItem("Blue", Color.web("#0000FF")),
                new ColorItem("Grape", Color.web("#6F2DA8")),
                new ColorItem("Violet", Color.web("#8A2BE2")),
                new ColorItem("Lavender", Color.web("#E6E6FA")),
                new ColorItem("Magenta", Color.web("#FF00FF")),
                new ColorItem("Salmon", Color.web("#FA8072")),
                new ColorItem("Charcoal", Color.web("#36454F")),
                new ColorItem("Grey", Color.web("#808080")),
                new ColorItem("Taupe", Color.web("#483C32"))
        );
        log.info("data warna berhasil dibuat!");
    }

    public void accountItemSeeder() {
        DataManager.getInstance().getDataAccountItem().setAll(
                new AccountItem(
                        "General",
                        new Image(Objects.requireNonNull(getClass().getResource("/account-type/general.png")).toString())
                ),
                new AccountItem(
                        "Cash",
                        new Image(Objects.requireNonNull(getClass().getResource("/account-type/cash.png")).toString())
                ),
                new AccountItem(
                        "Savings",
                        new Image(Objects.requireNonNull(getClass().getResource("/account-type/savings.png")).toString())
                ),
                new AccountItem(
                        "Credit",
                        new Image(Objects.requireNonNull(getClass().getResource("/account-type/credit.png")).toString())
                )
        );
        log.info("data jenis akun berhasil dibuat!");
    }

    public void currencySeeder() {
        DataManager.getInstance().getDataCurrency().setAll(
                new CurrencyItem("IDR", "Rupiah", "Rp", 0),
                new CurrencyItem("USD", "US Dollar", "$", 2),
                new CurrencyItem("EUR", "Euro", "€", 2)
        );
    }
}