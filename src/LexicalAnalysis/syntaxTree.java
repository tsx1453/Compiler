package LexicalAnalysis;

import java.util.*;

/**
 * Created by tsx14 on 2017/10/11.
 * 参考博客：http://www.cnblogs.com/snake-hand/p/3153396.html
 *     
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
public class syntaxTree {
/*
    (  )  *  |  +
  ( >  >  <  <  <
  ) <  <  <  <  <
  * >  <  =  >  >
  | >  <  <  =  <
  + >  <  <  >  =
  */
//    private static final char[][] pre = {{'=','<','<','<','<'},
//                                        {'>','=','>','>','>'},
//                                        {'>','<','=','>','>'},
//                                        {'>','<','<','=','<'},
//                                        {'>','<','<','>','='}
//                                };
    private static final char[] fuhao = {'(',')','*','|','+'};//判断操作符还是字母
    private char node;
    private syntaxTree lChild;
    private syntaxTree rChild;
    private boolean nullAble = false;
    private List<syntaxTree> firstPos = new ArrayList<>();
    private List<syntaxTree> lastPos = new ArrayList<>();
    private List<syntaxTree> followPos = new ArrayList<>(); //直接构造DFA的四个必要元素
    private static int count = 1;//节点序号赋值量
    private int countId;//从底向上从左到右编号
    private static List<String> follows = new ArrayList<>();
//    private LexicalAnalysis.syntaxTree root;

//    public LexicalAnalysis.syntaxTree getRoot() {
//        return root;
//    }
//
//    public void setRoot(LexicalAnalysis.syntaxTree root) {
//        this.root = root;
//    }

    public syntaxTree(char node) {
        this.node = node;
        this.lChild = null;
        this.rChild = null;
        if (node == '*'){
            nullAble = true;
        }
        if (!judge(node)) {
            this.countId = syntaxTree.count++;
        }
    }

    public syntaxTree(char node, syntaxTree lChild) {
        this.node = node;
        this.lChild = lChild;
        this.rChild = null;
        if (node == '*'){
            nullAble = true;
        }
        if (!judge(node)) {
            this.countId = syntaxTree.count++;
        }
    }

    public syntaxTree(char node, syntaxTree lChild, syntaxTree rChild) {
        this.node = node;
        this.lChild = lChild;
        this.rChild = rChild;
        if (node == '*'){
            nullAble = true;
        }
        if (!judge(node)) {
            this.countId = syntaxTree.count++;
        }
    }

    public int getCountId() {
        return countId;
    }

    public List<syntaxTree> getFollowPos() {
        return followPos;
    }

    public void setFollowPos(List<syntaxTree> followPos) {
        this.followPos.clear();
        this.followPos.addAll(followPos);
    }

    public void setFirstPos(List<syntaxTree> firstPos) {
        this.firstPos.clear();
        this.firstPos.addAll(firstPos);
    }

    public List<syntaxTree> getLastPos() {
        return lastPos;
    }

    public void setLastPos(List<syntaxTree> lastPos) {
        this.lastPos.clear();
        this.lastPos.addAll(lastPos);
    }

    public boolean isNullAble() {
        return nullAble;
    }

    public List<syntaxTree> getFirstPos() {
        return firstPos;
    }

    public char getNode() {
        return node;
    }

    public void setNode(char node) {
        this.node = node;
    }

    public syntaxTree getlChild() {
        return lChild;
    }

    public void setlChild(syntaxTree lChild) {
        this.lChild = lChild;
    }

    public syntaxTree getrChild() {
        return rChild;
    }

    public void setrChild(syntaxTree rChild) {
        this.rChild = rChild;
    }
    //建树
    public static syntaxTree build(String re){//构建语法树
//        System.out.println(re);
        count = 1;
        follows.clear();
        char[] reCharArray = stringAddSomething.operate(re).toCharArray();//为正则添加cat符号，用‘+’表示
//        for (char i:reCharArray){
//            System.out.print(i);
//        }
        Stack<Character> ops = new Stack<>();//操作符栈
        Stack<syntaxTree> sys = new Stack<>();//语法树节点栈
        int i = 0;
        while(true){
            if (reCharArray[i] == '(' || reCharArray[i] == '|' || reCharArray[i] == '+'){
                if ((reCharArray[i] == '+' || reCharArray[i] == '|')&& !ops.isEmpty()){
                    if (ops.peek() == '+'){
                        syntaxTree rChild = sys.pop();
                        syntaxTree lChild = sys.pop();
                        sys.push(new syntaxTree('+', lChild, rChild));
                        ops.pop();
                        ops.push(reCharArray[i]);
                    }else {
                        ops.push(reCharArray[i]);
                    }
                }else {
                    ops.push(reCharArray[i]);
                }
            }else if(reCharArray[i] == ')'){
                while(true) {
                    char pop = ops.pop();
                    if (pop == '|' || pop == '+') {
                        syntaxTree rChild = sys.pop();
                        syntaxTree lChild = sys.pop();
                        sys.push(new syntaxTree(pop, lChild, rChild));
                    }else {
                        break;
                    }
                    if (ops.peek() == '(') {
                        ops.pop();
                        break;
                    }
                }
            }else if(reCharArray[i] == '*'){
                syntaxTree newRoot = new syntaxTree('*',sys.pop());
                sys.push(newRoot);
            }else if (reCharArray[i] == '#'){
//                for (LexicalAnalysis.syntaxTree tree : sys){
//                    System.out.println(tree.getNode());
//                }
//                for (char ch : ops){
//                    System.out.print(ch);
//                }
                while (!ops.isEmpty()) {//到达末尾应先将栈中所有操作符出栈并且进行相关运算
                    syntaxTree rChild = sys.pop();
                    syntaxTree lChild = sys.pop();
                    sys.push(new syntaxTree(ops.pop(), lChild, rChild));
                }
                syntaxTree tree = new syntaxTree('+',sys.pop(),new syntaxTree('#'));
                tree.buildFirstAndLastPos(tree);
                tree.buildFollowPos(tree);
//                System.out.println(sys.peek());
                showTree(tree);
//                checkFirstPos(tree);
//                checkFollow(tree);
                return tree;
            } else {
                if(i-1<0){
                    sys.push(new syntaxTree(reCharArray[i]));
                } else {
                    sys.push(new syntaxTree(reCharArray[i]));
                }
            }
//            System.out.print("\nre= "+reCharArray[i]+" stack: ");
//            for (char a:ops){
//                System.out.print(a);
//            }
//            System.out.println(reCharArray[i]);
//            System.out.println("ops= "+ops);
//            System.out.print("sys= [");
//            for (LexicalAnalysis.syntaxTree t:sys){
//                System.out.print(t.getNode());
//            }
//            System.out.println("]");
            i++;
        }
    }
    //判断符号
    public static boolean judge(char ch){//判断符号，字母
        for (char i : fuhao){
            if (i == ch){
                return true;
            }
        }
        return false;
    }
    //此处运算参照龙书第三章相关表格或参考博客
    private void buildFirstAndLastPos(syntaxTree tree){//处理firstpos和lastpos
        if(tree != null){
//            System.out.print(tree.getNode());
            buildFirstAndLastPos(tree.getlChild());
            buildFirstAndLastPos(tree.getrChild());
            if (!judge(tree.getNode())){
                List<syntaxTree> temp = new ArrayList<>();
                temp.add(tree);
                tree.setFirstPos(temp);
                tree.setLastPos(temp);
                return;
            }
            if (tree.getNode() == '|'){
                List<syntaxTree> temp = new ArrayList<>();
                temp.addAll(tree.getlChild().getFirstPos());
                temp.addAll(tree.getrChild().getFirstPos());
                tree.setFirstPos(deleteDuplicate(temp));
                temp.clear();
                temp.addAll(tree.getlChild().getLastPos());
                temp.addAll(tree.getrChild().getLastPos());
                tree.setLastPos(deleteDuplicate(temp));
            }
            if (tree.getNode() == '+'){
                if (tree.getlChild().isNullAble()){
                    List<syntaxTree> temp = new ArrayList<>();
                    temp.addAll(tree.getlChild().getFirstPos());
                    temp.addAll(tree.getrChild().getFirstPos());
                    tree.setFirstPos(deleteDuplicate(temp));
                }else {
                    tree.setFirstPos(tree.getlChild().getFirstPos());
                }
                /*
                    对于lastpos的求法，和firstpos的求法类似，差别只在对于cat结点时候，求法为
                    if( nullable(c2) ){
                        lastpos(c1) 并上 lastpos(c2)
                    } else {
                        lastpos(c2)
                    }
                 */
                if (tree.getrChild().isNullAble()){
                    List<syntaxTree> temp = new ArrayList<>();
                    temp.addAll(tree.getlChild().getLastPos());
                    temp.addAll(tree.getrChild().getLastPos());
                    tree.setLastPos(deleteDuplicate(temp));
                }else {
                    tree.setLastPos(tree.getrChild().getLastPos());
                }
            }
            if (tree.getNode() == '*'){
                tree.setFirstPos(tree.getlChild().getFirstPos());
                tree.setLastPos(tree.getlChild().getFirstPos());
            }
        }
    }

    private static void checkFirstPos(syntaxTree tree){
        if(tree!=null){
            checkFirstPos(tree.getlChild());
            checkFirstPos(tree.getrChild());
            System.out.print(tree.getNode() + String.valueOf(tree.getCountId()) + ":");
            for (syntaxTree i:tree.getLastPos()){
//                System.out.print(i.getNode());
                System.out.print(i.getCountId());
            }
            System.out.println("");
        }
    }

    private static void checkFollow(syntaxTree tree){
        List<String> f = getFollows(tree);
        for (int i=0;i<f.size();i++){
            System.out.println(i+":"+f.get(i));
        }
    }

    public static void data(syntaxTree tree){//为后面构建DFA取得follows数据
        if (tree != null){
            data(tree.getlChild());
            data(tree.getrChild());
            StringBuilder follow = new StringBuilder();
            follow.append(String.valueOf(tree.getNode()) + ",");
            for (syntaxTree i : tree.getFollowPos()){
                follow.append(String.valueOf(i.getCountId())+",");
            }
            follows.add(follow.toString());
        }
    }

    public static List<String> getFollows(syntaxTree tree){
        follows.clear();
        data(tree);
        return follows;
    }

    private List<syntaxTree> deleteDuplicate(List<syntaxTree> list){//删除重复值
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return  list;
    }

    /*
        只有在遇到cat节点和star节点（星号节点）才有必要计算followpos值：
        1. cat节点表示两个节点的连接，所以两个节点中，后一个节点的firstpos集合是前一个节点的lastpos集合中每一个节点的followpos集合。
        2. 对于star节点，考虑到其后可以跟随自身，所以自身节点的firstpos集合是自身节点的lastpos集合中每一个节点的followpos集合。
     */
    private void buildFollowPos(syntaxTree tree){
        if(tree != null){
            buildFollowPos(tree.getlChild());
            buildFollowPos(tree.getrChild());
            if(!judge(tree.getNode())){
                return;
            }
            if(tree.getNode() == '*'){
                for (syntaxTree node : tree.getLastPos()){
//                    node.setFollowPos(tree.getFirstPos());
                    List<syntaxTree> temp = new ArrayList<>();
                    temp.addAll(node.getFollowPos());
                    temp.addAll(tree.getFirstPos());
                    node.setFollowPos(deleteDuplicate(temp));
                }
            }
            if(tree.getNode() == '+'){
                for (syntaxTree node : tree.getlChild().getLastPos()){
                    List<syntaxTree> temp = new ArrayList<>();
                    temp.addAll(node.getFollowPos());
                    temp.addAll(tree.getrChild().getFirstPos());
                    node.setFollowPos(deleteDuplicate(temp));
                }
            }
        }
    }

    private static void showTree(syntaxTree tree){
        if (!SyntaxWord.showData||tree == null){
            return;
        }
        System.out.println("tree:");
        LinkedList<syntaxTree> queue = new LinkedList<>();
        syntaxTree current = null;
        queue.offer(tree);
        int currentNum = 1;
        int next = 0;
        int level = 1;
        while (!queue.isEmpty()){
            current = queue.poll();
            System.out.print(current.getNode()+" ");
            currentNum--;
            if (current.getlChild()!=null){
                queue.offer(current.getlChild());
                next++;
            }
            if (current.getrChild()!=null){
                queue.offer(current.getrChild());
                next++;
            }
            if (currentNum == 0){

                System.out.println();
                currentNum = next;
                next=0;
            }
        }
    }

//    /*
    public static void main(String[] argc){
        syntaxTree test = syntaxTree.build("(e|?)c*(cfc|c)c*");
//        checkFirstPos(test);
//        test.preVisit(test);
//        LexicalAnalysis.syntaxTree a = test.getlChild().getrChild();
//        System.out.println(test.getlChild().getlChild().getlChild().getrChild().getNode());
//        test.buildFirstAndLastPos(test);
//        test.buildFollowPos(test);
//        test.checkFirstPos(test);
//        List<String> f = getFollows(test);
//        for (int i=0;i<f.size();i++){
//            System.out.println(i+":"+f.get(i));
//        }
    }
    // */

}
