package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TransactionControl implements Initializable {

    private Stage stage;
    private int valueChoosen = 0;

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
        if (stage != null) stage.close();
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