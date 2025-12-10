package controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DashboardControl {

    private boolean isExpanded = false;
    private final double expandedWidth = 200;
    private final double collapsedWidth = 55;
    private Label[] navLabel;

    @FXML private VBox navbarContainer;
    @FXML private Button toggleButton;
    @FXML private AnchorPane corePane;

    @FXML private HBox toggleHBox;
    @FXML private Label toggleIconLabel;
    @FXML private ImageView toggleIcon;

    @FXML private Image imgOpen;
    @FXML private Image imgClose;

    @FXML private Label item1Label;
    @FXML private Label item2Label;
    @FXML private Label item3Label;
    @FXML private Label item4Label;
    @FXML private Label item5Label;

    @FXML private void home(ActionEvent e){loadPage("home");}
    @FXML private void income(ActionEvent e){loadPage("income");}
    @FXML private void expense(ActionEvent e){loadPage("expense");}
    @FXML private void statistic(ActionEvent e){loadPage("statistic");}
    @FXML private void report(ActionEvent e){loadPage("report");}

//    @FXML
//    private void addTransaction(MouseEvent event) throws IOException {
//        PopupUtils.showPopup("/fxml/transaction.fxml", (Node) event.getSource());
//    }

    @FXML
    private void toggleNavbar() {
        isExpanded = !isExpanded;

        double targetWidth = isExpanded ? expandedWidth : collapsedWidth;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(200), // Durasi animasi
                        new KeyValue(navbarContainer.prefWidthProperty(), targetWidth)
                )
        );

        if (isExpanded) {
            // Tampilkan teks dan ubah ikon setelah animasi selesai
            timeline.setOnFinished(event -> {
                setTextsVisible(true);
                toggleIcon.setImage(imgOpen);
            });
            timeline.play();
        } else {
            // Sembunyikan teks sebelum animasi dimulai
            setTextsVisible(false);
            toggleIcon.setImage(imgClose);
            timeline.play();
        }
    }

    private void setTextsVisible(boolean visible) {
        for (Label text : navLabel) {
            text.setVisible(visible);
            text.setManaged(visible);
        }
    }

    @FXML
    public void initialize() {
        navLabel = new Label[]{item1Label, item2Label, item3Label, item4Label, item5Label};

        imgOpen  = new Image(getClass().getResource("/icons/left-arrow.png").toString());
        imgClose   = new Image(getClass().getResource("/icons/menu.png").toString());

        isExpanded = false;
        navbarContainer.setPrefWidth(collapsedWidth);
        setTextsVisible(false);
        toggleIcon.setImage(imgClose);
    }

    @FXML
    public void loadPage(String page) {
        try {
            FXMLLoader loadder = new FXMLLoader(getClass().getResource("/fxml/" + page + ".fxml"));
            Parent root = loadder.load();
            corePane.getChildren().setAll(root);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}