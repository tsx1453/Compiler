package SyntaxAnalysisPackage.LL1;    /*
    creat by tsx14 at 2017/11/7 17:22
    * 　┏┓　　　┏┓
        *┏┛┻━━━┛┻┓
        *┃　　　　　　　┃ 　
        *┃　　　━　　　┃
        *┃　┳┛　┗┳　┃
        *┃　　　　　　　┃
        *┃　　　┻　　　┃
        *┃　　　　　　　┃
        *┗━┓　　　┏━┛
        *　　┃　　　┃神兽保佑
        *　　┃　　　┃代码无BUG！
        *　　┃　　　┗━━━┓
        *　　┃　　　　　　　┣┓
        *　　┃　　　　　　　┏┛
        *　　┗┓┓┏━┳┓┏┛
        *　　　┃┫┫　┃┫┫
        *　　　┗┻┛　┗┻┛
        *　　　
        */

import java.util.*;

//处理文法，消除左递归，提取左公因子
public class GrammarProcess {

    public static void main(String[] args){
        String[] g = {"A->A c|a q b|d|a q f|a"};
        extractLeftCommonFactor(removeLeftRecursion(g));
    }
    //消除左递归
    private static Map<String,String> removeLeftRecursion(String[] grammer){
        Map<String,String> grammers = new HashMap<>();
        List<String> keySet = new ArrayList<>();
        grammer[0] = grammer[0];
        for (String i:grammer){
            String[] temp = i.replace("->","|").split("\\|");
            StringBuilder stringBuilder = new StringBuilder();
            for (int j=1;j<temp.length;j++){
                sbAppend(stringBuilder,temp[j]);
            }
            grammers.put(temp[0],stringBuilder.toString());
            keySet.add(temp[0]);
        }
//        List<String> keySet = new ArrayList<>();
//        keySet.addAll(grammers.keySet());
        int size = keySet.size();
        for (int i=0;i<size;i++){
//            System.out.println(keySet.get(i));
            StringBuilder stringBuilder = new StringBuilder();
            String[] values = grammers.get(keySet.get(i)).split("\\|");
            String key = keySet.get(i);
            for (int k=0;k<values.length;k++){
                if (values[k].length()==0){
                    continue;
                }
                String values2 = values[k].split(" ")[0];
                boolean flag = true;
                for (int j=0;j<i;j++){
                    if (values2.equals(keySet.get(j))){
//                        System.out.println("values2:"+values2);
                        String others = values[k].replace(values2,"");
                        flag = false;
                        String[] strs = grammers.get(values2).split("\\|");
                        StringBuilder stringBuilder2 = new StringBuilder();
                        for (String s:strs){
                            if (!s.equals("")){
                                sbAppend(stringBuilder2,s+others);
                            }
                        }
                        sbAppend(stringBuilder,stringBuilder2.toString());
                        break;
                    }
                }
//                System.out.println("2:"+values[k]);
                if (flag){
                    sbAppend(stringBuilder,values[k]);
                }
            }
//          消除左递归
            values = stringBuilder.toString().split("\\|");
            stringBuilder.delete(0,stringBuilder.length());
            List<String> sameList = new ArrayList<>();
            String newKey = null;
            StringBuilder newString = new StringBuilder();
            for (int k=0;k<values.length;k++){
                if (values[k].length() == 0){
                    continue;
                }
                String values2 = values[k].split(" ")[0];
                if (values2.equals(key)){
                    if (newKey == null){
                        newKey = key + "~";
                    }
                    sbAppend(newString,values[k].replace(values2+" ","")+" "+newKey);
                }else {
                    sameList.add(values[k]);
                }
            }
            if (newKey != null){
                for (String s:sameList){
                    if (s.equals("E")){
                        sbAppend(stringBuilder,newKey);
                    }else {
                        sbAppend(stringBuilder, s + " " + newKey);
                    }
                }
                newString.append("|E");
                grammers.put(key,stringBuilder.toString());
                grammers.put(newKey,newString.toString());
            }
//            grammers.put(key,stringBuilder.toString());
        }
//        System.out.println("消除左递归：");
//        for (String i:grammers.keySet()){
//            System.out.print(i+"->");
//            System.out.println(grammers.get(i));
//        }
        return grammers;
    }

    private static void sbAppend(StringBuilder sb,String str){
        if (sb.length()==0){
            sb.append(str);
        }else {
            sb.append("|"+str);
        }
    }
    //提取左公因子
    private static Map<String,String> extractLeftCommonFactor(Map<String,String> grammers){
        boolean flag = true;
        while (flag){
            List<String> keys = new ArrayList<>();
            keys.addAll(grammers.keySet());
            boolean flagFlag = false;
            for (String key:keys){
                Map<String,Integer> count = new HashMap<>();
                String[] grams = grammers.get(key).split("\\|");
                for (String i:grams){
                    if (i.length() == 0){
                        continue;
                    }
                    String tempKey = i.split(" ")[0];
                    if (tempKey.equals("E")){
                        continue;
                    }
                    if (!count.containsKey(tempKey)){
                        count.put(tempKey,1);
                    }else {
                        count.put(tempKey,count.get(tempKey)+1);
                    }
                }
                if (!flagFlag) {
                    flag = false;
                }
                List<String> preStrs = new ArrayList<>();
                for (String i:count.keySet()){
//                    System.out.println(i+":"+count.get(i));
                    if (count.get(i) > 1 && grams.length > 1){
                        flag = true;
                        if (!flagFlag){
                            flagFlag = true;
                        }
                        preStrs.add(i);
//                        System.out.println(i+":"+count.get(i));
                    }
                }
//                System.out.println(flag);
                for (String i:preStrs){
                    StringBuilder newGram = new StringBuilder();
                    StringBuilder oldGram = new StringBuilder();
                    String newKey = key + "~";
                    while (grammers.containsKey(newKey)){
                        newKey += "~";
                    }
                    oldGram.append(i + " " + newKey);
                    for (String j:grams){
                        if (j.length() == 0){
                            continue;
                        }
                        if (i.equals(j.split(" ")[0])){
                            if (j.split(" ").length == 1){
                                sbAppend(newGram,"E");
                            }else {
                                sbAppend(newGram,j.replace(j.split(" ")[0] + " ",""));
                            }
                        }else {
                            sbAppend(oldGram,j);
                        }
                    }
//                    System.out.println(newGram.toString());
                    grammers.put(newKey,newGram.toString());
                    grammers.put(key,oldGram.toString());
                }
            }
        }
//        System.out.println("提取左公因子：");
//        for (String i:grammers.keySet()){
//            System.out.print(i+"->");
//            System.out.println(grammers.get(i));
//        }
        return grammers;
    }

    public static Map<String,String> run(String[] gra){
        return extractLeftCommonFactor(removeLeftRecursion(gra));
    }
}
