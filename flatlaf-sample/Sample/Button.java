package Sample;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

public class Button {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Rounded Panel Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.putClientProperty("JPanel.roundRect", true); // bikin rounded corners
        panel.setBorder(BorderFactory.createTitledBorder("My Rounded Panel"));

        JButton button = new JButton("Rounded Button");
        button.putClientProperty("JButton.buttonType", "roundRect"); // tombol rounded

        panel.add(button);
        frame.add(panel);
        frame.setVisible(true);
    }
}