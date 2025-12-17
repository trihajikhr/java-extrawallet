package controller.transaction;

import dataflow.DataManager;
import helper.IOLogic;
import helper.Popup;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private ComboBox<Kategori> categoryComboBox;

    @FXML
    private ComboBox<TipeLabel> tipeLabelComboBox;
    private ObservableList<TipeLabel> tipeLabelList = FXCollections.observableArrayList();

    @FXML
    private TextField noteText;

    // payment type & status
    @FXML private ComboBox<String> paymentType, paymentStatus;

    @FXML
    private Button addLabelBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        log.info("template popup terbuka");
        showPopup();
        getAllInitFunction();
    }

    // DIPANGGIL dari controller pemanggil
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setParentController(TransactionControl parent) {
        this.parentController = parent;
    }

    // kumpulan init function
    private void getAllInitFunction() {
        initButtons();
        theImage = DataManager.getInstance().getImageTransactionForm();
        initPayment();
        IOLogic.isTextFieldValid(nameText, 20);
        IOLogic.isTextFieldValid(noteText, 50);

        loadCategoryComboBox();
        loadTipeLabelComboBox();
        loadAkunComboBox();
        loadMataUangComboBox();
        activateIncome();
        IOLogic.makeIntegerOnly(spinnerAmount, 1, 2_147_483_647, 0);

        mataUangComboBox.setMouseTransparent(true);
        mataUangComboBox.setFocusTraversable(false);
        mataUangComboBox.getStyleClass().add("locked");
        mataUangListener();
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
            ctrl.setParentTemplate(this, parentController);

            stage.showAndWait();

        } catch (IOException e) {
            log.error("gagal membuka panel tambah label!", e);
            Popup.showDanger("Gagal!", "Terjadi kesalahan!");
        }
    }

    public ObservableList<TipeLabel> getTipeLabelList() {
        return tipeLabelList;
    }

    public ComboBox<TipeLabel> getTipeLabel() {
        return tipeLabelComboBox;
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

    private void initPayment() {
        paymentType.setItems(DataManager.getInstance().getDataPeymentType());
        paymentStatus.setItems(DataManager.getInstance().getDataStatusType());
    }

    private void activateIncome() {
        select(0, incomeBtn, incomeImg, incomeLabel, "#01AA71");
        updateCategoryCombo("IN");
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

    private void loadTipeLabelComboBox(){
        ArrayList<TipeLabel> dataTipelabel = DataManager.getInstance().getDataTipeLabel();
        tipeLabelList = FXCollections.observableArrayList(dataTipelabel);

        tipeLabelComboBox.setItems(tipeLabelList);
        tipeLabelComboBox.setCellFactory(list -> new ListCell<TipeLabel>() {
            @Override
            protected void updateItem(TipeLabel item, boolean empty) {
                super.updateItem(item, empty);

                if(empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                // icon
                ImageView iconView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/icons/tagW.png")).toString()));
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
        tipeLabelComboBox.setButtonCell(tipeLabelComboBox.getCellFactory().call(null));
    }

    private void loadAkunComboBox() {
        ArrayList<Akun> dataAkun = DataManager.getInstance().getDataAkun();
        akunComboBox.setItems(FXCollections.observableArrayList(dataAkun));

        akunComboBox.setCellFactory(list -> new ListCell<Akun>() {
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
        akunComboBox.setButtonCell(akunComboBox.getCellFactory().call(null));
    }

    private void loadMataUangComboBox() {
        mataUangComboBox.setItems(DataManager.getInstance().getDataMataUang());
        mataUangComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(MataUang c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null
                        ? null
//                        : c.getCode() + " — " + c.getName());
                        : c.getKode());
            }
        });
        mataUangComboBox.setButtonCell(mataUangComboBox.getCellFactory().call(null));
    }

    private void mataUangListener() {
        akunComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mataUangComboBox.setValue(newVal.getMataUang());
            }
        });
    }
}