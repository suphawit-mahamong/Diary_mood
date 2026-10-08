package page;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import javax.swing.Timer.*;

public class calendar extends JPanel {

    private YearMonth currentYearMonth;
    private LocalDate selectedDate;

    private final JLabel monthYearLabel = new JLabel("", SwingConstants.CENTER);
    private final JPanel daysPanel = new JPanel(new GridLayout(6, 7));
    private final JLabel selectedDateLabel = new JLabel(" ", SwingConstants.CENTER);

    public calendar() {
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
