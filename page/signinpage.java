package page;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class signinpage extends JFrame{
    
    public signinpage(){
        
        pack();
        setTitle("Diary mood");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ImageIcon img = new ImageIcon("C:\\Users\\sukit\\Downloads\\lib-nobg.png");
        setIconImage(img.getImage());

        
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

                }

            } 
       });

    
    }

    public void Signin(){
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
        JTextField t1 =new JTextField(20);
        User.add(l1);
        User.add(t1);
        cp.add(User);

        cp.add(Box.createVerticalStrut(2));

        JPanel Pass = new JPanel(new FlowLayout());
        JLabel l2 =new JLabel("Password");
        JTextField t2 =new JTextField(20);
        Pass.add(l2);
        Pass.add(t2);
        cp.add(Pass);

        cp.add(Box.createVerticalStrut(2));

        JPanel Confirm = new JPanel(new FlowLayout());
        JLabel l3 =new JLabel("Confirm password");
        JTextField t3 =new JTextField(20);
        Confirm.add(l3);
        Confirm.add(t3);
        cp.add(Confirm);

        cp.add(Box.createVerticalStrut(2));

        JButton b =new JButton("Sign in");
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(b);

        cp.add(Box.createVerticalStrut(60));

        add(cp,BorderLayout.CENTER);
    }
}
