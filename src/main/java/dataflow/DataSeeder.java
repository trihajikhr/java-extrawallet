package dataflow;

import dataflow.basedata.AccountItem;
import dataflow.basedata.ColorItem;
import model.Category;
import model.Currency;
import helper.Converter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.enums.PaymentStatus;
import model.enums.PaymentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private DataSeeder() {}

    // [1] >=== SEED LOCAL DATA
    public static ObservableList<Category> seedKategori() {
        ObservableList<Category> data = FXCollections.observableArrayList();

        try {
            data.addAll(
                    // IN
                    new Category(
                            1,
                            "IN",
                            "Salary",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/1_salary.png")).toString()),
                            "/category-icons/1_salary.png",
                            Color.web("#D0006F")
                    ),
                    new Category(
                            2,
                            "IN",
                            "Allowance",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/2_allowance.png")).toString()),
                            "/category-icons/2_allowance.png",
                            Color.web("#FF0000")
                    ),
                    new Category(
                            3,
                            "IN",
                            "Bonuses",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/3_bonuses.png")).toString()),
                            "/category-icons/3_bonuses.png",
                            Color.web("#FF7F00")
                    ),
                    new Category(
                            4,
                            "IN",
                            "Busines",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/4_business.png")).toString()),
                            "/category-icons/4_business.png",
                            Color.web("#FFD700")
                    ),
                    new Category(
                            5,
                            "IN",
                            "Freelance/project",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/5_freelance.png")).toString()),
                            "/category-icons/5_freelance.png",
                            Color.web("#808000")
                    ),
                    new Category(
                            6,
                            "IN",
                            "Sales",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/6_sales.png")).toString()),
                            "/category-icons/6_sales.png",
                            Color.web("#32CD32")
                    ),
                    new Category(
                            7,
                            "IN",
                            "Dividends",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/7_dividends.png")).toString()),
                            "/category-icons/7_dividends.png",
                            Color.web("#3EB489")
                    ),
                    new Category(
                            8,
                            "IN",
                            "Investment Gains",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/8_investment.png")).toString()),
                            "/category-icons/8_investment.png",
                            Color.web("#008000")
                    ),
                    new Category(
                            9,
                            "IN",
                            "Incoming Transfer",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/9_incoming-transfer.png")).toString()),
                            "/category-icons/9_incoming-transfer.png",
                            Color.web("#3EB489")
                    ),
                    new Category(
                            10,
                            "IN",
                            "Gift",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/10_gift.png")).toString()),
                            "/category-icons/10_gift.png",
                            Color.web("#008080")
                    ),
                    new Category(
                            11,
                            "IN",
                            "Cashback",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/11_cashback.png")).toString()),
                            "/category-icons/11_cashback.png",
                            Color.web("#87CEEB")
                    ),
                    new Category(
                            12,
                            "IN",
                            "Commission",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/12_commission.png")).toString()),
                            "/category-icons/12_commission.png",
                            Color.web("#ADD8E6")
                    ),
                    new Category(
                            13,
                            "IN",
                            "Royalty",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/13_royalty.png")).toString()),
                            "/category-icons/13_royalty.png",
                            Color.web("#0000FF")
                    ),
                    new Category(
                            14,
                            "IN",
                            "App Reward",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/14_app-reward.png")).toString()),
                            "/category-icons/14_app-reward.png",
                            Color.web("#6F2DA8")
                    ),
                    new Category(
                            15,
                            "IN",
                            "Others",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/15_others.png")).toString()),
                            "/category-icons/15_others.png",
                            Color.web("#62718a")
                    ),

                    // OUT
                    new Category(
                            16,
                            "OUT",
                            "Food & Beverages",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/16_food.png")).toString()),
                            "/category-icons/16_food.png",
                            Color.web("#D0006F")
                    ),
                    new Category(
                            17,
                            "OUT",
                            "Daily Shopping",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/17_shopping.png")).toString()),
                            "/category-icons/17_shopping.png",
                            Color.web("#FF0000")
                    ),
                    new Category(
                            18,
                            "OUT",
                            "Transportation",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/18_transportation.png")).toString()),
                            "/category-icons/18_transportation.png",
                            Color.web("#FF7F00")
                    ),
                    new Category(
                            19,
                            "OUT",
                            "Bills & Utilities",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/19_bills.png")).toString()),
                            "/category-icons/19_bills.png",
                            Color.web("#FFD700")
                    ),
                    new Category(
                            20,
                            "OUT",
                            "Personal Shopping",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/13_royalty.png")).toString()),
                            "/category-icons/13_royalty.png",
                            Color.web("#808000")
                    ),
                    new Category(
                            21,
                            "OUT",
                            "Gadgets & Electronics",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/20_gadgets.png")).toString()),
                            "/category-icons/20_gadgets.png",
                            Color.web("#32CD32")
                    ),
                    new Category(
                            22,
                            "OUT",
                            "Health",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/21_health.png")).toString()),
                            "/category-icons/21_health.png",
                            Color.web("#3EB489")
                    ),
                    new Category(
                            23,
                            "OUT",
                            "Entertainment & Lifestyle",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/22_entertainment.png")).toString()),
                            "/category-icons/22_entertainment.png",
                            Color.web("#008000")
                    ),
                    new Category(
                            24,
                            "OUT",
                            "Education & Courses",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/23_education.png")).toString()),
                            "/category-icons/23_education.png",
                            Color.web("#008080")
                    ),
                    new Category(
                            25,
                            "OUT",
                            "Financial Obligations",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/24_obligation.png")).toString()),
                            "/category-icons/24_obligation.png",
                            Color.web("#87CEEB")
                    ),
                    new Category(
                            26,
                            "OUT",
                            "Home & Appliances",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/25_home.png")).toString()),
                            "/category-icons/25_home.png",
                            Color.web("#ADD8E6")
                    ),
                    new Category(
                            27,
                            "OUT",
                            "Family",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/28_family.png")).toString()),
                            "/category-icons/28_family.png",
                            Color.web("#0000FF")
                    ),
                    new Category(
                            28,
                            "OUT",
                            "Gift",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/10_gift.png")).toString()),
                            "/category-icons/10_gift.png",
                            Color.web("#6F2DA8")
                    ),
                    new Category(
                            29,
                            "OUT",
                            "Donation",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/27_donation.png")).toString()),
                            "/category-icons/27_donation.png",
                            Color.web("#8A2BE2")
                    ),
                    new Category(
                            30,
                            "OUT",
                            "Others",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/15_others.png")).toString()),
                            "/category-icons/15_others.png",
                            Color.web("#62718a")
                    ), new Category(
                            31,
                        "IN",
                            "Transfer, withdraw",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/transfer_extra.png")).toString()),
                            "/category-icons/transfer_extra.png",
                            Color.web("#32CD32")
                    ), new Category(
                            32,
                            "OUT",
                            "Transfer, withdraw",
                            new Image(Objects.requireNonNull(DataSeeder.class.getResource("/category-icons/transfer_extra.png")).toString()),
                            "/category-icons/transfer_extra.png",
                            Color.web("#32CD32")
                    )
            );
            log.info("pembuatan data category berhasil!");
            return data;

        } catch (Exception e) {
            log.error("terjadi kesalahan: ", e);
            return null;
        }
    }
    public static ObservableList<PaymentType> seedPaymentType() {
        log.info("data payment type berhasil diseed!");
        return FXCollections.observableArrayList(PaymentType.values());
    }
    public static ObservableList<PaymentStatus> seedPaymentStatus() {
        log.info("data payment status berhasil diseed!");
        return FXCollections.observableArrayList(PaymentStatus.values());
    }
    public static void seedColor() {
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
    public static void seedAccountItem() {
        DataManager.getInstance().getDataAccountItem().setAll(
                new AccountItem(
                        "General",
                        new Image(Objects.requireNonNull(DataSeeder.class.getResource("/account-type/general.png")).toString()),
                        "/account-type/general.png"
                ),
                new AccountItem(
                        "Cash",
                        new Image(Objects.requireNonNull(DataSeeder.class.getResource("/account-type/cash.png")).toString()),
                        "/account-type/cash.png"
                ),
                new AccountItem(
                        "Savings",
                        new Image(Objects.requireNonNull(DataSeeder.class.getResource("/account-type/savings.png")).toString()),
                        "/account-type/savings.png"
                ),
                new AccountItem(
                        "Credit",
                        new Image(Objects.requireNonNull(DataSeeder.class.getResource("/account-type/credit.png")).toString()),
                        "/account-type/credit.png"
                )
        );
        log.info("data jenis account berhasil dibuat!");
    }
    public static void seedCurrency() {
        DataManager.getInstance().getDataMataUang().setAll(
                new Currency(1, "IDR", "Rupiah", "Rp", 0),
                new Currency(2, "USD", "US Dollar", "$", 2),
                new Currency(3, "EUR", "Euro", "€", 2)
        );
        log.info("data currency lokal berhasil dibuat!");
    }
    public static Image[][] seedImageTransactionForm() {
        Image[][] theImage = new Image[][] {
                {
                        new Image(Objects.requireNonNull(DataSeeder.class.getResource("/icons/incomeW.png")).toString()),
                        new Image(Objects.requireNonNull(DataSeeder.class.getResource("/icons/incomeB.png")).toString())
                },
                {
                        new Image(Objects.requireNonNull(DataSeeder.class.getResource("/icons/expenseW.png")).toString()),
                        new Image(Objects.requireNonNull(DataSeeder.class.getResource("/icons/expenseB.png")).toString())
                },
                {
                        new Image(Objects.requireNonNull(DataSeeder.class.getResource("/icons/transferW.png")).toString()),
                        new Image(Objects.requireNonNull(DataSeeder.class.getResource("/icons/transferB.png")).toString())
                }
        };
        log.info("icon form transaksi berhasil di load");
        return theImage;
    }
    public static Map<PaymentStatus, Image> seedPaymentStatusImage() {
        try {
            Map<PaymentStatus, Image> data = new HashMap<>();
            data.put(
                    PaymentStatus.RECONCILED,
                    new Image(Objects.requireNonNull(DataSeeder.class.getResource("/icons/reconciled.png")).toString())
            );

            data.put(
                    PaymentStatus.CLEARED,
                    new Image(Objects.requireNonNull(DataSeeder.class.getResource("/icons/cleared.png")).toString())
            );

            data.put(
                    PaymentStatus.UNCLEARED,
                    new Image(Objects.requireNonNull(DataSeeder.class.getResource("/icons/uncleared.png")).toString())
            );

            log.info("payment status icon berhasil terload");
            return data;
        } catch (Exception e) {
            log.error("terjadi kesalahan: ", e);
            return null;
        }
     }

    // [2] >=== SEED DATA DATABASE
    public static void seedDatabaseKategori() {
        Database dataConnect = Database.getInstance();
        String querySql = "INSERT OR IGNORE INTO category (id, tipe, nama, icon_path, warna) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stat = dataConnect.getConnection().prepareStatement(querySql)) {
            for(Category ktr : DataManager.getInstance().getDataKategori()) {
                stat.setInt(1, ktr.getId());
                stat.setString(2, ktr.getType());
                stat.setString(3, ktr.getName());
                stat.setString(4, ktr.getIconPath());
                stat.setString(5, Converter.colorToHex(ktr.getColor()));

                stat.executeUpdate();
            }
            log.info("data category berhasil di seed ke database!");

        } catch (SQLException e) {
            log.error("gagal seed data category: ", e);
        }
    }
    public static void seedDatabaseCurrency() {
        Database dataConnect = Database.getInstance();
        String querySql = "INSERT OR IGNORE INTO mata_uang(id, kode, nama, simbol, desimal) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stat = dataConnect.getConnection().prepareStatement(querySql)) {
            for(Currency mk : DataManager.getInstance().getDataMataUang()) {
                stat.setInt(1, mk.getId());
                stat.setString(2, mk.getCode());
                stat.setString(3, mk.getName());
                stat.setString(4, mk.getSymbol());
                stat.setInt(5, mk.getDecimal());

                stat.executeUpdate();
            }
            log.info("table mata_uang berhasil di seed ke database!");

        } catch (Exception e) {
            log.error("data table mata_uang gagal diseed: ", e);
        }
    }
}