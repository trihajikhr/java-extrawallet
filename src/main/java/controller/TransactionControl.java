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

    private List<Button> theButtons;
    private List<ImageView> theImageView;
    private List<Label> theLabel;
    private Image[][] theImage;
    private List<String> theStyle;

    private final String BUTTON_BACKGROUND_DEFAULT = "-fx-background-color: #FFFFFF;";
    private final String LABEL_COLOR_BLACK = "-fx-text-fill: #000000;";
    private final String LABEL_COLOR_WHITE = "-fx-text-fill: #FFFFFF;";
    private final int SIZE = 3;

    // ini dipanggil dari main controller saat load FXML
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void closePopup() {
        if (stage != null) {
            stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        theButtons = List.of(incomeBtn, expenseBtn, transferBtn);
        theImageView = List.of(incomeImg, expenseImg, transferImg);
        theLabel = List.of(incomeLbl, expenseLbl, transferLbl);
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
        theStyle = List.of("#01AA71", "#F92222", "#0176FE");
        setupToggleButtons();
    }

    private void buttonChoosen(int t) {
        valueChoosen = t + 1;

        theImageView.get(t).setImage(theImage[t][0]);
        theButtons.get(t).setStyle("-fx-background-color:" + theStyle.get(t) + ";");
        theLabel.get(t).setStyle(LABEL_COLOR_WHITE);
    }

    private void clearAllButton(int t) {
        for(int i = 0; i < SIZE; i++) {
            if(theButtons.get(i) == theButtons.get(t)) continue;
            theButtons.get(i).setStyle(BUTTON_BACKGROUND_DEFAULT);
            theLabel.get(i).setStyle(LABEL_COLOR_BLACK);
            theImageView.get(i).setImage(theImage[i][1]);
        }
    }

    private void setupToggleButtons() {
        for (int i = 0; i < SIZE; i++) {
            final int index = i;
            theButtons.get(i).setOnAction(e -> {
                clearAllButton(index);
                buttonChoosen(index);
                System.out.println("Selected: " + valueChoosen); // DEBUGGER
            });
        }
    }

    public int getValueChoosen() {
        return valueChoosen;
    }
}