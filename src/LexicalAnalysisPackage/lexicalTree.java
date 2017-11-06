package LexicalAnalysisPackage;

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
public class lexicalTree {
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
    private lexicalTree lChild;
    private lexicalTree rChild;
    private boolean nullAble = false;
    private List<lexicalTree> firstPos = new ArrayList<>();
    private List<lexicalTree> lastPos = new ArrayList<>();
    private List<lexicalTree> followPos = new ArrayList<>(); //直接构造DFA的四个必要元素
    private static int count = 1;//节点序号赋值量
    private int countId;//从底向上从左到右编号
    private static List<String> follows = new ArrayList<>();
//    private LexicalAnalysisPackage.lexicalTree root;

//    public LexicalAnalysisPackage.lexicalTree getRoot() {
//        return root;
//    }
//
//    public void setRoot(LexicalAnalysisPackage.lexicalTree root) {
//        this.root = root;
//    }

    public lexicalTree(char node) {
        this.node = node;
        this.lChild = null;
        this.rChild = null;
        if (node == '*'){
            nullAble = true;
        }
        if (!judge(node)) {
            this.countId = lexicalTree.count++;
        }
    }

    public lexicalTree(char node, lexicalTree lChild) {
        this.node = node;
        this.lChild = lChild;
        this.rChild = null;
        if (node == '*'){
            nullAble = true;
        }
        if (!judge(node)) {
            this.countId = lexicalTree.count++;
        }
    }

    public lexicalTree(char node, lexicalTree lChild, lexicalTree rChild) {
        this.node = node;
        this.lChild = lChild;
        this.rChild = rChild;
        if (node == '*'){
            nullAble = true;
        }
        if (!judge(node)) {
            this.countId = lexicalTree.count++;
        }
    }

    public int getCountId() {
        return countId;
    }

    public List<lexicalTree> getFollowPos() {
        return followPos;
    }

    public void setFollowPos(List<lexicalTree> followPos) {
        this.followPos.clear();
        this.followPos.addAll(followPos);
    }

    public void setFirstPos(List<lexicalTree> firstPos) {
        this.firstPos.clear();
        this.firstPos.addAll(firstPos);
    }

    public List<lexicalTree> getLastPos() {
        return lastPos;
    }

    public void setLastPos(List<lexicalTree> lastPos) {
        this.lastPos.clear();
        this.lastPos.addAll(lastPos);
    }

    public boolean isNullAble() {
        return nullAble;
    }

    public List<lexicalTree> getFirstPos() {
        return firstPos;
    }

    public char getNode() {
        return node;
    }

    public void setNode(char node) {
        this.node = node;
    }

    public lexicalTree getlChild() {
        return lChild;
    }

    public void setlChild(lexicalTree lChild) {
        this.lChild = lChild;
    }

    public lexicalTree getrChild() {
        return rChild;
    }

    public void setrChild(lexicalTree rChild) {
        this.rChild = rChild;
    }
    //建树
    public static lexicalTree build(String re){//构建语法树
//        System.out.println(re);
        count = 1;
        follows.clear();
        char[] reCharArray = stringAddSomething.operate(re).toCharArray();//为正则添加cat符号，用‘+’表示
//        for (char i:reCharArray){
//            System.out.print(i);
//        }
        Stack<Character> ops = new Stack<>();//操作符栈
        Stack<lexicalTree> sys = new Stack<>();//语法树节点栈
        int i = 0;
        while(true){
            if (reCharArray[i] == '(' || reCharArray[i] == '|' || reCharArray[i] == '+'){
                if ((reCharArray[i] == '+' || reCharArray[i] == '|')&& !ops.isEmpty()){
                    if (ops.peek() == '+'){
                        lexicalTree rChild = sys.pop();
                        lexicalTree lChild = sys.pop();
                        sys.push(new lexicalTree('+', lChild, rChild));
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
                        lexicalTree rChild = sys.pop();
                        lexicalTree lChild = sys.pop();
                        sys.push(new lexicalTree(pop, lChild, rChild));
                    }else {
                        break;
                    }
                    if (ops.peek() == '(') {
                        ops.pop();
                        break;
                    }
                }
            }else if(reCharArray[i] == '*'){
                lexicalTree newRoot = new lexicalTree('*',sys.pop());
                sys.push(newRoot);
            }else if (reCharArray[i] == '#'){
//                for (LexicalAnalysisPackage.lexicalTree tree : sys){
//                    System.out.println(tree.getNode());
//                }
//                for (char ch : ops){
//                    System.out.print(ch);
//                }
                while (!ops.isEmpty()) {//到达末尾应先将栈中所有操作符出栈并且进行相关运算
                    lexicalTree rChild = sys.pop();
                    lexicalTree lChild = sys.pop();
                    sys.push(new lexicalTree(ops.pop(), lChild, rChild));
                }
                lexicalTree tree = new lexicalTree('+',sys.pop(),new lexicalTree('#'));
                tree.buildFirstAndLastPos(tree);
                tree.buildFollowPos(tree);
//                System.out.println(sys.peek());
                showTree(tree);
//                checkFirstPos(tree);
//                checkFollow(tree);
                return tree;
            } else {
                if(i-1<0){
                    sys.push(new lexicalTree(reCharArray[i]));
                } else {
                    sys.push(new lexicalTree(reCharArray[i]));
                }
            }
//            System.out.print("\nre= "+reCharArray[i]+" stack: ");
//            for (char a:ops){
//                System.out.print(a);
//            }
//            System.out.println(reCharArray[i]);
//            System.out.println("ops= "+ops);
//            System.out.print("sys= [");
//            for (LexicalAnalysisPackage.lexicalTree t:sys){
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
    private void buildFirstAndLastPos(lexicalTree tree){//处理firstpos和lastpos
        if(tree != null){
//            System.out.print(tree.getNode());
            buildFirstAndLastPos(tree.getlChild());
            buildFirstAndLastPos(tree.getrChild());
            if (!judge(tree.getNode())){
                List<lexicalTree> temp = new ArrayList<>();
                temp.add(tree);
                tree.setFirstPos(temp);
                tree.setLastPos(temp);
                return;
            }
            if (tree.getNode() == '|'){
                List<lexicalTree> temp = new ArrayList<>();
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
                    List<lexicalTree> temp = new ArrayList<>();
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
                    List<lexicalTree> temp = new ArrayList<>();
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

    private static void checkFirstPos(lexicalTree tree){
        if(tree!=null){
            checkFirstPos(tree.getlChild());
            checkFirstPos(tree.getrChild());
            System.out.print(tree.getNode() + String.valueOf(tree.getCountId()) + ":");
            for (lexicalTree i:tree.getLastPos()){
//                System.out.print(i.getNode());
                System.out.print(i.getCountId());
            }
            System.out.println("");
        }
    }

    private static void checkFollow(lexicalTree tree){
        List<String> f = getFollows(tree);
        for (int i=0;i<f.size();i++){
            System.out.println(i+":"+f.get(i));
        }
    }

    public static void data(lexicalTree tree){//为后面构建DFA取得follows数据
        if (tree != null){
            data(tree.getlChild());
            data(tree.getrChild());
            StringBuilder follow = new StringBuilder();
            follow.append(String.valueOf(tree.getNode()) + ",");
            for (lexicalTree i : tree.getFollowPos()){
                follow.append(String.valueOf(i.getCountId())+",");
            }
            follows.add(follow.toString());
        }
    }

    public static List<String> getFollows(lexicalTree tree){
        follows.clear();
        data(tree);
        return follows;
    }

    private List<lexicalTree> deleteDuplicate(List<lexicalTree> list){//删除重复值
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
    private void buildFollowPos(lexicalTree tree){
        if(tree != null){
            buildFollowPos(tree.getlChild());
            buildFollowPos(tree.getrChild());
            if(!judge(tree.getNode())){
                return;
            }
            if(tree.getNode() == '*'){
                for (lexicalTree node : tree.getLastPos()){
//                    node.setFollowPos(tree.getFirstPos());
                    List<lexicalTree> temp = new ArrayList<>();
                    temp.addAll(node.getFollowPos());
                    temp.addAll(tree.getFirstPos());
                    node.setFollowPos(deleteDuplicate(temp));
                }
            }
            if(tree.getNode() == '+'){
                for (lexicalTree node : tree.getlChild().getLastPos()){
                    List<lexicalTree> temp = new ArrayList<>();
                    temp.addAll(node.getFollowPos());
                    temp.addAll(tree.getrChild().getFirstPos());
                    node.setFollowPos(deleteDuplicate(temp));
                }
            }
        }
    }

    private static void showTree(lexicalTree tree){
        if (!LexicalAnalysis.showData||tree == null){
            return;
        }
        System.out.println("tree:");
        LinkedList<lexicalTree> queue = new LinkedList<>();
        lexicalTree current = null;
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
        lexicalTree test = lexicalTree.build("(e|?)c*(cfc|c)c*");
//        checkFirstPos(test);
//        test.preVisit(test);
//        LexicalAnalysisPackage.lexicalTree a = test.getlChild().getrChild();
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
