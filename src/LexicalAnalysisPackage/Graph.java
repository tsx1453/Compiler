package LexicalAnalysisPackage;

import java.util.ArrayList;
import java.util.List;

public class Graph {//DFA图

    private DFA statu;
    private List<DFA> toWhere;
    private List<String> because;

    public DFA getStatu() {
        return statu;
    }

    public void setStatu(DFA statu) {
        this.statu = statu;
    }

    public List<DFA> getToWhere() {
        return toWhere;
    }

    public void setToWhere(List<DFA> toWhere) {
        this.toWhere = toWhere;
    }

    public List<String> getBecause() {
        return because;
    }

    public void setBecause(List<String> because) {
        this.because = because;
    }

    public Graph(DFA statu) {
        this.statu = statu;
        this.toWhere = new ArrayList<>();
        this.because = new ArrayList<>();
    }

    public void set(String because,DFA to){
        int index = 0;
        String[] alpha = DFA.allAlphaWithoutSame;
        for (int i=0;i<alpha.length;i++){
            if (alpha[i].equals(because)){
                index = i;
                break;
            }
        }
        for (int i=0;i<index;i++){
            if (!this.because.contains(alpha[i])){
                this.because.add(alpha[i]);
                this.toWhere.add(new DFA("null"));
            }
        }
        this.because.add(because);
        this.toWhere.add(to);
    }

    public void check(){
        int index = 0;
        String[] alpha = DFA.allAlphaWithoutSame;
        for (int i=0;i<alpha.length;i++){
            if (!this.because.contains(alpha[i])){
                index = i;
                break;
            }
        }
        for (int i=index;i<alpha.length-1;i++){
            this.because.add(alpha[i]);
            this.toWhere.add(new DFA("null"));
        }
    }

    public int getSize(){
        return toWhere.size();
    }

    public void showData(){
        System.out.print(statu.getData() + " : ");
        for (int i = 0;i<getSize();i++){
            System.out.print(because.get(i) + "->");
            System.out.print(toWhere.get(i).getData() + ",");
        }
    }

    public static int[] getStaNum(Graph g,List<Graph> dfa){
        int[] num = new int[g.toWhere.size()];
        for (int i=0;i<g.toWhere.size();i++){
            for (int j=0;j<dfa.size();j++){
                if (g.toWhere.get(i).getData().equals("null")){
                    num[i] = -1;
                } else if (g.toWhere.get(i).getData().equals(dfa.get(j).getStatu().getData())){
                    num[i] = j;
                    break;
                }
            }
        }
        return num;
    }
}
