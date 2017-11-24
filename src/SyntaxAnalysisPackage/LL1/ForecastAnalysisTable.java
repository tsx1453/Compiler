package SyntaxAnalysisPackage.LL1;    /* 
    creat by tsx14 at 2017/11/14 20:02
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

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.*;

public class ForecastAnalysisTable {

    public List<String> nonTerminalSet;
    public List<String> terminalSet;
    public String[][] analysisTable;
    private String start;

    public static void main(String[] asda){
        String[] g = {"A->T A~",
                "A~->+ T A~|E",
                "T->F T~",
                "T~->* F T~|E",
                "F->( A )|id"
        };
        new ForecastAnalysisTable(g);
    }

    public ForecastAnalysisTable(String[] gra) {
        FirstAndFollows firstFollows = new FirstAndFollows(gra);
        nonTerminalSet = new ArrayList<>();
        terminalSet = new ArrayList<>();
        nonTerminalSet.addAll(firstFollows.getNonTerminalSet());
        terminalSet.addAll(buildTerminalSet(firstFollows.getGrammers()));
        terminalSet.add("$");
        analysisTable = new String[nonTerminalSet.size()][terminalSet.size()];
        buildAnalysisTable(firstFollows);
        start = firstFollows.getStartS();
    }

    private Set<String> buildTerminalSet(Map<String,String> gras){
        Set<String> terminal = new HashSet<>();
        for (String i:gras.keySet()){
            String[] a = gras.get(i).split("\\|");
            for (String j:a){
                for (String k:j.split(" ")){
                    if (k.length() == 0){
                        continue;
                    }
                    if (!nonTerminalSet.contains(k)){
                        terminal.add(k);
                    }
                }
            }
        }
//        System.out.println(terminal);
        return terminal;
    }

    private void buildAnalysisTable(FirstAndFollows firstAndFollows){
        initTable();
        Map<String,HashSet<String>> first = firstAndFollows.getFirsts();
        Map<String,HashSet<String>> follows = firstAndFollows.getFollows();
        Map<String,String> product = firstAndFollows.getSingleGram();
//        System.out.println(product);
        for (String pkey:firstAndFollows.getProductKey()){
            String firstStr = product.get(pkey).split(" ")[0];
//            System.out.println("FirstStr:"+firstStr);
            if (terminalSet.contains(firstStr)){
                if (firstStr.equals("E")){
                    for (String j:follows.get(pkey)){
                        if (terminalSet.contains(j)){
                            if (analysisTable[nonTerminalSet.indexOf(pkey)][terminalSet.indexOf(j)].length() == 0){
                                analysisTable[nonTerminalSet.indexOf(pkey)][terminalSet.indexOf(j)] += pkey + " " + product.get(pkey);
                            }else {
                                analysisTable[nonTerminalSet.indexOf(pkey)][terminalSet.indexOf(j)] += "\n" + pkey + " " + product.get(pkey);
                            }
                        }
                    }
                    continue;
                }
                if (analysisTable[nonTerminalSet.indexOf(pkey)][terminalSet.indexOf(firstStr)].length() == 0){
                    analysisTable[nonTerminalSet.indexOf(pkey)][terminalSet.indexOf(firstStr)] += pkey + " " + product.get(pkey);
                }else {
                    analysisTable[nonTerminalSet.indexOf(pkey)][terminalSet.indexOf(firstStr)] += "\n" + pkey + " " + product.get(pkey);
                }
            }else {
                for (String i:first.get(firstStr)){
//                    System.out.println(firstStr+":"+i);
                    if (terminalSet.contains(i)){
//                        System.out.println(i);
                        if (i.equals("E")){
//                            System.out.println(">");
                            for (String j:follows.get(pkey)){
                                if (terminalSet.contains(j)){
                                    if (analysisTable[nonTerminalSet.indexOf(pkey)][terminalSet.indexOf(j)].length() == 0){
                                        analysisTable[nonTerminalSet.indexOf(pkey)][terminalSet.indexOf(j)] += pkey + " " + product.get(pkey);
                                    }else {
                                        analysisTable[nonTerminalSet.indexOf(pkey)][terminalSet.indexOf(j)] += "\n" + pkey + " " + product.get(pkey);
                                    }
                                }
                            }
                            continue;
                        }
                        if (analysisTable[nonTerminalSet.indexOf(pkey)][terminalSet.indexOf(i)].length() == 0){
                            analysisTable[nonTerminalSet.indexOf(pkey)][terminalSet.indexOf(i)] += pkey + " " + product.get(pkey);
                        }else {
                            analysisTable[nonTerminalSet.indexOf(pkey)][terminalSet.indexOf(i)] += "\n" + pkey + " " + product.get(pkey);
                        }
                    }
                }
            }
        }
//        System.out.println(product);
//        System.out.println(nonTerminalSet);
//        System.out.println(terminalSet);
//        for (String i:terminalSet){
//            System.out.println(i);
//        }
//        for (int i=0;i<nonTerminalSet.size();i++){
//            for (int j=0;j<terminalSet.size();j++){
//                if (analysisTable[i][j].length() == 0){
//                    continue;
//                }
//                System.out.println(nonTerminalSet.get(i)+","+terminalSet.get(j)+":"+analysisTable[i][j]);
//            }
//        }
    }

    private void initTable(){
        for (int i=0;i<nonTerminalSet.size();i++){
            for (int j=0;j<terminalSet.size();j++){
                analysisTable[i][j] = "";
            }
        }
    }

    public String getStart() {
        return start;
    }
}
