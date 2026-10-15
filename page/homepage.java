package page;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;


public class homepage extends JFrame {
        private sidebar sideBar;
        private JPanel pinnedPanel;
        private JPanel todoList;

        public homepage() {

                setTitle("Diary mood");
                setSize(640, 640);
                setMinimumSize(new Dimension(640, 640));
                setLocationRelativeTo(null);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setLocationRelativeTo(null);

                ImageIcon img = new ImageIcon("C:\\Users\\sukit\\Downloads\\lib-nobg.png");
                setIconImage(img.getImage());

                createUI();
                // pack(); // ให้ Swing คำนวณขนาดจากเนื้อหาจริง
                setLocationRelativeTo(null);
                setVisible(true);
        }

        private void createUI() {

                // COLORS

                Color background = new Color(246, 227, 229);
                Color purple = new Color(187, 82, 138);
                Color darkText = new Color(60, 55, 70);

                // MAIN PANEL

                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                mainPanel.setBackground(background);

                mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));

                // EAST
                sideBar = new sidebar("HomePage");
                JPanel sideWrapper = new JPanel(new BorderLayout());
                sideWrapper.setOpaque(false);
                sideWrapper.add(sideBar, BorderLayout.NORTH);

                mainPanel.add(sideWrapper, BorderLayout.EAST);
                JButton menuBtn = new JButton("☰");
                menuBtn.addActionListener(e -> sideBar.toggle());
                JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                topBar.setBackground(background);
                topBar.add(menuBtn);
                mainPanel.add(topBar, BorderLayout.NORTH);

                // TOP

                JPanel topPanel = new JPanel(new BorderLayout());

                topPanel.setBackground(background);

                JPanel greetingPanel = new JPanel();
                greetingPanel.setLayout(new BoxLayout(greetingPanel, BoxLayout.Y_AXIS));
                greetingPanel.setBackground(background);

                // โชว์ชื่อผู้ใช้
                String name;
                if (Collection.isLoggedIn()) {
                        name = Collection.getUser();
                } else {
                        name = "User";
                }

                // โชว์ในหน้า homepage hi + username
                JLabel greetingLine1 = new JLabel("Hi, " + name);
                greetingLine1.setFont(new Font("SansSerif", Font.BOLD, 18));
                greetingLine1.setForeground(darkText);
                greetingLine1.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel greetingLine2 = new JLabel("How was your day?");
                greetingLine2.setFont(new Font("SansSerif", Font.PLAIN, 13));
                greetingLine2.setForeground(darkText);
                greetingLine2.setAlignmentX(Component.LEFT_ALIGNMENT);

                greetingPanel.add(greetingLine1);
                greetingPanel.add(greetingLine2);

                topPanel.add(greetingPanel, BorderLayout.WEST);
                topPanel.add(topBar, BorderLayout.EAST);
                mainPanel.add(topPanel, BorderLayout.NORTH);

                // CENTER

                JPanel contentPanel = new JPanel();
                contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
                contentPanel.setBackground(background);
                contentPanel.add(Box.createVerticalStrut(25));

                // NEW DIARY BUTTON

                JButton newDiaryButton = new JButton(" + NEW DIARY");

                newDiaryButton.setFont(new Font("SansSerif", Font.BOLD, 16));
                newDiaryButton.setBackground(purple);
                newDiaryButton.setForeground(Color.WHITE);
                newDiaryButton.setFocusPainted(false);
                newDiaryButton.setMaximumSize(new Dimension(420, 55));
                newDiaryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                newDiaryButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                if (e.getSource() == newDiaryButton) {
                                        // ใส่เปลี่ยนหน้า
                                        new Diary(new Runnable() {
                                                // เมื่อสร้าง diary เสร็จ ให้เรียก refreshPinned()" เพื่อให้หน้า Home โหลดรายการ pinned ใหม่ทันที ถ้าผู้ใช้ปักหมุด diary อันใหม่ จะขึ้นที่หน้า Home เลยโดยไม่ต้องเปิดใหม่
                                                public void run() {
                                                        refreshPinned();
                                                }
                                        });
                                }

                        }
                });

                contentPanel.add(newDiaryButton);

                contentPanel.add(Box.createVerticalStrut(25));

                // PINNED DIARY

                JLabel pinnedTitle = new JLabel("PINNED DIARY");
                pinnedTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
                pinnedTitle.setForeground(darkText);
                pinnedTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(pinnedTitle);
                contentPanel.add(Box.createVerticalStrut(10));

                // pinned
                // สร้าง panel เปล่าสำหรับใส่การ์ด pinned เรียงแนวตั้ง แล้วเรียก refreshPinned() ครั้งแรกเพื่อโหลดข้อมูลมาแสดง
                pinnedPanel = new JPanel();
                pinnedPanel.setLayout(new BoxLayout(pinnedPanel, BoxLayout.Y_AXIS));
                pinnedPanel.setBackground(background);
                pinnedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(pinnedPanel);
                refreshPinned();
                contentPanel.add(Box.createVerticalStrut(25));

                // list to do

                JLabel ListToDoTitle = new JLabel("TODAY");
                ListToDoTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
                ListToDoTitle.setForeground(darkText);
                ListToDoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(ListToDoTitle);
                contentPanel.add(Box.createVerticalStrut(10));

                JPanel todoPanel = new JPanel(new BorderLayout(0, 8));
                todoPanel.setBackground(Color.WHITE);
                todoPanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
                todoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 145));
                todoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                // แถวกรอกงานใหม่
                JPanel inputRow = new JPanel(new BorderLayout(8, 0));
                inputRow.setBackground(Color.WHITE);
                JTextField todoField = new JTextField(10);
                JButton addTodoButton = new JButton("Add");
                addTodoButton.setBackground(purple);
                addTodoButton.setForeground(Color.WHITE);
                addTodoButton.setFocusPainted(false);
                inputRow.add(todoField, BorderLayout.CENTER);
                inputRow.add(addTodoButton, BorderLayout.EAST);
                contentPanel.add(todoPanel);
                // รายการงาน
                todoList = new JPanel();
                todoList.setLayout(new BoxLayout(todoList, BoxLayout.Y_AXIS));
                todoList.setBackground(Color.WHITE);

                JScrollPane todoScroll = new JScrollPane(todoList);
                todoScroll.setBorder(null);
                todoScroll.getViewport().setBackground(Color.WHITE);

                todoPanel.add(inputRow, BorderLayout.NORTH);
                todoPanel.add(todoScroll, BorderLayout.CENTER);

                // กด Add หรือกด Enter ในช่องพิมพ์
                ActionListener addTodoAction = new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                String text = todoField.getText().trim();
                                if (text.isEmpty()) {
                                        return;
                                }
                                addTodo(text);
                                todoField.setText("");
                                todoField.requestFocus();
                        }
                };
                addTodoButton.addActionListener(addTodoAction);
                todoField.addActionListener(addTodoAction);
                contentPanel.add(todoPanel);

                mainPanel.add(contentPanel, BorderLayout.CENTER);

                // แถบไปหน้าอื่น

                JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 5));

                bottomPanel.setBackground(background);

                JButton diaryButton = new JButton("Diary");

                diaryButton.setBorderPainted(false);

                diaryButton.setContentAreaFilled(false);

                diaryButton.setFocusPainted(false);

                bottomPanel.add(diaryButton);

                mainPanel.add(bottomPanel, BorderLayout.SOUTH);

                // ADD MAIN PANEL

                add(mainPanel);
        }

        // เมธอดนี้ทำหน้าที่ โหลดและวาดรายการ diary ที่ปักหมุดใหม่
        private void refreshPinned() {
                // ลบ component ทุกอย่างใน pinnedPanel ออกก่อน
                pinnedPanel.removeAll();
                // คัดเฉพาะอันที่ปักหมุด
                List<DiaryEntry> pinned = new ArrayList<>();
                //  สร้าง storage ของผู้ใช้ที่ล็อกอินอยู่ (โฟลเดอร์ diaries/<username>) อ่านไฟล์ .txt ทั้งหมด คืนเป็นลิสต์ที่เรียงจากวันที่ใหม่ไปเก่าวนดูทีละอัน เก็บเฉพาะอันที่ isPinned() เป็น true ลงลิสต์ pinned
                for (DiaryEntry e : DiaryStorage.CurrentUser().loadAll()) {
                        if (e.isPinned()) {
                                pinned.add(e);
                        }
                }
                // แสดงข้อความแนะนำว่า "ยังไม่มี diary ที่ปักหมุด กดรูป ♡ ในหน้า History เพื่อปักหมุด" จัดชิดซ้าย
                if (pinned.isEmpty()) {
                        JLabel none = new JLabel("No pinned diary yet. Tap ♡ in History to pin one.");
                        none.setForeground(new Color(60, 55, 70));
                        none.setAlignmentX(Component.LEFT_ALIGNMENT);
                        pinnedPanel.add(none);
                }
                // วนลูป เพื่อเลือกค่าที่น้อยที่สุดมาแสดง โดยจะแสดงแค่ 2 อัน
                for (int k = 0; k < Math.min(pinned.size(), 2); k++) {
                        DiaryEntry e = pinned.get(k);
                        pinnedPanel.add(createPinnedNote(e.getTitle(), e.getDate().toString()));
                        pinnedPanel.add(Box.createVerticalStrut(10));
                }
                // บอก Swing ให้วาดใหม่ หลังเพิ่ม/ลบ component
                pinnedPanel.revalidate();
                pinnedPanel.repaint();
        }

        // Pinned Note รับ หัวข้อโน้ตกับวันที่

        private JPanel createPinnedNote(String title, String date) {
                JPanel note = new JPanel(new BorderLayout());
                note.setBackground(Color.WHITE);
                note.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
                note.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
                note.setAlignmentX(Component.LEFT_ALIGNMENT);

                // ข้อความที่แสดงชื่อเรื่องและวันที่เป็นโน้ตแยกกัน
                JPanel textPanel = new JPanel();
                textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
                textPanel.setBackground(Color.WHITE);

                JLabel titleLabel = new JLabel(title);
                titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel dateLabel = new JLabel(date);
                dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
                dateLabel.setForeground(Color.GRAY);
                dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                textPanel.add(titleLabel);
                textPanel.add(dateLabel);

                note.add(textPanel, BorderLayout.CENTER);

                return note;
        }

        private void addTodo(String text) {
                JCheckBox box = new JCheckBox(text);
                box.setBackground(Color.WHITE);
                box.setFont(new Font("SansSerif", Font.PLAIN, 14));
                box.setAlignmentX(Component.LEFT_ALIGNMENT);

                box.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                if (box.isSelected()) {
                                        todoList.remove(box); // ติ๊กแล้วหายไป
                                        todoList.revalidate();
                                        todoList.repaint();
                                }
                        }
                });
                todoList.add(box);
                todoList.revalidate();
                todoList.repaint();
        }

}
