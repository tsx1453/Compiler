package LexicalAnalysis;   /*
    creat by tsx14 at 2017/10/22 22:03
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

import Tools.MyTools;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
public class draw {

    private List<minDfa> dfa = null;
    private String[] alpha = null;

    public static void main(String[] a){

        draw d = new draw(reText.identlfierRe);
        d.init();
    }


       public draw() {
       }

       public draw(String re) {
           this.dfa = minDfa.build(DFA.build(re));
           this.alpha = new MyTools().getAlpha(re);
       }

       private void init(){
        JFrame frame = new JFrame();
        frame.setSize(500,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        Mypanel mypanel = new Mypanel();
        frame.add(mypanel);

    }

    class Mypanel extends JPanel{
        public void paint(Graphics g){
            super.paint(g);
//            g.drawRect(10,10,100,75);
//            g.drawRect(350,10,100,75);
//            Node(100,10,g,"a");
//            Node(350,10,g,"b");
//            Line(90,10,350,10,g,"ads");
//            selfToSelf(430,10,"qqq",g);
            dfaGraph(g);
        }

        private void dfaGraph(Graphics g){
            int total = dfa.size();
            List<data> dataList = new ArrayList<>();
            for (minDfa i:dfa){
                int num = dfa.indexOf(i);
                System.out.println(i.isEnd());
                if (num%2==0){
//                    Node(100,num*50+50,g,String.valueOf(i.getStaId()));
                    dataList.add(new data(100,(num+1)*50,i.isEnd()));
                }else {
//                    Node(280,num*50,g,String.valueOf(i.getStaId()));
                    dataList.add(new data(280,num*50,i.isEnd()));
                }
            }
            for (data i:dataList){
                Node(i.x,i.y,g,String.valueOf(dataList.indexOf(i)),i.end);
            }
            for (minDfa i:dfa){
                int count = 0;
                StringBuilder stringBuilder = new StringBuilder();
                data my = dataList.get(dfa.indexOf(i));
                int num = dfa.indexOf(i);
                int[] to = i.getTo();
                for (int j=0;j<to.length;j++){
                    if (to[j]==-1){
                        continue;
                    }
                    if (num == to[j]){
                        stringBuilder.append(alpha[j]);
                        continue;
                    }
                    data todata = dataList.get(to[j]);
                    if (num%2==0) {
                        Line(my.x + 80, my.y + (my.count++) * 10, todata.x, todata.y + (todata.count++) * 10, g, alpha[j]);
                    }else {
                        Line(my.x,my.y+(my.count++)*10,todata.x+80,todata.y+(todata.count++)*10,g,alpha[j]);
                    }
                    count++;
                }
                if (stringBuilder.length() == 0){
                    continue;
                }
                if (num%2==0){
                    selfToSelf(my.x-40,my.y,stringBuilder.toString(),g);
                }else {
                    selfToSelf(my.x+80,my.y,stringBuilder.toString(),g);
                }
            }
        }

        private void Line(int x1,int y1,int x2,int y2,Graphics g,String because){
            g.drawLine(x1,y1,x2,y2);
            g.setColor(Color.magenta);
            g.fillOval(x2,y2,5,5);
            g.setColor(Color.black);
            g.drawString(because,(x1+x2)/2,(y1+y2)/2);
        }

        private void Node(int x,int y,Graphics g,String name,boolean end){
            g.setColor(Color.orange);
            g.fillRect(x,y,80,50);
            g.setColor(Color.black);
            g.drawString(name,(2*x+80)/2,(2*y+50)/2);
            if (end){
                g.drawRect(x+10,y+10,60,30);
            }
        }

        private void selfToSelf(int x,int y,String str,Graphics g){
            g.drawOval(x,y,40,40);
            g.drawString(str,(2*x+20)/2,(2*y+40)/2);
        }
    }

    class data{
        public int x;
        public int y;
        public int count;
        public boolean end;

        public data(int x, int y,boolean end) {
            this.x = x;
            this.y = y;
            this.count = 0;
            this.end = end;
        }
    }

}
