package page;
import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
;


public class Diary extends JFrame {

    private JTextField d;
    private JTextField t;
    private JTextArea c;
     private File s; //selectedImageFile
    private JLabel i; //imagePreview

    
    public Diary(){
        
        setTitle("Diary mood");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        createDiary();

        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void createDiary(){

         // COLORS 
        Color background = new Color(246,227,229);
        Color purple = new Color(187,82,138);
        Color darkText = new Color(60, 55, 70);

        JPanel cp =new JPanel();
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
        cp.setBackground(background);
        cp.add(Box.createVerticalGlue());

        cp.add(Box.createVerticalStrut(40));
        JPanel Date = new JPanel(new FlowLayout());
        JLabel D =new JLabel("Date");
        D.setAlignmentX(Component.LEFT_ALIGNMENT);
        d = new JTextField(30);
        Date.setBackground(background);
        Date.add(D);
        Date.add(d);
        cp.add(Date);

        cp.add(Box.createVerticalStrut(2));

        JPanel title = new JPanel(new FlowLayout());
        JLabel T = new JLabel("Title");
        T.setAlignmentX(Component.LEFT_ALIGNMENT);
        t = new JTextField(30);
        title.setBackground(background);
        title.add(T);
        title.add(t);
        cp.add(title);

        cp.add(Box.createVerticalStrut(2));

        JLabel C = new JLabel("Content");
        C.setAlignmentX(Component.CENTER_ALIGNMENT);
        c = new JTextArea(10,20);
        c.setLineWrap(true);
        c.setWrapStyleWord(true);

        JScrollPane content = new JScrollPane(c);
        content.setMaximumSize(new Dimension(600,350));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(C);
        cp.add(content);

        JButton addImageButton = new JButton("+ Add Image");
        addImageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addImageButton.setBackground(purple);
        addImageButton.setForeground(Color.WHITE);
        addImageButton.setFocusPainted(false);

        // แสดงตัวอย่างรูปที่เลือก (ตอนแรกยังไม่มีรูป เลยว่างไว้ก่อน)
        i = new JLabel();
        i.setAlignmentX(Component.CENTER_ALIGNMENT);
        i.setHorizontalAlignment(SwingConstants.CENTER);
        i.setPreferredSize(new Dimension(200, 150));
        i.setMaximumSize(new Dimension(200, 150));
        i.setBorder(BorderFactory.createDashedBorder(darkText));

        addImageButton.addActionListener(e -> {

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("เลือกรูปภาพ");

    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image files (jpg, jpeg, png, gif)","jpg", "jpeg", "png", "gif"
    );
    fileChooser.setFileFilter(filter);

    int result = fileChooser.showOpenDialog(this);

    if (result == JFileChooser.APPROVE_OPTION) {

        s = fileChooser.getSelectedFile();

        ImageIcon icon = new ImageIcon(s.getPath());

        // ย่อรูปให้พอดีกับกรอบ preview
        Image scaledImage = icon.getImage().getScaledInstance(
                200, 150, Image.SCALE_SMOOTH
        );

        i.setIcon(new ImageIcon(scaledImage));
        i.setText(null);
    }
});

cp.add(addImageButton);
cp.add(Box.createVerticalStrut(10));
cp.add(i);

        cp.add(Box.createVerticalStrut(30));

        JButton b = new JButton("Create");
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(b);
        b.setBackground(purple);
        b.setForeground(Color.WHITE);

        cp.add(Box.createVerticalStrut(20));

        add(cp,BorderLayout.CENTER);
        cp.add(Box.createVerticalGlue());

    }
    

    public static void main(String[] args) {
        new Diary();

    }
}

