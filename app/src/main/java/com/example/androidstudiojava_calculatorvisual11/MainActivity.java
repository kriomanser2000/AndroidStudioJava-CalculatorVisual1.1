package com.example.androidstudiojava_calculatorvisual11;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity
{
    private TextView display, tvResult;
    private StringBuilder expressionBuilder = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_layout);
        display = findViewById(R.id.display);
        tvResult = findViewById(R.id.tvResult);
        initializeButtons();
    }

    private void initializeButtons()
    {
        int[] numberButtons = {R.id.button9, R.id.button13, R.id.button18, R.id.button10, R.id.button14,
                R.id.button16, R.id.button20, R.id.button19, R.id.button11, R.id.button12};
        for (int id : numberButtons)
        {
            findViewById(id).setOnClickListener(this::onNumberClick);
        }
        findViewById(R.id.button17).setOnClickListener(this::onDecimalPointClick);
        int[] operatorButtons = {R.id.button22, R.id.button23, R.id.button24, R.id.button8, R.id.button3, R.id.button7};
        for (int id : operatorButtons)
        {
            findViewById(id).setOnClickListener(this::onOperatorClick);
        }
        findViewById(R.id.button1).setOnClickListener(this::onSqrtClick);
        findViewById(R.id.button2).setOnClickListener(this::onPiClick);
        findViewById(R.id.button4).setOnClickListener(this::onFactorialClick);
        findViewById(R.id.button6).setOnClickListener(this::onParenthesesClick);
        findViewById(R.id.button5).setOnClickListener(this::onClearClick); // AC
        findViewById(R.id.button21).setOnClickListener(this::onBackspaceClick); // ⌫
        findViewById(R.id.button25).setOnClickListener(this::onEqualClick); // =
    }
    private void onNumberClick(View view)
    {
        Button button = (Button) view;
        String number = button.getText().toString();
        expressionBuilder.append(number);
        display.setText(expressionBuilder.toString());
    }
    private void onDecimalPointClick(View view)
    {
        if (expressionBuilder.length() > 0)
        {
            char lastChar = expressionBuilder.charAt(expressionBuilder.length() - 1);
            if (Character.isDigit(lastChar))
            {
                expressionBuilder.append(".");
                display.setText(expressionBuilder.toString());
            }
        }
    }
    private void onOperatorClick(View view)
    {
        Button button = (Button) view;
        String operator = button.getText().toString();
        if (expressionBuilder.length() == 0)
        {
            if (operator.equals("-"))
            {
                expressionBuilder.append(operator);
                display.setText(expressionBuilder.toString());
            }
            return;
        }
        char lastChar = expressionBuilder.charAt(expressionBuilder.length() - 1);
        if ("+-×÷^".indexOf(lastChar) != -1)
        {
            expressionBuilder.setCharAt(expressionBuilder.length() - 1, operator.charAt(0));
        }
        else
        {
            expressionBuilder.append(operator);
        }
        display.setText(expressionBuilder.toString());
    }
    private void onSqrtClick(View view)
    {
        expressionBuilder.append("sqrt(");
        display.setText(expressionBuilder.toString());
    }
    private void onPiClick(View view)
    {
        expressionBuilder.append(Math.PI);
        display.setText(expressionBuilder.toString());
    }
    private void onFactorialClick(View view)
    {
        expressionBuilder.append("!");
        display.setText(expressionBuilder.toString());
    }
    private void onParenthesesClick(View view)
    {
        int open = countOccurrences(expressionBuilder.toString(), '(');
        int close = countOccurrences(expressionBuilder.toString(), ')');
        if (open > close)
        {
            expressionBuilder.append(")");
        }
        else
        {
            expressionBuilder.append("(");
        }
        display.setText(expressionBuilder.toString());
    }
    private void onClearClick(View view)
    {
        expressionBuilder.setLength(0);
        display.setText("");
        tvResult.setText("0");
    }
    private void onBackspaceClick(View view)
    {
        if (expressionBuilder.length() > 0)
        {
            expressionBuilder.deleteCharAt(expressionBuilder.length() - 1);
            display.setText(expressionBuilder.toString());
        }
    }
    private void onEqualClick(View view)
    {
        String expressionStr = expressionBuilder.toString();
        if (expressionStr.isEmpty())
        {
            return;
        }
        try
        {
            expressionStr = handleFactorial(expressionStr);
            Expression expression = new ExpressionBuilder(expressionStr)
                    .functions(new net.objecthunter.exp4j.function.Function("sqrt", 1)
                    {
                        @Override
                        public double apply(double... args)
                        {
                            return Math.sqrt(args[0]);
                        }
                    })
                    .build();
            double result = expression.evaluate();
            tvResult.setText(String.valueOf(result));
            expressionBuilder.setLength(0);
            expressionBuilder.append(result);
            display.setText(expressionBuilder.toString());
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Некоректний вираз", Toast.LENGTH_SHORT).show();
        }
    }
    private String handleFactorial(String expression) throws Exception
    {
        while (expression.contains("!"))
        {
            int index = expression.indexOf('!');
            int start = index - 1;
            while (start >= 0 && (Character.isDigit(expression.charAt(start)) || expression.charAt(start) == '.'))
            {
                start--;
            }
            String numberStr = expression.substring(start + 1, index);
            double number = Double.parseDouble(numberStr);
            if (number != (int) number || number < 0)
            {
                throw new Exception("Некоректний факторіал");
            }
            long factorial = factorial((int) number);
            expression = expression.substring(0, start + 1) + factorial + expression.substring(index + 1);
        }
        return expression;
    }
    private long factorial(int num)
    {
        if (num <= 1) return 1;
        return num * factorial(num - 1);
    }
    private int countOccurrences(String str, char ch)
    {
        int count = 0;
        for(char c: str.toCharArray()){
            if(c == ch)
            {
                count++;
            }
        }
        return count;
    }
}
