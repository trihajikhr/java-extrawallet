package helper;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.math.BigDecimal;

public final class IOLogic {

    private IOLogic() {}

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
    public static void makeIntegerOnlyBlankInitial(Spinner<BigDecimal> spinner, BigDecimal min, BigDecimal max) {
        spinner.setEditable(true);

        SpinnerValueFactory<BigDecimal> vf = new SpinnerValueFactory<BigDecimal>() {
            {
                setValue(min);
            }

            @Override
            public void decrement(int steps) {
                if (getValue() == null) setValue(min);
                else {
                    BigDecimal val = getValue().subtract(BigDecimal.ONE.multiply(BigDecimal.valueOf(steps)));
                    setValue(val.compareTo(min) < 0 ? min : val);
                }
            }

            @Override
            public void increment(int steps) {
                if (getValue() == null) setValue(min);
                else {
                    BigDecimal val = getValue().add(BigDecimal.ONE.multiply(BigDecimal.valueOf(steps)));
                    setValue(val.compareTo(max) > 0 ? max : val);
                }
            }
        };

        spinner.setValueFactory(vf);

        spinner.getEditor().clear();

        TextFormatter<BigDecimal> formatter = new TextFormatter<>(
                c -> {
                    String text = c.getControlNewText();
                    if (text.isBlank()) return c;
                    if (!text.matches("-?\\d*(\\.\\d*)?")) return null;
                    try {
                        BigDecimal val = new BigDecimal(text);
                        return (val.compareTo(min) >= 0 && val.compareTo(max) <= 0) ? c : null;
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