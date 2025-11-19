package panel;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

// template dasar untuk latihan

public class Main {
    public static void main(String[] args) {
        try {
            // Set FlatLaf look and feel
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        // Buat frame
        JFrame frame = new JFrame("Extra Wallet");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        // Buat panel
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("My Panel"));

        // Tambahkan label ke panel
        JLabel label = new JLabel("Hello FlatLaf!");
        JLabel label2 = new JLabel("Bismillah semoga masuk!");
        panel.add(label);
        panel.add(label2);

        // Tambahkan panel ke frame
        frame.add(panel);

        frame.setLocationRelativeTo(null); // center frame
        frame.setVisible(true);
    }
}
