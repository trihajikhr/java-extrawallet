package dataflow.basedata;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class AccountItem {
    private String label;
    private Image icon;
    private ObjectProperty<Color> warna = new SimpleObjectProperty<>(Color.WHITE);

    public AccountItem(String label, Image icon) {
        this.label = label;
        this.icon = icon;
    }

    public String getLabel() { return label; }
    public Image getIcon() { return icon; }
    public Color getWarna() { return warna.get(); }
    public void setWarna(Color c) { warna.set(c); }
    public ObjectProperty<Color> warnaProperty() { return warna; }
}