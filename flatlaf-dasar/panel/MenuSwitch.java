package panel;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;

public class MenuSwitch {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Extra Wallet - Panel Switch");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        // ===== MENU BAR =====
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem panelAItem = new JMenuItem("Panel A");
        JMenuItem panelBItem = new JMenuItem("Panel B");
        menu.add(panelAItem);
        menu.add(panelBItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        // ===== CONTAINER PANEL DENGAN CARDLAYOUT =====
        JPanel container = new JPanel(new CardLayout());

        JPanel panelA = new JPanel();
        panelA.add(new JLabel("Ini Panel A"));
        panelA.add(new JPasswordField());

        JPanel panelB = new JPanel();
        panelB.add(new JLabel("Ini Panel B"));
        panelB.add(new JButton("Death"));

        container.add(panelA, "PanelA");
        container.add(panelB, "PanelB");

        frame.add(container);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // ===== ACTION MENU =====
        CardLayout cl = (CardLayout) (container.getLayout());

        panelAItem.addActionListener(e -> cl.show(container, "PanelA"));
        panelBItem.addActionListener(e -> cl.show(container, "PanelB"));
    }
}