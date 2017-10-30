package LexicalAnalysis;

import java.util.*;

public class DFA {
    
    private boolean states;
    private String data;
    private boolean end;
    public static String[] allAlphaWithoutSame;

    public boolean isEnd() {
        return end;
    }

    public void setEnd() {
        this.end = true;
    }

    public boolean isStates() {
        return states;
    }

    public void setStates() {
        this.states = true;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public DFA(String data) {
        this.data = data;
        this.states = false;
        this.end = false;
    }

    public static List<Graph> build(String regex){
        String[] alpha = getAlpha(regex);
        String[] alphaWithoutSame = getAlphaWithoutSame(alpha);
        allAlphaWithoutSame = null;
        allAlphaWithoutSame = alphaWithoutSame;
        syntaxTree tree = syntaxTree.build(regex);
        List<String> follows = cleaData(syntaxTree.getFollows(tree));
//        for (String str : alpha){
//            System.out.println(str);
//        }
        List<DFA> Dstates = new ArrayList<>();
//        Dstates.add(new LexicalAnalysis.DFA());
//        System.out.println(tree.getFirstPos());
        StringBuilder first = new StringBuilder();
        for (syntaxTree i : tree.getFirstPos()){
            first.append(String.valueOf(i.getCountId()));
        }
//        System.out.println(alphaWithoutSame);
        DFA a = new DFA(strSort(first.toString()));
        if (isEnd(first.toString(),alpha)){
            a.setEnd();
        }
        Dstates.add(a);
        int index = getUbCheckedItem(Dstates);
        List<Graph> dfaGraph = new ArrayList<>();
        dfaGraph.add(new Graph(Dstates.get(0)));
        while (index != -1){
            Dstates.get(index).setStates();
            for (String i : alphaWithoutSame){
                List<Integer> posions = getPosions(i,Dstates.get(index).getData(),alpha);
                StringBuilder newU = new StringBuilder();
                for (int j : posions){
                    if ((j-1)<follows.size()) {
                        newU.append(follows.get(j - 1));
                    }
                }
                String strU = strSort(setToString(getAlphaWithoutSame(newU.toString().split(""))));
                if (strU.length() == 0){
                    continue;
                }
                DFA U = new DFA(strU);
                if (isInDstates(strU,Dstates)){
                    if (isEnd(strU,alpha)){
                        U.setEnd();
                    }
                    Dstates.add(U);
                    dfaGraph.add(new Graph(U));
                }
                dfaGraph.get(index).set(i,U);
//                System.out.println(setToString(getAlphaWithoutSame(newU.toString().split(""))));
            }
            index = getUbCheckedItem(Dstates);
        }
        for (int i=0;i<dfaGraph.size();i++){
            dfaGraph.get(i).check();
        }
        if (SyntaxWord.showData){
            System.out.println("dfa:");
            for (Graph graph:dfaGraph){
                graph.showData();
                System.out.println(graph.getStatu().isEnd());
            }
        }
        return dfaGraph;
    }

    private static boolean isEnd(String sta,String[] alpha){
        for (String a : sta.split("")){
//            System.out.println(alpha[Integer.valueOf(a) - 1]);
            if (a.length() == 0){
                continue;
            }
            int va = Integer.valueOf(a);
            if (va == 0){
                continue;
            }
            if (alpha[va - 1].equals("#")){
//                System.out.println(alpha[Integer.valueOf(a) - 1]);
                return true;
            }
        }
        return false;
    }

    private static boolean isInDstates(String dfa,List<DFA> Dstates){
        for (DFA d : Dstates){
            if (stringComplete(d.getData(),dfa)){
                return false;
            }
        }
        return true;
    }

    private static boolean stringComplete(String a,String b){
        List<Integer> an = getNumber(a);
        List<Integer> bn = getNumber(b);
        if (an.size() != bn.size()){
            return false;
        }
        for (int i=0;i<an.size();i++){
            if (an.get(i) != bn.get(i)){
                return false;
            }
        }
        return true;
    }

    private static List<Integer> getNumber(String str){
        List<Integer> list = new ArrayList<>();
        for (String i : str.split("")){
            if (i.length() == 0){
                continue;
            }
            list.add(Integer.valueOf(i));
        }
        list.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 > o2){
                    return 1;
                }
                else return -1;
            }
        });
        return list;
    }

    private static String setToString(String[] a){
        StringBuilder stringBuilder = new StringBuilder();
        for (String i : a){
            stringBuilder.append(i);
        }
        return stringBuilder.toString();
    }

    private static List<Integer> getPosions(String str,String data,String[] alpha){
        String[] datas = data.split("");
        List<Integer> list = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        for (int i = 0;i<alpha.length;i++){
            if (alpha[i].equals(str)){
                list.add(i+1);
            }
        }
        for (String s : datas){
            for (int i : list){
                if (s.length() == 0){
                    break;
                }
                if (Integer.valueOf(s) == i){
                    list2.add(i);
                }
            }
        }
        return list2;
    }

    private static int getUbCheckedItem(List<DFA> state){
        for (int i=0;i<state.size();i++){
            if (!state.get(i).isStates()){
                return i;
            }
        }
        return -1;
    }

    private static String[] getAlpha(String str){
        String[] a = str.split("");
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : a){
            if (string.equals("(")||string.equals(")")||string.equals("|")||string.equals("+")||string.equals("*")){
                continue;
            }
            stringBuilder.append(string);
        }
        stringBuilder.append("#");
        return stringBuilder.toString().split("");
    }

    private static String[] getAlphaWithoutSame(String[] a){
        StringBuilder stringBuilder = new StringBuilder();
        for (String i : a){
            if (stringBuilder.toString().contains(i)){
                continue;
            }
            stringBuilder.append(i);
        }
        return stringBuilder.toString().split("");
    }

    private static List<String> cleaData(List<String> list){
        List<String> temp = new ArrayList<>();
        for (String str : list){
//            System.out.println(str);
            if (syntaxTree.judge(str.toCharArray()[0])){
                continue;
            }
            String[] a = str.split(",");
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 1;i < a.length; i++){
                stringBuilder.append(a[i]);
            }
            temp.add(stringBuilder.toString());
        }
        return temp;
    }

    public static DFA Donate(String be,String state,List<Graph> list){
        state = strSort(state);
        int index = getDfaIndex(list,state);
        if (index == -1){
            System.out.println("error");
            return null;
        }
//        System.out.println(list.get(index).getToWhere().get(list.get(index).getBecause().indexOf(be)).getData());
        return list.get(index).getToWhere().get(list.get(index).getBecause().indexOf(be));
    }

    private static int getDfaIndex(List<Graph> list,String state){
        for (int i = 0;i<list.size();i++){
//            System.out.println(state + ":" + list.get(i).getStatu().getData());
            if (state.equals(list.get(i).getStatu().getData())){
                return i;
            }
        }
        return -1;
    }

    private static String strSort(String str){
        List<Integer> numberList = new ArrayList<>();
        for (String s : str.split("")){
            if (s.length() == 0){
                continue;
            }
            numberList.add(Integer.valueOf(s));
        }
        numberList.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 > o2){
                    return 1;
                }else {
                    return -1;
                }
            }
        });
        StringBuilder stringBuilder = new StringBuilder();
        for (int i : numberList){
            stringBuilder.append(String.valueOf(i));
        }
        return stringBuilder.toString();
    }

    public static void main(String[] argc){
        List<Graph> dfa = build(reText.digitRe);
//        System.out.println(dfa.get(0).getStatu() );
//        System.out.println(Donate("b","1236",dfa));
//        Donate("b","2135",dfa);
//        System.out.println(strSort("3215"));
//        for (int i:getPosions("a","123","ababb#".split(""))){
//            System.out.println(i);
//        }
//        System.out.println(stringComplete("123","321"));
    }


}


