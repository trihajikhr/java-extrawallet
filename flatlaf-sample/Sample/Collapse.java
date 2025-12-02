package Sample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.FlatLightLaf;

public class Collapse {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Collapsible Sidebar Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // ===== Sidebar =====
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(50, 50, 50));
        sidebar.setLayout(new GridLayout(0, 1, 5, 5));
        sidebar.setPreferredSize(new Dimension(150, 0));

        JButton btnPanelA = new JButton("Panel A");
        JButton btnPanelB = new JButton("Panel B");

        sidebar.add(btnPanelA);
        sidebar.add(btnPanelB);

        // ===== Toggle button untuk sidebar =====
        JButton toggleSidebar = new JButton("☰");
        toggleSidebar.addActionListener(e -> {
            sidebar.setVisible(!sidebar.isVisible());
            frame.revalidate(); // penting supaya layout update
        });

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.add(toggleSidebar);

        frame.add(topBar, BorderLayout.NORTH);

        // ===== Main container dengan CardLayout =====
        JPanel mainContainer = new JPanel(new CardLayout());
        JPanel panelA = new JPanel();
        panelA.setBackground(new Color(100, 200, 150));
        panelA.add(new JLabel("Ini Panel A"));

        JPanel panelB = new JPanel();
        panelB.setBackground(new Color(200, 100, 150));
        panelB.add(new JLabel("Ini Panel B"));

        mainContainer.add(panelA, "PanelA");
        mainContainer.add(panelB, "PanelB");

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(mainContainer, BorderLayout.CENTER);

        // ===== Tombol sidebar switch panel =====
        CardLayout cl = (CardLayout) mainContainer.getLayout();
        btnPanelA.addActionListener(e -> cl.show(mainContainer, "PanelA"));
        btnPanelB.addActionListener(e -> cl.show(mainContainer, "PanelB"));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}