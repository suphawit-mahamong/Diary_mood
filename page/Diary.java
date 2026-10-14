package page;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.time.format.DateTimeParseException;

public class Diary extends JFrame {

    private JTextField d;
    private JTextField t;
    private JTextArea c;
    private File s; // selectedImageFile
    private JLabel i; // imagePreview
    private DiaryStorage storage;

    public Diary() {

        setTitle("Diary mood");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        storage = new DiaryStorage(new File("diaries"));
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

        JLabel C = new JLabel("Content");
        C.setAlignmentX(Component.CENTER_ALIGNMENT);
        c = new JTextArea(10, 20);
        c.setLineWrap(true);
        c.setWrapStyleWord(true);

        JScrollPane content = new JScrollPane(c);
        content.setMaximumSize(new Dimension(600, 350));
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

        cp.add(addImageButton);
        cp.add(Box.createVerticalStrut(10));
        cp.add(i);

        cp.add(Box.createVerticalStrut(30));

        JButton b = new JButton("Create");
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(b);
        b.setBackground(purple);
        b.setForeground(Color.WHITE);
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
                JOptionPane.showMessageDialog(this, "Please enter a date");
                return;
            }
            if (titleText.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter a title");
                return;
            }
            try {
                LocalDate date = LocalDate.parse(dateText);
                DiaryEntry entry = new DiaryEntry(storage.createNewId(), date, titleText, contentText, imagePath);
                storage.save(entry);
                JOptionPane.showMessageDialog(this, "Saved!");
                d.setText("");
                t.setText("");
                c.setText("");
                s = null;
                i.setIcon(null);
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
