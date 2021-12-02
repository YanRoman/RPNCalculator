package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class RPNCalculator implements ActionListener {
    JFrame frame;
    JTextField textField;
    JButton[] numberButtons = new JButton[10];
    JButton[] functionButtons = new JButton[8];
    JButton addButton, subButton, mulButton, divButton;
    JButton decButton, equButton, delButton, clrButton;
    JPanel panel;
    Font myFont = new Font("",Font.PLAIN, 30);
    double result = 0;
    String current = " ";
    char operator;

    private static int getPriority(char token){
        if (token == '*' || token == '/') return  3;
        else if (token == '+' || token == '-') return 2;
        else if (token == '(') return 1;
        else if (token == ')') return -1;
        else return 0;
    }
    public static String ExpressionToRPN(String Expr){
        StringBuilder current = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        int priority;
        for (int i = 0; i < Expr.length(); i++){
            priority = getPriority(Expr.charAt(i));

            if (priority == 0) current.append(Expr.charAt(i));
            if (priority == 1) stack.push(Expr.charAt(i));
            if (priority > 1){
                current.append(' ');
                while (!stack.empty()){
                    if (getPriority(stack.peek()) >= priority) current.append(stack.pop());
                    else break;
                }
                stack.push(Expr.charAt(i));
            }
            if (priority == -1){
                current.append(' ');
                while(getPriority(stack.peek()) != 1) current.append(stack.pop());
                stack.pop();
            }
        }
        while (!stack.empty()) current.append(stack.pop());
        return current.toString();
    }
    public static double RPNToAnswer(String rpn){
        String operand = "";
        Stack<Double> stack = new Stack<>();
        for (int i = 0; i < rpn.length(); i++){
            if (rpn.charAt(i) == ' ') continue;
            if (getPriority(rpn.charAt(i)) == 0){
                while (rpn.charAt(i) != ' ' && getPriority(rpn.charAt(i)) == 0){
                    operand += rpn.charAt(i++);
                    if (i == rpn.length()) break;
                }
                stack.push(Double.parseDouble(operand));
                operand = "";
            }
            if (getPriority(rpn.charAt(i)) > 1){
                double a = stack.pop(), b = stack.pop();
                if (rpn.charAt(i) == '+') stack.push(b+a);
                if (rpn.charAt(i) == '-') stack.push(b-a);
                if (rpn.charAt(i) == '*') stack.push(b*a);
                if (rpn.charAt(i) == '/') stack.push(b/a);
            }
        }

        return stack.pop();
    }

    RPNCalculator(){
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 550);
        frame.setLayout(null);

        textField = new JTextField();
        textField.setBounds(50, 25, 300, 50);
        textField.setFont(myFont);
        textField.setEditable(false);

        addButton = new JButton("+");
        subButton = new JButton("-");
        mulButton = new JButton("*");
        divButton = new JButton("/");
        decButton = new JButton(".");
        equButton = new JButton("=");
        delButton = new JButton("Delete");
        clrButton = new JButton("Clear");

        functionButtons[0] = addButton;
        functionButtons[1] = subButton;
        functionButtons[2] = mulButton;
        functionButtons[3] = divButton;
        functionButtons[4] = decButton;
        functionButtons[5] = equButton;
        functionButtons[6] = delButton;
        functionButtons[7] = clrButton;

        for (int i = 0; i < 8; i++){
            functionButtons[i].addActionListener(this);
            functionButtons[i].setFont(myFont);
            functionButtons[i].setFocusable(false);
        }

        for (int i = 0; i < 10; i++){
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberButtons[i].setFont(myFont);
            numberButtons[i].setFocusable(false);
        }

        delButton.setBounds(50,430,145,50);
        clrButton.setBounds(205,430,145,50);

        panel = new JPanel();
        panel.setBounds(50,100,300,300);
        panel.setLayout(new GridLayout(4,4,10,10));
        //panel.setBackground(Color.gray);

        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(addButton);

        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(subButton);

        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(mulButton);
        panel.add(decButton);
        panel.add(numberButtons[0]);
        panel.add(equButton);
        panel.add(decButton);
        panel.add(divButton);


        frame.add(panel);
        frame.add(delButton);
        frame.add(clrButton);
        frame.add(textField);
        frame.setVisible(true);


    }
    public static void main(String[] args) {
        RPNCalculator calc = new RPNCalculator();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        for(int i = 0; i<10; i++){
            if (e.getSource() == numberButtons[i]){
                textField.setText(textField.getText().concat(String.valueOf(i)));
            }
        }
        if (e.getSource() == decButton){
            textField.setText(textField.getText().concat("."));
        }
        if (e.getSource() == addButton){
            current += textField.getText() + " +";
            textField.setText("");
        }
        if (e.getSource() == subButton){
            current += textField.getText() + " -";
            textField.setText("");
        }
        if (e.getSource() == mulButton){
            current += textField.getText() + " *";
            textField.setText("");
        }
        if (e.getSource() == divButton){
            current += textField.getText() + " /";
            textField.setText("");
        }
        if(e.getSource() == equButton){
            current += textField.getText();


            textField.setText(String.valueOf(RPNToAnswer(ExpressionToRPN(current))));
            current = "";
        }
        if (e.getSource() == clrButton){
            textField.setText("");
        }
        if (e.getSource() == delButton){
            String string = textField.getText();
            textField.setText("");
            for (int i = 0; i < string.length() - 1; i++){
                textField.setText(textField.getText() + string.charAt(i));
            }
        }
    }
}
