package com.stevenrios.calculator;

import static org.junit.Assert.*;
import org.junit.Test;

public class CalculatorEngineTest {
    /** Feeds keys like "12+3=" ("*" = ×, "/" = ÷, "-" = −, "<" = backspace, "~" = ±, "%" = percent). */
    private static CalculatorEngine run(String keys) {
        CalculatorEngine c = new CalculatorEngine();
        for (char k : keys.toCharArray()) {
            if (Character.isDigit(k)) c.digit(k);
            else switch (k) {
                case '.': c.dot(); break; case '=': c.equals(); break; case '<': c.backspace(); break;
                case '~': c.toggleSign(); break; case '%': c.percent(); break; case 'C': c.clear(); break;
                case '+': c.operator('+'); break; case '-': c.operator('−'); break;
                case '*': c.operator('×'); break; case '/': c.operator('÷'); break;
            }
        }
        return c;
    }
    @Test public void basics() {
        assertEquals("5", run("2+3=").getDisplay());
        assertEquals("−1", run("2-3=").getDisplay());
        assertEquals("42", run("6*7=").getDisplay());
        assertEquals("2.5", run("5/2=").getDisplay());
    }
    @Test public void precedence() { assertEquals("14", run("2+3*4=").getDisplay()); assertEquals("1", run("10-6/2*3=").getDisplay()); }
    @Test public void decimalsAreExact() { assertEquals("0.3", run(".1+.2=").getDisplay()); }
    @Test public void divideByZeroShowsError() {
        CalculatorEngine c = run("8/0=");
        assertEquals("Error", c.getDisplay()); assertTrue(c.isError());
        c.digit('4'); assertEquals("4", c.getDisplay()); // recovers on next input
    }
    @Test public void backspace() {
        assertEquals("12", run("123<").getDisplay());
        assertEquals("0", run("1<<").getDisplay());
        assertEquals("0", run("8/0=<").getDisplay());
    }
    @Test public void signAndPercent() { assertEquals("−5", run("5~").getDisplay()); assertEquals("0.5", run("50%").getDisplay()); }
    @Test public void oneDotOnly() { assertEquals("1.25", run("1..2.5").getDisplay()); }
    @Test public void expressionLine() { assertEquals("2 + 3 =", run("2+3=").getExpression()); }
    @Test public void newNumberAfterEquals() { assertEquals("7", run("2+3=7").getDisplay()); }
    @Test public void clear() { assertEquals("0", run("99+1C").getDisplay()); }
}
