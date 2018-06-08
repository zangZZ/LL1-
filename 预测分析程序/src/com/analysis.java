package com;

import java.util.*;
/**
 * Created by Administrator on 2018/6/3.
 */
public class analysis {
    public Stack<String> strStack = new Stack<String>();
    public Stack<String> cStack = new Stack<String>();
    public String[] stack,input,output;
    public analysis(String str,String c,Map map){
        init(str,c,map);
    }
    public void init(String str,String c,Map map){
        StringBuilder stackStr = new StringBuilder("");
        StringBuilder inStr = new StringBuilder("");
        StringBuilder outStr = new StringBuilder("");
       // strStack.push(str);
        strStack.push("#");
        for(int i =str.length()-1;i>=0;i--){
            if(str.charAt(i) != ' ')
                strStack.push(str.charAt(i)+"");
        }
        cStack.push("#");
        cStack.push(c);
        map.get(c);
        int i = 0;
        String s = "#"+c;
        String ss = str+"# ";
        stackStr.append(s+" ");
        inStr.append(ss+" ");
        while(!strStack.empty()&&!cStack.empty()){
            if(map.containsKey(cStack.peek())) {
                grammar g = (grammar) map.get(cStack.peek());
                if(g.hasnull&&g.mapFollow.containsKey(strStack.peek())){
                    cStack.pop();
                   // stackStr.append(" "+s.substring(0,s.length()-1));
                    outStr.append(g.C+"::=ε ");
                    inStr.append(ss+" ");
                    if(s.charAt(s.length()-1) == '`') {
                        stackStr.append(s.substring(0,s.length()-2)+" ");
                        s = s.substring(0,s.length()-2);
                    }else {
                        stackStr.append(s.substring(0, s.length() - 1) + " ");
                        s = s.substring(0, s.length() - 1);
                    }
                }else {
                    i = g.mapFirst.get(strStack.peek());
                    if (s.charAt(s.length() - 1) == '`')
                        s = s.substring(0, s.length() - 2);
                    else s = s.substring(0, s.length() - 1);
                    cStack.pop();
                    for (int n = g.section[i].length-1; n >= 0; n--) {
                        cStack.push(g.section[i][n]);
                        s += g.section[i][n];
                    }
                    outStr.append(g.child[i]+" ");
                    stackStr.append(s+" ");
                    inStr.append(ss+" ");
                }
            }else{
                strStack.pop();
                cStack.pop();
                outStr.append("# ");
                inStr.append(ss.substring(1)+" ");
                stackStr.append(s.substring(0,s.length()-1)+" ");
                ss = ss.substring(1);
                s = s.substring(0,s.length()-1);
            }
        }
        stack = stackStr.toString().split("\\s+");
        input = inStr.toString().split("\\s+");
        output = outStr.toString().split("\\s+");
        System.out.println(stackStr.toString());
        System.out.println(inStr.toString());
        System.out.println(outStr.toString());
    }
}
