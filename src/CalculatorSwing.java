import com.formdev.flatlaf.FlatDarculaLaf; // Dark theme
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class CalculatorSwing extends JFrame {

    public CalculatorSwing() {
        setTitle("Numerical Methods Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        // Top banner - Dark theme
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(new Color(40, 40, 40)); // Dark background
        banner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Numerical Methods Calculator");
        title.setFont(new Font("Consolas", Font.BOLD, 26));
        title.setForeground(Color.WHITE); // White text

        JLabel subtitle = new JLabel("Solve complex equations with various numerical methods");
        subtitle.setFont(new Font("Consolas", Font.PLAIN, 14));
        subtitle.setForeground(Color.LIGHT_GRAY); // Light gray text

        banner.add(title, BorderLayout.NORTH);
        banner.add(subtitle, BorderLayout.SOUTH);

        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);

        String[] tabNames = {"Newton-Raphson", "Secant", "Bisection", "Fixed-Point", "False Position"};
        boolean[] hasTwoGuesses = {false, true, true, false, true};

        for (int i = 0; i < tabNames.length; i++) {
            tabs.addTab(tabNames[i], new MethodCalculatorPanel(tabNames[i], hasTwoGuesses[i]));
            JPanel tabPanel = new JPanel();
            tabPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for better centering
            JLabel tabLabel = new JLabel(tabNames[i]);
            tabLabel.setFont(new Font("Consolas", Font.PLAIN, 21));
            tabLabel.setForeground(Color.WHITE);
            tabPanel.setOpaque(false);
            tabPanel.add(tabLabel);
            tabs.setTabComponentAt(i, tabPanel);
        }

        add(banner, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf()); // Apply Flat Dark Theme
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new CalculatorSwing().setVisible(true));
    }
}