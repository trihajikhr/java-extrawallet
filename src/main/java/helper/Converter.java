package helper;

import javafx.scene.paint.Color;

public class Converter {
    private static Converter instance;

    private Converter() {}

    public static Converter getInstance() {
        if(instance == null) {
            instance = new Converter();
        }
        return instance;
    }

    public String colorToHex(Color warna) {
        String hex = String.format("#%02X%02X%02X",
                (int)(warna.getRed()*255),
                (int)(warna.getGreen()*255),
                (int)(warna.getBlue()*255));

        return hex;
    }

    public Color hexToColor(String hex) {
        return Color.web(hex);
    }
}