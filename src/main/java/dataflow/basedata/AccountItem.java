package dataflow.basedata;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class AccountItem {
    private String label;
    private Image icon;
    private String iconPath;
    private ObjectProperty<Color> warna = new SimpleObjectProperty<>(Color.WHITE);

    public AccountItem(String label, Image icon, String iconPath) {
        this.label = label;
        this.icon = icon;
        this.iconPath = iconPath;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public Color getWarna() {
        return warna.get();
    }

    public ObjectProperty<Color> warnaProperty() {
        return warna;
    }

    public void setWarna(Color warna) {
        this.warna.set(warna);
    }
}