package SyntaxAnalysisPackage.LR0;    /*
    creat by tsx14 at 2017/11/20 21:17
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

public class DFA {

    public static int count = 0;
    public int id;//每个DFA自己带有一个标号
    public List<String> items;
    public Map<String,DFA> nexts;//邻接链表

    public DFA() {
        id = count++;
//        System.out.println(id);
        this.items = new ArrayList<>();
        this.nexts = new HashMap<>();
    }
}
