package page;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;

public class loginpage extends JFrame{
    public loginpage() {
        super("test");
        setLayout(null);
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        JButton j = new JButton("Login");
        JLabel i = new JLabel("usename");
        
        p.setBounds(50, 50, 200, 100);   // x, y, width, height
        i.setBounds(50, 160, 100, 30);
        j.setBounds(50, 160, 100, 30);


        add(p);
        p.add(i);
        add(j);
        


        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
}
