package com.example.jcalc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
TODO
Добавить операции корня и степени
 */
public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnLongClickListener {

    OperationExecutor operationExecutor;
    TextView tvExpression;
    DB db;
    ConstraintLayout mainLayout;
    float dX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        operationExecutor = new OperationExecutor();
        db = new DB(this);
        db.open();

        tvExpression = findViewById(R.id.tv_expression);
        tvExpression.setOnLongClickListener(this);
        mainLayout = findViewById(R.id.mainLayout);
        mainLayout.setOnTouchListener(this);
        tvExpression.setOnTouchListener(this);
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
        try {
            String expression = operationExecutor.toString();
            String result = operationExecutor.execute();

            db.addRec(expression, result);

            if (result.length() > 8 && result.length() <= 12) {
                tvExpression.setTextSize(65);
            }
            if (result.length() > 12 && result.length() < 20) {
                tvExpression.setTextSize(50);
            }
            tvExpression.setText(result);
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка ввода", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId() == R.id.mainLayout){
            float x = event.getX();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = x;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if(Math.abs(dX - x) > 200) {
                        dX = 0;
                        Intent intent = new Intent(this, HistoryActivity.class);
                        startActivity(intent);
                    }
                    break;
            }
        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        operationExecutor.clearData();
        ((TextView) v).setText("");
        return false;
    }

    public void textFieldClick(View view) {
        operationExecutor.deleteSymbol();
        tvExpression.setText(operationExecutor.toString());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("firstOperand", operationExecutor.getFirstOperand());
        outState.putString("operation", operationExecutor.getOperation());
        outState.putString("secondOperation", operationExecutor.getSecondOperand());
        outState.putInt("currentStatus", operationExecutor.getCurrentStatus());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        operationExecutor.setFirstOperand(savedInstanceState.getString("firstOperand"));
        operationExecutor.setOperation(savedInstanceState.getString("operation"));
        operationExecutor.setSecondOperand(savedInstanceState.getString("secondOperation"));
        operationExecutor.setCurrentStatus(savedInstanceState.getInt("currentStatus"));
        tvExpression.setText(operationExecutor.toString());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        tvExpression.setText(operationExecutor.toString());
    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}