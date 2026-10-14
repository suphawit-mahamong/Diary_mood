package page;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.*;

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
        entries = storage.loadAll();
        refreshHistoryList();

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

        // เก็บหัวเรื่องไว้ใน property ของการ์ด เพื่อให้ช่องค้นหากรองได้ (ค่อยทำ)
        card.putClientProperty("title", getTitle());

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
            int ok = JOptionPane.showConfirmDialog(card, "ลบรายการนี้?", "ยืนยัน",
                    JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                if (!storage.delete(entry)) {
                    JOptionPane.showMessageDialog(card, "Delete failed");
                    return;
                }
                entries.remove(entry);
                refreshHistoryList();
            }
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
            pinButton.setForeground(new Color(220, 50, 80));   // ปักหมุดอยู่
            } else {
                pinButton.setText("♡");
                pinButton.setForeground(Color.GRAY);  // ยังไม่ปักหมุด
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
                    JOptionPane.showMessageDialog(card, "Save failed: " + ex.getMessage());
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

    private boolean matches(DiaryEntry e, String q) {
        if (filterDate != null && !e.getDate().equals(filterDate)) {
            return false;
        }
        if (q.isEmpty())
            return true;
        return e.getTitle().toLowerCase().contains(q) || e.getDate().format(dateFmt).toLowerCase().contains(q);
    }

    private void showDetail(DiaryEntry entry) {
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

    private void showEdit(DiaryEntry entry) {
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

        JTextArea text = new JTextArea(entry.getContent());
        text.setFont(new Font("Tahoma", Font.PLAIN, 14));
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(text);

        String[] newImage = { entry.getImagePath() };
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 150));

        updatePreview(imageLabel, newImage[0]);

        JButton changeImageButton = new JButton("Change Image");
        changeImageButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Image files (jpg, jpeg, png, gif)", "jpg", "jpeg", "png", "gif"));
            if (chooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                newImage[0] = chooser.getSelectedFile().getPath();
                updatePreview(imageLabel, newImage[0]);
            }
        });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            LocalDate newDate;
            try {
                newDate = LocalDate.parse(dateField.getText().trim());
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Date must be yyyy-MM-dd, e.g. 2026-10-13");
                return;
            }

            if (titleField.getText().isBlank()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a title");
                return;
            }
            entry.setDate(newDate);
            entry.setTitle(titleField.getText());
            entry.setContent(text.getText());
            entry.setImagePath(newImage[0]);
            try {
                storage.save(entry);
                refreshHistoryList();
                dialog.dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(dialog, "Save failed: " + ex.getMessage());
            }
        });

        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.setBackground(Color.WHITE);
        bottom.add(imageLabel, BorderLayout.NORTH);
        bottom.add(changeImageButton, BorderLayout.CENTER);
        bottom.add(saveButton, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

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