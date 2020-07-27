package com.example.jcalc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class OperationExecutor {
    private int ENTER_FIRST_OPERAND = 0;
    private int ENTER_OPERATION = 1;
    private int ENTER_SECOND_OPERAND = 2;
    private int ENTER_AFTER_RESULT = 3;
    private int currentStatus = ENTER_FIRST_OPERAND;

    private String result = "";
    private String firstOperand = "";
    private String secondOperand = "";
    private String operation = "";

    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
    }

    public int getCurrentStatus() {
        return currentStatus;
    }

    public void setFirstOperand(String firstOperand) {
        this.firstOperand = firstOperand;
    }

    public String getFirstOperand() {
        return firstOperand;
    }

    public void setSecondOperand(String secondOperand) {
        this.secondOperand = secondOperand;
    }

    public String getSecondOperand() {
        return secondOperand;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void enterNumber(String num) {
        if(currentStatus == ENTER_FIRST_OPERAND) {
            firstOperand = checkOperand(firstOperand, num);
        } else if(currentStatus == ENTER_SECOND_OPERAND) {
            secondOperand = checkOperand(secondOperand, num);
        }
    }

    public void enterOperation(String op) {
        /*
        Возможность ввести отрицательное число
        При вводе нескольких минусов. последующие игнорируются
        **Добавить дейтсвия корня и степени
         */
        if(currentStatus == ENTER_FIRST_OPERAND) {
            if(firstOperand.isEmpty()) {
                if(op.equals("-")) firstOperand = op;
            } else {
                if(firstOperand.equals("-") && op.equals("-")) return;
                operation = op;
                currentStatus = ENTER_SECOND_OPERAND;
            }
        } else if(currentStatus == ENTER_SECOND_OPERAND) {
            if(secondOperand.isEmpty()) {
                if(op.equals("-")) secondOperand = op;
            } else {

            }
        } else if(currentStatus == ENTER_AFTER_RESULT) {
            operation = op;
            currentStatus = ENTER_SECOND_OPERAND;
        }
    }

    private String checkOperand(String operand, String num) {
        /*
        Если операнд пустой, или отрицательный, то при введении "." заменять на "0."
        Если вводится подряд несколько нулей, то игнорировать последующие нули
         */
        String oper = operand;
        if(operand.isEmpty() || operand.equals("-")){
            if(num.equals(".")) {
                oper += "0.";
            } else {
                oper += num;
            }
        } else {
            if(operand.equals("0") || operand.equals("-0") && num.equals("0")) return operand;
            else oper += num;
        }
        return oper;
    }

    public String execute() {
        Double firstOper = Double.valueOf(firstOperand);
        Double secondOper = Double.valueOf(secondOperand);
        switch (operation) {
            case "+":
                result = Double.toString(firstOper + secondOper);
                break;
            case "-":
                result = Double.toString(firstOper - secondOper);
                break;
            case "*":
                result = Double.toString(firstOper * secondOper);
                break;
            case "/":
                result = Double.toString(firstOper / secondOper);
                break;
        }
        checkResultText();
        firstOperand = result;
        secondOperand = "";
        operation = "";
        currentStatus = ENTER_AFTER_RESULT;
        return result;
    }

    public void checkResultText() {
        BigDecimal bd = new BigDecimal(result);
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        result = bd.toString();
        String fraction = result.substring(result.indexOf(".") + 1);
        if (fraction.equals("000")) {
            result = result.substring(0, result.indexOf("."));
        }
    }

    @Override
    public String toString() {
        return firstOperand + ' ' + operation + ' ' + secondOperand;
    }

    public void clearData() {
        firstOperand = "";
        secondOperand = "";
        operation = "";
        result = "";
        currentStatus = ENTER_FIRST_OPERAND;
    }
}
