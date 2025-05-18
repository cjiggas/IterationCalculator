import com.formdev.flatlaf.FlatLightLaf;
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
        // Top banner
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(new Color(51, 102, 255));
        banner.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Numerical Methods Calculator");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Solve complex equations with various numerical methods");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Color.WHITE);

        banner.add(title, BorderLayout.NORTH);
        banner.add(subtitle, BorderLayout.SOUTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Newton-Raphson", new MethodCalculatorPanel(
                "Newton's method finds successively better approximations using the function's derivative.", false));
        tabs.addTab("Secant", new MethodCalculatorPanel(
                "The Secant method uses a sequence of roots of secant lines to approximate a root of a function.", true));
        tabs.addTab("Bisection", new MethodCalculatorPanel(
                "The Bisection method repeatedly bisects an interval and selects a subinterval in which a root must lie.", true));
        tabs.addTab("Fixed-Point", new MethodCalculatorPanel(
                "The Fixed-Point method iterates a function to find a point where f(x) = x.", false));
        tabs.addTab("False Position", new MethodCalculatorPanel(
                "The False Position method is similar to bisection but uses a secant line to find the root.", true));

        add(banner, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new CalculatorSwing().setVisible(true));
    }
}
