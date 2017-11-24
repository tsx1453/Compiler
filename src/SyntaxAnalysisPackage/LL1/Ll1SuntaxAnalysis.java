package SyntaxAnalysisPackage.LL1;    /* 
    creat by tsx14 at 2017/11/15 19:31
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

public class Ll1SuntaxAnalysis {

    public static void main(String[] assda){
        String[] g = {"A->A + T|T",
                "F->( A )|id",
                "T->T * F|F"
        };
        analysis(new ForecastAnalysisTable(g),"id + id * id");
    }
    //龙书P144下部伪代码
    public static void analysis(ForecastAnalysisTable table,String str){
        Stack<String> stack = new Stack<>();
        str += " $";
        String[] data = str.split(" ");
        int index = 0;
        String itemX = table.getStart();
        stack.push("$");
        stack.push(itemX);
        while (!itemX.equals("$")){
            if (itemX.equals(data[index])){
                stack.pop();
                index++;
            }else if (table.terminalSet.contains(itemX)){
                System.out.println("ERROR");
            }else if (table.analysisTable[table.nonTerminalSet.indexOf(itemX)][table.terminalSet.indexOf(data[index])].length() == 0){
                System.out.println("ERROR");
            }else if (table.analysisTable[table.nonTerminalSet.indexOf(itemX)][table.terminalSet.indexOf(data[index])].length() > 0){
                String[] product = table.analysisTable[table.nonTerminalSet.indexOf(itemX)][table.terminalSet.indexOf(data[index])].split("\n")[0].split(" ");
                stack.pop();
                for (int i=product.length-1;i>0;i--){
                    if (!product[i].equals("E")) {
                        stack.push(product[i]);
                    }
                }
            }
            itemX = stack.peek();
//            System.out.println(stack);
        }
        if (itemX.equals(data[index])){
            System.out.println("SUCCESS!");
        }
    }

}
