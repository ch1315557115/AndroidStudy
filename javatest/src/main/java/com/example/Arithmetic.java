package com.example;

/**
 * Created by cao-hao on 17-9-8.
 */


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;



/**
 *
 *  通过四则运算表达式字符串，构造逆波兰表达式，计算结果
 *
 * 　(1)从左至右扫描该算术表达式，从第一个字符开始判断，
 *      如果该字符是数字，则分析到该数字串的结束并将该数字串直接输出。
 *
 *　(2)如果不是数字，该字符则是运算符，此时需比较优先关系。
 　　    做法如下：将该字符与运算符栈顶的运算符的优先关系相比较。
 如果，该字符优先关系高于此运算符栈顶的运算符，则将该运算符入栈。
 倘若不是的话，则将栈顶的运算符从栈中弹出，直到栈顶运算符的优先级
 低于当前运算符，将该字符入栈。

 　　(3)重复上述操作(1)-(2)直至扫描完整个简单算术表达式，确定所有字符都得到正确处理，
 我们便可以将中缀式表示的简单算术表达式转化为逆波兰表示的简单算术表达式。
 *
 *
 */
public class Arithmetic {

    /**
     * +
     */
    private final static String OP1 = "+";

    /**
     * -
     */
    private final static String OP2 = "-";

    /**
     * *
     */
    private final static String OP3 = "*";

    /**
     * /
     */
    private final static String OP4 = "/";

    /**
     * (
     */
    private final static String OPSTART = "(";

    /**
     * )
     */
    private final static String OPEND = ")";

    //四则运算式
    private String exp;


    //四则运算解析
    private List<String> expList = new ArrayList<String>();

    //存放逆波兰表达式
    private List<String> rpnList = new ArrayList<String>();

    //存放分子
    private Deque<Integer> FenZiStack = new ArrayDeque<>();

    //存放分母
    private Deque<Integer> FenMuStack = new ArrayDeque<>();


    /**
     * 四则运算
     * @param exp           四则运算表达式
     *
     */
    public Arithmetic(String exp) {
        this.exp = exp;

        parse();
        createRPN();
    }

    /**
     * 分析四则运算表达式，将数字与运算符进行分解          String str = "4-(5+(((1+2)*4)-3)-0)";
     */
    private void parse() {
        int length = exp.length();

        String tempStr = "";
        for (int i = 0; i < length; i++) {
            String tempChar = exp.substring(i, i + 1);
            if (isNumber(tempChar)) {
                tempStr += tempChar;
            } else {
                if (!tempStr.equals("")) {
                    expList.add(tempStr);
                }
                expList.add(tempChar);
                tempStr = "";
            }
        }
        if (!tempStr.equals("")) {
            expList.add(tempStr);
        }
        for (int j=0;j<expList.size();j++){

            System.out.print(expList.get(j));
        }
        System.out.println();

    }

    /**
     * 判断当前字符或字符串是否是数字
     * @param str
     * @return
     */
    private boolean isNumber(String str) {
        return str.startsWith("0")
                || str.startsWith("1")
                || str.startsWith("2")
                || str.startsWith("3")
                || str.startsWith("4")
                || str.startsWith("5")
                || str.startsWith("6")
                || str.startsWith("7")
                || str.startsWith("8")
                || str.startsWith("9");

    }

    /**
     * 判断当前字符是否是 (
     * @param str
     * @return
     */
    private boolean isParenthesesStart(String str) {
        return str.equals(OPSTART);
    }

    /**
     * 判断当前字符是否是  )
     * @param str
     * @return
     */
    private boolean isParenthesesEnd(String str) {
        return str.equals(OPEND);
    }

    /**
     * 判断当前字符是否是高优先级运算符  * /
     * @param str
     * @return
     */
    private boolean isHeighOperator(String str) {
        if (str.equals(OP3)
                || str.equals(OP4)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 对比两个字符串的优先级
     * @param str1
     * @param str2
     * @return
     */
    private boolean compare(String str1, String str2) {
        if (str1.equals(OPSTART)) {
            return false;
        }
        if (isHeighOperator(str2)) {
            return false;
        } else {
            if (isHeighOperator(str1)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 将分解后的四则运算列表构建成逆波兰表达式列表
     */
    private void createRPN() {
        Stack stack = new Stack();

        int length = expList.size();
        for (int i = 0; i < length; i++) {
            String c = expList.get(i);

            //如果是数字，直接放到逆波兰链表的最后
            if (isNumber(c)) {
                rpnList.add(c);
            } else {
                //如果不是数字

                //如果是左括号，则直接将左括号压入栈
                if (isParenthesesStart(c)) {
                    stack.push(c);
                } else if (isParenthesesEnd(c)) {
                    //如果是右括号

                    //进行出栈操作，直到栈为空或者遇到第一个左括号
                    while (!stack.isEmpty()) {
                        //将栈顶字符串做出栈操作
                        String tempC = stack.pop();
                        if (!tempC.equals(OPSTART)) {
                            //如果不是左括号，则将字符串直接放到逆波兰链表的最后
                            rpnList.add(tempC);
                        }else{
                            //如果是左括号，退出循环操作
                            break;
                        }
                    }
                } else {
                    //如果栈内为空
                    if (stack.isEmpty()) {
                        //将当前字符串直接压栈
                        stack.push(c);
                    } else {
                        //如果栈不为空

                        //比较栈顶字符串与当前字符串的优先级，
                        if (compare(stack.top(), c)) {
                            //如果栈顶元素的优先级大于当前字符串,则进行出栈操作
                            //将栈顶元素直接放到逆波兰链表的最后
                            //直到栈内为空，或者当前元素的优先级不小于栈顶元素优先级
                            while (!stack.isEmpty() && compare(stack.top(), c)) {
                                rpnList.add(stack.pop());
                            }
                        }
                        //将当前字符串直接压栈
                        stack.push(c);
                    }
                }

            }
        }

        //如果栈不为空，则将栈中所有元素出栈放到逆波兰链表的最后
        while (!stack.isEmpty()) {
            rpnList.add(stack.pop());
        }
    }

    /**
     * 化成最简分数
     * @param numerator
     * @param denominator
     * @return
     */
    public  String simpleFraction(int numerator , int denominator){
        int resultNumerator = numerator;
        int resultDenominator = denominator;
        if (resultDenominator == 0){
            return "分母不能为0";
        }
        if (numerator < 0 ){
            numerator = -numerator;
        }
        if (denominator < 0 ){
            denominator = -denominator;
        }
        int divisor = gcd(numerator , denominator);
        if (resultDenominator / divisor == 1){
            return resultNumerator / divisor + "";
        } else if(resultDenominator / divisor == -1){
            return -resultNumerator / divisor + "";
        } else {
            return resultNumerator/ divisor + "/" + resultDenominator/divisor;
        }
    }
    public  int gcd(int m,int n){
        min(m,n);
        int s = 1;
        for(int i = 2;i <= min(m,n);i ++){
            for(int j = 2;j <= i;j++){
                if(m % j == 0 && n % j == 0){
                    m = m / j;
                    n = n / j;
                    s = s * j;
                }
            }
        }
        return s;
    }
    public  int min(int m,int n){
        if(m > n){
            return n;
        }
        else{
            return m;
        }
    }

    /**
     * 计算逆波兰表达式结果保留整数
     * @return
     */
    public String calculateZhengShu() {
        Stack numberStack = new Stack();
        String op1, op2, is = null;
        int length=rpnList.size();
        for(int i=0;i<length;i++){
            String temp=rpnList.get(i);
            if(isNumber(temp)){
                numberStack.push(temp);
            }else{
                op1 = (String) numberStack.pop();
                op2 = (String) numberStack.pop();
//                numberStack.push(Calculate.twoResult(temp, op1, op2));
            }
        }

        return numberStack.pop();
    }
/*  //  *
     * 计算逆波兰表达式结果保留分数
     * @return
     */
    public void calculateFenShu() {
        int length = rpnList.size();
        for (int i = 0; i < length; i++) {
            String temp = rpnList.get(i);
            if (isNumber(temp)) {
                FenZiStack.push(Integer.parseInt(temp));
                FenMuStack.push(Integer.parseInt("1"));
            } else {
                Integer fenZi1 = FenZiStack.pop();
                Integer fenMu1 = FenMuStack.pop();

                Integer fenZi2 = FenZiStack.pop();
                Integer fenMu2 = FenMuStack.pop();

                Integer fenZi3 = calculateFenZi(fenZi1, fenZi2,fenMu1, fenMu2,temp);
                Integer fenMu3 = calculateFenMu(fenZi1, fenZi2,fenMu1, fenMu2, temp);

                if (fenZi3 != null && fenMu3 != null) {
                    FenZiStack.push(fenZi3);
                    FenMuStack.push(fenMu3);
                }
            }
        }
    }

    private Integer calculateFenZi(int fenZi1, int fenZi2,int fenMu1,int fenMu2, String op) {

            if ("+".equals(op)) {
                return fenZi1 * fenMu2+ fenZi2 * fenMu1;
            }
            if ("-".equals(op)) {
                return fenZi2 * fenMu1 - fenZi1 * fenMu2;
            }
            if ("*".equals(op)) {
                return fenZi2 * fenZi1;
            }
            if ("/".equals(op)) {
            return fenZi2 * fenMu1;
            }

        return null;
    }

    private Integer calculateFenMu(int fenZi1, int fenZi2,int fenMu1,int fenMu2, String op) {

        if ("+".equals(op)) {
            return fenMu1 * fenMu2;
        }
        if ("-".equals(op)) {
            return fenMu1 * fenMu2;
        }
        if ("*".equals(op)) {
            return fenMu1 * fenMu2;
        }
        if ("/".equals(op)) {
            return fenZi1 * fenMu2;
        }

        return null;
    }

    /**
     * 获取逆波兰表达式字符串
     * @return
     */
    public String getRPN(){

        String rpn="";

        int rpnLength=rpnList.size();
        for(int i=0;i<rpnLength;i++){
            rpn+=rpnList.get(i)+" ";
        }

        return rpn;
    }



    /**
     * 栈
     * @author shiwei
     *
     */
    private class Stack {

        LinkedList<String> stackList = new LinkedList<String>();

        public Stack() {

        }

        /**
         * 入栈
         * @param expression
         */
        public void push(String expression) {
            stackList.addLast(expression);
        }

        /**
         * 出栈
         * @return
         */
        public String pop() {
            return stackList.removeLast();
        }

        /**
         * 栈顶元素
         * @return
         */
        public String top() {
            return stackList.getLast();
        }

        /**
         * 栈是否为空
         * @return
         */
        public boolean isEmpty() {
            return stackList.isEmpty();
        }
    }

    public static void main(String[] args) {

        String str = "5+(((1+2)*4)-3+6)/2";
        System.out.println("====================================");

        //将四则运算字符串分解为逆波兰表达式后计算结果
        Arithmetic arithmetic=new Arithmetic(str);
        String rpn=arithmetic.getRPN();
        System.out.println("逆波兰表达式 : "+rpn);
        System.out.println("计算结果为整数 : "+arithmetic.calculateZhengShu());

        arithmetic.calculateFenShu();
        int num1 = arithmetic.FenZiStack.pop();
        int num2 = arithmetic.FenMuStack.pop();
        System.out.println("计算结果为分数 : "+arithmetic.simpleFraction(1920,1776));

    }

}


