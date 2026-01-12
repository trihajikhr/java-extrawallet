package helper;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class Converter {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Converter() {}

    public static String colorToHex(Color warna) {
        return String.format("#%02X%02X%02X",
                (int)(warna.getRed()*255),
                (int)(warna.getGreen()*255),
                (int)(warna.getBlue()*255));
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

    public static String numberFormatter(String number) {
        StringBuilder result = new StringBuilder(number);
        int lokasiTitik = number.indexOf('.');

        int counter = 0;
        if(lokasiTitik != -1) {
            for(int i=lokasiTitik-1; i>=0; i--) {
                counter++;
                if(counter == 3 && i != 0) {
                    counter = 0;
                    result.insert(i, ',');
                }
            }
        } else {
            for(int i=number.length()-1; i >= 0; i--) {
                counter++;
                if(counter == 3 && i != 0) {
                    counter = 0;
                    result.insert(i, ',');
                }
            }
        }

        return result.toString();
    }
}