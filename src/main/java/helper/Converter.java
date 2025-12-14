package helper;

import javafx.scene.paint.Color;

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
}