package page;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.time.format.DateTimeParseException;

public class Diary extends JFrame {

    private JTextField d;
    private JTextField t;
    private JTextArea c;
    private File s; // selectedImageFile
    private JLabel i; // imagePreview
    private DiaryStorage storage = DiaryStorage.CurrentUser();
    private JLabel  Notice;
    private Runnable onCreated;
    private boolean pinned = false;

    public Diary() {
    this(null);
}

    public Diary(Runnable OnCreated) {
        this.onCreated = OnCreated;

        setTitle("Diary mood");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createDiary();

        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void pickDate() {
        JDialog dialog = new JDialog(this, "Select date", true);
        calendar cal = new calendar();
        cal.setOnDatePicked(picked -> {
            d.setText(picked.toString());
            dialog.dispose();
        });
        dialog.add(cal);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void createDiary() {

        // COLORS
        Color background = new Color(246, 227, 229);
        Color purple = new Color(187, 82, 138);
        Color darkText = new Color(60, 55, 70);

        JPanel cp = new JPanel();
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
        cp.setBackground(background);
        cp.add(Box.createVerticalGlue());

        cp.add(Box.createVerticalStrut(40));
        JPanel Date = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
        JLabel D = new JLabel("Date");
        D.setAlignmentX(Component.LEFT_ALIGNMENT);
        d = new JTextField(10);
        Date.setBackground(background);
        d.setEditable(false);
        d.setText(LocalDate.now().toString());

        JButton pickDateButton = new JButton("📅");
        pickDateButton.addActionListener(e -> pickDate());
        pickDateButton.setMargin(new Insets(2, 6, 2, 6));
        pickDateButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        Date.add(D);
        Date.add(d);
        Date.add(pickDateButton);
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

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(background);
        JLabel C = new JLabel("Content");
        C.setAlignmentX(Component.LEFT_ALIGNMENT);
        c = new JTextArea(10, 20);
        c.setLineWrap(true);
        c.setWrapStyleWord(true);

        JScrollPane content = new JScrollPane(c);
        content.setMaximumSize(new Dimension(600, 350));
        content.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(C);
        left.add(Box.createVerticalStrut(5));
        left.add(content);

        
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(background);
        JButton addImageButton = new JButton("+ Add Image");
        addImageButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addImageButton.setBackground(purple);
        addImageButton.setForeground(Color.WHITE);
        addImageButton.setFocusPainted(false);

        // แสดงตัวอย่างรูปที่เลือก (ตอนแรกยังไม่มีรูป เลยว่างไว้ก่อน)
        i = new JLabel();
        i.setAlignmentX(Component.LEFT_ALIGNMENT);
        i.setHorizontalAlignment(SwingConstants.CENTER);
        i.setPreferredSize(new Dimension(200, 150));
        i.setMaximumSize(new Dimension(200, 150));
        i.setBorder(BorderFactory.createDashedBorder(darkText));
        right.add(Box.createVerticalStrut(25));   // ให้รูปเริ่มระดับเดียวกับกล่องเนื้อหา
        right.add(i);
        right.add(Box.createVerticalStrut(10));
        right.add(addImageButton);


        addImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("เลือกรูปภาพ");
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Image files (jpg, jpeg, png, gif)", "jpg", "jpeg", "png", "gif");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                s = fileChooser.getSelectedFile();
                ImageIcon icon = new ImageIcon(s.getPath());

                // ย่อรูปให้พอดีกับกรอบ preview
                Image scaledImage = icon.getImage().getScaledInstance(
                        200, 150, Image.SCALE_SMOOTH);

                i.setIcon(new ImageIcon(scaledImage));
                i.setText(null);
            }
        });
        JPanel row = new JPanel(new BorderLayout(15, 0));
        row.setBackground(background);
        row.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));
        row.add(left, BorderLayout.CENTER);   // เนื้อหายืดตามความกว้างที่เหลือ
        row.add(right, BorderLayout.EAST);    // รูปกว้างคงที่ 200
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        cp.add(row);
        cp.add(Box.createVerticalStrut(30));

        JButton b = new JButton("Create");
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(b);
        b.setBackground(purple);
        b.setForeground(Color.WHITE);

        JButton pinButton = new JButton("♡");
        pinButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        pinButton.setFocusPainted(false);
        pinButton.setBorderPainted(false);
        pinButton.setContentAreaFilled(false);
        pinButton.setForeground(Color.RED);
        pinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                // สลับสถานะ
                pinned = !pinned;
                if(pinned){
                    pinButton.setText("♥");
                    pinButton.setForeground(Color.RED);
                } else{
                    pinButton.setText("♡");
                    pinButton.setForeground(Color.RED);
                }                       
            }
        });

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonRow.setBackground(background);
        buttonRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        buttonRow.add(b);
        buttonRow.add(pinButton);
        cp.add(buttonRow);

        Notice = new JLabel(" ");
        Notice.setForeground(new Color(200, 40, 40));
        Notice.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(Notice);

        b.addActionListener(e -> {
            // 1. ดึงค่าจาก d, t, c, s
            // 2. เช็กว่าช่องไม่ว่าง
            // 3. เขียนลงไฟล์ CSV
            // 4. เคลียร์ช่อง / แจ้งผู้ใช้ว่าบันทึกแล้ว
            String dateText = d.getText();
            String titleText = t.getText();
            String contentText = c.getText();
            String imagePath = (s == null) ? null : s.getPath();
            if (dateText.isBlank()) {
                Notice.setText( "Please enter a date");
                return;
            }
            if (titleText.isBlank()) {
                Notice.setText("Please enter a title");
                return;
            }
            try {
                LocalDate date = LocalDate.parse(dateText);
                DiaryEntry entry = new DiaryEntry(storage.createNewId(), date, titleText, contentText, imagePath);
                entry.setPinned(pinned);
                storage.save(entry);

                if (onCreated != null) {
                    onCreated.run();     // บอกหน้า home ให้รีเฟรช
                    }

                Notice.setText( "Saved!");
                d.setText(LocalDate.now().toString());
                t.setText("");
                c.setText("");
                s = null;
                i.setIcon(null);

                pinned = false;
                pinButton.setText("♡");
                pinButton.setForeground(Color.RED);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "พิมพ์วันที่เป็นรูปแบบ yyyy-MM-dd เช่น 2026-10-13");
                return;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Save failed: " + ex.getMessage());
                return;
            }
        });

        cp.add(Box.createVerticalStrut(20));

        add(cp, BorderLayout.CENTER);
        cp.add(Box.createVerticalGlue());

    }

    public static void main(String[] args) {
        new Diary();

    }
}
