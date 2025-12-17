package controller.transaction;

import dataflow.DataManager;
import helper.Popup;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Akun;
import model.Kategori;
import model.MataUang;
import model.TipeLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TemplateControl implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(TemplateControl.class);
    private Stage stage;
    private boolean closing = false;
    IntegerProperty valueChoosen = new SimpleIntegerProperty(0);

    private double xOffset = 0;
    private double yOffset = 0;

    private TransactionControl parentController;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField nameText;

    // button blok switcher
    @FXML private Button incomeBtn, expenseBtn;
    @FXML private Label incomeLabel, expenseLabel;
    @FXML private Image[][] theImage;
    @FXML private ImageView incomeImg;
    @FXML private ImageView expenseImg;

    @FXML
    private Spinner<Integer> spinnerAmount;

    @FXML
    private ComboBox<MataUang> mataUangComboBox;

    @FXML
    private ComboBox<Akun> akunComboBox;

    @FXML
    private ComboBox<Kategori> kategoriComboBox;

    @FXML
    private ComboBox<TipeLabel> tipeLabelComboBox;

    @FXML
    private TextField noteText;

    @FXML
    private ComboBox<String> paymentType, paymentStatus;

    @FXML
    private Button addLabelBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        log.info("template popup terbuka");
        showPopup();
        theImage = DataManager.getInstance().getImageTransactionForm();
    }

    // DIPANGGIL dari controller pemanggil
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setParentController(TransactionControl parent) {
        this.parentController = parent;
    }

    @FXML
    public void showPopup() {
        if (stage == null) return;

        rootPane.setOpacity(0);
        rootPane.setScaleX(0.6);
        rootPane.setScaleY(0.6);

        FadeTransition fade = new FadeTransition(Duration.millis(250), rootPane);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(250), rootPane);
        scale.setFromX(0.6);
        scale.setFromY(0.6);
        scale.setToX(1);
        scale.setToY(1);

        ParallelTransition pt = new ParallelTransition(fade, scale);
        pt.setInterpolator(Interpolator.EASE_OUT);
        pt.play();
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

    @FXML
    private void addLabelOnTemplate(ActionEvent evt) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/label.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();

            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);

            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(10.0);
            dropShadow.setOffsetX(5.0);
            dropShadow.setOffsetY(5.0);
            dropShadow.setColor(Color.rgb(0, 0, 0, 0.4));
            root.setEffect(dropShadow);

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);

//        stage.setMinWidth(450);
//        stage.setMinHeight(560);
//        stage.setMaxWidth(800);
//        stage.setMaxHeight(700);

            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            LabelControl ctrl = loader.getController();
            ctrl.setStage(stage);
            ctrl.setParentTemplate(this);

            stage.showAndWait();

        } catch (IOException e) {
            log.error("gagal membuka panel tambah label!", e);
            Popup.showDanger("Gagal!", "Terjadi kesalahan!");
        }
    }

    private void initButtons() {
        incomeBtn.getStyleClass().add("choice-btn");
        expenseBtn.getStyleClass().add("choice-btn");

        incomeBtn.setOnAction(e -> activateIncome());
        expenseBtn.setOnAction(e -> {
            select(1, expenseBtn, expenseImg, expenseLabel, "#F92222");
            updateCategoryCombo("OUT");
        });
    }

    private void activateIncome() {
        select(0, incomeBtn, incomeImg, incomeLabel, "#01AA71");
        updateCategoryCombo("IN");
    }

    private void updateCategoryCombo(String type) {
        kategoriComboBox.getSelectionModel().clearSelection();

        List<Kategori> filtered = DataManager.getInstance().getDataKategori().stream()
                .filter(k -> k.getTipe().equals(type))
                .toList();

        kategoriComboBox.setItems(
                FXCollections.observableArrayList(filtered)
        );
    }

    private void select(int index, Button btn, ImageView img, Label lbl, String color) {
        clearSelection();
        valueChoosen.setValue(index + 1);
        // kasih warna ke tombol
        btn.setStyle("-selected-color: " + color + ";");
        btn.getStyleClass().add("choice-btn-selected");

        // label & icon jadi putih
        lbl.setStyle("-fx-text-fill: white;");
        img.setImage(theImage[index][0]);
        System.out.println("user memilih: " + (valueChoosen.getValue() == 1 ? "income" : valueChoosen.getValue() == 2 ? "expense" : "transfer"));
    }

    private void clearSelection() {
        // button
        incomeBtn.getStyleClass().remove("choice-btn-selected");
        incomeBtn.setStyle("");

        expenseBtn.getStyleClass().remove("choice-btn-selected");
        expenseBtn.setStyle("");

        // label
        incomeLabel.setStyle("-fx-text-fill: black;");
        expenseLabel.setStyle("-fx-text-fill: black;");

        // reset icon ke hitam
        incomeImg.setImage(theImage[0][1]);
        expenseImg.setImage(theImage[1][1]);
    }
}