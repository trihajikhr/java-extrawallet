package helper;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

public class IOLogic {
    public static void makeIntegerOnly(Spinner<Integer> spinner, int min, int max, int initial) {
        spinner.setEditable(true);

        SpinnerValueFactory.IntegerSpinnerValueFactory vf =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial);
        spinner.setValueFactory(vf);

        TextFormatter<Integer> formatter = new TextFormatter<>(
                new IntegerStringConverter() {
                    @Override
                    public Integer fromString(String s) {
                        if (s == null || s.isBlank()) return null;
                        return Integer.valueOf(s);
                    }
                },
                initial,
                c -> {
                    String text = c.getControlNewText();
                    if (!text.matches("-?\\d*")) return null;

                    if (text.isBlank() || text.equals("-")) return c;

                    try {
                        int val = Integer.parseInt(text);
                        return (val >= min && val <= max) ? c : null;
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
        );

        spinner.getEditor().setTextFormatter(formatter);

        formatter.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                vf.setValue(0);
            } else {
                vf.setValue(newVal);
            }
        });

        spinner.getEditor().textProperty().addListener((obs, old, now) -> {
            if (now.isBlank()) {
                spinner.getValueFactory().setValue(0);
            }
        });

        spinner.increment(0); // force init state
    }
}
