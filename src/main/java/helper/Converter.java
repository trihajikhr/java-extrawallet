package helper;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.util.function.Function;

public class Converter {
    private Converter() {}

    public static String colorToHex(Color warna) {
        String hex = String.format("#%02X%02X%02X",
                (int)(warna.getRed()*255),
                (int)(warna.getGreen()*255),
                (int)(warna.getBlue()*255));

        return hex;
    }

    public static Color hexToColor(String hex) {
        return Color.web(hex);
    }

    public static <T> void bindEnumComboBox(ComboBox<T> comboBox, Function<T, String> labelFn) {
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(T obj) {
                return obj == null ? "" : labelFn.apply(obj);
            }
            @Override
            public T fromString(String string) {
                return null;
            }
        });

        comboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : labelFn.apply(item));
            }
        });

        comboBox.setButtonCell(comboBox.getCellFactory().call(null));
    }
}