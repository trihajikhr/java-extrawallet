package controller;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TransactionControl implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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

        incomeBtn_1.setOnAction(e -> select(1,0, incomeBtn_1, incomeImg, incomeLbl, "#01AA71"));
        expenseBtn_1.setOnAction(e -> select(1,1, expenseBtn_1, expenseImg, expenseLbl, "#F92222"));
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