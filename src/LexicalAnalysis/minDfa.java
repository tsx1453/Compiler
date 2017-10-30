package LexicalAnalysis;

import java.util.*;

public class minDfa {

    private int staId;
    private int[] to;
    private boolean end;

    public minDfa() {
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd() {
        this.end = true;
    }

    public int getStaId() {
        return staId;
    }

    public void setStaId(int staId) {
        this.staId = staId;
    }

    public int[] getTo() {
        return to;
    }

    public void setTo(int[] to) {
        this.to = to;
    }

    public minDfa(int staId) {
        this.staId = staId;
        this.end = false;
    }
    //构建最小化DFA,具体操作展开可见
    public static List<minDfa> build(List<Graph> dfa){
        List<minDfa> minDfaList = new ArrayList<>();
        for (Graph g:dfa){
            minDfa newNode = new minDfa(dfa.indexOf(g));
            newNode.setTo(Graph.getStaNum(g,dfa));
            if (g.getStatu().isEnd()){
                newNode.setEnd();
            }
            minDfaList.add(newNode);
        }//把原本的复杂的用字符串表示的状态转换为简单的用数字表示的状态的DFA
//        System.out.println(dfa.size());
        if (SyntaxWord.showData){
            for (minDfa i:minDfaList){
                System.out.print(i.staId);
                System.out.print(":");
                for (int j:i.to){
                    System.out.print(j);
                }
                System.out.println(i.isEnd());
            }
            System.out.println();
        }
        /*
        两个List<int>链表，第一次手动根据是否为终态手动划分，将状态装入l1,然后根据状态转移的条件来划分，
        从第一个逐个往后判断，第一个直接装入list的第一个位置，并用一个变量last记录指向的状态所在分区的区号（即在表中位置）
        遍历后面每一个状态，分别对其所指向状态的分区区号与last比较，相同装入上一个元素装入位置，不同装入新位置，直到两个链
        表元素完全一样,结束循环
         */
        ArrayList<intList>[] list = (ArrayList<intList>[]) new ArrayList[2];
//        List<LexicalAnalysis.intList> list2 = new ArrayList<>();
        list[0] = new ArrayList<>();
        list[1] = new ArrayList<>();
        list[0].add(new intList());
        list[0].add(new intList());
        for (minDfa i:minDfaList){
            if (i.isEnd()){
                list[0].get(1).data.add(i.getStaId());
            }else {
                list[0].get(0).data.add(i.getStaId());
            }
        }
        for (int i = 0;i<list[0].size();i++){
            if (list[0].get(i).data.size() == 0){
                list[0].remove(i);
            }
        }
//        showList(list[0],0);
        int last = -1;
        int index = 1;
        int alphaNumber = DFA.allAlphaWithoutSame.length-1;
        int alpha = 0;
        while (!isSame(list[0],list[1])){
//            System.out.println("?");
//            System.out.println("index="+index);
//            showList(list[0],0);
//            showList(list[1],1);
//            list[index].add(new LexicalAnalysis.intList());
//            int size = list[index].size()-1;
//            list[index].get(size).data.add(0);
//            last = getNextStateUnin(list[(index + 1)%2],minDfaList,0,)
//            for (String i:LexicalAnalysis.DFA.allAlphaWithoutSame){
//                System.out.print(LexicalAnalysis.DFA.allAlphaWithoutSame.length);
//            }

            for (intList i:list[index]){
                i.data.clear();
            }
            list[index].clear();
//            System.out.println("list"+index+" cleared!\nnum"+alpha);
            for (int i=0;i<minDfaList.size();i++){
                int now = getNextStateUnin(list[(index+1)%2],minDfaList,i,alpha);
                if (now == -1){
                    list[index].add(new intList());
                    int size = list[index].size()-1;
                    list[index].get(size).data.add(i);
                    last = -1;
                    continue;
                }
//                System.out.println("now="+now+" last="+last);
                if (now != last){
                    list[index].add(new intList());
                    int size = list[index].size()-1;
                    list[index].get(size).data.add(i);
                    last = now;
//                    System.out.println("list"+index+" add "+i+" at "+size);
                }else {
                    if (togetherWithLast(i,i-1,list[index])) {
                        list[index].get(list[index].size() - 1).data.add(i);
                    }else {
                        list[index].add(new intList());
                        int size = list[index].size()-1;
                        list[index].get(size).data.add(i);
                    }
//                    System.out.println("list"+index+" add "+i+" at "+(list[index].size()-1));
                }
            }
            last = -1;
            if (list[index].size()>=list[(index+1)%2].size()){
                index = (index+1)%2;
            }else {
                list[index] = list[(index+1)%2];
            }
//            if (alpha+1 == 2){
//                break;
//            }
            alpha = (alpha+1)%alphaNumber;
        }
//        showList(list[0],-1);
        List<minDfa> finalDfa = new ArrayList<>();
        for (int i=0;i<list[0].size();i++){
            minDfa newNode = new minDfa(i);
            newNode.setTo(getFinalTo(list[0],minDfaList,i));
            if (isEnd(list[0].get(i),minDfaList)){
                newNode.setEnd();
            }
            finalDfa.add(newNode);
        }
//        for (String a:LexicalAnalysis.DFA.allAlphaWithoutSame){
//            System.out.print(a);
//        }
        if (SyntaxWord.showData){
            System.out.println("\nfinal data");
            for (minDfa i:finalDfa){
                System.out.print(i.staId);
                System.out.print(":");
                for (int j:i.to){
                    System.out.print(j);
                }
                System.out.println("");
            }
        }
//        String[] alphaSet = LexicalAnalysis.DFA.allAlphaWithoutSame;
//        int[][] data = new int[finalDfa.size()][alphaSet.length-1];
//        for (int i=0;i<finalDfa.size();i++){
//            for (int j=0;j<alphaSet.length-1;j++){
//                data[i][j] = finalDfa.get(i).to[j];
//            }
//        }
//        return data;
        return finalDfa;
    }
    //判断是否是可接受状态
    private static boolean isEnd(intList list,List<minDfa> dfas){
        for (int i:list.data){
            if (dfas.get(i).isEnd()){
                return true;
            }
        }
        return false;
    }

    private static int[] getFinalTo(List<intList> list,List<minDfa> dfa,int index){
        //将原本的dfa合并之后转换成新的to
//        System.out.println(dfa.size()+" "+list.size()+" "+list.get(index).data.size());
        int[] to = new int[dfa.get(list.get(index).data.get(0)).getTo().length];
        for (int i=0;i<to.length;i++){
            to[i] = getNextStateUnin(list,dfa,list.get(index).data.get(0),i);
        }
        return to;
    }

    private static void showList(List<intList> list,int id){
        if (id != -1) {
            System.out.println("\nlist" + id + ":");
        }else {
            System.out.println("LexicalAnalysis.minDfa set:");
        }
        for (intList i:list){
            System.out.print("id="+list.indexOf(i)+":");
            for (int a:i.data){
                System.out.print(a);
            }
            System.out.println();
        }
    }

    private static int getNextStateUnin(List<intList> list,List<minDfa> minDfas,int state,int be){
        if (be >= minDfas.get(state).to.length){
            return -1;
        }
        int next = minDfas.get(state).to[be];//找出当前状态对应可到达状态在list中的区域
//        System.out.println("minDfas size="+minDfas.size()+" to size="+minDfas.get(state).to.length+" be="+be);
        for (int i=0;i<list.size();i++){
            for (int j:list.get(i).data){
                if (next == j){
//                    System.out.println("state="+state+" be="+be+" next="+next+" j="+j+" i="+i);
                    return i;
                }
            }
        }
        return -1;
    }

    private static boolean togetherWithLast(int a,int b,List<intList> list){
        int aIndex = 0;
        int bIndex = 0;
        for (int i=0;i<list.size();i++){
            for (int j=0;j<list.get(i).data.size();j++){
                if (list.get(i).data.get(j) == a){
                    aIndex = i;
                }
                if (list.get(i).data.get(j) == b){
                    bIndex = i;
                }
            }
        }
        if (aIndex == bIndex){
            return true;
        }
        return false;
    }

    public static boolean isSame(List<intList> list1,List<intList> list2){
        if (list1.size() != list2.size()){
            return false;
        }
        for (int i=0;i<list1.size();i++){
            if (list1.get(i).data.size() != list2.get(i).data.size()){
                return false;
            }
            for (int j=0;j<list1.get(i).data.size();j++){
                if (list1.get(i).data.get(j) != list2.get(i).data.get(j)){
                    return false;
                }
            }
        }
        return true;
    }

    public int getNext(int al){
        return this.to[al];
    }

    public static void main(String[] a){
        List<minDfa> dfa = build(DFA.build("c*"));
//        System.out.println(dfa.get(dfa.get(1).getNext(2)).isEnd());
    }
}

class intList{//最小化DFA时用于辅助的类

    public List<Integer> data;

    public intList() {
        this.data = new ArrayList<>();
    }
}
