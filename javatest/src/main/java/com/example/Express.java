package com.example;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by cao-hao on 17-9-7.
 */


public class Express {

    private String originalExp;
    private char[] chars;
    private Deque<Character> opStack = new ArrayDeque<>(); //运算符栈
    private Deque<Double> execStack = new ArrayDeque<>(); //运算逆波兰表达式栈
    private LinkedList<String> exps = new LinkedList<>(); //逆波兰表达式

    private Express() {
    }

    private Express(String exp) {
        this.originalExp = exp;
    }

    /**
     * 静态构造方法
     *
     * @param exp
     *
     * @return
     */
    public static Express create(String exp) {
        Express express = new Express(exp);
        express.init();
        return express;
    }

    /**
     * 中缀表达式转换成后缀表达式，即逆波兰表达式
     */
    private void init() {
        chars = originalExp.toCharArray();
        for (char ch : chars) {
            handle(ch);
        }
       /* while (stack.size() > 0) {
            polish.offer(String.valueOf(stack.pop()));
        }
        System.out.println("Calcstra Queue:" + polish.toString());*/
        handle('#');
    }

    private void handle(char ch) {
        if (ch == ' ') {
            return;
        }
        if (ch == '#') {
            while (!opStack.isEmpty()) {
                exps.add(opStack.pop() + "");
            }
            return;
        }
        if (isOp(ch + "")) { //运算符还是操作数，操作数直接直加入exps里，运算符进行入栈操作
            if (opStack.isEmpty()) { //运算符栈为空时直接入栈
                opStack.push(ch);
            } else { //否则和栈顶元素比较
                char c = opStack.getFirst();
                if (ch == ')') {  // 如果被处理操作符是 ')'，将opStack里的元素出栈并添加到exps里，直到碰到'('。'(' 和')'直接抛弃
                    for (char c1 = opStack.pop(); c1 != '('; c1 = opStack.pop()) {
                        exps.add(c1 + "");

                    }
                } else if (ch == '(' || compare(ch, c) > 0) { // 如果被处理操作符是'('或者优先级比栈顶元素高，直接入栈
                    opStack.push(ch);
                } else { //如果被处理操作符是非括号，并且优先级不高于栈顶元素（包括等于），将栈顶元素出栈，并加入到exps里，直到被处理操作符高于栈顶元素
                    for (char c1 = opStack.getFirst();
                         compare(ch, c1) <= 0 && !opStack.isEmpty();) {
                        c1 = opStack.pop();
                        exps.add(c1 + "");
                    }
                    opStack.push(ch); //将被处理操作符入栈
                }
            }

        } else {
            exps.add(ch + "");
        }
    }

    private int compare(char c1, char c2) {
        return opValue(c1) - opValue(c2);
    }

    private int opValue(char ch) {
        switch (ch) {
            case '+':
                return 1;
            case '_':
                return 1;
            case '*':
                return 2;
            case '/':
                return 2;
            //            case '(':
            //                return 3;
            default:
                return 0;
        }
    }

    /**
     * 计算逆波兰表达式
     * @return
     */
    public Double exec() {
        while (!exps.isEmpty()) {
            String o = exps.pop();
            if (isOp(o)) {
                Double v1 = execStack.pop();
                Double v2 = execStack.pop();
                Double v3 = calculate(v1, v2, o);
                if (v3 != null) {
                    execStack.push(v3);
                }
            } else {
                execStack.push(Double.parseDouble(o));
            }
        }
        return execStack.pop();
    }

    private Double calculate(Double v1, Double v2, String op) {
        if ("+".equals(op)) {
            return v1 + v2;
        }
        if ("-".equals(op)) {
            return v2 - v1;
        }
        if ("*".equals(op)) {
            return v2 * v1;
        }
        if ("/".equals(op)) {
            return v2 / v1;
        }
        return null;
    }

    private boolean isOp(String ch) {
        return "+".equals(ch) || "-".equals(ch) || "*".equals(ch) || "/".equals(ch)
                || "(".equals(ch) || ")".equals(ch);
    }

    public static void main(String[] args) {
        Express express = Express.create("5 + ((1 + 2) * 4)- 3");
        System.out.println(express.exec());
//        System.out.println((5 + ((1.0 + 2) * 4) - 3));

        /*Express express = Express.create("5 + (((1 + 2) * 4)- 3)");
        System.out.println(express.exec());*/
        System.out.println((5 + (((1 + 2) * 4)- 3)));//   5+(((1+2)*4)-3-1)
    }
}