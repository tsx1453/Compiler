package SyntaxAnalysisPackage.LR0;    /*
    creat by tsx14 at 2017/11/20 20:55
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

public class GrammerDFA {

    public List<String> singleGram;
    public List<DFA> graDfa;
    public String start;
    public List<String> nonTerminalSet;
    public List<String> terminalSet;

    public static void main(String[] asa){
        String[] g = {"A->A + n|n"};
        GrammerDFA test = new GrammerDFA(g);
//        test.getSingleGram(g);
//        test.buildTerminalSet(g);
//        for (String i:test.singleGram){
//            System.out.println(i);
//        }
//        test.graDfa = new ArrayList<>();
//        test.buildDfa(test.upProcess(test.singleGram.get(0)));
//        System.out.println(test.graDfa.size());
        for (DFA d:test.graDfa){
            System.out.println(d.id+":");
            for (String s:d.items){
                System.out.println(s);
            }
        }
//        System.out.println(test.upProcess("~S S A _ D"));
    }

    public GrammerDFA(String[] g) {
        getSingleGram(g);
        buildTerminalSet(g);
        graDfa = new ArrayList<>();
        buildDfa(upProcess(singleGram.get(0)));
        graDfa.sort(new Comparator<DFA>() {
            @Override
            public int compare(DFA o1, DFA o2) {
                if (o1.id > o2.id){
                    return 1;
                }else if (o1.id < o2.id){
                    return -1;
                }else {
                    return 0;
                }
            }
        });
    }
//  把文法拆分成单个单个不包含 ‘|’ 的并且拓广文法,同时获取非终极字符
    private void getSingleGram(String[] g){
        singleGram = new ArrayList<>();
        nonTerminalSet = new ArrayList<>();
        for (String i:g){
            String[] temp = i.replace("->","|").split("\\|");
            if (!nonTerminalSet.contains(temp[0])){
                nonTerminalSet.add(temp[0]);
            }
            for (int j=1;j<temp.length;j++){
                singleGram.add(temp[0]+" "+temp[j]);
            }
        }
        String startg = singleGram.get(0).split(" ")[0];
        singleGram.add(0,"~"+startg+" "+startg);//拓广文法
        start = "~"+startg;
        nonTerminalSet.add(0,start);
    }
//  获取终极字符
    private void buildTerminalSet(String[] g){
        Set<String> tempSet = new HashSet<>();
        for (String i:g){
            String[] te = i.replace("->","|").split("\\|");
            for (String j:te){
                for (String k:j.split(" ")){
                    if (!nonTerminalSet.contains(k)){
                        tempSet.add(k);
                    }
                }
            }
        }
        terminalSet = new ArrayList<>();
        terminalSet.addAll(tempSet);
    }
//  递归调用自己不断生成DFA
    private DFA buildDfa(String s){
        DFA newDfa = new DFA();
        newDfa.items.add(s);
        int index = findIndex(s);
        String[] ss = s.split(" ");
//        System.out.println("index="+index+",s= "+s);
        if (index+1 < ss.length){
            if (nonTerminalSet.contains(ss[index+1])){
                for (String i:singleGram){//添加闭包项
                    if (i.split(" ")[0].equals(ss[index+1])){
                        newDfa.items.add(upProcess(i));
                    }
                }
            }
            //构建邻接链表，先判断是否指向自己，如果指向自己则把key加入一个列表，最后统一添加
            List<String> keysToSelf = new ArrayList<>();
            for (String i:newDfa.items){
                int p = findIndex(i);
                String nextS = upProcess(i);
                if (nextS.equals(newDfa.items.get(0))){
                    keysToSelf.add(i.split(" ")[p+1]);
                }else {
                    int tempInt = isAlreadyContains(upProcess(i));
                    //检测是否已经存在以免重复
                    if (tempInt == -1) {
                        newDfa.nexts.put(i.split(" ")[p + 1], buildDfa(upProcess(i)));
                    }else {
                        newDfa.nexts.put(i.split(" ")[p + 1], graDfa.get(tempInt));
                    }
                }
            }
            for (String i:keysToSelf){
                newDfa.nexts.put(i,newDfa);
            }
        }
//        System.out.println("id="+newDfa.id);
        if (newDfa.id > graDfa.size()){
            graDfa.add(newDfa);
        }else {
            graDfa.add(newDfa.id,newDfa);
        }
        return newDfa;
    }
//  给文法添加进度标识，为了避免与文法中可能存在的 ‘.’ 冲突，用 ‘_’表示,向前移位，移动标识符
    private String upProcess(String s){
        StringBuilder stringBuilder = new StringBuilder();
        String[] temp = s.split(" ");
        int index = 0;
        for (int i=0;i<temp.length;i++){
            if (temp[i].equals("_")){
                index = i;
                break;
            }
        }
        if (index == 0){
            stringBuilder.append(temp[0]+" _");
            for (int i=1;i<temp.length;i++){
                stringBuilder.append(" "+temp[i]);
            }
            return stringBuilder.toString();
        }
        for (int i=0;i<index;i++){
            stringBuilder.append(temp[i]+" ");
        }
        stringBuilder.append(temp[index+1]);
        stringBuilder.append(" _");
        for (int i=index+2;i<temp.length;i++){
            stringBuilder.append(" "+temp[i]);
        }
        return stringBuilder.toString();
    }
//  找出下标
    private int findIndex(String s){
        String[] ss = s.split(" ");
        for (int i=0;i<ss.length;i++){//找出核心项的下标
            if (ss[i].equals("_")){
                return i;
            }
        }
        return 0;
    }
//  判断是不是已经存在了
    private int isAlreadyContains(String key){
        //核心项唯一所以比较核心项即可
        for (DFA d:graDfa){
            if (key.equals(d.items.get(0))){
                return graDfa.indexOf(d);
            }
        }
        return -1;
    }
}
