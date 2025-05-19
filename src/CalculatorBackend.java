import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatorBackend {

    // Evaluate function f(x)
    public static double evaluateFunction(String equation, double x) throws Exception {
        Expression expression = new ExpressionBuilder(equation)
                .variables("x")
                .build()
                .setVariable("x", x);
        return expression.evaluate();
    }

    // Numerical derivative f'(x) using central difference
    public static double derivative(String equation, double x) throws Exception {
        double h = 1e-6;
        double fxh1 = evaluateFunction(equation, x + h);
        double fxh2 = evaluateFunction(equation, x - h);
        return (fxh1 - fxh2) / (2 * h);
    }

    // Class to hold iteration step info
    public static class Step {
        private int step;
        private Double x0, x1, x2, fx, xn, xn1;
        private double tolerance = 1e-5; // default

        // Constructor for Newton-Raphson, Fixed Point
        public Step(int step, Double xn, Double xn1, double tolerance) {
            this.step = step;
            this.xn = xn;
            this.xn1 = xn1;
            this.tolerance = tolerance;
        }

        // Constructor for Bisection, False Position, Secant
        public Step(int step, Double x0, Double x1, Double x2, Double fx, double tolerance) {
            this.step = step;
            this.x0 = x0;
            this.x1 = x1;
            this.x2 = x2;
            this.fx = fx;
            this.tolerance = tolerance;
        }

        public int getStep() { return step; }
        public Double getXn() { return xn; }
        public Double getXn1() { return xn1; }
        public Double getX0() { return x0; }
        public Double getX1() { return x1; }
        public Double getX2() { return x2; }
        public Double getFx() { return fx; }

        @Override
        public String toString() {
            if (xn != null && xn1 != null) {
                return String.format("Step %d: Xn = %s, Xn+1 = %s", step,
                    formatToTolerance(xn, tolerance), formatToTolerance(xn1, tolerance));
            } else if (x0 != null && x1 != null && x2 != null && fx != null) {
                return String.format("Step %d: x0 = %s, x1 = %s, x2 = %s, f(x2) = %s",
                    step, formatToTolerance(x0, tolerance), formatToTolerance(x1, tolerance),
                    formatToTolerance(x2, tolerance), formatToTolerance(fx, tolerance));
            } else {
                return "Incomplete step data.";
            }
        }
    }

    // Round value to the decimal places implied by tolerance
    public static double roundToTolerance(double value, double tolerance) {
        int decimalPlaces = (int) Math.ceil(-Math.log10(tolerance));
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String formatToTolerance(double value, double tolerance) {
        int decimalPlaces = (int) Math.ceil(-Math.log10(tolerance));
        StringBuilder pattern = new StringBuilder("0");
        if (decimalPlaces > 0) {
            pattern.append(".");
            for (int i = 0; i < decimalPlaces; i++) pattern.append("#"); 
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat(pattern.toString());
        return df.format(roundToTolerance(value, tolerance));
    }

    /**
     * Fixed Point Iteration Method
     * 
     * @param equation  g(x) equation for fixed point iteration
     * @param x0        initial guess
     * @param tolerance convergence tolerance
     * @param step      current step number (start from 1)
     * @param maxSteps  maximum allowed steps
     * @param steps     list to record iteration steps
     * @return          approximated root or null if convergence fails
     * @throws Exception on exceeding max steps or if derivative condition fails
     */
    public static Double fixedPoint(String equation, double x0, double tolerance, int step, int maxSteps, List<Step> steps) throws Exception {

        if (step > maxSteps) {
            throw new Exception("Maximum Number of Iterations Exceeded. Try checking the function or initial guess.");
        }

        // Check Lipschitz condition: |g'(x)| < 1 for convergence (approximate at x0)
        if (Math.abs(derivative(equation, x0)) > 1) {
            throw new Exception("The derivative at x0 is >= 1, Fixed Point Iteration may not converge.");
        }

        double x1 = evaluateFunction(equation, x0);
        steps.add(new Step(step, x0, x1, tolerance));

        if (Math.abs(x1 - x0) <= tolerance) {
            return x1;
        }

        return fixedPoint(equation, x1, tolerance, step + 1, maxSteps, steps);
    }

    /**
     * Newton-Raphson Method
     * 
     * @param equation  function f(x)
     * @param x0        initial guess
     * @param tolerance convergence tolerance
     * @param step      current step number (start from 1)
     * @param steps     list to record iteration steps
     * @return          list of Steps until convergence
     * @throws Exception on zero derivative or failure
     */
    public static List<Step> newton(String equation, double x0, double tolerance, int step, List<Step> steps) throws Exception {
        double fx = evaluateFunction(equation, x0);
        double dfx = derivative(equation, x0);

        if (dfx == 0) {
            throw new ArithmeticException("Derivative is zero at x = " + x0);
        }

        double x1 = x0 - fx / dfx;
        x1 = roundToTolerance(x1, tolerance);
        steps.add(new Step(step, x0, x1, tolerance)); 

        if (Math.abs(x1 - x0) < tolerance || Math.abs(fx) < tolerance) {
            return steps;
        }

        return newton(equation, x1, tolerance, step + 1, steps);
    }

    /**
     * Bisection Method
     * 
     * @param equation  function f(x)
     * @param x0        interval start
     * @param x1        interval end
     * @param tolerance convergence tolerance
     * @param step      current step number (start from 1)
     * @param maxSteps  maximum allowed steps
     * @param steps     list to record iteration steps
     * @return          approximated root or null if fails
     * @throws Exception on invalid interval or exceeding max steps
     */
    public static Double bisection(String equation, double x0, double x1, double tolerance, int step, int maxSteps, List<Step> steps) throws Exception {

        if (step == 1) {
            double f0 = evaluateFunction(equation, x0);
            double f1 = evaluateFunction(equation, x1);
            if (f0 * f1 >= 0) {
                throw new Exception("Function values at the interval endpoints must have opposite signs.");
            }
        }

        if (step > maxSteps) {
            throw new Exception("Maximum Number of Iterations Exceeded.");
        }

        double x2 = (x0 + x1) / 2.0;
        double fx2 = evaluateFunction(equation, x2);
        x2 = roundToTolerance(x2, tolerance);

        steps.add(new Step(step, x0, x1, x2, fx2, tolerance));

        if (Math.abs(fx2) <= tolerance || Math.abs(x1 - x0) <= tolerance) {
            return x2;
        }


        if (evaluateFunction(equation, x0) * fx2 < 0) {
            return bisection(equation, x0, x2, tolerance, step + 1, maxSteps, steps);
        } else {
            return bisection(equation, x2, x1, tolerance, step + 1, maxSteps, steps);
        }
    }

    /**
     * Secant Method
     * 
     * @param equation  function f(x)
     * @param x0        first guess
     * @param x1        second guess
     * @param tolerance convergence tolerance
     * @param step      current step number (start from 1)
     * @param maxSteps  maximum allowed steps
     * @param steps     list to record iteration steps
     * @return          approximated root or null if fails
     * @throws Exception on divide by zero or exceeding max steps
     */
    public static Double secant(String equation, double x0, double x1, double tolerance, int step, int maxSteps, List<Step> steps) throws Exception {

        if (step > maxSteps) {
            throw new Exception("Maximum Number of Iterations Exceeded.");
        }

        double f0 = evaluateFunction(equation, x0);
        double f1 = evaluateFunction(equation, x1);

        double denominator = f1 - f0;
        if (Math.abs(denominator) < 1e-12) {
            
            if (Math.abs(f1) <= tolerance) {
                steps.add(new Step(step, x0, x1, x1, f1, tolerance));
                return x1;
            }
            throw new Exception("Divide by zero error!");
        }

        double x2 = x1 - f1 * (x1 - x0) / denominator;
        x2 = roundToTolerance(x2, tolerance);
        double f2 = evaluateFunction(equation, x2);
        steps.add(new Step(step, x0, x1, x2, f2, tolerance));

        
        if (Math.abs(f2) <= tolerance || Math.abs(x2 - x1) <= tolerance) {
            return x2;
        }

        return secant(equation, x1, x2, tolerance, step + 1, maxSteps, steps);
    }

    /**
     * False Position (Regula Falsi) Method
     * 
     * @param equation  function f(x)
     * @param x0        interval start
     * @param x1        interval end
     * @param tolerance convergence tolerance
     * @param step      current step number (start from 1)
     * @param maxSteps  maximum allowed steps
     * @param steps     list to record iteration steps
     * @return          approximated root or null if fails
     * @throws Exception on divide by zero or exceeding max steps
     */
    public static Double falsePosition(String equation, double x0, double x1, double tolerance, int step, int maxSteps, List<Step> steps) throws Exception {
        // Check for max steps
        if (step > maxSteps) {
            throw new Exception("Maximum Number of Iterations Exceeded. Try changing the guess values.");
        }

        double f0 = evaluateFunction(equation, x0);
        double f1 = evaluateFunction(equation, x1);

        // Check for valid interval
        if (step == 1 && f0 * f1 >= 0) {
            throw new Exception("Function values at the interval endpoints must have opposite signs.");
        }
        if (f0 == f1) {
            throw new Exception("Divide by zero error!");
        }

        // Calculate x2 using False Position formula
        double x2 = (x0 * f1 - x1 * f0) / (f1 - f0);
        x2 = roundToTolerance(x2, tolerance);
        double fx2 = evaluateFunction(equation, x2);
        fx2 = roundToTolerance(fx2, tolerance);

        // Calculate error (difference from previous x2)
        double error = (steps.isEmpty()) ? Math.abs(x1 - x0) : Math.abs(x2 - steps.get(steps.size() - 1).getX2());

        // Record step
        steps.add(new Step(step, x0, x1, x2, fx2, tolerance));

        // Stopping criteria: function value or error
        if (Math.abs(fx2) <= tolerance || error <= tolerance) {
            return x2;
        }

        // Update interval for next step
        if (f0 * fx2 < 0) {
            // Root is between x0 and x2
            return falsePosition(equation, x0, x2, tolerance, step + 1, maxSteps, steps);
        } else {
            // Root is between x2 and x1
            return falsePosition(equation, x2, x1, tolerance, step + 1, maxSteps, steps);
        }
    }
}

