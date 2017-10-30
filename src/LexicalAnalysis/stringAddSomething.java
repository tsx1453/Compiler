package LexicalAnalysis;

/**
 * Created by tsx14 on 2017/10/11.
 */
public class stringAddSomething {//将正则表达式中没有写的cat补上,这里我用的 + 表示

    private static final String plus = "+";
    private static final char[] fuhao = {'(',')','|','*'};
///*
    public static void main(String[] argc){
        System.out.println(operate("(e|?)c*(cfc|c)c*"));
    }
//*/
    public static String operate(String str){
        char[] strCharArray = str.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i<strCharArray.length-1;i++){
            stringBuilder.append(strCharArray[i]);
            if (strCharArray[i] == '*' && isNotFuhao(strCharArray[i+1])){
                stringBuilder.append(plus);
            }
            if (isNotFuhao(strCharArray[i])&&isNotFuhao(strCharArray[i+1])){
                stringBuilder.append(plus);
            }
            if (strCharArray[i] == ')' && strCharArray[i+1] == '('){
                stringBuilder.append(plus);
            }
            if (strCharArray[i+1] == '(' && isNotFuhao(strCharArray[i])){
                stringBuilder.append(plus);
            }
            if (strCharArray[i] == ')' && isNotFuhao(strCharArray[i+1])){
                stringBuilder.append(plus);
            }
            if (strCharArray[i] == '*' && strCharArray[i+1] == '('){
                stringBuilder.append(plus);
            }
        }
        stringBuilder.append(strCharArray[strCharArray.length-1]);
        stringBuilder.append("#");
        return  stringBuilder.toString();
    }

    public static boolean isNotFuhao(char ch){
        for (char i : fuhao){
            if (i == ch){
                return false;
            }
        }
        return true;
    }

}
