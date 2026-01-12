package dataflow;

import dataflow.basedata.ColorItem;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import model.Account;
import model.Category;
import model.LabelType;
import model.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Objects;

public final class DataLoader {
    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private DataLoader() {}

    public static void kategoriComboBoxLoader(ComboBox<Category> dataKategoriComboBox) {
        ArrayList<Category> listCategory = DataManager.getInstance().getDataKategori();
        dataKategoriComboBox.setItems(FXCollections.observableArrayList(listCategory));

        dataKategoriComboBox.setCellFactory(list -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
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
                                item.getColor(),
                                new CornerRadii(8),
                                Insets.EMPTY
                        )
                ));

                // teks
                Label label = new Label(item.getName());
                label.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

                // gabung
                HBox box = new HBox(10, iconBox, label);
                box.setAlignment(Pos.CENTER_LEFT);

                setGraphic(box);
            }
        });
        dataKategoriComboBox.setButtonCell(dataKategoriComboBox.getCellFactory().call(null));
    }
    public static void tipeLabelComboBoxLoader(ComboBox<LabelType> dataTipeLabelComboBox) {
        ArrayList<LabelType> labelType = DataManager.getInstance().getDataTipeLabel();
        dataTipeLabelComboBox.setItems(FXCollections.observableArrayList(labelType));

        dataTipeLabelComboBox.setCellFactory(list -> new ListCell<LabelType>() {
            @Override
            protected void updateItem(LabelType item, boolean empty) {
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
                                item.getColor(),
                                new CornerRadii(8),
                                Insets.EMPTY
                        )
                ));

                // teks
                Label label = new Label(item.getName());
                label.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

                // gabung
                HBox box = new HBox(10, iconBox, label);
                box.setAlignment(Pos.CENTER_LEFT);

                setGraphic(box);
            }
        });
        dataTipeLabelComboBox.setButtonCell(dataTipeLabelComboBox.getCellFactory().call(null));
    }
    public static void akunComboBoxLoader(ComboBox<Account> dataAkunComboBox) {
        ArrayList<Account> dataAccount = DataManager.getInstance().getDataAkun();
        dataAkunComboBox.setItems(FXCollections.observableArrayList(dataAccount));

        dataAkunComboBox.setCellFactory(list -> new ListCell<Account>() {
            @Override
            protected void updateItem(Account item, boolean empty) {
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
                                item.getColor(),
                                new CornerRadii(8),
                                Insets.EMPTY
                        )
                ));

                // teks
                Label label = new Label(item.getName());
                label.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

                // gabung
                HBox box = new HBox(10, iconBox, label);
                box.setAlignment(Pos.CENTER_LEFT);

                setGraphic(box);
            }
        });
        dataAkunComboBox.setButtonCell(dataAkunComboBox.getCellFactory().call(null));
    }
    public static void mataUangComboBoxLoader(ComboBox<Currency> dataMataUangComboBox) {
        dataMataUangComboBox.setItems(DataManager.getInstance().getDataMataUang());
        dataMataUangComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Currency c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null
                        ? null
                        : c.getCode());
            }
        });
        dataMataUangComboBox.setButtonCell(dataMataUangComboBox.getCellFactory().call(null));
    }
    public static void warnaComboBoxLoader(ComboBox<ColorItem> dataWarnaComboBox) {
        dataWarnaComboBox.setItems(DataManager.getInstance().getDataColor());
        dataWarnaComboBox.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(ColorItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Circle circle = new Circle(8, item.getColor());
                    Label label = new Label(item.getLabel());
                    label.setStyle("-fx-text-fill: black;");
                    HBox box = new HBox(8, circle, label);
                    box.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(box);
                }
            }
        });

        dataWarnaComboBox.setButtonCell(dataWarnaComboBox.getCellFactory().call(null));
    }
}
