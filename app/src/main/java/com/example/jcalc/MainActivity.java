package com.example.jcalc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
TODO
-Добавить операции корня и степени
-Уменьшение размера шрифта при вводе больших чисел
-При вводе числа после результата, удалять результат
 */
public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {

    OperationExecutor operationExecutor;
    TextView tvExpression;
    Button btnEqual;
    DB db;
    ConstraintLayout mainLayout;
    float tvTextSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        operationExecutor = new OperationExecutor();
        db = new DB(this);
        db.open();

        tvExpression = findViewById(R.id.tv_expression);
        tvExpression.setOnLongClickListener(this);
        tvTextSize = tvExpression.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
        mainLayout = findViewById(R.id.mainLayout);
        btnEqual = findViewById(R.id.btn_equal);
        btnEqual.setOnLongClickListener(this);
    }

    public void numberBtnClick(View view) {
//        Intent intentVibrate =new Intent(getApplicationContext(),VibrateService.class);
//        startService(intentVibrate);
        vibration();
        String textBtn = ((Button) view).getText().toString();
        operationExecutor.enterNumber(textBtn);
        tvExpression.setText(operationExecutor.toString());
        changeTextSize();
    }

    public void operationBtnClick(View view) {
//        Intent intentVibrate =new Intent(getApplicationContext(),VibrateService.class);
//        startService(intentVibrate);
        vibration();
        String textOperation = ((Button) view).getText().toString();
        operationExecutor.enterOperation(textOperation);
        tvExpression.setText(operationExecutor.toString());
        changeTextSize();
    }

    public void executeExpression(View view) {
        try {
            String expression = operationExecutor.toString();
            String result = operationExecutor.execute();
            db.addRec(expression, result);
            tvExpression.setText(result);
            changeTextSize();
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка ввода", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void changeTextSize() {
        //Увеличивать размер шрифта на 4sp каждые 8 символов. -2 - из-за пробелов между операндами
        int currentTextLength = tvExpression.getText().toString().length() - 2;
        tvExpression.setTextSize(tvTextSize - ((int)(currentTextLength / 8) * 4));
    }

    @Override
    public boolean onLongClick(View v) {
        switch(v.getId()){
            case R.id.tv_expression:
                operationExecutor.clearData();
                ((TextView) v).setText(operationExecutor.toString());
                changeTextSize();
                break;
            case R.id.btn_equal:
                Intent intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }

    public void textFieldClick(View view) {
        vibration();
        operationExecutor.deleteSymbol();
        tvExpression.setText(operationExecutor.toString());
        changeTextSize();
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

    private void vibration() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(100,15));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
        }
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