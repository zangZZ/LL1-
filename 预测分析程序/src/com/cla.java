package com;

import com.sun.org.apache.regexp.internal.REUtil;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import java.rmi.MarshalledObject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/31.
 */
public class cla {
    public grammar g[];
    public String [] Vt,Vn = new String[20];
    public String [][] result;
    public int num_n=0,num_t=0;
    public Map<String,grammar> map = new LinkedHashMap<>();
    public analysis a;

    public void init(String str){
        Map<String,String> map = new LinkedHashMap<>();
        String string = "";
        String string_ = "";
        String list[] = str.split("\\n");
        for(int i = 0;i<list.length;i++){
                String ss[] = list[i].substring(4).split("\\|");
                if (ss.length > 1)
                    for (String s : ss) {
                        if (s.charAt(0) == list[i].charAt(0))
                            string_ += s.substring(1) + list[i].charAt(0)+"`" + "|";
                        else if(s.charAt(0) == 'ε')
                            string +=s.charAt(0)+ "|";
                          else string += s + list[i].charAt(0)+"`" + "|";
                    }
            if(string_ != "") {
                    map.put(list[i].charAt(0)+"",string);
                    map.put(list[i].charAt(0)+"`",string_+"ε");
                    Vn[num_n++] = list[i].charAt(0)+"";
                    Vn[num_n++] = list[i].charAt(0)+"`";
                    string_ = "";
                    string = "";
            }else{
                    map.put(list[i].charAt(0)+"",list[i].substring(4));
                    Vn[num_n++] = list[i].charAt(0)+"";
                }
        }
        g = new grammar[map.entrySet().size()];
        int i = 0;
        for(Map.Entry<String, String> entry : map.entrySet()){
            System.out.println(entry.getKey()+"::="+entry.getValue());
            g[i] =new grammar(entry.getKey()+"::="+entry.getValue());
            this.map.put(entry.getKey(),g[i++]);
        }
    }

    public void hasFollow(){
        for(grammar g:this.g){
            for(String s:g.getFirst(map)){
                if(s != null&&s.equals("ε")) {
                    getFollow(g.C);
                    String list[] = g.follow.split("\\s+");
                    for(String ss:list)
                        g.mapFollow.put(ss," ");
                }
            }
        }
    }

    public cla(String str){
        this.init(str);
        System.out.println(this.g[4].hasnull);
        this.hasEnd();
        this.hasFollow();
        this.getVt();
        this.makeResult();
      //  this.a = new analysis("i+i*i",this.g[0].C,this.map);
    }

    public void getVt(){
        Map<String,String> m = new HashMap();
        String str = "";
        for(grammar g:this.g)
            for(String s[]:g.section)
                for(String ss:s)
                    if(!map.containsKey(ss)&&!ss.equals("ε"))
                        m.put(ss,"");
        Vt = new String[m.size()+1];
        int i = 0;
        for(Map.Entry<String,String> entry:m.entrySet())
            Vt[i++] = entry.getKey();
        Vt[i] = "#";
    }

    public String getFollow(String c){
        String sb = "";
        boolean istrue = false;
        grammar g1;
        int m = 0;
        for(grammar g:this.g){
            for(String s[]:g.section){
                for(int i = 0;i<s.length;i++){
                    if(s[i].equals(c)&&i != s.length-1) {
                        m = i+1;
                        while (m != s.length){
                            if(map.containsKey(s[m])&&map.get(s[m]).hasnull) {
                               if(m == s.length-1)
                                   istrue = true;
                                m++;
                            }
                            else break;
                        }
                    }else if(s[i].equals(c)&&i == s.length-1)
                            istrue = true;
                    if(s[i].equals(c)&&istrue&&!g.C.equals(c)){
                        sb += this.getFollow(g.C);
                        sb += map.get(g.C).follow;
                    }
                    if(s[i].equals(c)&&i != s.length-1) {
                        if (!map.containsKey(s[i + 1]))
                            sb += s[i+1]+" ";
                        else {
                            g1 = map.get(s[i+1]);
                            for (String ss : g1.getFirst(map)) {
                                if(ss != null&&!ss.equals("ε"))
                                    sb += ss+" ";
                            }
                            m = i;
                            while(m+1 !=s.length-1&&map.containsKey(s[m+1])&&map.get(s[m+1]).hasnull){
                                g1 = map.get(s[m+1]);
                                for (String ss : g1.getFirst(map)) {
                                    if(ss != null&&!ss.equals("ε"))
                                       // sb.append(ss + " ");
                                        sb += ss+" ";
                                }
                                if(m+2 != s.length-1&&!map.containsKey(s[m+2])) {
                                    //sb.append(s[m+2]);
                                    sb += s[m+2];
                                    break;
                                }
                                m++;
                            }
                        }
                    }
                }
            }
        }
        map.get(c).follow += sb;
        return sb;
    }

    public void hasEnd(){
        for(String s[]:g[0].section)
            for(int i = s.length-1;i >= 0;i--) {
                if (map.containsKey(s[i]) && s[i].length() == 2) {
                    map.get(s[i]).follow += " # ";
                }
                if (map.containsKey(s[i]) && s[i].length() == 1) {
                    map.get(s[i]).follow += " # ";
                    break;
                }
            }
    }

    public void makeResult(){
        result = new String[map.size()+1][Vt.length+1];
        result[0][0] = "";
        for(int m = 0;m<result.length;m++) {
            for (int n = 0; n < result[m].length; n++) {
                if(m == 0&&n ==0){
                    result[0][0] = "";
                  //  System.out.print(result[m][n]+"\t\t\t");
                }
                if (m == 0 && n != 0) {
                    result[m][n] = Vt[n - 1];
                //    System.out.print(result[m][n]+"\t\t\t");
                }
                if (m != 0 && n == 0) {
                    result[m][n] = g[m - 1].C;
                //    System.out.print(result[m][n]+"\t\t\t");
                }
                if (m != 0 && n != 0) {
                    result[m][n] = find(result[m][0], result[0][n]);
                //    System.out.print(result[m][n]+"\t\t");
                }
            }
          //  System.out.println();
        }

    }
    public String find(String c,String str){
        grammar g =  map.get(c);
        String string =c + "::=";
        int i = 0;
        if(g.hasnull) {
            String list[] = g.follow.split("\\s+");
            System.out.println(g.follow);
            for (String ss : list) {
                if (ss.equals(str))
                    return c + "::=ε";
            }
        }
        if(g.mapFirst.containsKey(str)){
            int m = g.mapFirst.get(str);
            for (String s : g.section[m])
                string += s;
        }else string = "";
        return string;
    }

    public static void main(String arg[]){
        String str = "E::=E+T|T\nT::=T*F|F\nF::=(E)|i";
        String str1 = "E::=TZ\nZ::=+TZ|ε\nT::=FH\nH::=*FH|ε\nF::=(E)|i";
        String str2 = "S::=AB\nA::=Aaa|a\nB::=Bbb|b";
        cla c = new cla(str);
        /*c.init(str1);
        System.out.println(c.g[4].hasnull);
        c.hasEnd();
        c.hasFollow();
        c.getVt();
        c.makeResult();
        analysis a = new analysis("i+i*i",c.g[0].C,c.map);*/
       /* for(grammar g:c.g){
            System.out.println(g.C+g.first[1]);
        }
       for(String s:c.Vt)
           System.out.println(s);
       for(String s[]:c.result) {
           for (String ss : s)
               System.out.print(ss+"\t\t\t\t");
           System.out.println();
       }*/
      /* for(int i = 0;i<a.stack.length;i++){
           System.out.print(a.stack[i]+"\t\t\t");
           System.out.print(a.input[i]+"\t\t\t");
           System.out.print(a.output[i]+"\t\t\t");
           System.out.println();
       }*/

      for(String s[]:c.result) {
          for (String ss : s)
              System.out.print(ss+"\t\t\t");
          System.out.println();
      }

    }
}
