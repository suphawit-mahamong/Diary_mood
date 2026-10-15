package page;

// ทำ Gui ปกติของjava
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
// ใช้เก็บรูป
import java.io.File;
// ใช้จับ error
import java.io.IOException;
// ใช้ระบุวันที่วันนี้เป็นค่าเริ่มต้น
import java.time.LocalDate;
// ใช้กรองหน้าต่างเลือกไฟล์ให้เห็นเฉพาะรูป jpg, jpeg, png, gif
import javax.swing.filechooser.FileNameExtensionFilter;

public class Diary extends JFrame {

    private JTextField d;
    private JTextField t;
    private JTextArea c;
    private File s; // selectedImageFile
    private JLabel i; // imagePreview
    // ตัวแปรที่ใช้บันทึก/โหลก Diary
    private DiaryStorage storage = DiaryStorage.CurrentUser();
    private JLabel Notice;
    // ชนิดของตัวแปรเป็น Runnable หรือคือ งานชิ้นหนึ่งที่สั่งให้ทำได้ มีเมธอดเดียวคือ run()
    private Runnable onCreated;
    private boolean pinned = false;
    //เปิดหน้าเขียน diary โดยไม่ต้องแจ้งใครเมื่อสร้างเสร็จ (ใช้ทดสอบในหน้านี้)
    public Diary() {
        this(null);
    }
    // รับ Runnable (คือเหมือนกล่องห่อโค้ดไว้ รอให้คนอื่นมาเรียกใช้ผ่าคำำสั่ง run()) มาเก็บไว้ใน field onCreated เป็น callback ที่จะถูกเรียกหลังบันทึก diary สำเร็จ (onCreated.run()) หน้า Home ส่งมาเพื่อให้รีเฟรช pinned ใหม่
    public Diary(Runnable OnCreated) {
        this.onCreated = OnCreated;

        setTitle("Diary mood");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        // สร้างส่วนประกอบทั้งหมด (ช่องวันที่ หัวข้อ เนื้อหา รูป ปุ่ม Create)
        createDiary();

        setLocationRelativeTo(null);
        setVisible(true);

    }
    // ฟั่งก์ชั่นเปิดปฏิทินเพิ่มวันที่
    private void pickDate() {
        // สร้างหน้าต่างย่อยชื่อ “Select date” โดยมีหน้า Diary เป็นหน้าต่างแม่
        JDialog dialog = new JDialog(this, "Select date", true);
        // สร้างปฏิทินจากคลาส calendar
        calendar cal = new calendar();
        // ตั้งการตอบกลับ เมื่อผู้ใช้ คลิก เลือกวันที่จากปฏิทินโดยคลิกเสร็จจะปิดปฏิทินทันที ดดยวันที่ที่เลือกมาจะถูกใส่ลงช่องวันที่ในรูปแบบ yyyy-MM-dd ให้เลย
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
        // สร้างโครงหน้า
        JPanel cp = new JPanel();
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
        cp.setBackground(background);
        cp.add(Box.createVerticalGlue());
        cp.add(Box.createVerticalStrut(40));
        // สร้างช่องให้วันที่แบบ ไม่สามารถกรอกหรือแก้ไขเองได้ต้องเลือกจากปฏิทิน
        JPanel Date = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
        JLabel D = new JLabel("Date");
        D.setAlignmentX(Component.LEFT_ALIGNMENT);
        d = new JTextField(10);
        Date.setBackground(background);
        d.setEditable(false);
        d.setText(LocalDate.now().toString());
        // ปุ่มให้เลือกวันที่จากปฏิทิน โดยจะสามารถเลือกและแก้ไขวันเดือนปีได้จากตัวนี้อย่างเดียว
        JButton pickDateButton = new JButton("📅");
        pickDateButton.addActionListener(e -> pickDate());
        pickDateButton.setMargin(new Insets(2, 6, 2, 6));
        pickDateButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        Date.add(D);
        Date.add(d);
        Date.add(pickDateButton);
        cp.add(Date);

        cp.add(Box.createVerticalStrut(2));
        //ช่องกรอกหัวข้อเรื่อง
        JPanel title = new JPanel(new FlowLayout());
        JLabel T = new JLabel("Title");
        T.setAlignmentX(Component.LEFT_ALIGNMENT);
        t = new JTextField(30);
        title.setBackground(background);
        title.add(T);
        title.add(t);
        cp.add(title);

        cp.add(Box.createVerticalStrut(2));
        //ช่องทางซ้ายมือสำหรับกรอกข้อความ 10 บรรทัด ตัดบรรทัดอัตโนมัติตามขอบคำ โดยมี Scroll เพื่อเลื่อนดูเนื้อหายาวๆ
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(background);
        JLabel C = new JLabel("Content");
        C.setAlignmentX(Component.LEFT_ALIGNMENT);
        c = new JTextArea(10, 20);
        c.setLineWrap(true);
        c.setWrapStyleWord(true);
        // ช่องกรอกเนื้อหา
        JScrollPane content = new JScrollPane(c);
        content.setMaximumSize(new Dimension(600, 350));
        content.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(C);
        left.add(Box.createVerticalStrut(5));
        left.add(content);
        // ช่องทางขวาไว้สำหรับโชวร์ตัวอย่างรูปที่เพิ่ม และปุ่มเพิ่มรูป
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
        right.add(Box.createVerticalStrut(25)); // ให้รูปเริ่มระดับเดียวกับกล่องเนื้อหา
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
        // รวม ทางซ้ายกับทางขวาเป็น แถวเดียวกัน โดย เนื้อหาอยู่กลาง ยืดตามพื้นที่ที่เหลือ รูปอยู่ขวา กว้างคงที่ เว้นระยะ 15 พิกเซล และจำกัดความสูงแถวไม่เกิน 260
        JPanel row = new JPanel(new BorderLayout(15, 0));
        row.setBackground(background);
        row.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));
        row.add(left, BorderLayout.CENTER); // เนื้อหายืดตามความกว้างที่เหลือ
        row.add(right, BorderLayout.EAST); // รูปกว้างคงที่ 200
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        cp.add(row);
        cp.add(Box.createVerticalStrut(30));
        // ปุ่มสำหรับบันทึก Diary เป็นไฟล์
        JButton b = new JButton("Create");
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(b);
        b.setBackground(purple);
        b.setForeground(Color.WHITE);
        // ปุ่มสำหรับเพิ่มการปักหมุด Diary ที่ชอบ
        JButton pinButton = new JButton("♡");
        pinButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        pinButton.setFocusPainted(false);
        pinButton.setBorderPainted(false);
        pinButton.setContentAreaFilled(false);
        pinButton.setForeground(Color.RED);
        pinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // สลับสถานะ
                pinned = !pinned;
                if (pinned) {
                    pinButton.setText("♥");
                    pinButton.setForeground(Color.RED);
                } else {
                    pinButton.setText("♡");
                    pinButton.setForeground(Color.RED);
                }
            }
        });
        // รวมปุ่ม Create กับ ปุ่มปักหมุดเป็นแถวเดียวกัน
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonRow.setBackground(background);
        buttonRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        buttonRow.add(b);
        buttonRow.add(pinButton);
        cp.add(buttonRow);
        // ป้ายข้อความแจ้งเตือน
        Notice = new JLabel(" ");
        Notice.setForeground(new Color(200, 40, 40));
        Notice.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(Notice);
        // เมื่อกดปุ่ม Create แล้วจะมีการส่งข้อมูล วันเดือนปี, หัวข้อเรื่อง, เนื้อหา และรูปปภาพ
        b.addActionListener(e -> {
            // ดึงข้อมุลจากที่กรอกมา
            String dateText = d.getText();
            String titleText = t.getText();
            String contentText = c.getText();
            // ตอนกดปุ่ม Create ถ้ายังไม่เลือกรูป path เป็น null
            String imagePath = (s == null) ? null : s.getPath();
            // ตรวจสอบว่า วันที่เป็นช่องว่างไหม (ปัจจุบันให้เลือกจากปฏิทินแล้วจะไม่มีห้องช่องว่างแล้ว)
            if (dateText.isBlank()) {
                Notice.setText("Please enter a date");
                return;
            }
            // ตรวจสอบว่า หัวข้อเป็นช่องว่างไหม
            if (titleText.isBlank()) {
                Notice.setText("Please enter a title");
                return;
            }

            try {
                // แปลงข้อวมูลวันที่จาก ข้อความเป็น LocalDate
                LocalDate date = LocalDate.parse(dateText);
                // สร้าง DiaryEntry ด้วย id ใหม่จาก createNewId() เป็นรูปแบบ "2026-10-15_143005"
                DiaryEntry entry = new DiaryEntry(storage.createNewId(), date, titleText, contentText, imagePath);
                //ตั้งสถานะปักหมุดต่ามที่ผู้ใช้กดรูปหัวใจไว้และเก็บไว้ใน pinned
                entry.setPinned(pinned);
                //เขียนไฟล์เป็น .txt ไว้ในโฟลเดอร์ผู้ใช้งาน
                storage.save(entry);
                // หลังบันทึกเสร็จให้ระบบตรวจสอบและรีเฟรชหน้า home
                if (onCreated != null) {
                    onCreated.run(); // บอกหน้า home ให้รีเฟรช
                }
                //ปิดหน้า diary
                dispose();
                //แจ้งเตือ่นว่าเซฟแล้ว และรีเซฟ ทุกช่อง ให้ว่างหรือกลับไปเป็นค่าเริ่มต้น
                Notice.setText("Saved!");
                d.setText(LocalDate.now().toString());
                t.setText("");
                c.setText("");
                s = null;
                i.setIcon(null);

                pinned = false;
                pinButton.setText("♡");
                pinButton.setForeground(Color.RED);
                //กรณีบันทึกล้มเหลวให้แจ้งเตือนที่ผิด
            } catch (IOException ex) {
                //โชว์ให้ผู้ใช้เห็นด้วยว่าเซฟไม่สำเร็จเพราะอะไร
                Notice.setText("Save failed: " + ex.getMessage());
                ex.printStackTrace();
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
