package com.stevenrios.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private final CalculatorEngine calc = new CalculatorEngine();
    private TextView result, expression;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.textResult);
        expression = findViewById(R.id.textExpression);
        int[] digits = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        for (int i = 0; i < 10; i++) { final char d = (char) ('0' + i); findViewById(digits[i]).setOnClickListener(v -> { calc.digit(d); render(); }); }
        int[] ops = {R.id.btnAdd, R.id.btnSub, R.id.btnMul, R.id.btnDiv};
        for (int id : ops) findViewById(id).setOnClickListener(v -> { calc.operator(((Button) v).getText().charAt(0)); render(); });
        findViewById(R.id.btnDot).setOnClickListener(v -> { calc.dot(); render(); });
        findViewById(R.id.btnEquals).setOnClickListener(v -> { calc.equals(); render(); });
        findViewById(R.id.btnClear).setOnClickListener(v -> { calc.clear(); render(); });
        findViewById(R.id.btnBack).setOnClickListener(v -> { calc.backspace(); render(); });
        findViewById(R.id.btnSign).setOnClickListener(v -> { calc.toggleSign(); render(); });
        findViewById(R.id.btnPercent).setOnClickListener(v -> { calc.percent(); render(); });
        render();
    }

    private void render() {
        result.setText(calc.getDisplay());
        result.setTextColor(calc.isError() ? 0xFFFF5252 : 0xFFFFFFFF);
        expression.setText(calc.getExpression());
    }
}
