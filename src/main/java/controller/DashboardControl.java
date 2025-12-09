package controller;

import javafx.scene.layout.AnchorPane;
import javafx.animation.Interpolator;
import javafx.scene.control.Button;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.fxml.FXML;

public class DashboardControl {

    @FXML
    private AnchorPane labelSidebar;

    @FXML
    private AnchorPane mainContent;

    @FXML
    private Button toggleButton;

    // boolean untuk memberi tahu apakah sidebar terbuka atau tidak
    private boolean open = false;

    // variabel global dan final
    private final double SIDEBAR_WIDTH = 200;
    private final double ANCHOR_ICON_WIDTH = 60;
    private final Duration D = Duration.millis(220);

    @FXML
    private final double BUTTON_SHIFT = SIDEBAR_WIDTH;

    // inisialisasi
    @FXML
    public void initialize() {
        // ketika baru buka app, sidebar hidden
        labelSidebar.setTranslateX(-SIDEBAR_WIDTH);
        mainContent.setTranslateX(0);
        toggleButton.setText("≡");
    }

    // fungsi sidebar open dan tutup, diset OnAction:toggleSidebar
    @FXML
    public void toggleSidebar() {
        toggleButton.setDisable(true);;

        double labelTarget = open ? -SIDEBAR_WIDTH : 0;
        double contentTarget = open ? 0 : SIDEBAR_WIDTH;

        double buttonTarget = open ? 0 : BUTTON_SHIFT;

        KeyValue kvLabel = new KeyValue(labelSidebar.translateXProperty(), labelTarget, Interpolator.EASE_BOTH);
        KeyValue kvContent = new KeyValue(mainContent.translateXProperty(), contentTarget, Interpolator.EASE_BOTH);
        KeyValue kvButton = new KeyValue(toggleButton.translateXProperty(), buttonTarget, Interpolator.EASE_BOTH);

        KeyFrame kf = new KeyFrame (D, kvLabel, kvContent, kvButton);
        Timeline tl = new Timeline(kf);

        tl.setOnFinished(evt -> {
            open = !open;
            toggleButton.setText(open ? "←" : "≡");
            toggleButton.setDisable(false);
        });

        tl.play();
    }
}