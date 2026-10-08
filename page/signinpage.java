package page;
import data.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;

public class signinpage extends JFrame{

    private JTextField t1;
    private JTextField t2;
    private JTextField t3;
    private JLabel Notice;

    // ================= COLORS =================
    private final Color background = new Color(246, 227, 229);
    private final Color purple = new Color(187, 82, 138);
    private final Color darkText = new Color(60, 55, 70);


    public signinpage(){

        pack();
        setTitle("Diary mood");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(background);

        //สร้างปุ่มไปหน้า Login 
        JPanel Login = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        Login.setBackground(background);

        JButton b = new JButton("Login");
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBackground(purple);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);

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

       setLocationRelativeTo(null);
       setVisible(true);
    
    }

    public void Signin(){
        JPanel cp = new JPanel();
        cp.setLayout(new BoxLayout(cp,BoxLayout.Y_AXIS));
        cp.setBackground(background);

        cp.add(Box.createVerticalStrut(50));
        Dimension Size = new Dimension(130, 25);

        JLabel text = new JLabel("Sign in");
        text.setFont(new Font("Times New Roman", Font.PLAIN , 30));
        text.setForeground(darkText);
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(text);

        cp.add(Box.createVerticalStrut(50));

        JPanel UserPanel = new JPanel(new FlowLayout());
        UserPanel.setBackground(background);

        JLabel l1 =new JLabel("Username");
        l1.setFont(new Font("SansSerif", Font.PLAIN, 14));
        l1.setForeground(darkText);
        l1.setPreferredSize(Size);
        l1.setHorizontalAlignment(SwingConstants.RIGHT);

        t1 =new JTextField(20);
        UserPanel.add(l1);
        UserPanel.add(t1);
        cp.add(UserPanel);

        cp.add(Box.createVerticalStrut(2));

        JPanel Pass = new JPanel(new FlowLayout());
        Pass.setBackground(background);

        JLabel l2 =new JLabel("Password");
        l2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        l2.setForeground(darkText);
        l2.setPreferredSize(Size);
        l2.setHorizontalAlignment(SwingConstants.RIGHT);

        t2 =new JTextField(20);
        Pass.add(l2);
        Pass.add(t2);
        cp.add(Pass);

        cp.add(Box.createVerticalStrut(2));

        JPanel ConfirmPanel = new JPanel(new FlowLayout());
        ConfirmPanel.setBackground(background);

        JLabel l3 =new JLabel("Confirm password");
        l3.setFont(new Font("SansSerif", Font.PLAIN, 14));
        l3.setForeground(darkText);
        l3.setPreferredSize(Size);
        l3.setHorizontalAlignment(SwingConstants.RIGHT);

        t3 =new JTextField(20);

        ConfirmPanel.add(l3);
        ConfirmPanel.add(t3);
        cp.add(ConfirmPanel);

        cp.add(Box.createVerticalStrut(2));

        JButton b =new JButton("Sign in");
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBackground(purple);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(b);

        cp.add(Box.createVerticalStrut(5));

        Notice = new JLabel(" ");
        Notice.setForeground(new Color(200, 40, 40));
        Notice.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(Notice);

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == b){
                    String username = t1.getText().trim();
                    String password = new String(t2.getText()).trim();
                    String confirmPass = new String(t3.getText()).trim();
                    String error = User.signin(username, password, confirmPass);
                    if (error != null) {
                        Notice.setText(error);
                        return;
                    }
                    error = User.signin(username, password, confirmPass);
                    if (error == null) {
                        new loginpage();
                        dispose();
                    } else {
                        Notice.setText(error);
                        t2.setText("");
                        t3.setText("");
    }
}
            }
        });

        cp.add(Box.createVerticalStrut(60));

        add(cp,BorderLayout.CENTER);
    }
    
}
