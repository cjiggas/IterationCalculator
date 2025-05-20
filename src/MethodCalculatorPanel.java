import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;

public class MethodCalculatorPanel extends JPanel {
    private JTextField functionField;
    private JTextField initialGuessField1;
    private JTextField initialGuessField2;
    private JTextField toleranceField;
    private JTextArea historyArea;
    private JTextField answerField;
    private JButton calculateButton;
    private boolean hasTwoGuesses;

    public MethodCalculatorPanel(String methodDescription, boolean hasTwoGuesses) {
        this.hasTwoGuesses = hasTwoGuesses;

        // Replace the color definitions at the start with these dark theme colors
        Color outlineColor = new Color(64, 69, 82);  // darker blue-gray for borders
        Color backgroundColor = new Color(32, 33, 36);  // dark background
        Color panelBackgroundColor = new Color(41, 42, 45);  // slightly lighter than background
        Color textColor = new Color(220, 220, 220);  // light gray for text
        Color hintTextColor = new Color(128, 128, 128);  // medium gray for hints
        Color accentColor = new Color(86, 128, 194);  // muted blue for button

        // Main panel background
        setBackground(backgroundColor);
        setLayout(new MigLayout("fill, insets 32, gap 32", "[grow,fill][grow,fill]", "[grow,fill]"));

        // Left panel for inputs
        JPanel left = new JPanel(new MigLayout(
                "fillx, wrap 2, gapy 18", "[grow,fill][grow,fill]", ""
        ));
        left.setOpaque(false);

        // Function label
        JLabel funcLabel;
        if (methodDescription.toLowerCase().contains("fixed-point")) {
            funcLabel = new JLabel("Enter Function g(x)");
        } else {
            funcLabel = new JLabel("Enter Function f(x)");
        }
        funcLabel.setFont(new Font("Consolas", Font.BOLD, 17));
        funcLabel.setForeground(textColor);
        left.add(funcLabel, "span 2, wrap");

        // Function input in RoundedPanel
        String placeholder = methodDescription.toLowerCase().contains("fixed-point") ? 
            "e.g., sqrt(4+x)" : "e.g., x^2 - 4x + 4";
        functionField = new PlaceholderTextField(placeholder);
        functionField.setFont(new Font("Monospaced", Font.PLAIN, 16));
        functionField.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(21, outlineColor, 3),  // outer border with rounded corners
            BorderFactory.createEmptyBorder(8, 16, 8, 16)  // inner padding
        ));
        functionField.setBackground(panelBackgroundColor);
        functionField.setForeground(textColor);
        functionField.setCaretColor(textColor);
        functionField.setOpaque(true);

        // Create panel with proper radius and padding
        RoundedPanel functionPanel = new RoundedPanel(27, panelBackgroundColor, outlineColor);
        functionPanel.setLayout(new BorderLayout());
        functionPanel.add(functionField, BorderLayout.CENTER);
        left.add(functionPanel, "span 2, h 54!, growx, wrap"); // Increased height from 48 to 54

        // Initial Guess labels
        JLabel guessLabel1 = new JLabel("Initial Guess");
        guessLabel1.setFont(new Font("Consolas", Font.BOLD, 17));
        guessLabel1.setForeground(textColor);
        left.add(guessLabel1);

        JLabel guessLabel2 = new JLabel("Initial Guess");
        guessLabel2.setFont(new Font("Consolas", Font.BOLD, 17));
        guessLabel2.setForeground(textColor);
        if (hasTwoGuesses) {
            left.add(guessLabel2, "wrap");
        } else {
            left.add(new JLabel(), "wrap");
        }

        // Initial Guess fields
        initialGuessField1 = new PlaceholderTextField(hasTwoGuesses ? "e.g., x₀ = 1" : "e.g., 1.0");
        initialGuessField1.setFont(new Font("Monospaced", Font.PLAIN, 16));
        initialGuessField1.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(21, outlineColor, 3),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        initialGuessField1.setBackground(panelBackgroundColor);
        initialGuessField1.setForeground(textColor);
        initialGuessField1.setCaretColor(textColor);
        initialGuessField1.setOpaque(true);

        // Add first initial guess field
        RoundedPanel guess1Panel = new RoundedPanel(27, panelBackgroundColor, outlineColor);
        guess1Panel.setLayout(new BorderLayout());
        guess1Panel.add(initialGuessField1, BorderLayout.CENTER);
        left.add(guess1Panel, "h 54!, growx");

        if (hasTwoGuesses) {
            initialGuessField2 = new PlaceholderTextField("e.g., x₁ = 2");
            initialGuessField2.setFont(new Font("Monospaced", Font.PLAIN, 16));
            initialGuessField2.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(21, outlineColor, 3),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
            ));
            initialGuessField2.setBackground(panelBackgroundColor);
            initialGuessField2.setForeground(textColor);
            initialGuessField2.setCaretColor(textColor);
            initialGuessField2.setOpaque(true);

            // Add second initial guess field
            RoundedPanel guess2Panel = new RoundedPanel(27, panelBackgroundColor, outlineColor);
            guess2Panel.setLayout(new BorderLayout());
            guess2Panel.add(initialGuessField2, BorderLayout.CENTER);
            left.add(guess2Panel, "h 54!, growx, wrap");
        } else {
            left.add(new JLabel(), "wrap");
        }

        // Initial Guess hints
        JLabel guessHint1 = new JLabel(hasTwoGuesses ? "Initial guess for x₀" : "Starting point for iteration");
        guessHint1.setFont(new Font("Monospaced", Font.PLAIN, 14));
        guessHint1.setForeground(hintTextColor);
        left.add(guessHint1);

        if (hasTwoGuesses) {
            JLabel guessHint2 = new JLabel("Initial guess for x₁");
            guessHint2.setFont(new Font("Monospaced", Font.PLAIN, 14));
            guessHint2.setForeground(hintTextColor);
            left.add(guessHint2, "wrap");
        } else {
            left.add(new JLabel(), "wrap");
        }

        // Tolerance label
        JLabel tolLabel = new JLabel("Tolerance");
        tolLabel.setFont(new Font("Consolas", Font.BOLD, 17));
        tolLabel.setForeground(textColor);
        left.add(tolLabel, "span 2, wrap");

        // Tolerance field with rounded panel
        toleranceField = new PlaceholderTextField("e.g., 0.0001");
        toleranceField.setFont(new Font("Monospaced", Font.PLAIN, 16));
        toleranceField.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(21, outlineColor, 3),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        toleranceField.setBackground(panelBackgroundColor);
        toleranceField.setForeground(textColor);
        toleranceField.setCaretColor(textColor);
        toleranceField.setOpaque(true);

        // Add tolerance field in rounded panel
        RoundedPanel tolerancePanel = new RoundedPanel(27, panelBackgroundColor, outlineColor);
        tolerancePanel.setLayout(new BorderLayout());
        tolerancePanel.add(toleranceField, BorderLayout.CENTER);
        left.add(tolerancePanel, "span 2, h 54!, growx, wrap");

        // Tolerance hint
        JLabel tolHint = new JLabel("Desired accuracy of result");
        tolHint.setFont(new Font("Monospaced", Font.PLAIN, 14));
        tolHint.setForeground(hintTextColor);
        left.add(tolHint, "span 2, wrap");

       
        // Calculate button - fully rounded, no panel wrapper
        calculateButton = new JButton("Calculate");
        calculateButton.setFont(new Font("Consolas", Font.BOLD, 18));
        calculateButton.setBackground(accentColor);
        calculateButton.setForeground(textColor);
        calculateButton.setFocusPainted(false);
        calculateButton.setBorderPainted(true);
        calculateButton.setContentAreaFilled(false);
       

        // Custom painting for smooth rounded background
        calculateButton.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(calculateButton.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 54, 54);
                super.paint(g, c);
                g2.dispose();
            }
        });

        // Add directly to the layout, not inside a RoundedPanel
        left.add(calculateButton, "span 2, growx, h 54!, gaptop 30, aligny bottom");

        // Right panel for history and answer
        JPanel right = new JPanel(new MigLayout("fill, wrap 1", "[grow]", "[grow][]"));
        right.setOpaque(false);

        int radius = 21; // Define the radius value for rounded panels
        RoundedPanel historyPanel = new RoundedPanel(radius, panelBackgroundColor, outlineColor);
        historyPanel.setLayout(new BorderLayout());

        // Update history label styling
        JLabel historyLabel = new JLabel("Calculation History");
        historyLabel.setFont(new Font("Consolas", Font.BOLD, 20));
        historyLabel.setForeground(textColor);
        historyLabel.setBorder(BorderFactory.createEmptyBorder(10, 18, 0, 0));
        historyPanel.add(historyLabel, BorderLayout.NORTH);

        // Update history area styling
        historyArea = new JTextArea("No calculations yet. Start by entering a function.");
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        historyArea.setBackground(panelBackgroundColor);
        historyArea.setForeground(textColor);
        historyArea.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);

        // Create scroll pane with rounded corners
        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBorder(null);
        scrollPane.setBackground(panelBackgroundColor);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Add to historyPanel
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        right.add(historyPanel, "grow, push");

        RoundedPanel answerPanel = new RoundedPanel(radius, panelBackgroundColor, outlineColor);
        answerPanel.setLayout(new MigLayout("insets 0, gap 0", "[grow]", "[][]"));

        // Update Root label with white text
        JLabel answerLabel = new JLabel("Root");
        answerLabel.setFont(new Font("Consolas", Font.BOLD, 20));
        answerLabel.setForeground(textColor);  // Using textColor (light gray/white) from theme
        answerLabel.setBorder(BorderFactory.createEmptyBorder(10, 18, 0, 0));

        // Update answer field with dark theme colors
        answerField = new JTextField();
        answerField.setEditable(false);
        answerField.setFont(new Font("Monospaced", Font.BOLD, 15));
        answerField.setBackground(panelBackgroundColor);  // Dark background
        answerField.setForeground(Color.WHITE); 
        answerField.setCaretColor(Color.WHITE);
        answerField.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        answerField.setOpaque(true);

        answerPanel.add(answerLabel, "wrap");
        answerPanel.add(answerField, "growx");
        right.add(answerPanel, "growx, h 85!");

        add(left, "grow, push, w 50%");
        add(right, "grow, push, w 50%");

        // Set the panel name for method detection
        if (methodDescription.toLowerCase().contains("newton")) {
            setName("Newton-Raphson");
        } else if (methodDescription.toLowerCase().contains("fixed-point")) {
            setName("Fixed-Point");
        } else if (methodDescription.toLowerCase().contains("false position")) {
            setName("False Position");
        } else if (methodDescription.toLowerCase().contains("secant")) {
            setName("Secant");
        } else if (methodDescription.toLowerCase().contains("bisection")) {
            setName("Bisection");
        }

        calculateButton.addActionListener(e -> {
            calculateButton.setEnabled(false);
            calculateButton.setText("Calculating...");
            answerField.setForeground(Color.white);
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    try {
                        Thread.sleep(2000); // 2 second delay
                        performCalculation();
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    return null;
                }

                @Override
                protected void done() {
                    calculateButton.setEnabled(true);
                    calculateButton.setText("Calculate");
                }
            };
            worker.execute();
        });
    }
            private boolean isNumeric(String s) {
            try {
                Double.parseDouble(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        private boolean isValidTolerance(double tol) {
            return tol > 0 && tol < 1;
        }

    private void performCalculation() {
        String function = functionField.getText().trim();
        String x0Text = initialGuessField1.getText().trim();
        String tolText = toleranceField.getText().trim();
        String x1Text = (initialGuessField2 != null) ? initialGuessField2.getText().trim() : null;

        clearHistory(); // clear before checking

        if (function.isEmpty()) {
            showError("Error: Function field cannot be empty.");
            return;
        }

        double x0, tol, x1 = 0;

        if (!isNumeric(x0Text)) {
            showError("Error: Initial guess x₀ must be numeric.");
            return;
        }
        if (!isNumeric(tolText)) {
            showError("Error: Tolerance must be numeric.");
            return;
        }
        x0 = Double.parseDouble(x0Text);
        tol = Double.parseDouble(tolText);

        if (!isValidTolerance(tol)) {
            showError("Error: Tolerance must be a positive value less than 1.");
            return;
        }

        if (hasTwoGuesses) {
            if (x1Text == null || x1Text.isEmpty()) {
                showError("Error: Second initial guess x₁ is required.");
                return;
            }
            if (!isNumeric(x1Text)) {
                showError("Error: Initial guess x₁ must be numeric.");
                return;
            }
            x1 = Double.parseDouble(x1Text);
            if (x0 == x1) {
                showError("Error: Initial guesses x₀ and x₁ must be different.");
                return;
            }

        String method = getName();
        if (method.equals("Bisection") || method.equals("False Position") || method.equals("Secant")) {
            try {
                double fx0 = CalculatorBackend.evaluateFunction(function, x0);
                double fx1 = CalculatorBackend.evaluateFunction(function, x1);
                if (fx0 * fx1 >= 0) {
                    showError("Error: Initial guesses must bracket the root (f(x₀) and f(x₁) must have opposite signs).");
                    return;
                }
            } catch (Exception e) {
                showError("Error evaluating function: " + e.getMessage());
                return;
            }
        }
        } 
        


        try {
            // Clear old data
            clearHistory();

            java.util.List<CalculatorBackend.Step> steps = new java.util.ArrayList<>();
            int maxSteps = 100; // You may adjust this as needed

            switch (getName()) {
                case "Newton-Raphson":
                    CalculatorBackend.newton(function, x0, tol, 1, steps);
                    break;
                case "Fixed-Point":
                    CalculatorBackend.fixedPoint(function, x0, tol, 1, maxSteps, steps);
                    break;
                case "Bisection":
                    CalculatorBackend.bisection(function, x0, x1, tol, 1, maxSteps, steps);
                    break;
                case "Secant":
                    CalculatorBackend.secant(function, x0, x1, tol, 1, maxSteps, steps);
                    break;
                case "False Position":
                    CalculatorBackend.falsePosition(function, x0, x1, tol, 1, maxSteps, steps);
                    break;
                default:
                    showError("Error: Unknown method.");
                    return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Calculation Steps:\n");
           sb.append("--------------------------------------------------------------------------\n");

            if (!steps.isEmpty()) {
                String method = getName();
                switch (method) {
                    case "Newton-Raphson":
                        sb.append(String.format("%-8s %-18s %-18s\n", "Step", "Xn", "Xn+1"));
                       sb.append("----------------------------------------------------------------------------\n");
                        for (CalculatorBackend.Step step : steps) {
                            sb.append(String.format("%-8d %-18s %-18s\n",
                                step.getStep(),
                                CalculatorBackend.formatToTolerance(step.getXn(), tol),
                                CalculatorBackend.formatToTolerance(step.getXn1(), tol)
                            ));
                        }
                        break;
                    case "Fixed-Point":
                        sb.append(String.format("%-8s %-18s %-18s\n", "Step", "Xn", "Xn+1"));
                        sb.append("--------------------------------------------------------------------------\n");
                        for (CalculatorBackend.Step step : steps) {
                            sb.append(String.format("%-8d %-18s %-18s\n",
                                step.getStep(),
                                CalculatorBackend.formatToTolerance(step.getXn(), tol),
                                CalculatorBackend.formatToTolerance(step.getXn1(), tol)
                            ));
                        }
                        break;
                    case "Secant":
                    case "Bisection":
                    case "False Position":
                        sb.append(String.format("%-8s %-18s %-18s %-18s %-18s\n", "Step", "x0", "x1", "x2", "f(x2)"));
                        sb.append("--------------------------------------------------------------------------\n");
                        for (CalculatorBackend.Step step : steps) {
                            sb.append(String.format("%-8d %-18s %-18s %-18s %-18s\n",
                                step.getStep(),
                                CalculatorBackend.formatToTolerance(step.getX0(), tol),
                                CalculatorBackend.formatToTolerance(step.getX1(), tol),
                                CalculatorBackend.formatToTolerance(step.getX2(), tol),
                                CalculatorBackend.formatToTolerance(step.getFx(), tol)
                            ));
                        }
                        break;
                    default:
                        // Fallback: use toString()
                        for (CalculatorBackend.Step step : steps) {
                            sb.append(step.toString()).append("\n");
                        }
                }
            }

            historyArea.setText(sb.toString());

            if (!steps.isEmpty()) {
                double root;
                switch (getName()) {
                    case "Newton-Raphson":
                    case "Fixed-Point":
                        root = steps.get(steps.size() - 1).getXn1();
                        break;
                    case "Secant":
                    case "Bisection":
                    case "False Position":
                        root = steps.get(steps.size() - 1).getX2();
                        break;
                    default:
                        root = 0; // or handle error
                }
                double roundedRoot = CalculatorBackend.roundToTolerance(root, tol);
                answerField.setForeground(Color.WHITE);
                answerField.setText(String.valueOf(roundedRoot));
            } else {
                showError("No steps returned. Calculation failed.");
            }
        } catch (Exception e) {
            showError("Error during calculation: " + e.getMessage());
            clearHistory();
        }
    }

    private void showError(String msg) {
        answerField.setForeground(Color.RED);
        answerField.setText(msg);
    }

   private void clearHistory() {
        historyArea.setText("");
    }
}
