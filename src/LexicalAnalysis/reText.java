package LexicalAnalysis;

import java.util.Arrays;

/**
 * Created by tsx14 on 2017/10/9.
 */
//  a -> _
//  b -> [a-z]|[A-Z]
//  c -> [0-9]
//  d -> null
//  e -> +|-
//  f -> .
//
public class reText { //用于保存正则表达式的类
//    变量名的正则表达式
    public static String identlfierRe = "(b|a)(a|b|c)*";
//    数字的正则表达式
    public static String digitRe = "(e|?)c*(cfc|c)c*";
//    保留字
    public static String[] reservedRe = {"False", "None", "True", "and", "as",
        "assert", "break", "class", "continue", "def", "del", "elif", "else",
        "except", "finally", "for", "from", "global", "if", "import", "in",
        "is", "lambda", "nonlocal", "not", "or", "pass", "raise", "return",
        "try", "while", "with", "yield"};
//    运算符
    public static String[] operateRe = {"+","-","*","/","=","(",")","|",":",";","<",">","{","}",",",".","[","]"};
//    判断保留字的方法
    public static boolean isReserved(String targetValue) {
        return Arrays.asList(reservedRe).contains(targetValue);
    }
//    判断是否是运算符的方法
    public static boolean isOperate(String targetValue) {
//        return Arrays.asList(operateRe).contains(targetValue);
        for (String i:operateRe){
            if (i.equals(targetValue)){
                return true;
            }
        }
        return false;
    }

}
