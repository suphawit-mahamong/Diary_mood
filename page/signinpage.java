package page;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;

public class signinpage extends JFrame{

    private JTextField t1;
    private JTextField t2;
    private JTextField t3;
    
    public signinpage(){
        
        pack();
        setTitle("Diary mood");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        //สร้างปุ่มไปหน้า Login 
        JPanel Login = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton b = new JButton("Login");
        Login.add(b);
        add(Login,BorderLayout.NORTH);

        Signin();

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() ==b){
                    //ใส่เปลี่ยนหน้า
                    new loginpage();
                    dispose();
                }

            } 
       });
       setVisible(true);
    
    }

    private void Signin(){
        JPanel cp = new JPanel();
        cp.setLayout(new BoxLayout(cp,BoxLayout.Y_AXIS));

        cp.add(Box.createVerticalStrut(50));

        JLabel text = new JLabel("Sign in");
        text.setFont(new Font("Times New Roman", Font.PLAIN , 30));
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(text);

        cp.add(Box.createVerticalStrut(50));

        JPanel User = new JPanel(new FlowLayout());
        JLabel l1 =new JLabel("Username");
        t1 =new JTextField(20);
        User.add(l1);
        User.add(t1);
        cp.add(User);

        cp.add(Box.createVerticalStrut(2));

        JPanel Pass = new JPanel(new FlowLayout());
        JLabel l2 =new JLabel("Password");
        t2 =new JTextField(20);
        Pass.add(l2);
        Pass.add(t2);
        cp.add(Pass);

        cp.add(Box.createVerticalStrut(2));

        JPanel Confirm = new JPanel(new FlowLayout());
        JLabel l3 =new JLabel("Confirm password");
        t3 =new JTextField(20);
        Confirm.add(l3);
        Confirm.add(t3);
        cp.add(Confirm);

        cp.add(Box.createVerticalStrut(2));

        JButton b =new JButton("Sign in");
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(b);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() ==b){
                    //ใส่เปลี่ยนหน้า
                    new loginpage();
                    dispose();
                }

            } 
        });

        cp.add(Box.createVerticalStrut(60));

        add(cp,BorderLayout.CENTER);
    }
}
