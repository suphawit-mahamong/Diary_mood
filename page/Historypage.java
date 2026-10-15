package page;

//ใช้ทำ gui ทั่วไฟ
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//เช็คไฟล์์กับตรวจ error
import java.io.File;
import java.io.IOException;
// List คือ interface ของลิสต์ และ ArrayList คือตัวที่ใช้งานจริง ใช้เก็บ entries
import java.util.List;
import java.util.ArrayList;
//LocalDate เก็บวันที่ DateTimeFormatter จัดรูปแบบวันที่เป็น dd/MM/yy DateTimeParseException เกิดเมื่อแปลงข้อความเป็นวันที่ไม่ได้ (ใช้ในหน้าต่าง Edit)
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Historypage extends JFrame {

    // ================= COLORS =================
    private final Color background = new Color(246, 227, 229);
    private final Color purple = new Color(187, 82, 138);
    private final Color darkText = new Color(60, 55, 70);

    private sidebar sideBar;
    private calendar calendar;
    private final DiaryStorage storage = DiaryStorage.CurrentUser();
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yy");
    private List<DiaryEntry> entries = new ArrayList<>();
    private LocalDate filterDate = null;
    private JLabel filterLabel = new JLabel("Showing: all");
    private JLabel Notice;

    // พาเนลที่เก็บรายการ history ทั้งหมด (ไว้เพื่อลบ/กรองการ์ดออกได้)
    private JPanel listPanel;
    private JTextField searchField;

    public Historypage() {

        setTitle("Diary mood");
        setSize(650, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(background);

        createUI();
        // ทำให้ตัวหน้า Refresh ทุกครั้งเพื่อดึงข้อมูลที่พึ่งเพิ่มขึ้นมา
        addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                entries = storage.loadAll();
                refreshHistoryList();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createUI() {

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(background);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));

        // ช่องค้นหา

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        searchPanel.setPreferredSize(new Dimension(0, 40));
        searchPanel.setMaximumSize(new Dimension(560, 40));

        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setBorder(null);

        JLabel searchIcon = new JLabel("🔎");
        searchIcon.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                refreshHistoryList();
            }
        });

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // EAST (side bar)
        sideBar = new sidebar("HistoryPage");
        mainPanel.add(sideBar, BorderLayout.EAST);
        JButton menuBtn = new JButton("☰");
        menuBtn.addActionListener(e -> sideBar.toggle());

        /*
         * JPanel topBar = new JPanel(new BorderLayout(10, 0));
         * topBar.setBackground(background);
         * topBar.add(searchPanel, BorderLayout.CENTER);
         * topBar.add(menuBtn, BorderLayout.EAST);
         * mainPanel.add(topBar, BorderLayout.NORTH);
         */

        JPanel sideWrapper = new JPanel(new BorderLayout());
        sideWrapper.setOpaque(false);
        sideWrapper.add(sideBar, BorderLayout.NORTH);

        mainPanel.add(sideWrapper, BorderLayout.EAST);

        // History

        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setBackground(background);

        centerWrapper.add(Box.createVerticalStrut(15));

        JLabel historyTitle = new JLabel("History");
        historyTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        historyTitle.setForeground(darkText);
        // historyTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setBackground(background);
        titleRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        titleRow.add(historyTitle, BorderLayout.WEST);
        titleRow.add(menuBtn, BorderLayout.EAST);

        // centerWrapper.add(historyTitle);
        // centerWrapper.add(Box.createVerticalStrut(15));
        centerWrapper.add(titleRow);
        centerWrapper.add(Box.createVerticalStrut(15));

        // Calendar
        calendar = new calendar();
        calendar.setOnDatePicked(picked -> {
            if (picked.equals(filterDate)) {
                filterDate = null; // กดวันเดิมซ้ำ = ยกเลิกตัวกรอง
            } else {
                filterDate = picked;
            }
            refreshHistoryList();
        });
        calendar.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerWrapper.add(calendar);
        filterLabel.setForeground(darkText);
        filterLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerWrapper.add(filterLabel);

        // พาเเนลที่จะใส่การ์ด history เรียงกันเป็น Y_AXIS (แนวตั้ง)
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(background);
        listPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ข้อมูลสมมติ
        /*
         * entries = storage.loadAll();
         * refreshHistoryList();
         */

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(background);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.getVerticalScrollBar();

        centerWrapper.add(scrollPane);

        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        add(mainPanel);

        // ช่องค้นหา มีแค่ gui ยังไม่ได้ใส่ให้ค้นหาได้
    }

    // เพิ่มรายการ history ใหม่เข้า listPanel

    // private void DiaryEntry(String date, String title) {

    // JPanel card = createHistoryCard(date, title);
    // listPanel.add(card);
    // listPanel.add(Box.createVerticalStrut(10));

    // listPanel.revalidate();
    // listPanel.repaint();
    // }

    private void refreshHistoryList() {
        listPanel.removeAll();
        filterLabel.setText(filterDate == null ? "Showing: all"
                : "Showing: " + filterDate.format(dateFmt) + " (click again to show all)");
        String q = (searchField == null) ? "" : searchField.getText().trim().toLowerCase();
        int shown = 0;
        for (DiaryEntry entry : entries) {
            if (!matches(entry, q))
                continue;
            listPanel.add(createHistoryCard(entry));
            listPanel.add(Box.createVerticalStrut(10));
            shown++;
        }

        if (shown == 0) {
            JLabel empty = new JLabel("No diary found.");
            empty.setForeground(darkText);
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(empty);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    // สร้าง history 1 ช่อง พร้อมปุ่ม แก้ไข,ลบ

    // private JPanel createHistoryCard(String date, String title) {
    private JPanel createHistoryCard(DiaryEntry entry) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 15));
        card.setMaximumSize(new Dimension(560, 75));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // วันที่ + หัวเรื่อง
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        // JLabel dateLabel = new JLabel(date);
        JLabel dateLabel = new JLabel(entry.getDate().format(dateFmt));
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dateLabel.setForeground(Color.GRAY);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // JLabel titleLabel = new JLabel(title);
        JLabel titleLabel = new JLabel(entry.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setForeground(darkText);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(dateLabel);
        textPanel.add(titleLabel);

        // ปุ่ม แก้ไข,ลบ
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(Color.WHITE);

        JButton editButton = new JButton("Edit");
        editButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        editButton.setForeground(purple);
        editButton.setBorderPainted(false);
        editButton.setContentAreaFilled(false);
        editButton.setFocusPainted(false);
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        editButton.addActionListener(e -> showEdit(entry));

        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        deleteButton.setForeground(Color.RED.darker());
        deleteButton.setBorderPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.addActionListener(e -> {
            JDialog confirm = new JDialog(this, "ยืนยัน", true);
            confirm.setSize(250, 130);
            confirm.setLocationRelativeTo(this);

            JLabel question = new JLabel("ลบรายการนี้?", SwingConstants.CENTER);

            JButton yesButton = new JButton("Yes");
            yesButton.addActionListener(e2 -> {
                if (storage.delete(entry)) {
                    entries.remove(entry);
                    refreshHistoryList();
                }
                confirm.dispose();
            });

            JButton noButton = new JButton("No");
            noButton.addActionListener(e2 -> confirm.dispose());

            JPanel buttons = new JPanel();
            buttons.add(yesButton);
            buttons.add(noButton);

            confirm.add(question, BorderLayout.CENTER);
            confirm.add(buttons, BorderLayout.SOUTH);
            confirm.setVisible(true);
        });

        // ปุ่มแก้ไข/ลบ

        actionPanel.add(editButton);
        actionPanel.add(deleteButton);

        JButton pinButton = new JButton();
        pinButton.setFont(new Font("SansSerif", Font.PLAIN, 20));
        pinButton.setBorderPainted(false);
        pinButton.setContentAreaFilled(false);
        pinButton.setFocusPainted(false);
        pinButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ตั้งหน้าตาปุ่มตามสถานะปัจจุบัน
        if (entry.isPinned()) {
            pinButton.setText("♥");
            pinButton.setForeground(new Color(220, 50, 80)); // ปักหมุดอยู่
        } else {
            pinButton.setText("♡");
            pinButton.setForeground(Color.GRAY); // ยังไม่ปักหมุด
        }
        pinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // สถานะ
                boolean oldValue = entry.isPinned();
                boolean newValue;
                if (oldValue) {
                    newValue = false;
                } else {
                    newValue = true;
                }
                entry.setPinned(newValue);

                // บันทึกลงไฟล์
                try {
                    storage.save(entry);
                } catch (IOException ex) {
                    // บันทึกไม่ได้คืนค่าเดิม
                    entry.setPinned(oldValue);
                    ex.printStackTrace();
                    return;
                }

                // รายการใหม่หัวใจเปลี่ยนสี
                refreshHistoryList();
            }
        });

        card.add(pinButton, BorderLayout.WEST);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(actionPanel, BorderLayout.EAST);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showDetail(entry);
            }
        });
        return card;
    }

    // เช็คว่า diary นี้ควรแสดงในรายการไหม โดยเทียบกับข้อความค้นหา q
    // และวันที่ที่เลือกจากปฏิทิน (filterDate) ถ้า true คือควร
    private boolean matches(DiaryEntry e, String q) {
        if (filterDate != null && !e.getDate().equals(filterDate)) {
            return false;
        }
        if (q.isEmpty())
            return true;
        return e.getTitle().toLowerCase().contains(q) || e.getDate().format(dateFmt).toLowerCase().contains(q);
    }

    // ไว้ใช้ดูเนื้อหาใน Diary ที่เลือก
    private void showDetail(DiaryEntry entry) {
        // จะทำการขึ้นเป็หน้าต่างย่อ และทพให้กดข้างหลังไม่ได้
        JDialog dialog = new JDialog(this, entry.getTitle(), true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JLabel day = new JLabel(entry.getDate().format(dateFmt));
        JTextArea text = new JTextArea(entry.getContent());
        text.setFont(new Font("Tahoma", Font.PLAIN, 14));
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(text);

        // เช็คว่ามีรูปอยู่ไหม มี path ไหม, path ไม่ว่างไหม, และไฟล์รูปยังอยู่จริงไหม
        // ถ้าผ่านหมดค่อยย่อรูปเป็น 200x150 แล้วแสดง ถ้าไม่ผ่านก็ข้ามไปเฉยๆ ไม่ error
        String path = entry.getImagePath();
        if (path != null && !path.isBlank() && new File(path).exists()) {
            ImageIcon icon = new ImageIcon(path);
            Image scaled = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaled));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(imageLabel, BorderLayout.SOUTH);
        }

        panel.add(day, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // ไว้ใช้สำหรับให้โชว์เนื้อหาทั้งหมดในdiary นั้นเพื่อแก้ไขข้อมูล
    private void showEdit(DiaryEntry entry) {
        // ทำให้มีหน้าต่างย่อนขึ้นมาและไม่สามารถกดข้างหลังได้
        JDialog dialog = new JDialog(this, "Edit", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JTextField titleField = new JTextField(entry.getTitle());

        JTextField dateField = new JTextField(entry.getDate().toString());
        JPanel top = new JPanel(new GridLayout(2, 1, 0, 5));
        top.setBackground(Color.WHITE);
        top.add(dateField);
        top.add(titleField);

        // ช่องแก้ไขเนื้อหา
        JTextArea text = new JTextArea(entry.getContent());
        text.setFont(new Font("Tahoma", Font.PLAIN, 14));
        // ถ้าข้อความยาวเกินไปจะตัดข้อความและขึ้นบรรทัดใหม่
        text.setLineWrap(true);
        // ตัดข้อคามตามขอบคำ
        text.setWrapStyleWord(true);
        // ถ้ายาวเกินให้ใช้ scroll เพื่อเลื่่อนดูได้
        JScrollPane scroll = new JScrollPane(text);

        // จัดการรูปภาพ
        // เก็บ path เดิมของ entry
        String[] newImage = { entry.getImagePath() };
        // ไว้ใช้เก็บรูปตัวอย่าง
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 150));

        // เมธอด เพื่อแสดงรูปปัจจุบัน (ถ้าไม่มีรูปหรือไฟล์หาย ก็จะล้างไอคอนให้ว่าง)
        updatePreview(imageLabel, newImage[0]);

        // ปุ่มเปลี่ยนรูป
        JButton changeImageButton = new JButton("Change Image");
        changeImageButton.addActionListener(e -> {
            // ใช้สำหรับเลือกไฟล์ขอระบบ
            JFileChooser chooser = new JFileChooser();
            // กรอกเฉพาะนามสกุลตามที่กำหนด
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Image files (jpg, jpeg, png, gif)", "jpg", "jpeg", "png", "gif"));
            // แสดงหน้าต่างเลือกไฟล์ โดยมี dialog เป็นหน้าต่างแม่ ถ้าผู้ใช้กด Open จะได้
            // APPROVE_OPTION ถ้ากด Cancel หรือปิด จะไม่เข้าเงื่อนไข และไม่มีอะไรเปลี่ยน
            if (chooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                // เก็บ path ของไฟล์ที่เลือกลงอาร์เรย์ แล้วอัปเดตรูปตัวอย่างให้เห็นทันที
                newImage[0] = chooser.getSelectedFile().getPath();
                updatePreview(imageLabel, newImage[0]);
            }
        });

        // กล่องไว้แจ้งเตือน
        Notice = new JLabel(" ");
        Notice.setForeground(Color.RED);

        // ปุ่มเซฟหลังจากแก้ข้อมูลเสร็จ
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // ตรวจวันที่
            LocalDate newDate;

            try {
                // แปลงข้อมูลเป็นวันที่ ถ้าผิดจะมีข้อความแจ้งว่าวันที่ผิด
                newDate = LocalDate.parse(dateField.getText().trim());
            } catch (DateTimeParseException ex) {
                Notice.setText("Please enter a date");
                return;
            }
            // ถ้าตัว title ว่างเปล่าหรือมีแต่ช่องว่าจะขึ้นเตือนให้สิ่งหัวข้อ
            if (titleField.getText().isBlank()) {
                Notice.setText("Please enter a title");
                return;
            }
            // อัพเดทข้อมูลใน entry
            entry.setDate(newDate);
            entry.setTitle(titleField.getText());
            entry.setContent(text.getText());
            entry.setImagePath(newImage[0]);
            try {
                // เขียนทับไฟล์ .txt เดิม(ใช้ id เดิม จึงเป็นการแก้ ไม่ได้สร้างใหม่) และเขียนเสร็จแล้วปิดหน้า
                storage.save(entry);
                refreshHistoryList();
                dialog.dispose();
                // ถ้า error จะแจ้งผลลัพธ์
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // สร้างแผงย่อยที่เรียงจากบนลงล่าง: รูปตัวอย่าง → ปุ่ม Change Image → ปุ่ม Save โดย (0, 8) คือเว้นระยะแนวตั้ง 8 พิกเซล
        JPanel buttons = new JPanel(new BorderLayout(0, 8));
        buttons.setBackground(Color.WHITE);
        buttons.add(changeImageButton, BorderLayout.NORTH);
        buttons.add(saveButton, BorderLayout.CENTER);
        buttons.add(Notice, BorderLayout.SOUTH);

        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.setBackground(Color.WHITE);
        bottom.add(imageLabel, BorderLayout.NORTH);
        bottom.add(buttons, BorderLayout.CENTER);

        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // เมธวดช่วย แสดงรูปตัวอย่างใน labal ถ้า path ไม่ใช่ null, ไม่ว่าง และไฟล์มีอยู่จริง จะโหลดรูป ย่อเป็น 200x150 แล้วตั้งเป็นไอคอนของ label ถ้าไม่ผ่านเงื่อนไข จะเคลียร์ไอคอน (null) ให้ label ว่าง
    private void updatePreview(JLabel label, String path) {
        if (path != null && !path.isBlank() && new File(path).exists()) {
            Image scaled = new ImageIcon(path).getImage()
                    .getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaled));
        } else {
            label.setIcon(null);
        }
    }

    // MAIN (ทดสอบหน้านี้แยก)

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Historypage page = new Historypage();
            page.setVisible(true);
        });
    }
}