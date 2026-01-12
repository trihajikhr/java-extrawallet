package helper;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

public final class IOLogic {
    private IOLogic() {}

    public static void makeIntegerOnlyBlankInitial(Spinner<Integer> spinner, int min, int max) {
        spinner.setEditable(true);
        SpinnerValueFactory.IntegerSpinnerValueFactory vf = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
        spinner.setValueFactory(vf);
        spinner.getEditor().clear();

        TextFormatter<Integer> formatter = new TextFormatter<>(
                new IntegerStringConverter(),
                null, // initial VALUE = null
                c -> {
                    String text = c.getControlNewText();

                    if (text.isBlank()) return c;
                    if (!text.matches("-?\\d*")) return null;
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
            if (newVal != null) {
                vf.setValue(newVal);
            }
        });
        spinner.focusedProperty().addListener((obs, was, is) -> {
            if (!is && spinner.getEditor().getText().isBlank()) {
                vf.setValue(min);
            }
        });
    }
    public static void isTextFieldValid(TextField theTextField, int length) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().length() <= length) {
                return change; // allow input
            } else {
                return null; // reject input
            }
        });
        theTextField.setTextFormatter(formatter);
    }
    public static String normalizeSpaces(String input) {
        if (input == null) return null;

        return input
                .trim()
                .replaceAll("\\s+", " ");
    }
}