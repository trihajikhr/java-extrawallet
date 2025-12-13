package controller;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import model.Kategori;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TransactionControl implements Initializable {

    // logger
    private static final Logger log = LoggerFactory.getLogger(TransactionControl.class);

    private Stage stage;
    private int valueChoosen = 0;
    private boolean closing = false;

    @FXML private AnchorPane rootPane;

    @FXML private Button incomeBtn_1;
    @FXML private Button expenseBtn_1;
    @FXML private Button transferBtn_1;

    @FXML private Button incomeBtn_2;
    @FXML private Button expenseBtn_2;
    @FXML private Button transferBtn_2;

    @FXML private ImageView incomeImg;
    @FXML private ImageView expenseImg;
    @FXML private ImageView transferImg_1;
    @FXML private ImageView transferImg_2;

    @FXML private Label incomeLbl;
    @FXML private Label expenseLbl;
    @FXML private Label transferLbl_1;
    @FXML private Label transferLbl_2;

    @FXML private GridPane inoutForm;
    @FXML private GridPane transForm;

    private Image[][] theImage;

    @FXML private ComboBox<Kategori> categoryComboBox;
    @FXML private ComboBox<String> paymentType_1, paymentType_2;
    @FXML private ComboBox<String> paymentStatus_1, paymentStatus_2;

    private final ObservableList<Kategori> allCategories = FXCollections.observableArrayList();
    private final ObservableList<String> paymentTypeData = FXCollections.observableArrayList();
    private final ObservableList<String> paymenttStatusData = FXCollections.observableArrayList();

    private final ObjectProperty<String> selectedPaymentType =
            new SimpleObjectProperty<>();

    private final ObjectProperty<String> selectedPaymentStatus =
            new SimpleObjectProperty<>();

    private final StringProperty noteState = new SimpleStringProperty();
    @FXML private TextField msgNotes_1, msgNotes_2;


    // DIPANGGIL dari controller lain
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void closePopup() {
        if (closing) return;
        closing = true;
        if (stage == null) return;

        FadeTransition fade = new FadeTransition(Duration.millis(150), rootPane);
        fade.setFromValue(1);
        fade.setToValue(0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(150), rootPane);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(0.8);
        scale.setToY(0.8);

        ParallelTransition hideAnim = new ParallelTransition(fade, scale);
        hideAnim.setInterpolator(Interpolator.EASE_BOTH);

        hideAnim.setOnFinished(e -> stage.close());
        hideAnim.play();
    }

    private void updateCategoryCombo(String type) {
        categoryComboBox.getSelectionModel().clearSelection();

        List<Kategori> filtered = allCategories.stream()
                .filter(k -> k.getTipe().equals(type))
                .toList();

        categoryComboBox.setItems(
                FXCollections.observableArrayList(filtered)
        );
    }

    private void initPaymentData() {
        paymentTypeData.addAll(
                "Cash",
                "Debit card",
                "Credit card",
                "Transfer",
                "Voucher",
                "Mobile payment"
        );

        paymentType_1.setItems(paymentTypeData);
        paymentType_2.setItems(paymentTypeData);

        paymenttStatusData.addAll(
                "Reconciled",
                "Cleared",
                "Uncleared"
        );

        paymentStatus_1.setItems(paymenttStatusData);
        paymentStatus_2.setItems(paymenttStatusData);

        paymentType_1.valueProperty().bindBidirectional(selectedPaymentType);
        paymentType_2.valueProperty().bindBidirectional(selectedPaymentType);

        paymentStatus_1.valueProperty().bindBidirectional(selectedPaymentStatus);
        paymentStatus_2.valueProperty().bindBidirectional(selectedPaymentStatus);
    }

    private void messageNotesBinding() {
        msgNotes_1.textProperty().bindBidirectional(noteState);
        msgNotes_2.textProperty().bindBidirectional(noteState);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("Transaksi pop up terbuka");

        // beberapa fungsi init
        initPaymentData();
        messageNotesBinding();

        // load combobox kategori
        allCategories.addAll(
                // IN
                new Kategori(
                        1,
                        "IN",
                        "Salary",
                        new Image(getClass().getResource("/category-icons/1_salary.png").toString()),
                        Color.web("#D0006F")
                ),
                new Kategori(
                        2,
                        "IN",
                        "Allowance",
                        new Image(getClass().getResource("/category-icons/2_allowance.png").toString()),
                        Color.web("#FF0000")
                ),
                new Kategori(
                        3,
                        "IN",
                        "Bonuses",
                        new Image(getClass().getResource("/category-icons/3_bonuses.png").toString()),
                        Color.web("#FF7F00")
                ),
                new Kategori(
                        4,
                        "IN",
                        "Busines",
                        new Image(getClass().getResource("/category-icons/4_business.png").toString()),
                        Color.web("#FFD700")
                ),
                new Kategori(
                        5,
                        "IN",
                        "Freelance/project",
                        new Image(getClass().getResource("/category-icons/5_freelance.png").toString()),
                        Color.web("#808000")
                ),
                new Kategori(
                        6,
                        "IN",
                        "Sales",
                        new Image(getClass().getResource("/category-icons/6_sales.png").toString()),
                        Color.web("#32CD32")
                ),
                new Kategori(
                        7,
                        "IN",
                        "Dividends",
                        new Image(getClass().getResource("/category-icons/7_dividends.png").toString()),
                        Color.web("#3EB489")
                ),
                new Kategori(
                        8,
                        "IN",
                        "Investment Gains",
                        new Image(getClass().getResource("/category-icons/8_investment.png").toString()),
                        Color.web("#008000")
                ),
                new Kategori(
                        9,
                        "IN",
                        "Incoming Transfer",
                        new Image(getClass().getResource("/category-icons/9_incoming-transfer.png").toString()),
                        Color.web("#3EB489")
                ),
                new Kategori(
                        10,
                        "IN",
                        "Gift",
                        new Image(getClass().getResource("/category-icons/10_gift.png").toString()),
                        Color.web("#008080") // Teal
                ),
                new Kategori(
                        11,
                        "IN",
                        "Cashback",
                        new Image(getClass().getResource("/category-icons/11_cashback.png").toString()),
                        Color.web("#87CEEB") // Sky Blue
                ),
                new Kategori(
                        12,
                        "IN",
                        "Commission",
                        new Image(getClass().getResource("/category-icons/12_commission.png").toString()),
                        Color.web("#ADD8E6") // Light Blue
                ),
                new Kategori(
                        13,
                        "IN",
                        "Royalty",
                        new Image(getClass().getResource("/category-icons/13_royalty.png").toString()),
                        Color.web("#0000FF") // Blue
                ),
                new Kategori(
                        14,
                        "IN",
                        "App Reward",
                        new Image(getClass().getResource("/category-icons/14_app-reward.png").toString()),
                        Color.web("#6F2DA8") // Grape
                ),
                new Kategori(
                        15,
                        "IN",
                        "Others",
                        new Image(getClass().getResource("/category-icons/15_others.png").toString()),
                        Color.web("#62718a") // Violet
                ),

                // OUT
                new Kategori(
                        16,
                        "OUT",
                        "Food & Beverages",
                        new Image(getClass().getResource("/category-icons/16_food.png").toString()),
                        Color.web("#D0006F") // Berry Red
                ),
                new Kategori(
                        17,
                        "OUT",
                        "Daily Shopping",
                        new Image(getClass().getResource("/category-icons/17_shopping.png").toString()),
                        Color.web("#FF0000") // Red
                ),
                new Kategori(
                        18,
                        "OUT",
                        "Transportation",
                        new Image(getClass().getResource("/category-icons/18_transportation.png").toString()),
                        Color.web("#FF7F00") // Orange
                ),
                new Kategori(
                        19,
                        "OUT",
                        "Bills & Utilities",
                        new Image(getClass().getResource("/category-icons/19_bills.png").toString()),
                        Color.web("#FFD700") // Yellow
                ),
                new Kategori(
                        20,
                        "OUT",
                        "Personal Shopping",
                        new Image(getClass().getResource("/category-icons/13_royalty.png").toString()),
                        Color.web("#808000") // Olive Green
                ),
                new Kategori(
                        21,
                        "OUT",
                        "Gadgets & Electronics",
                        new Image(getClass().getResource("/category-icons/20_gadgets.png").toString()),
                        Color.web("#32CD32") // Lime Green
                ),
                new Kategori(
                        22,
                        "OUT",
                        "Health",
                        new Image(getClass().getResource("/category-icons/21_health.png").toString()),
                        Color.web("#3EB489") // Mint Green
                ),
                new Kategori(
                        23,
                        "OUT",
                        "Entertainment & Lifestyle",
                        new Image(getClass().getResource("/category-icons/22_entertainment.png").toString()),
                        Color.web("#008000") // Green
                ),
                new Kategori(
                        24,
                        "OUT",
                        "Education & Courses",
                        new Image(getClass().getResource("/category-icons/23_education.png").toString()),
                        Color.web("#008080") // Teal
                ),
                new Kategori(
                        25,
                        "OUT",
                        "Financial Obligations",
                        new Image(getClass().getResource("/category-icons/24_obligation.png").toString()),
                        Color.web("#87CEEB") // Sky Blue
                ),
                new Kategori(
                        26,
                        "OUT",
                        "Home & Appliances",
                        new Image(getClass().getResource("/category-icons/25_home.png").toString()),
                        Color.web("#ADD8E6") // Light Blue
                ),
                new Kategori(
                        27,
                        "OUT",
                        "Family",
                        new Image(getClass().getResource("/category-icons/28_family.png").toString()),
                        Color.web("#0000FF") // Blue
                ),
                new Kategori(
                        28,
                        "OUT",
                        "Gift",
                        new Image(getClass().getResource("/category-icons/10_gift.png").toString()),
                        Color.web("#6F2DA8") // Grape
                ),
                new Kategori(
                        29,
                        "OUT",
                        "Donation",
                        new Image(getClass().getResource("/category-icons/27_donation.png").toString()),
                        Color.web("#8A2BE2") // Violet
                ),
                new Kategori(
                        30,
                        "OUT",
                        "Others",
                        new Image(getClass().getResource("/category-icons/15_others.png").toString()),
                        Color.web("#62718a") // Lavender
                )
        );
        categoryComboBox.setItems(allCategories);

        categoryComboBox.setCellFactory(list -> new ListCell<Kategori>() {
            @Override
            protected void updateItem(Kategori item, boolean empty) {
                super.updateItem(item, empty);

                if(empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                // icon
                ImageView iconView = new ImageView(item.getIcon());
                iconView.setFitWidth(14);
                iconView.setFitHeight(14);
                iconView.setPreserveRatio(true);

                // background
                StackPane iconBox = new StackPane(iconView);
                iconBox.setPrefSize(28,28);;
                iconBox.setMaxSize(28,28);

                iconBox.setBackground(new Background(
                        new BackgroundFill(
                                item.getWarna(),
                                new CornerRadii(8),
                                Insets.EMPTY
                        )
                ));

                // teks
                Label label = new Label(item.getLabel());
                label.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

                // gabung
                HBox box = new HBox(10, iconBox, label);
                box.setAlignment(Pos.CENTER_LEFT);

                setGraphic(box);
            }
        });
        categoryComboBox.setButtonCell(categoryComboBox.getCellFactory().call(null));

        // ======================================================================================

        // Load icon: [0] = putih, [1] = hitam
        theImage = new Image[][] {
                {
                        new Image(getClass().getResource("/icons/incomeW.png").toString()),
                        new Image(getClass().getResource("/icons/incomeB.png").toString())
                },
                {
                        new Image(getClass().getResource("/icons/expenseW.png").toString()),
                        new Image(getClass().getResource("/icons/expenseB.png").toString())
                },
                {
                        new Image(getClass().getResource("/icons/transferW.png").toString()),
                        new Image(getClass().getResource("/icons/transferB.png").toString())
                }
        };

        inoutForm.setVisible(true);   // default
        transForm.setVisible(false);

        initButtons();
        clearSelection(1); // default: semua putih, teks hitam, icon hitam

        rootPane.setOpacity(0);

        rootPane.setScaleX(0.8);
        rootPane.setScaleY(0.8);
        rootPane.setOpacity(0);

        FadeTransition fade = new FadeTransition(Duration.millis(200), rootPane);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(200), rootPane);
        scale.setFromX(0.8);
        scale.setFromY(0.8);
        scale.setToX(1);
        scale.setToY(1);

        ParallelTransition showAnim = new ParallelTransition(fade, scale);
        showAnim.setInterpolator(Interpolator.EASE_BOTH);
        showAnim.play();
    }

    private void initButtons() {

        // Set class dasar
        incomeBtn_1.getStyleClass().add("choice-btn");
        expenseBtn_1.getStyleClass().add("choice-btn");
        transferBtn_1.getStyleClass().add("choice-btn");

        // incomeBtn_1.setOnAction(e -> select(1,0, incomeBtn_1, incomeImg, incomeLbl, "#01AA71"));
        // expenseBtn_1.setOnAction(e -> select(1,1, expenseBtn_1, expenseImg, expenseLbl, "#F92222"));

        incomeBtn_1.setOnAction(e -> {
            select(1,0, incomeBtn_1, incomeImg, incomeLbl, "#01AA71");
            updateCategoryCombo("IN");
        });
        expenseBtn_1.setOnAction(e -> {
            select(1,1, expenseBtn_1, expenseImg, expenseLbl, "#F92222");
            updateCategoryCombo("OUT");
        });
        transferBtn_1.setOnAction(e -> select(1,2, transferBtn_2, transferImg_2, transferLbl_2, "#0176FE"));

        incomeBtn_2.setOnAction(e -> select(2,0, incomeBtn_1, incomeImg, incomeLbl, "#01AA71"));
        expenseBtn_2.setOnAction(e -> select(2,1, expenseBtn_1, expenseImg, expenseLbl, "#F92222"));
        transferBtn_2.setOnAction(e -> select(2,2, transferBtn_2, transferImg_2, transferLbl_2, "#0176FE"));
    }

    private void showInOut() {
        inoutForm.setVisible(true);
        transForm.setVisible(false);

        inoutForm.toFront();
    }

    private void showTransfer() {
        transForm.setVisible(true);
        inoutForm.setVisible(false);

        transForm.toFront();
    }

    private void select(int layer, int index, Button btn, ImageView img, Label lbl, String color) {

        if(layer == 1) {
            if(index != 2) {
                clearSelection(layer);
            } else {
                clearSelection(2);
                showTransfer();
            }

            valueChoosen = index + 1;

            // kasih warna ke tombol
            btn.setStyle("-selected-color: " + color + ";");
            btn.getStyleClass().add("choice-btn-selected");

            // label & icon jadi putih
            lbl.setStyle("-fx-text-fill: white;");
            img.setImage(theImage[index][0]); // icon putih

        } else if(layer == 2) {
            if(index == 2) return;
            showInOut();
            clearSelection(1);
            valueChoosen = index + 1;

            // kasih warna ke tombol
            btn.setStyle("-selected-color: " + color + ";");
            btn.getStyleClass().add("choice-btn-selected");

            // label & icon jadi putih
            lbl.setStyle("-fx-text-fill: white;");
            img.setImage(theImage[index][0]); // icon putih
        }
        System.out.println("select: " + valueChoosen);
    }

    private void clearSelection(int layer) {

        if(layer == 1){
            // reset button
            for (Button b : List.of(incomeBtn_1, expenseBtn_1, transferBtn_1)) {
                b.getStyleClass().remove("choice-btn-selected");
                b.setStyle("");  // hilangkan selected-color
            }

            // reset label
            for (Label l : List.of(incomeLbl, expenseLbl, transferLbl_1)) {
                l.setStyle("-fx-text-fill: black;");
            }

            // reset icon ke hitam
            incomeImg.setImage(theImage[0][1]);
            expenseImg.setImage(theImage[1][1]);
            transferImg_1.setImage(theImage[2][1]);

        } else if(layer == 2) {
            // reset button
            for (Button b : List.of(incomeBtn_2, expenseBtn_2, transferBtn_2)) {
                b.getStyleClass().remove("choice-btn-selected");
                b.setStyle("");  // hilangkan selected-color
            }

            transferLbl_2.setStyle("-fx-text-fill: black;");
            transferImg_2.setImage(theImage[2][1]);
        }
    }

    public int getValueChoosen() {
        return valueChoosen;
    }

    // OPSI COMBO BOX EDITABLE!
//        accountComboBox.setEditable(true);
//        ObservableList<AccountItem> allItems =
//                FXCollections.observableArrayList(accountComboBox.getItems());
//
//        accountComboBox.getEditor().textProperty().addListener((obs, oldText, newText) -> {
//            if (newText == null) return;
//
//            String keyword = newText.toLowerCase();
//
//            List<AccountItem> filtered = allItems.stream()
//                    .filter(item -> item.getLabel().toLowerCase().contains(keyword))
//                    .toList();
//
//            accountComboBox.getItems().setAll(filtered);
//            accountComboBox.show();
//        });
//
//        accountComboBox.setConverter(new StringConverter<>() {
//            @Override
//            public String toString(AccountItem item) {
//                return item == null ? "" : item.getLabel();
//            }
//
//            @Override
//            public AccountItem fromString(String string) {
//                return null; // nggak dipakai
//            }
//        });
}