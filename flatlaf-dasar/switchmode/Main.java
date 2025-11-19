package switchmode;

import javax.swing.*;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatDarculaLaf;


public class Main {
    private static boolean darkMode = false;
    public static void main(String[] args) {
        try {
            // Set mode default terang
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("FlatLaf Dark / Light Mode");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        JButton toggleButton = new JButton("Switch Mode");

        toggleButton.addActionListener(e -> {
            try {
                if(darkMode) {
                    UIManager.setLookAndFeel(new FlatIntelliJLaf());
                } else {
                    UIManager.setLookAndFeel(new FlatDarculaLaf());
                }
                darkMode = !darkMode;

                // Update semua komponen agar berubah
                SwingUtilities.updateComponentTreeUI(frame);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        panel.add(toggleButton);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
