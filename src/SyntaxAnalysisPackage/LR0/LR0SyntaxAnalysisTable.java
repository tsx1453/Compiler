package SyntaxAnalysisPackage.LR0;    /*
    creat by tsx14 at 2017/11/21 17:47
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LR0SyntaxAnalysisTable {

    public List<Map<String,String>> table;
    public List<String> singleGram;
    public String start;

    public static void main(String[] aaa){
        String[] g = {"S->a A|b B",
                "A->c A|d",
                "B->c B|d"};
        LR0SyntaxAnalysisTable test = new LR0SyntaxAnalysisTable(g);
    }

    public LR0SyntaxAnalysisTable(String[] g) {
        GrammerDFA dfa = new GrammerDFA(g);
        table = new ArrayList<>();
        singleGram = new ArrayList<>();
        singleGram.addAll(dfa.singleGram);
        start = dfa.start;
        buildTable(dfa);
    }
    //此处自己理解写出,可意会不可言传~~~
    private void buildTable(GrammerDFA gDfa){
        for (DFA dfa:gDfa.graDfa){
            Map<String,String> newMap = new HashMap<>();
            String[] temp = dfa.items.get(0).split(" ");
            if (dfa.items.size()==1 && temp[temp.length-1].equals("_")){
                if (temp[0].equals(start)){
                    newMap.put("$","accept");
                }else {
                    int position = getIndexOfGra(dfa.items.get(0));
                    for (String i:gDfa.terminalSet){
                        newMap.put(i,"reduce"+position);
                    }
                    newMap.put("$","reduce"+position);
                }
            }else {
                for (String key:dfa.nexts.keySet()){
                    if (gDfa.terminalSet.contains(key)){
                        newMap.put(key,"shift"+dfa.nexts.get(key).id);
                    }else {
                        newMap.put(key,"goto"+dfa.nexts.get(key).id);
                    }
                }
            }
            table.add(newMap);
        }
//        for (Map<String,String> map:table){
//            System.out.println("\n"+table.indexOf(map)+":");
//            for (String key:map.keySet()){
//                System.out.print(key+":"+map.get(key)+";");
//            }
//        }
//        for (String i:singleGram){
//            System.out.println(singleGram.indexOf(i)+":"+i);
//        }
    }
    //获取文法的编号
    private int getIndexOfGra(String str){
//        System.out.println("str: "+str);
        for (String i:singleGram){
//            System.out.println("i: "+i+";");
            if (str.replace(" _","").equals(i)){
                return singleGram.indexOf(i);
            }
        }
//        System.out.println("debug "+str);
        return -1;
    }
}
