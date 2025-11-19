package panel;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

public class EmbeddedMenuBar {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Extra Wallet - MenuBar Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        // === MENU BAR ===
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New Transaction");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(newItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem undoItem = new JMenuItem("Undo");
        JMenuItem redoItem = new JMenuItem("Redo");
        editMenu.add(undoItem);
        editMenu.add(redoItem);

        // View Menu
        JMenu viewMenu = new JMenu("View");
        JMenuItem toggleMode = new JMenuItem("Toggle Dark/Light Mode");
        viewMenu.add(toggleMode);

        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);

        // Set menu bar to frame
        frame.setJMenuBar(menuBar);

        // === MAIN PANEL ===
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Welcome to Extra Wallet!");
        panel.add(label);
        frame.add(panel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // === ACTIONS ===
        exitItem.addActionListener(e -> System.exit(0));
        toggleMode.addActionListener(e -> {
            try {
                if(UIManager.getLookAndFeel() instanceof FlatLightLaf) {
                    UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                } else {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                }
                SwingUtilities.updateComponentTreeUI(frame);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
