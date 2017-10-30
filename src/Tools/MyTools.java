package Tools;

public class MyTools {

    public String[] getAlpha(String str){
        String[] a = str.split("");
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : a){
            if (string.equals("(")||string.equals(")")||string.equals("|")||string.equals("+")||string.equals("*")){
                continue;
            }
            stringBuilder.append(string);
        }
        stringBuilder.append("#");
        StringBuilder stringBuilder1 = new StringBuilder();
        for (String i : stringBuilder.toString().split("")){
            if (stringBuilder1.toString().contains(i)){
                continue;
            }
            stringBuilder1.append(i);
        }
        return stringBuilder1.toString().split("");
    }//从正则表达式中获取不重复的string数组

    public static void main(String[] a){
        MyTools myTools = new MyTools();
        for (String s:myTools.getAlpha("(c|b)*(a|c|b|)*")){
            System.out.print(s);
        }
    }

}
