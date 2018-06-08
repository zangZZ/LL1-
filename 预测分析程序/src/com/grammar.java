package com;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/31.
 */
public class grammar {
    public String C;
    public String grammar;
    public String section[][];//分割子文法语句
    public String first[] = new String[10];
    public String child[];//子文法语句
    public Map<String,Integer> mapFirst = new HashMap<>();//first集
    public String follow = "";//follow集压缩到String字符串中，以空格分割
    public String Vt[] = new String[50];
    public int num_n = 0,num_t = 0;
    public boolean hasnull = false;
    public Map<String,String> mapFollow = new HashMap<>();

    public grammar(String str){
        this.sec(str);
        this.grammar = str;
     //   this.getFirst(map);
    }

   /* public void handle(String str,int n){
        int num_s = 0;
        StringBuilder sb = new StringBuilder("");
        String list[] = str.split("\\s+");
        for(String ss:list) {
            String tem = ":";
            for (int i = 0; i < ss.length(); i++) {
                if(ss.charAt(i) == '`'){
                    sb.append("`");
                    //section[n][num_s-1] += '`';
                }else if (ss.charAt(i) >= 'A' && ss.charAt(i) <= 'Z') {
                    if (tem.equals(":")) {
                        sb.append(" "+String.valueOf(ss.charAt(i)));
                        //section[n][num_s++] = String.valueOf(ss.charAt(i));
                        //    System.out.println(section[num_]);
                    } else {
                        Vt[num_t++] = tem.substring(1);
                        //section[n][num_s++] = tem.substring(1);
                        sb.append(" "+tem.substring(1));
                        tem = ":";
                        // section[n][num_s++] = String.valueOf(ss.charAt(i));
                        sb.append(" "+String.valueOf(ss.charAt(i)));
                    }

                    //section[n][num_s++] = String.valueOf(ss.charAt(i));
                } else if (ss.charAt(i) >= 'a' && ss.charAt(i) <= 'z') {
                    tem += ss.charAt(i);
                    if(i == ss.length()-1){
                        Vt[num_t++] = tem.substring(1);
                        sb.append(" "+tem.substring(1));
                        // section[n][num_s++] = tem.substring(1);
                        tem = ":";
                    }
                } else {
                    if(ss.charAt(0) == 'ε')
                        hasnull = true;
                    if (tem.equals(":")) {
                        Vt[num_t++] = String.valueOf(ss.charAt(i));
                        sb.append(" "+String.valueOf(ss.charAt(i)));
                        //section[n][num_s++] = String.valueOf(ss.charAt(i));
                        //    System.out.println(section[num_]);
                    } else {
                        Vt[num_t++] = tem.substring(1);
                        //section[n][num_s++] = tem.substring(1);
                        sb.append(" "+tem.substring(1));
                        tem = ":";
                        Vt[num_t++] = String.valueOf(ss.charAt(i));
                       // section[n][num_s++] = String.valueOf(ss.charAt(i));
                        sb.append(" "+String.valueOf(ss.charAt(i)));
                    }
                }
            }
        }
      //  System.out.println(sb.toString());
        String s[] = sb.toString().split("\\s+");
        section[n] = new String[s.length-1];
        for(int i = 0;i<section[n].length;i++)
            section[n][i] = s[i+1];
    }*/

    public void handle(String str,int n){
        StringBuilder sb = new StringBuilder("");
        for(int i = 0;i<str.length();i++) {
            if (str.charAt(i) != ' '&&str.charAt(i) != '`')
                sb.append(" "+ str.charAt(i));
            if(str.charAt(i) == '`')
                sb.append("`");
            if(str.charAt(i) == 'ε') {
                this.hasnull = true;
                break;
            }
        }
        String s[] = sb.toString().split("\\s+");
        section[n] = new String[s.length-1];
        for(int i = 0;i<section[n].length;i++)
            section[n][i] = s[i+1];
    }

    public String[] getFirst(Map map){
        int num_f = 0;
        for(int i = 0;i<section.length;i++){
        //    System.out.println(section[i][0]);
            if(map.containsKey(section[i][0])){
                grammar r = (grammar) map.get(section[i][0]);
                for(String ss:r.getFirst(map)){
                    if(ss != null) {
                        first[num_f++] = ss;
                        mapFirst.put(ss,i);
                    }
                }
            }else{ first[num_f++] = section[i][0];mapFirst.put(section[i][0],i);}
        }
        return first;
    }


    public void sec(String str){
        this.C = str.charAt(0)+"";
        if(str.charAt(1) == '`') {
            C += "`";
            str = str.substring(5);
        }
        else str = str.substring(4);
        String list[] = str.split("\\|");
        child =new String[list.length];
        section = new String[list.length][];
        for(int i = 0;i<list.length;i++){
            handle(list[i],i);
            child[i] =this.C+"::="+list[i];
        }
    }

    public static void main(String arg[]){
        String str = "V::=begin E +V`|i+E+p";
        String str1 = "A`::=aaA`|ε";
        grammar g = new grammar(str1);
        System.out.println(g.C);
      //  System.out.println(g.section[1].length+"");
        for(String s[]:g.section) {
            for(String ss:s) {
                System.out.print(ss+" ");
            }
            System.out.println();
        }
        for(int i = 0;i<g.num_t;i++)
            System.out.print(g.Vt[i]);
        System.out.println();
    }

}
