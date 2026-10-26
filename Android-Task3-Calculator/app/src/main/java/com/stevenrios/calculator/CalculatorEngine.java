package com.stevenrios.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Immediate-execution calculator state machine (like a pocket calculator, but with operator precedence:
 * 2 + 3 × 4 = 14). Uses BigDecimal so 0.1 + 0.2 = 0.3. Division by zero puts the display into "Error".
 * Pure Java — fully unit-tested on the JVM.
 */
public class CalculatorEngine {
    public static final String ERROR = "Error";
    private static final MathContext MC = new MathContext(15, RoundingMode.HALF_EVEN);

    private final StringBuilder entry = new StringBuilder("0"); // number being typed
    private final StringBuilder expr = new StringBuilder();     // tokens shown on the top line
    private final java.util.List<Object> tokens = new java.util.ArrayList<>();
    private boolean error = false, justEvaluated = false;

    public void digit(char d) {
        if (error) clear();
        if (justEvaluated) { clear(); }
        if (entry.toString().equals("0")) entry.setLength(0);
        else if (entry.toString().equals("-0")) entry.setLength(1);
        if (entry.length() < 15) entry.append(d);
    }

    public void dot() {
        if (error || justEvaluated) clear();
        if (entry.indexOf(".") < 0) entry.append('.');
    }

    public void backspace() {
        if (error) { clear(); return; }
        if (justEvaluated) return;
        entry.setLength(entry.length() - 1);
        if (entry.length() == 0 || entry.toString().equals("-")) entry.replace(0, entry.length(), "0");
    }

    public void toggleSign() {
        if (error) return;
        if (entry.charAt(0) == '-') entry.deleteCharAt(0); else entry.insert(0, '-');
    }

    public void percent() {
        if (error) return;
        setEntry(new BigDecimal(entry.toString()).divide(BigDecimal.valueOf(100), MC));
    }

    /** op is one of + − × ÷ */
    public void operator(char op) {
        if (error) return;
        justEvaluated = false;
        tokens.add(new BigDecimal(entry.toString()));
        tokens.add(op);
        expr.append(display(entry.toString())).append(' ').append(op).append(' ');
        entry.replace(0, entry.length(), "0");
    }

    public void equals() {
        if (error || tokens.isEmpty()) return;
        tokens.add(new BigDecimal(entry.toString()));
        expr.append(display(entry.toString())).append(" =");
        try { setEntry(evaluate()); }
        catch (ArithmeticException e) { error = true; }
        tokens.clear();
        justEvaluated = true;
    }

    public void clear() { entry.replace(0, entry.length(), "0"); expr.setLength(0); tokens.clear(); error = false; justEvaluated = false; }

    public String getDisplay() { return error ? ERROR : display(entry.toString()); }
    public String getExpression() { return expr.toString(); }
    public boolean isError() { return error; }

    // two-pass precedence: × ÷ first, then + −
    private BigDecimal evaluate() {
        java.util.List<Object> t = new java.util.ArrayList<>(tokens);
        for (int i = 1; i < t.size(); i += 2) {
            char op = (Character) t.get(i);
            if (op == '×' || op == '÷') {
                BigDecimal a = (BigDecimal) t.get(i - 1), b = (BigDecimal) t.get(i + 1);
                if (op == '÷' && b.signum() == 0) throw new ArithmeticException("Division by zero");
                t.set(i - 1, op == '×' ? a.multiply(b, MC) : a.divide(b, MC));
                t.remove(i); t.remove(i); i -= 2;
            }
        }
        BigDecimal acc = (BigDecimal) t.get(0);
        for (int i = 1; i < t.size(); i += 2)
            acc = (Character) t.get(i) == '+' ? acc.add((BigDecimal) t.get(i + 1), MC) : acc.subtract((BigDecimal) t.get(i + 1), MC);
        return acc;
    }

    private void setEntry(BigDecimal v) {
        String s = v.stripTrailingZeros().toPlainString();
        if (s.replace("-", "").replace(".", "").length() > 15) s = v.round(new MathContext(10)).stripTrailingZeros().toString();
        entry.replace(0, entry.length(), s);
    }

    private static String display(String s) { return s.startsWith("-") ? "−" + s.substring(1) : s; }
}
