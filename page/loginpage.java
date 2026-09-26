package page;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;

public class loginpage extends JFrame {

    private JTextField t1;
    private JPasswordField t2;
    
    public loginpage(){
        
        setTitle("Diary mood");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        //สร้างปุ่มไปหน้า Sign in 
        JPanel Login = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton b = new JButton("Sign in");
        Login.add(b);
        add(Login,BorderLayout.NORTH);

        Login();

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() ==b){
                    //ใส่เปลี่ยนหน้า
                    new signinpage();
                    dispose();
                }

            } 
        });

       setVisible(true);
    }

    private void Login(){
        JPanel cp = new JPanel();
        cp.setLayout(new BoxLayout(cp,BoxLayout.Y_AXIS));

        cp.add(Box.createVerticalStrut(50));

        JLabel text = new JLabel("Login");
        text.setFont(new Font("Times New Roman", Font.PLAIN , 30));
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(text);

        cp.add(Box.createVerticalStrut(70));

        JPanel User = new JPanel(new FlowLayout());
        JLabel l1 =new JLabel("Username");
        t1 =new JTextField(20);
        User.add(l1);
        User.add(t1);
        cp.add(User);

        cp.add(Box.createVerticalStrut(2));

        JPanel Pass = new JPanel(new FlowLayout());
        JLabel l2 =new JLabel("Password");
        t2 =new JPasswordField(20);
        Pass.add(l2);
        Pass.add(t2);
        cp.add(Pass);

        cp.add(Box.createVerticalStrut(2));

        JButton b =new JButton("Login");
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(b);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() ==b){
                    //ใส่เปลี่ยนหน้า
                    new homepage();
                    dispose();
                }

            } 
        });

        cp.add(Box.createVerticalStrut(60));

        add(cp,BorderLayout.CENTER);
    }
    
    
    
}
