package com.example.calculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdvanceCalcu extends AppCompatActivity {
    private TextView operationTextView;
    private TextView totalTextView; // Declare totalTextView for displaying the result

    SwitchCompat switchMode;
    boolean nightmode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_advance_calcu);

        switchMode = findViewById(R.id.switchMode);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightmode = sharedPreferences.getBoolean("nightmode", false);

                if (nightmode){
                    switchMode.setChecked(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                }
                switchMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (nightmode){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            editor = sharedPreferences.edit();
                            editor.putBoolean("nightmode", false);
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            editor = sharedPreferences.edit();
                            editor.putBoolean("nightmode", true);

                        }
                        editor.apply();
                        v.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recreate();
                            }
                        }, 200); // 200 ms delay
                    }
                });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        operationTextView = findViewById(R.id.operation); // The TextView to show the operation
        totalTextView = findViewById(R.id.total);         // The TextView to show the result/total

        // Set placeholder "0" in operationTextView when the app is first opened
        operationTextView.setText("0");

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
        btnPercent.setOnClickListener(view -> appendToOperation("%"));
        btnEFunc.setOnClickListener(view -> backspaceOperation());
    }

    private void backspaceOperation() {
        String currentText = operationTextView.getText().toString();
        if (currentText.length() > 0) {
            // Remove the last character from the operation string
            operationTextView.setText(currentText.substring(0, currentText.length() - 1));
        }
        if (operationTextView.getText().toString().isEmpty()) {
            operationTextView.setText("0"); // Display "0" if the operation text is empty
        }
        adjustTextSize(operationTextView); // Adjust text size after backspacing
    }

    private void appendToOperation(String value) {
        String currentText = operationTextView.getText().toString();

        // If the current text is "0", replace it with the new value
        if (currentText.equals("0")) {
            operationTextView.setText(value);
        } else {
            if (currentText.length() > 0) {
                char lastChar = currentText.charAt(currentText.length() - 1);

                // Replace the arithmetic operator if needed
                if ("+-×÷".indexOf(lastChar) != -1) {
                    if ("+-×÷".indexOf(value) != -1) {
                        currentText = currentText.substring(0, currentText.length() - 1) + value;
                        operationTextView.setText(currentText);
                        return;
                    }
                }
            }

            // Handle percentage conversion
            if ("%".equals(value)) {
                // If a number is followed by a '%', convert the number to a percentage
                String[] parts = currentText.split("[+\\-×÷]");
                String lastPart = parts[parts.length - 1];

                // Convert the last number into a percentage (divide by 100)
                if (!lastPart.isEmpty()) {
                    try {
                        double number = Double.parseDouble(lastPart);
                        double percentage = number / 100;
                        currentText = currentText.substring(0, currentText.length() - lastPart.length()) + percentage;
                        operationTextView.setText(currentText);
                    } catch (NumberFormatException e) {
                        // Handle invalid input, if any
                        Toast.makeText(this, "Invalid number for percentage", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            } else {
                // Append the normal value if it's not a percentage
                operationTextView.append(value);
            }
        }

        adjustTextSize(operationTextView); // Adjust text size after appending
    }

    private void clearOperation() {
        operationTextView.setText("0"); // Set "0" as placeholder after clearing the operation
        totalTextView.setText("");
        adjustTextSize(operationTextView); // Reset text size after clearing
    }

    private void calculateResult() {
        String operation = operationTextView.getText().toString();

        if (operation.isEmpty() || operation.equals("0")) {
            Toast.makeText(this, "Enter a valid expression", Toast.LENGTH_SHORT).show();
            return;
        }

        // Remove the last operator if present
        if (operation.endsWith("+") || operation.endsWith("-") || operation.endsWith("*") || operation.endsWith("÷")) {
            operation = operation.substring(0, operation.length() - 1);
        }

        try {
            double result = evaluateExpression(operation);
            totalTextView.setText(String.valueOf(result)); // Display result in totalTextView
            adjustTextSize(totalTextView); // Adjust text size after calculating
        } catch (Exception e) {
            Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
        }
    }

    private void adjustTextSize(TextView textView) {
        String text = textView.getText().toString();
        float textSize = textView == operationTextView ? 45f : 50f; // Initial sizes

        // Adjust the text size based on the length of the text
        if (text.length() > 15) {
            textSize -= 15; // Reduce size if more than 15 characters
        } else if (text.length() > 10) {
            textSize -= 10; // Reduce size if more than 10 characters
        } else if (text.length() > 5) {
            textSize -= 5; // Reduce size if more than 5 characters
        }

        textView.setTextSize(textSize); // Set the adjusted text size
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
