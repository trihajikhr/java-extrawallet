package controller;

import dataflow.DataManager;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import model.Akun;
import model.Kategori;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TransactionControl implements Initializable {

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

    @FXML private ComboBox<Kategori> categoryComboBox;
    @FXML private ComboBox<String> paymentType_1, paymentType_2;
    @FXML private ComboBox<String> paymentStatus_1, paymentStatus_2;
    @FXML private ComboBox<Akun> akunComboBox_inout, akunComboBox_from, akunComboBox_to;

    private final ObjectProperty<String> selectedPaymentType =
            new SimpleObjectProperty<>();

    private final ObjectProperty<String> selectedPaymentStatus =
            new SimpleObjectProperty<>();

    private final StringProperty noteState = new SimpleStringProperty();
    @FXML private TextField msgNotes_1, msgNotes_2;

    @FXML private Image[][] theImage;

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

        List<Kategori> filtered = DataManager.getInstance().getDataKategori().stream()
                .filter(k -> k.getTipe().equals(type))
                .toList();

        categoryComboBox.setItems(
                FXCollections.observableArrayList(filtered)
        );
    }

    private void initPaymentData() {
        paymentType_1.setItems(DataManager.getInstance().getDataPeymentType());
        paymentType_2.setItems(DataManager.getInstance().getDataPeymentType());

        paymentStatus_1.setItems(DataManager.getInstance().getDataStatusType());
        paymentStatus_2.setItems(DataManager.getInstance().getDataStatusType());

        paymentType_1.valueProperty().bindBidirectional(selectedPaymentType);
        paymentType_2.valueProperty().bindBidirectional(selectedPaymentType);

        paymentStatus_1.valueProperty().bindBidirectional(selectedPaymentStatus);
        paymentStatus_2.valueProperty().bindBidirectional(selectedPaymentStatus);
    }

    private void messageNotesBinding() {
        msgNotes_1.textProperty().bindBidirectional(noteState);
        msgNotes_2.textProperty().bindBidirectional(noteState);
    }

    private void loadCategoryComboBox(){
        ArrayList<Kategori> listKategori = DataManager.getInstance().getDataKategori();
        categoryComboBox.setItems(FXCollections.observableArrayList(listKategori));

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
                Label label = new Label(item.getNama());
                label.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

                // gabung
                HBox box = new HBox(10, iconBox, label);
                box.setAlignment(Pos.CENTER_LEFT);

                setGraphic(box);
            }
        });
        categoryComboBox.setButtonCell(categoryComboBox.getCellFactory().call(null));
    }

    private void loadAkunComboBox(ComboBox<Akun> dataAkunComboBox) {
        ArrayList<Akun> dataAkun = DataManager.getInstance().getDataAkun();
        dataAkunComboBox.setItems(FXCollections.observableArrayList(dataAkun));

        dataAkunComboBox.setCellFactory(list -> new ListCell<Akun>() {
            @Override
            protected void updateItem(Akun item, boolean empty) {
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
                Label label = new Label(item.getNama());
                label.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

                // gabung
                HBox box = new HBox(10, iconBox, label);
                box.setAlignment(Pos.CENTER_LEFT);

                setGraphic(box);
            }
        });
        dataAkunComboBox.setButtonCell(dataAkunComboBox.getCellFactory().call(null));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("Transaksi pop up terbuka");

        // beberapa fungsi init
        initPaymentData();
        messageNotesBinding();

        // load data combobox
        loadCategoryComboBox();
        loadAkunComboBox(akunComboBox_inout);
        loadAkunComboBox(akunComboBox_from);
        loadAkunComboBox(akunComboBox_to);

        theImage = DataManager.getInstance().getImageTransactionForm();

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
        System.out.println("user memilih: " + (valueChoosen == 1 ? "income" : valueChoosen == 2 ? "expense" : "transfer"));
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
            transferImg_2.setImage(DataManager.getInstance().getImageTransactionForm()[2][1]);
        }
    }

    public int getValueChoosen() {
        return valueChoosen;
    }
}