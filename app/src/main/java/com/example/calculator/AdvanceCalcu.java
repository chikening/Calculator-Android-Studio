package com.example.calculator;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdvanceCalcu extends AppCompatActivity {
    private TextView operationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_advance_calcu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        operationTextView = findViewById(R.id.operation);
        Button btn0 = findViewById(R.id.btn_zero);
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        Button btn6 = findViewById(R.id.btn6);
        Button btn7 = findViewById(R.id.btn7);
        Button btn8 = findViewById(R.id.btn8);
        Button btn9 = findViewById(R.id.btn9);
        Button btnPlus = findViewById(R.id.btn_plus);
        Button btnMinus = findViewById(R.id.btn_minus);
        Button btnMultiply = findViewById(R.id.btn_multiply);
        Button btnDivide = findViewById(R.id.btn_divide);
        Button btnEquals = findViewById(R.id.btn_equals);
        Button btnDeci = findViewById(R.id.btn_deci);
        //Button btnReset = findViewById(R.id.empty);
        Button btnAC = findViewById(R.id.btnAc);
        Button btnEFunc = findViewById(R.id.btnfucn);
        Button btnPercent = findViewById(R.id.btnpercent);

        btn0.setOnClickListener(view -> appendToOperation("0"));
        btn1.setOnClickListener(view -> appendToOperation("1"));
        btn2.setOnClickListener(view -> appendToOperation("2"));
        btn3.setOnClickListener(view -> appendToOperation("3"));
        btn4.setOnClickListener(view -> appendToOperation("4"));
        btn5.setOnClickListener(view -> appendToOperation("5"));
        btn6.setOnClickListener(view -> appendToOperation("6"));
        btn7.setOnClickListener(view -> appendToOperation("7"));
        btn8.setOnClickListener(view -> appendToOperation("8"));
        btn9.setOnClickListener(view -> appendToOperation("9"));
        btnPlus.setOnClickListener(view -> appendToOperation("+"));
        btnMinus.setOnClickListener(view -> appendToOperation("-"));
        btnMultiply.setOnClickListener(view -> appendToOperation("×"));
        btnDivide.setOnClickListener(view -> appendToOperation("÷"));
        btnEquals.setOnClickListener(view -> calculateResult());
        btnDeci.setOnClickListener(view -> appendToOperation("."));
        btnAC.setOnClickListener(view -> clearOperation());
        //btnReset.setOnClickListener(view -> clearOperation());
        btnPercent.setOnClickListener(view -> appendToOperation("%"));
        btnEFunc.setOnClickListener(view -> backspaceOperation());

    }

    private void backspaceOperation() {
        String currentText = operationTextView.getText().toString();
        if (currentText.length() > 0) {
            // Remove the last character
            operationTextView.setText(currentText.substring(0, currentText.length() - 1));
        }
    }
    private void appendToOperation(String value) {
        String currentText = operationTextView.getText().toString();

        if (currentText.length() > 0) {
            char lastChar = currentText.charAt(currentText.length() - 1);

            //it will replace the arithmetic
            if ("+-×÷".indexOf(lastChar) != -1) {
                if ("+-×÷".indexOf(value) != -1) {
                    currentText = currentText.substring(0, currentText.length() - 1) + value;
                    operationTextView.setText(currentText);
                    return;
                }
            }
        }
        operationTextView.append(value);
    }

    private void clearOperation() {
        operationTextView.setText("");
    }
    private void calculateResult() {
        String operation = operationTextView.getText().toString();

        if (operation.isEmpty()) {
            Toast.makeText(this, "Enter a valid expression", Toast.LENGTH_SHORT).show();
            return;
        }

        if (operation.endsWith("+") || operation.endsWith("-") || operation.endsWith("*") || operation.endsWith("÷")) {
            operation = operation.substring(0, operation.length() - 1);
        }

        try {
            double result = evaluateExpression(operation);
            operationTextView.setText(String.valueOf(result));
        } catch (Exception e) {
            Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
        }
    }

    private double evaluateExpression(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('×')) x *= parseFactor(); // multiplication
                    else if (eat('÷')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else if (eat('%')) { // percentage
                    x = parseFactor() / 100.0; // handle percentage
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                return x;
            }
        }.parse();
    }

}