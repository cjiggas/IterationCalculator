import net.miginfocom.swing.MigLayout;
import javax.swing.*;
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

        Color outlineColor = new Color(200, 210, 230); // soft blue/gray outline
        int radius = 40; // rounder corners

        setBackground(new Color(250, 252, 255));
        setLayout(new MigLayout("fill, insets 32, gap 32", "[grow,fill][grow,fill]", "[grow,fill]"));

        // Left panel for inputs
        JPanel left = new JPanel(new MigLayout(
                "fillx, wrap 2, gapy 18", "[grow,fill][grow,fill]", ""
        ));
        left.setOpaque(false);

        // Function label
        JLabel funcLabel = new JLabel("Enter Function f(x)");
        funcLabel.setFont(new Font("Consolas", Font.BOLD, 17));
        left.add(funcLabel, "span 2, wrap");

        // Function input in RoundedPanel
        functionField = new PlaceholderTextField("e.g., x^2 - 4x + 4");
        functionField.setFont(new Font("Monospaced", Font.PLAIN, 16));
        functionField.setBorder(null); // Remove outline
        functionField.setBackground(new Color(245, 247, 250));
        RoundedPanel functionPanel = new RoundedPanel(radius, new Color(245, 247, 250), outlineColor);
        functionPanel.setLayout(new BorderLayout());
        functionPanel.add(functionField, BorderLayout.CENTER);
        left.add(functionPanel, "span 2, h 48!, growx, wrap");

        // Initial Guess labels
        JLabel guessLabel1 = new JLabel("Initial Guess");
        guessLabel1.setFont(new Font("Consolas", Font.BOLD, 17));
        left.add(guessLabel1);

        JLabel guessLabel2 = new JLabel("Initial Guess");
        guessLabel2.setFont(new Font("Consolas", Font.BOLD, 17));
        if (hasTwoGuesses) {
            left.add(guessLabel2, "wrap");
        } else {
            left.add(new JLabel(), "wrap");
        }

        // Initial Guess fields in RoundedPanels
        initialGuessField1 = new PlaceholderTextField(hasTwoGuesses ? "e.g., x₀ = 1" : "e.g., 1.0");
        initialGuessField1.setFont(new Font("Monospaced", Font.PLAIN, 16));
        initialGuessField1.setBorder(null); // Remove outline
        initialGuessField1.setBackground(new Color(245, 247, 250));
        RoundedPanel guessPanel1 = new RoundedPanel(radius, new Color(245, 247, 250), outlineColor);
        guessPanel1.setLayout(new BorderLayout());
        guessPanel1.add(initialGuessField1, BorderLayout.CENTER);
        left.add(guessPanel1, "h 48!, growx");

        if (hasTwoGuesses) {
            initialGuessField2 = new PlaceholderTextField("e.g., x₁ = 2");
            initialGuessField2.setFont(new Font("Monospaced", Font.PLAIN, 16));
            initialGuessField2.setBorder(null); // Remove outline
            initialGuessField2.setBackground(new Color(245, 247, 250));
            RoundedPanel guessPanel2 = new RoundedPanel(radius, new Color(245, 247, 250), outlineColor);
            guessPanel2.setLayout(new BorderLayout());
            guessPanel2.add(initialGuessField2, BorderLayout.CENTER);
            left.add(guessPanel2, "h 48!, growx, wrap");
        } else {
            left.add(new JLabel(), "wrap");
        }

        // Initial Guess hints
        JLabel guessHint1 = new JLabel(hasTwoGuesses ? "Initial guess for x₀" : "Starting point for iteration");
        guessHint1.setFont(new Font("Monospaced", Font.PLAIN, 14));
        guessHint1.setForeground(new Color(150, 150, 150));
        left.add(guessHint1);

        if (hasTwoGuesses) {
            JLabel guessHint2 = new JLabel("Initial guess for x₁");
            guessHint2.setFont(new Font("Monospaced", Font.PLAIN, 14));
            guessHint2.setForeground(new Color(150, 150, 150));
            left.add(guessHint2, "wrap");
        } else {
            left.add(new JLabel(), "wrap");
        }

        // Tolerance label
        JLabel tolLabel = new JLabel("Tolerance");
        tolLabel.setFont(new Font("Consolas", Font.BOLD, 17));
        left.add(tolLabel, "span 2, wrap");

        // Tolerance field in RoundedPanel
        toleranceField = new PlaceholderTextField("e.g., 0.0001");
        toleranceField.setFont(new Font("Monospaced", Font.PLAIN, 16));
        toleranceField.setBorder(null); // Remove outline
        toleranceField.setBackground(new Color(245, 247, 250));
        RoundedPanel tolPanel = new RoundedPanel(radius, new Color(245, 247, 250), outlineColor);
        tolPanel.setLayout(new BorderLayout());
        tolPanel.add(toleranceField, BorderLayout.CENTER);
        left.add(tolPanel, "span 2, h 48!, growx, wrap");

        // Tolerance hint
        JLabel tolHint = new JLabel("Desired accuracy of result");
        tolHint.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tolHint.setForeground(new Color(150, 150, 150));
        left.add(tolHint, "span 2, wrap");

        // Description Panel in RoundedPanel
        // RoundedPanel descPanel = new RoundedPanel(radius, new Color(245, 247, 250), outlineColor);
        // descPanel.setLayout(new BorderLayout());
        // JLabel descTitle = new JLabel("Method Description");
        // descTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        // descTitle.setForeground(Color.BLACK);
        // descTitle.setBorder(BorderFactory.createEmptyBorder(8, 16, 0, 0));
        // JTextArea desc = new JTextArea(methodDescription);
        // desc.setLineWrap(true);
        // desc.setWrapStyleWord(true);
        // desc.setEditable(false);
        // desc.setOpaque(false);
        // desc.setFont(new Font("SansSerif", Font.PLAIN, 14));
        // desc.setForeground(Color.DARK_GRAY);
        // desc.setBorder(BorderFactory.createEmptyBorder(0, 16, 12, 16));
        // descPanel.add(descTitle, BorderLayout.NORTH);
        // descPanel.add(desc, BorderLayout.CENTER);
        // left.add(descPanel, "span 2, growx, h 80!, wrap");

        // Calculate button in RoundedPanel
        calculateButton = new JButton("Calculate");
        calculateButton.setFont(new Font("Consolas", Font.BOLD, 18));
        calculateButton.setBackground(new Color(51, 102, 255));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFocusPainted(false);
        calculateButton.setBorder(null); // Remove outline
        RoundedPanel calcPanel = new RoundedPanel(radius, new Color(51, 102, 255), outlineColor);
        calcPanel.setLayout(new BorderLayout());
        calcPanel.add(calculateButton, BorderLayout.CENTER);
        left.add(calcPanel, "span 2, growx, h 54!, gaptop 30, aligny bottom");

        // Right panel for history and answer
        JPanel right = new JPanel(new MigLayout("fill, wrap 1", "[grow]", "[grow][]"));
        right.setOpaque(false);

        RoundedPanel historyPanel = new RoundedPanel(radius, new Color(245, 247, 250), outlineColor);
        historyPanel.setLayout(new BorderLayout());
        JLabel historyLabel = new JLabel("Calculation History");
        historyLabel.setFont(new Font("Consolas", Font.BOLD, 17));
        historyLabel.setForeground(Color.BLACK);
        historyLabel.setBorder(BorderFactory.createEmptyBorder(10, 18, 0, 0));
        historyPanel.add(historyLabel, BorderLayout.NORTH);

        historyArea = new JTextArea("No calculations yet. Start by entering a function.");
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 15)); 
        historyArea.setBackground(new Color(245, 247, 250));
        historyArea.setForeground(new Color(150, 150, 150));
        historyArea.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(245, 247, 250));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        right.add(historyPanel, "grow, push");

        RoundedPanel answerPanel = new RoundedPanel(radius, new Color(245, 247, 250), outlineColor);
        answerPanel.setLayout(new BorderLayout());
        JLabel answerLabel = new JLabel("Root");
        answerLabel.setFont(new Font("Consolas", Font.BOLD, 20));
        answerLabel.setForeground(Color.BLACK);
        answerLabel.setBorder(BorderFactory.createEmptyBorder(10, 18, 0, 0));
        answerPanel.add(answerLabel, BorderLayout.NORTH);

        answerField = new JTextField();
        answerField.setEditable(false);
        answerField.setFont(new Font("Monospaced", Font.BOLD, 15));
        answerField.setBackground(new Color(245, 247, 250));
        answerField.setForeground(Color.DARK_GRAY);
        answerField.setBorder(new RoundedBorder(radius, outlineColor));
        answerPanel.add(answerField, BorderLayout.CENTER);
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
            answerField.setForeground(Color.DARK_GRAY);
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    performCalculation();
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
                answerField.setForeground(Color.BLACK);
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
