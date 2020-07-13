package com.example.jcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    OperationExecutor operationExecutor;
    TextView tvExpression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        operationExecutor = new OperationExecutor();
        tvExpression = findViewById(R.id.tv_expression);
    }

    public void numberBtnClick(View view) {
        String textBtn = ((Button) view).getText().toString();
        operationExecutor.enterNumber(textBtn);
        tvExpression.setText(operationExecutor.toString());
    }

    public void operationBtnClick(View view) {
        String textOperation = ((Button) view).getText().toString();
        operationExecutor.enterOperation(textOperation);
        tvExpression.setText(operationExecutor.toString());
    }

    public void executeExpression(View view) {
        String result = operationExecutor.execute();
        if (result.length() > 8 && result.length() <= 12) {
            tvExpression.setTextSize(65);
        }
        if (result.length() > 12 && result.length() < 20) {
            tvExpression.setTextSize(50);
        }
        tvExpression.setText(result);
    }
}