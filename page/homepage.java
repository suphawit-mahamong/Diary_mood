package page;

import java.awt.*;
import javax.swing.*;

import page.homepage.CalendarPanel;

import java.awt.event.*;
import java.util.Locale;
import java.util.Scanner;
import java.time.*;
import java.time.format.TextStyle;

public class homepage extends JFrame {
        private static final int SIDEBAR_EXPANDED_WIDTH = 150;
        private static final int SIDEBAR_COLLAPSED_WIDTH = 55;
        private boolean sidebarExpanded = true;

        private JPanel sidebar;
        private final java.util.List<JButton> sidebarButtons = new java.util.ArrayList<>();
        private final String[] sidebarLabels = { "HomePage", "HistoryPage", "Logout" };

        public homepage() {

                setTitle("Diary mood");
                setSize(650, 640);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setLocationRelativeTo(null);

                ImageIcon img = new ImageIcon("C:\\Users\\sukit\\Downloads\\lib-nobg.png");
                setIconImage(img.getImage());

                createUI();
                pack();                     // ให้ Swing คำนวณขนาดจากเนื้อหาจริง
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

                // TOP

                JPanel topPanel = new JPanel(new BorderLayout());

                topPanel.setBackground(background);

                JPanel greetingPanel = new JPanel();
                greetingPanel.setLayout(new BoxLayout(greetingPanel, BoxLayout.Y_AXIS));
                greetingPanel.setBackground(background);

                JLabel greetingLine1 = new JLabel("Hi, User");
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

                mainPanel.add(topPanel, BorderLayout.NORTH);

                // CENTER

                JPanel contentPanel = new JPanel();

                contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

                contentPanel.setBackground(background);

                contentPanel.add(Box.createVerticalStrut(25));

                // EAST
                mainPanel.add(createSidebar(), BorderLayout.EAST);

                // NEW DIARY BUTTON

                JButton newDiaryButton = new JButton(" + NEW DIARY");

                newDiaryButton.setFont(new Font("SansSerif", Font.BOLD, 16));

                newDiaryButton.setBackground(purple);
                newDiaryButton.setForeground(Color.WHITE);

                newDiaryButton.setFocusPainted(false);

                newDiaryButton.setMaximumSize(new Dimension(420, 55));

                newDiaryButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                contentPanel.add(newDiaryButton);

                contentPanel.add(Box.createVerticalStrut(25));

                // PINNED DIARY

                JLabel pinnedTitle = new JLabel("PINNED DIARY");

                pinnedTitle.setFont(new Font("SansSerif", Font.BOLD, 16));

                pinnedTitle.setForeground(darkText);

                pinnedTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

                contentPanel.add(pinnedTitle);

                contentPanel.add(Box.createVerticalStrut(10));

                // pinned Diary 1

                JPanel pinnedDiary1 = createPinnedNote("My first day at university", "15/09/2026");

                contentPanel.add(pinnedDiary1);

                contentPanel.add(Box.createVerticalStrut(10));

                // pinned Diary 2

                JPanel pinnedDiary2 = createPinnedNote("My favorite memory", "10/09/2026");

                contentPanel.add(pinnedDiary2);

                contentPanel.add(Box.createVerticalStrut(25));

                // list to do

                JLabel ListToDoTitle = new JLabel("TODAY");

                ListToDoTitle.setFont(new Font("SansSerif", Font.BOLD, 16));

                ListToDoTitle.setForeground(darkText);

                ListToDoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

                contentPanel.add(ListToDoTitle);

                contentPanel.add(Box.createVerticalStrut(10));

                JPanel todoPanel = new JPanel();

                todoPanel.setLayout(new BoxLayout(todoPanel, BoxLayout.Y_AXIS));

                todoPanel.setBackground(Color.WHITE);

                todoPanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

                todoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 145));

                todoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JCheckBox task1 = new JCheckBox("Writing diary");

                JCheckBox task2 = new JCheckBox("Read a book");

                JCheckBox task3 = new JCheckBox("Do homework");

                task1.setBackground(Color.WHITE);
                task2.setBackground(Color.WHITE);
                task3.setBackground(Color.WHITE);

                task1.setFont(new Font("SansSerif", Font.PLAIN, 14));

                task2.setFont(new Font("SansSerif", Font.PLAIN, 14));

                task3.setFont(new Font("SansSerif", Font.PLAIN, 14));

                todoPanel.add(task1);
                todoPanel.add(task2);
                todoPanel.add(task3);

                contentPanel.add(todoPanel);

                // Calendar
                CalendarPanel calendarPanel = new CalendarPanel();
                calendarPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(calendarPanel);

                /*JScrollPane scrollPane = new JScrollPane(contentPanel);
                scrollPane.setBorder(null);
                scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                mainPanel.add(scrollPane, BorderLayout.CENTER);*/
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

        // sidebar
        private JPanel createSidebar() {
                sidebar = new JPanel();
                sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
                sidebar.setBackground(new Color(204, 204, 204));
                sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
                sidebar.setPreferredSize(new Dimension(
                                sidebarExpanded ? SIDEBAR_EXPANDED_WIDTH : SIDEBAR_COLLAPSED_WIDTH, 0));

                JButton toggleButton = new JButton("☰");
                toggleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                toggleButton.setMaximumSize(new Dimension(40, 30));
                toggleButton.setFocusPainted(false);
                toggleButton.addActionListener(e -> toggleSidebar());
                sidebar.add(toggleButton);
                sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

                for (String label : sidebarLabels) {
                        JButton btn = new JButton(sidebarExpanded ? label : label.substring(0, 1));
                        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

                        Dimension btnSize = new Dimension(sidebarExpanded ? 120 : 40, 35);
                        btn.setMaximumSize(btnSize);
                        btn.setPreferredSize(btnSize);
                        btn.setMargin(new Insets(0, 0, 0, 0));
                        btn.setFocusPainted(false);

                        // ผูก action ตาม label ของปุ่ม
                        switch (label) {
                                case "HomePage":
                                        btn.addActionListener(e -> {
                                                new homepage();
                                                dispose();
                                        });
                                        break;

                                case "HistoryPage":
                                        btn.addActionListener(e -> {
                                                new Historypage();
                                                dispose();
                                        });
                                        break;

                                case "Logout":
                                        btn.addActionListener(e -> {
                                                new loginpage();
                                                dispose();
                                        });
                                        break;
                        }

                        sidebarButtons.add(btn);
                        sidebar.add(btn);
                        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
                }

                sidebar.add(Box.createVerticalGlue());
                return sidebar;
        }

        private void toggleSidebar() {
                sidebarExpanded = !sidebarExpanded;
                sidebar.setPreferredSize(new Dimension(
                                sidebarExpanded ? SIDEBAR_EXPANDED_WIDTH : SIDEBAR_COLLAPSED_WIDTH, 0));

                for (int i = 0; i < sidebarButtons.size(); i++) {
                        JButton btn = sidebarButtons.get(i);
                        String full = sidebarLabels[i];
                        btn.setToolTipText(full);
                        btn.setText(sidebarExpanded ? full : full.substring(0, 1));
                        btn.setMaximumSize(new Dimension(sidebarExpanded ? 120 : 40, 35));
                }

                sidebar.revalidate();
                sidebar.repaint();
        }

        // Caendar
        class CalendarPanel extends JPanel {
                private YearMonth currentYearMonth;
                private LocalDate selectedDate;

                private final JLabel monthYearLabel = new JLabel("", SwingConstants.CENTER);
                private final JPanel daysPanel = new JPanel(new GridLayout(6, 7));
                private final JLabel selectedDateLabel = new JLabel(" ", SwingConstants.CENTER);

                public CalendarPanel() {
                        currentYearMonth = YearMonth.now();
                        selectedDate = LocalDate.now();
                        setLayout(new BorderLayout(8, 8));

                        add(buildHeaderPanel(), BorderLayout.NORTH);
                        add(buildCalendarPanel(), BorderLayout.CENTER);
                        add(buildFooterPanel(), BorderLayout.SOUTH);

                        refreshCalendar();
                }

                public JPanel buildHeaderPanel() {
                        JPanel header = new JPanel(new BorderLayout());

                        JButton prevButton = new JButton("<");
                        JButton nextButton = new JButton(">");
                        JButton todayButton = new JButton("Today");

                        prevButton.addActionListener(e -> {
                                // มาจาก java.time มันคือ ตัวแปร currentYearMonth มีชนิดข้อมูลเป็น YearMonth จาก
                                // java.time
                                // ส่วน .minusMonths คือการที่ method ที่คืนค่า YearMonth ใหม่ โดยลบเดือนออกไป 1
                                // เดือนจากค่าปัจจุบัน
                                currentYearMonth = currentYearMonth.minusMonths(1);
                                // เรียก method สร้างปฏิทินใหม่
                                refreshCalendar();
                        });

                        nextButton.addActionListener(e -> {
                                // มาจาก java.time มันคือ ตัวแปร currentYearMonth มีชนิดข้อมูลเป็น YearMonth จาก
                                // java.time
                                // ส่วน .plusMonthss คือการที่ method ที่คืนค่า YearMonth ใหม่
                                // โดยเพิ่มเดือนออกไป 1 เดือนจากค่าปัจจุบัน
                                currentYearMonth = currentYearMonth.plusMonths(1);
                                // เรียก method สร้างปฏิทินใหม่
                                refreshCalendar();
                        });
                        todayButton.addActionListener(e -> {
                                // มาจาก java.time มันคือ ตัวแปร currentYearMonth มีชนิดข้อมูลเป็น YearMonth จาก
                                // java.time
                                // ส่วน .now คือstatic method ที่คืนค่า YearMonth ของ เดือนและปีปัจจุบัน
                                // (ดึงจากนาฬิการะบบ) ทำให้ปฏิทินกลับไปแสดง "เดือนนี้"
                                // ไม่ว่าก่อนหน้านี้ผู้ใช้จะกด prev/next เลื่อนไปเดือนไหนก็ตาม
                                currentYearMonth = currentYearMonth.now();
                                // LocalDate.now() คืนค่า วันที่ปัจจุบัน แบบเต็ม (วัน-เดือน-ปี)
                                selectedDate = LocalDate.now();
                                // เรียก method สร้างปฏิทินใหม่
                                refreshCalendar();
                        });

                        JPanel nevJPanel = new JPanel(new FlowLayout());
                        nevJPanel.add(prevButton);
                        nevJPanel.add(nextButton);
                        nevJPanel.add(todayButton);

                        header.add(nevJPanel, BorderLayout.CENTER);
                        header.add(todayButton, BorderLayout.EAST);
                        header.add(monthYearLabel, BorderLayout.NORTH);

                        return header;
                }

                public JPanel buildCalendarPanel() {
                        JPanel wrapper = new JPanel(new BorderLayout());

                        // แถวหัววันในสัปดาห์
                        JPanel weekDaysPanel = new JPanel(new GridLayout(1, 7));
                        String[] dayNames = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
                        for (String d : dayNames) {
                                JLabel label = new JLabel(d, SwingConstants.CENTER);
                                label.setFont(new Font("SansSerif", Font.BOLD, 14));
                                weekDaysPanel.add(label);
                        }

                        wrapper.add(weekDaysPanel, BorderLayout.NORTH);
                        wrapper.add(daysPanel, BorderLayout.CENTER);
                        return wrapper;
                }

                public JPanel buildFooterPanel() {
                        JPanel footer = new JPanel(new BorderLayout());
                        selectedDateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                        footer.add(selectedDateLabel, BorderLayout.CENTER);
                        return footer;
                }

                private void refreshCalendar() {
                        daysPanel.removeAll();

                        // อัปเดตหัวข้อ เดือน/ปี (พ.ศ.)
                        String monthName = currentYearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                        int year = currentYearMonth.getYear();
                        monthYearLabel.setText(monthName + " " + year);

                        LocalDate firstOfMonth = currentYearMonth.atDay(1);
                        // DayOfWeek: MONDAY=1 ... SUNDAY=7 -> แปลงให้ SUNDAY=0
                        int startOffset = firstOfMonth.getDayOfWeek().getValue() % 7;

                        // ช่องว่างก่อนวันที่ 1
                        for (int i = 0; i < startOffset; i++) {
                                daysPanel.add(new JLabel(""));
                        }

                        int daysInMonth = currentYearMonth.lengthOfMonth();
                        for (int day = 1; day <= daysInMonth; day++) {
                                LocalDate date = currentYearMonth.atDay(day);
                                daysPanel.add(buildDayButton(date));
                        }
                        int totalCells = startOffset + daysInMonth;
                        int trailing = (6 * 7) - totalCells;
                        for (int i = 0; i < trailing; i++) {
                                daysPanel.add(new JLabel(""));
                        }

                        updateSelectedLabel();
                        daysPanel.revalidate();
                        daysPanel.repaint();
                }

                private JButton buildDayButton(LocalDate date) {
                        JButton dayButton = new JButton(String.valueOf(date.getDayOfMonth()));
                        dayButton.setMargin(new Insets(2, 2, 2, 2));

                        if (date.equals(LocalDate.now())) {
                                dayButton.setBackground(new Color(255, 230, 150)); // ไฮไลต์วันนี้
                                dayButton.setOpaque(true);
                        }
                        if (date.equals(selectedDate)) {
                                dayButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                        }

                        dayButton.addActionListener(e -> {
                                selectedDate = date;
                                refreshCalendar();
                        });

                        return dayButton;
                }

                private void updateSelectedLabel() {
                        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                        String month = selectedDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                        int year = selectedDate.getYear();
                        selectedDateLabel.setText(
                                        "Selected: " + dayOfWeek + ", " + month + " " + selectedDate.getDayOfMonth()
                                                        + ", " + year);
                }

        }
}
