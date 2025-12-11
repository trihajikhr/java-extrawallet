package controller;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
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

    @FXML
    private AnchorPane rootPane;

    @FXML private Button closeButton;
    @FXML private Button incomeBtn;
    @FXML private Button expenseBtn;
    @FXML private Button transferBtn;

    @FXML private ImageView incomeImg;
    @FXML private ImageView expenseImg;
    @FXML private ImageView transferImg;

    @FXML private Label incomeLbl;
    @FXML private Label expenseLbl;
    @FXML private Label transferLbl;

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

        initButtons();
        clearSelection(); // default: semua putih, teks hitam, icon hitam

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
        incomeBtn.getStyleClass().add("choice-btn");
        expenseBtn.getStyleClass().add("choice-btn");
        transferBtn.getStyleClass().add("choice-btn");

        incomeBtn.setOnAction(e -> select(0, incomeBtn, incomeImg, incomeLbl, "#01AA71"));
        expenseBtn.setOnAction(e -> select(1, expenseBtn, expenseImg, expenseLbl, "#F92222"));
        transferBtn.setOnAction(e -> select(2, transferBtn, transferImg, transferLbl, "#0176FE"));
    }

    private void select(int index, Button btn, ImageView img, Label lbl, String color) {

        clearSelection(); // bersihkan semua dulu
        valueChoosen = index + 1;

        // kasih warna ke tombol
        btn.setStyle("-selected-color: " + color + ";");
        btn.getStyleClass().add("choice-btn-selected");

        // label & icon jadi putih
        lbl.setStyle("-fx-text-fill: white;");
        img.setImage(theImage[index][0]); // icon putih
        System.out.println("select: " + valueChoosen);
    }

    private void clearSelection() {

        // reset button
        for (Button b : List.of(incomeBtn, expenseBtn, transferBtn)) {
            b.getStyleClass().remove("choice-btn-selected");
            b.setStyle("");  // hilangkan selected-color
        }

        // reset label
        for (Label l : List.of(incomeLbl, expenseLbl, transferLbl)) {
            l.setStyle("-fx-text-fill: black;");
        }

        // reset icon ke hitam
        incomeImg.setImage(theImage[0][1]);
        expenseImg.setImage(theImage[1][1]);
        transferImg.setImage(theImage[2][1]);
    }

    public int getValueChoosen() {
        return valueChoosen;
    }
}