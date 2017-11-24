package SyntaxAnalysisPackage.LR0;    /* 
    creat by tsx14 at 2017/11/21 19:46
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

import java.util.Stack;

public class LR0SyntaxAnalysis {

    public static void main(String[] asa){
        String[] g = {"A->A + n|n"};
        analysis(new LR0SyntaxAnalysisTable(g),"n+n");
    }
    //根据龙书P160上方伪码
    public static void analysis(LR0SyntaxAnalysisTable table,String str){
        str += "$";
        String[] data = str.split("");
        int index = 0;
        Stack<Integer> stack = new Stack<>();
        stack.push(0);
//        for (String i:table.singleGram){
//            System.out.println(i);
//        }
//        System.out.println(table.table.size());
//        for (String key:table.table.get(2).keySet()){
//            System.out.println(key+":"+table.table.get(2).get(key));
//        }
        while (true){
            String action = table.table.get(stack.peek()).get(data[index]);
//            System.out.println(data[index]);
//            System.out.println(stack);
            if (action.contains("shift")){
                stack.push(Integer.valueOf(action.replace("shift","")));
                index++;
            }else if (action.contains("reduce")){
//                System.out.println(action);
//                System.out.println(Integer.valueOf(action.replace("reduce","")));
                for (int i=0;i<table.singleGram.get(Integer.valueOf(action.replace("reduce",""))).split(" ").length-1;i++){
                    stack.pop();
                }
                action = table.table.get(stack.peek()).get(table.singleGram.get(Integer.valueOf(action.replace("reduce",""))).split(" ")[0]).replace("goto","");
                stack.push(Integer.valueOf(action));
            }else if (action.equals("accept")){
                System.out.print("SUCCEED");
                return;
            }else {
                System.out.println("ERROR");
                return;
            }
        }
    }

}
