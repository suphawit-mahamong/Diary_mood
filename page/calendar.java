package page;

import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import javax.swing.*;

public class calendar extends JPanel {

    private YearMonth currentYearMonth;
    private LocalDate selectedDate;
    // ================= COLORS =================
    private final Color background = new Color(246, 227, 229);
    private final Color purple = new Color(187, 82, 138);
    private final Color darkText = new Color(60, 55, 70);

    private final Color dayBg = new Color(255, 249, 250); // พื้นปุ่มวันที่
    private final Color todayBg = new Color(246, 227, 229); // today
    private final Color selectedBg = new Color(187, 82, 138); // วันที่เรากดเลือก
    private final Color weekendText = new Color(200, 60, 90); // วันหยุดจ้าเสาร์อาทิตย์

    private final JLabel monthYearLabel = new JLabel("", SwingConstants.CENTER);
    private final JPanel daysPanel = new JPanel(new GridLayout(6, 7));
    private final JLabel selectedDateLabel = new JLabel(" ", SwingConstants.CENTER);
    private java.util.function.Consumer<LocalDate> onDatePicked;

    public calendar() {
        currentYearMonth = YearMonth.now();
        selectedDate = LocalDate.now();
        setLayout(new BorderLayout(8, 8));
        setBackground(background);
        setPreferredSize(new Dimension(760, 640));

        add(buildHeaderPanel(), BorderLayout.NORTH);
        add(buildCalendarPanel(), BorderLayout.CENTER);
        add(buildFooterPanel(), BorderLayout.SOUTH);

        refreshCalendar();
    }

    public JPanel buildHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(background);

        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        JButton todayButton = new JButton("Today");
        for (JButton b : new JButton[] { prevButton, nextButton, todayButton }) {
            b.setOpaque(true);
            b.setBackground(purple);
            b.setForeground(Color.WHITE);
        }
        monthYearLabel.setForeground(purple);

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
        nevJPanel.setBackground(background);

        header.add(nevJPanel, BorderLayout.CENTER);
        header.add(todayButton, BorderLayout.EAST);
        header.add(monthYearLabel, BorderLayout.NORTH);


        return header;
    }

    public JPanel buildCalendarPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(background);

        // แถวหัววันในสัปดาห์
        JPanel weekDaysPanel = new JPanel(new GridLayout(1, 7));
        weekDaysPanel.setBackground(background);
        String[] dayNames = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        for (int i = 0; i < dayNames.length; i++) {
            JLabel label = new JLabel(dayNames[i], SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 14));
            
            //ตรงนี้วนลูปเปลี่ยนสีเสาร์-อาทิตย์
            if (i == 0 || i == 6) {
                label.setForeground(weekendText);
            } else {
                label.setForeground(purple);
            }


            weekDaysPanel.add(label);
        }

        daysPanel.setBackground(background);

        wrapper.add(weekDaysPanel, BorderLayout.NORTH);
        wrapper.add(daysPanel, BorderLayout.CENTER);
        return wrapper;
    }

    public JPanel buildFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(background);
        selectedDateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        selectedDateLabel.setForeground(darkText);
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

        boolean isWeekend = date.getDayOfWeek() == java.time.DayOfWeek.SATURDAY
                || date.getDayOfWeek() == java.time.DayOfWeek.SUNDAY;

        dayButton.setOpaque(true);
        dayButton.setBackground(dayBg);
        dayButton.setForeground(isWeekend ? weekendText : darkText);

        if (date.equals(LocalDate.now())) {
            dayButton.setBackground(todayBg); // ไฮไลต์วันนี้
            dayButton.setForeground(darkText);
        }
        if (date.equals(selectedDate)) {
            dayButton.setBackground(selectedBg);
            dayButton.setForeground(Color.WHITE);
            dayButton.setBorder(BorderFactory.createLineBorder(purple, 2));
        }

        dayButton.addActionListener(e -> {
            selectedDate = date;
            refreshCalendar();
            if (onDatePicked != null) {
                onDatePicked.accept(date);
            }
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

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setOnDatePicked(java.util.function.Consumer<LocalDate> listener) {
        this.onDatePicked = listener;
    }

}