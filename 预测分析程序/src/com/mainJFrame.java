package com;

/**
 * Created by Administrator on 2018/5/30.
 */
import java.awt.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public class mainJFrame extends JFrame implements ActionListener {
    JPanel p1, p2, p3, p4;
    JTextArea t;
    TextField textField;
    JButton but1,but2,but3;
    JTable table1,table2,table3;
    cla c;
    grammar g;
    DefaultTableModel tableModel1,tableModel2,tableModel3;
    mainJFrame() {
        super("预测分析程序");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(2,2));
        this.setBounds(400,30,1000,1000);
        t=new JTextArea(25,40);
        but1 = new JButton("重置");
        but2 = new JButton("确定");
        but3 = new JButton("确定");
        but1.addActionListener(this);
        but2.addActionListener(this);
        but3.addActionListener(this);
        p1 = new JPanel();
        p2 = new JPanel();
        p3 = new JPanel();
        p4 = new JPanel();
        String[] titles1={"grammar","first","follow"} ,titles2={"步骤","栈","输入","输出"};
        tableModel1 = new DefaultTableModel();
        tableModel2 = new DefaultTableModel(titles1,1);
        tableModel3 = new DefaultTableModel(titles2,1);
        JTable jTable1 = new JTable(tableModel1);
        JTable jTable2 = new JTable(tableModel2);
        JTable jTable3 = new JTable(tableModel3);
        textField = new TextField(20);
        p1.setLayout(new FlowLayout());
        p1.add(t);
        p1.add(but1);
        p1.add(but2);
        p2.add(new JScrollPane(jTable1),TOP_ALIGNMENT);
        p2.add(textField);
        p2.add(but3);
        p3.add(new JScrollPane(jTable2));
        p4.add(new JScrollPane(jTable3));
        add(p1);
        add(p2);
        add(p3);
        add(p4);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
        String ss = "";
        String s = "";
        String str = "";
        if(e.getSource().equals(but1)) {
            t.setText("");
            tableModel1.setRowCount(0);
            tableModel2.setRowCount(0);
            tableModel3.setRowCount(0);
        }
        try {
            if (e.getSource().equals(but2)) {
                c = new cla(t.getText());
                tableModel1.setRowCount(c.result.length - 1);
                tableModel1.setColumnCount(c.result[0].length);
                tableModel1.setColumnIdentifiers(c.result[0]);
                for (int m = 1; m < c.result.length; m++)
                    for (int n = 0; n < c.result[m].length; n++) {
                        tableModel1.setValueAt(c.result[m][n], m - 1, n);
                    }
                tableModel2.setRowCount(c.result.length-1);
                for (int m = 0; m < c.result.length-1; m++) {
                    tableModel2.setValueAt(c.g[m].grammar, m, 0);
                    for (Map.Entry<String, Integer> entry : c.g[m].mapFirst.entrySet())
                        ss += entry.getKey()+"   ";
                    tableModel2.setValueAt(ss, m, 1);
                    ss = "";
                    for (Map.Entry<String, String> entry : c.g[m].mapFollow.entrySet())
                        s += entry.getKey()+"   ";
                    tableModel2.setValueAt(s, m, 2);
                    s = "";
                }

                  //  }
            }
        }catch (Exception ex){ ex.getStackTrace();JOptionPane.showMessageDialog(null,"文法错误");}

        if(e.getSource().equals(but3)){
            try {
                str = textField.getText();
                c.a = new analysis(str, c.g[0].C, c.map);
                tableModel3.setRowCount(c.a.input.length);
                for (int m = 0; m < c.a.stack.length; m++) {
                    tableModel3.setValueAt(c.a.stack[m], m, 1);
                    tableModel3.setValueAt(m, m, 0);
                    tableModel3.setValueAt(c.a.input[m], m, 2);
                    tableModel3.setValueAt(c.a.output[m], m, 3);
                }
            }catch (Exception ex){ ex.getStackTrace();JOptionPane.showMessageDialog(null,"所输入的字符串不符合该文法");}

        }
    }

    public static void main (String arg[]){
        new mainJFrame();
    }
}
