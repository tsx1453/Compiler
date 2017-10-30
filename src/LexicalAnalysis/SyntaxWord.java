package LexicalAnalysis;

import Tools.MyTools;
import Tools.myIoClass;

import java.util.ArrayList;
import java.util.List;

public class SyntaxWord {

    public static boolean showData = false;
    private List<minDfa> digit = null;
    private List<minDfa> identlfier = null;
    MyTools tools = null;
    private String[] digitAlpha = null;
    private String[] identlfierAlpha = null;

    public SyntaxWord() {
        MyTools tools = new MyTools();
        digitAlpha =  tools.getAlpha(reText.digitRe);
        identlfierAlpha = tools.getAlpha(reText.identlfierRe);
        digit = minDfa.build(DFA.build(reText.digitRe));
        identlfier = minDfa.build(DFA.build(reText.identlfierRe));
    }

    public static void main(String[] a){
        SyntaxWord test = new SyntaxWord();
//        String[] t = {"a1234","1a23","while","asdsad_909","+123.345"};
//        for (String i:t){
//            test.demo(i);
//        }
//        test.chToToken("for(int i=0;i<5;i++)");
        String[] testData = new myIoClass().read("test.pt").split("\n");
//        test.chToToken(testData[0]);
        for (String i:testData){
            test.chToToken(i);
        }

    }


    private void demo(String t){
//        switch (syntax(t)){
//            case LexicalAnalysis.reText.DIGIT:
//                System.out.println("digit");
//                break;
//            case LexicalAnalysis.reText.IDENTLFIER:
//                System.out.println("identlfier");
//                break;
//            case LexicalAnalysis.reText.OPERATE:
//                System.out.println("operate");
//                break;
//            case LexicalAnalysis.reText.RESERVED:
//                System.out.println("reserved");
//                break;
//        }
    }

    private int getIndex(String str,String[] alpha){
        for (int i=0;i<alpha.length;i++){
//            System.out.println(alpha[i]+str);
            if (str.equals(alpha[i])){
//                System.out.println(i);
                return i;
            }
        }
        return -1;
    }
    //用正则匹配
    public int Match(String str,List<minDfa> dfa,String[] alpha){
        str+="#";
//        System.out.println(dfa.size());
        minDfa now = dfa.get(0);
        for (String a:str.split("")){
            if (a.length() == 0){
                continue;
            }
            if (a.equals("#") && now.isEnd()){
                return -1;
            }
//            System.out.println(a+" "+alphasToAlpha(a));
            int index = getIndex(alphasToAlpha(a),alpha);
            if (index == -1){
//                System.out.println(1);
//                showError(str,str.indexOf(a));
                return str.indexOf(a);
            }
            int dfaIndex = now.getNext(index);
            if (dfaIndex == -1){
//                System.out.println(dfaIndex);
//                showError(str,str.indexOf(a));
                return str.indexOf(a);
            }
            now = dfa.get(dfaIndex);
        }
        return -2;
    }

    private String alphasToAlpha(String str){
        char a = str.charAt(0);
//        System.out.println(a);
        if ((a>='a'&&a<='z')||(a>='A'&&a<='Z')){
            return "b";
        }else if (a == '_'){
            return "a";
        }else if (a>='0'&&a<='9'){
            return "c";
        }else if (a == '+' || a == '-'){
            return "e";
        }else if (a == '.'){
            return "f";
        } else {
            return str;
        }
    }

    private void showError(String str,int index){
        if (index < 0){
            return;
        }
        System.out.println("lexical error:");
        System.out.println(str.replace("#",""));
        for (int i=0;i<index;i++){
            System.out.print(" ");
        }
        System.out.println("^");
    }

    public Token syntax(String str){//判断每个token的类型
        char first = str.charAt(0);
        if (reText.isOperate(str)){
            return new Token(str,Token.tag);
        }else if (reText.isReserved(str)){
            return new Token(str,Token.re);
        }else if (first == '+'||first == '-'||(first>='0'&&first<='9')){
            if (first >= '0'&&first <= '9') {
                str = "?" + str;
            }
            int match = Match(str,digit,digitAlpha);
            if (match == 2){
//                System.out.println(match);
                if (Match(str.substring(2,str.length()),identlfier,identlfierAlpha) == -1){
                    showError(str.substring(1,str.length()),0);
                    System.out.println("error,check your code");
                    return new Token(str.substring(2,str.length()),Token.id);
                }
            }
            showError(str.substring(1,str.length()),match-1);
            return new Token(str,Token.digit);
        }else {
            showError(str,Match(str,identlfier,identlfierAlpha));
            return new Token(str,Token.id);
        }
    }

    public void run(String path){
        myIoClass io = new myIoClass();
        String myCode = io.read(path);
        String[] everyLine = myCode.split("\n");
        for (int i=0;i<everyLine.length;i++){

        }
    }
    //获取token序列
    private void chToToken(String str){
        List<Token> tokenList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        boolean strFlag = false;
        for (String i:str.split("")){
//            System.out.println(i);
            if (i.length() == 0){
                continue;
            }
            if (i.equals("'")){
                if (!strFlag){
                    strFlag = true;
                    continue;
                }else {
                    strFlag = false;
                    tokenList.add(new Token(stringBuilder.toString(),Token.str));
                    stringBuilder.delete(0,stringBuilder.length());
                    continue;
                }
            }
            if (strFlag){
                stringBuilder.append(i);
                continue;
            }
            if (reText.isOperate(i)){
//                System.out.println("judge:"+i);
                if (stringBuilder.length()!=0){
                    tokenList.add(syntax(stringBuilder.toString()));
//                    System.out.println("sb:"+stringBuilder.toString());
                }
                stringBuilder.delete(0,stringBuilder.length());
                tokenList.add(new Token(i,Token.tag));
//                System.out.println("sb:"+i);
                continue;
            }
            if (i.equals(" ")){
                if (stringBuilder.length()!=0){
                    tokenList.add(syntax(stringBuilder.toString()));
//                    System.out.println("sb:"+stringBuilder.toString());
                }
                stringBuilder.delete(0,stringBuilder.length());
                continue;
            }
            stringBuilder.append(i);
        }
//        for (LexicalAnalysis.Token i:tokenList){
//            System.out.println(i.getType()+":"+i.getValue());
//        }
        System.out.println(tokenToStr(tokenList));
    }

    public String tokenToStr(List<Token> tokens){
        StringBuilder stringBuilder = new StringBuilder();
        for (Token i:tokens){
            stringBuilder.append("<"+i.getType()+","+i.getValue()+"> ");
        }
        return stringBuilder.toString();
    }

}


