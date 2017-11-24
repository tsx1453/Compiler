package SyntaxAnalysisPackage.LL1;    /*
    creat by tsx14 at 2017/11/13 19:10
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

public class FirstAndFollows {

    private Map<String,String> grammers;
    private Set<String> nonTerminalSet = null;
    private Map<String,HashSet<String>> firsts = null;
    private Map<String,HashSet<String>> follows = null;
    private Map<String,String> singleGram = null;
    private String startS = null;
    private List<String> productKey;

    public FirstAndFollows(String[] gra) {
        Build(gra);
    }

    public void Build(String[] gra){
        grammers = GrammarProcess.run(gra);
        startS = Start(gra);
        nonTerminalSet = new HashSet<>();
        productKey = new ArrayList<>();
        firsts = new HashMap<>();
        follows = new HashMap<>();
        singleGram = new IdentityHashMap<>();
        nonTerminalSet.addAll(grammers.keySet());
        Firsts();
        Follows();
//        System.out.println(grammers.keySet());
    }

    private static void main(String[] asa){
        String[] g = {"A->T A’",
                "A’->+ T A’|E",
                "T->F T’",
                "T’->* F T’|E",
                "F->( A )|i"
        };
        FirstAndFollows test = new FirstAndFollows(g);
    }

    public String Start(String[] g){
        String start = "";
        for (String i:g[0].replace("->","|").split("\\|")){
            if (i.length() == 0){
                continue;
            }
            start = i;
            break;
        }
        return start;
    }

    private void Firsts(){
        for (String i:nonTerminalSet){
            buildFirsts(i);
//            System.out.println(i);
        }
//        System.out.println("First:");
//        for (String i:firsts.keySet()){
//            System.out.println(i + " : " + firsts.get(i));
//        }
    }

    private void buildFirsts(String nowKey){
        String[] grams = grammers.get(nowKey).split("\\|");
        for (String i:grams){
            String firStr = i.split(" ")[0];
            HashSet<String> s = new HashSet<>();
            if (!nonTerminalSet.contains(firStr)){
                s.add(firStr);
            }else {
                buildFirsts(firStr);
                s.addAll(firsts.get(firStr));
            }
            if (firsts.containsKey(nowKey)){
                s.addAll(firsts.get(nowKey));
            }
            firsts.put(nowKey,s);
        }
    }

    public List<String> getProductKey() {
        return productKey;
    }

    private void Follows(){
        HashSet<String> teSet = new HashSet<>();
        teSet.add("$");
//        System.out.println(startS);
        follows.put(startS,teSet);
        for (String key:grammers.keySet()){
            for (String i:grammers.get(key).split("\\|")){
                if (i.length() == 0){
                    continue;
                }
                String tempKey = new String(key);
                singleGram.put(tempKey,i);
                productKey.add(tempKey);
            }
        }
//        System.out.println("*************************************");
//        for (String i:singleGram.keySet()){
//            System.out.println(i + "->" + singleGram.get(i));
//        }
//        System.out.println("*************************************");
        boolean flag = true;
        while (flag) {
            flag = false;
            for (String i : nonTerminalSet) {
                if (buildFollows(i)){
                    flag = true;
                }
            }
        }
//        System.out.println("Follows:");
//        for (String i:nonTerminalSet){
//            System.out.println(i + " : " + follows.get(i));
//        }
    }

    private boolean buildFollows(String nowKey){
        HashSet<String> s = new HashSet<>();
        for (String i:singleGram.keySet()){
            if (singleGram.get(i).contains(nowKey)){
                String[] grams = singleGram.get(i).split(" ");
                for (int j = 0;j < grams.length;j++){
                    if (grams[j].equals(nowKey)){
                        if (j == grams.length - 1){
//                            buildFollows(i);
                            if (follows.containsKey(i)) {
                                s.addAll(follows.get(i));
                            }
                        }else {
                            if (nonTerminalSet.contains(grams[j+1])){
                                HashSet<String> first = new HashSet<>();
                                first.addAll(firsts.get(grams[j+1]));
                                if (first.contains("E")){
//                                    buildFollows(i);
                                    if (follows.containsKey(i)) {
                                        s.addAll(follows.get(i));
                                    }
                                    first.remove("E");
                                    s.addAll(first);
                                }else {
                                    s.addAll(first);
                                }
                            }else {
                                s.add(grams[j+1]);
                            }
                        }
                    }
                }
            }
        }
        int oldLen = -1;
        if (follows.containsKey(nowKey)){
            s.addAll(follows.get(nowKey));
            oldLen = follows.get(nowKey).size();
        }
        follows.put(nowKey,s);
        return oldLen < follows.get(nowKey).size();
    }

    public Set<String> getNonTerminalSet() {
        return nonTerminalSet;
    }

    public Map<String, HashSet<String>> getFirsts() {
        return firsts;
    }

    public Map<String, HashSet<String>> getFollows() {
        return follows;
    }

    public Map<String, String> getSingleGram() {
        return singleGram;
    }

    public String getStartS() {
        return startS;
    }

    public Map<String, String> getGrammers() {
        return grammers;
    }
}
