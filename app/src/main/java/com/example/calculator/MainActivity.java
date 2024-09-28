package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText num1, num2;
    private TextView total;
    private Button addition, subtract, multiply, division, advCalcu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        total = findViewById(R.id.total);

        addition = findViewById(R.id.addition);
        subtract = findViewById(R.id.subtract);
        multiply = findViewById(R.id.multiply);
        division = findViewById(R.id.division);
        advCalcu = findViewById(R.id.advcalcu);

        addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                calculate('+');
            }
        });
        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                calculate('-');
            }
        });
        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                calculate('*');
            }
        });
        division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                calculate('/');
            }
        });
        advCalcu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, AdvanceCalcu.class);
                startActivity(intent);

            }
        });


    }
    private void calculate(char operator){
        String input1 = num1.getText().toString();
        String input2 = num2.getText().toString();

        if (input1.isEmpty() || input2.isEmpty()){
            total.setText("enter a number");
        }
        double number1 = Double.parseDouble(input1);
        double number2 = Double.parseDouble(input2);
        int result = 0;

        switch (operator) {
            case '+':
                result = (int) Math.round(number1 + number2);
                break;
            case '-':
                result = (int) Math.round(number1 - number2);
                break;
            case '*':
                result = (int) Math.round(number1 * number2);
                break;
            case '/':
                if (number2 != 0) {
                    result = (int) Math.round(number1 / number2);
                } else {
                    total.setText("cannot be divided by zero");
                    return;
                }
                break;
        }
        total.setText(String.valueOf(result));


    }
}