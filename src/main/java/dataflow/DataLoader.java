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
import model.Akun;
import model.Kategori;
import model.MataUang;
import model.TipeLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Objects;

public class DataLoader {
    private static DataLoader instance;
    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    public static DataLoader getInstance() {
        if(instance == null) {
            instance = new DataLoader();
            log.info("objek data loader berhasil dibuat!");
        }
        return instance;
    }

    public void kategoriComboBoxLoader(ComboBox<Kategori> dataKategoriComboBox) {
        ArrayList<Kategori> listKategori = DataManager.getInstance().getDataKategori();
        dataKategoriComboBox.setItems(FXCollections.observableArrayList(listKategori));

        dataKategoriComboBox.setCellFactory(list -> new ListCell<Kategori>() {
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
        dataKategoriComboBox.setButtonCell(dataKategoriComboBox.getCellFactory().call(null));
    }

    public void tipeLabelComboBoxLoader(ComboBox<TipeLabel> dataTipeLabelComboBox) {
        dataTipeLabelComboBox.setCellFactory(list -> new ListCell<TipeLabel>() {
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
        dataTipeLabelComboBox.setButtonCell(dataTipeLabelComboBox.getCellFactory().call(null));
    }

    public void akunComboBoxLoader(ComboBox<Akun> dataAkunComboBox) {
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

    public void mataUangComboBoxLoader(ComboBox<MataUang> dataMataUangComboBox) {
        dataMataUangComboBox.setItems(DataManager.getInstance().getDataMataUang());
        dataMataUangComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(MataUang c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null
                        ? null
                        : c.getKode());
            }
        });
        dataMataUangComboBox.setButtonCell(dataMataUangComboBox.getCellFactory().call(null));
    }

    public void warnaComboBoxLoader(ComboBox<ColorItem> dataWarnaComboBox) {
        dataWarnaComboBox.setItems(DataManager.getInstance().getDataColor());
        dataWarnaComboBox.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(ColorItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Circle circle = new Circle(8, item.getWarna());
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
