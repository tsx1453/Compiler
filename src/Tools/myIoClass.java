package Tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by tsx14 on 2017/10/9.
 */
public class myIoClass { //文本操作的类

    public String read(String path){
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String temp;
            while ((temp = bufferedReader.readLine()) != null){
                sb.append(temp+"\n");
            }
            bufferedReader.close();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
//        System.out.println("read success");
        return sb.toString();
    }

    public void write(String path,String data){
//        System.out.println(data);
        try {
            FileWriter fileWriter = new FileWriter(path);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(data);
            bufferedWriter.close();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
//        System.out.println("write success");
    }
///*
    public static void main(String[] argc){
//        write("./test2.txt",String.valueOf(read("E:\\JAVA\\CompilerCoding\\out\\production\\CompilerCoding/test.txt")));
//        System.out.println(String.valueOf(read("E:\\JAVA\\CompilerCoding\\out\\production\\CompilerCoding/test.txt")));
//        System.out.println("asd d".split(""));
//        for (String a:"for(int i=0;i<0 ;i++)".split("")){
//            System.out.println(a);
//        }
//        System.out.println(new Tools.myIoClass().read("test.pt").split("\n")[0]);
        System.out.println("'");
    }
    //*/
}
