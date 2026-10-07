package page;

import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import javax.swing.*;

public class Historypage extends JFrame {

    // ================= COLORS =================
    private final Color background = new Color(246, 227, 229);
    private final Color purple = new Color(187, 82, 138);
    private final Color darkText = new Color(60, 55, 70);

    private static final int SIDEBAR_EXPANDED_WIDTH = 150;
    private static final int SIDEBAR_COLLAPSED_WIDTH = 55;
    private boolean sidebarExpanded = true;

    private JPanel sidebar;
    private final java.util.List<JButton> sidebarButtons = new java.util.ArrayList<>();
    private final String[] sidebarLabels = { "HomePage", "HistoryPage", "Logout" };

    // พาเนลที่เก็บรายการ history ทั้งหมด (ไว้เพื่อลบ/กรองการ์ดออกได้)
    private JPanel listPanel;

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
        searchPanel.setMaximumSize(new Dimension(560, 40));

        JTextField searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setBorder(null);

        JLabel searchIcon = new JLabel("🔎");
        searchIcon.setFont(new Font("SansSerif", Font.PLAIN, 14));

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // EAST
        mainPanel.add(createSidebar(), BorderLayout.EAST);

        // History

        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setBackground(background);

        centerWrapper.add(Box.createVerticalStrut(15));

        JLabel historyTitle = new JLabel("History");
        historyTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        historyTitle.setForeground(darkText);
        historyTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerWrapper.add(historyTitle);
        centerWrapper.add(Box.createVerticalStrut(15));

        // Calendar
        CalendarPanel calendarPanel = new CalendarPanel();
        calendarPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerWrapper.add(calendarPanel);

        // พาเเนลที่จะใส่การ์ด history เรียงกันเป็น Y_AXIS (แนวตั้ง)
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(background);
        listPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ข้อมูลสมมติ
        addHistoryEntry("12/09/26", "Title1");
        addHistoryEntry("13/09/26", "Title2");
        addHistoryEntry("14/09/26", "Title3");

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

    private void addHistoryEntry(String date, String title) {

        JPanel card = createHistoryCard(date, title);
        listPanel.add(card);
        listPanel.add(Box.createVerticalStrut(10));

        listPanel.revalidate();
        listPanel.repaint();
    }

    // สร้าง history 1 ช่อง พร้อมปุ่ม แก้ไข,ลบ

    private JPanel createHistoryCard(String date, String title) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 15));
        card.setMaximumSize(new Dimension(560, 75));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // วันที่ + หัวเรื่อง
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dateLabel.setForeground(Color.GRAY);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setForeground(darkText);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(dateLabel);
        textPanel.add(titleLabel);

        // เก็บหัวเรื่องไว้ใน property ของการ์ด เพื่อให้ช่องค้นหากรองได้ (ค่อยทำ)
        card.putClientProperty("title", title);

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
        editButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        deleteButton.setForeground(Color.RED.darker());
        deleteButton.setBorderPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setFocusPainted(false);
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ปุ่มแก้ไข/ลบ

        actionPanel.add(editButton);
        actionPanel.add(deleteButton);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(actionPanel, BorderLayout.EAST);

        return card;
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

    // MAIN (ทดสอบหน้านี้แยก)

    public static void main(String[] args) {
            Historypage page = new Historypage();
            page.setVisible(true);
    }
}