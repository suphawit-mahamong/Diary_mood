package page;
//ใช้สร้าง Gui
import java.awt.*;
import javax.swing.*;
// ใช้Locale ในคลาสนี้ บังคับให้วันเดือน เป็นภาษาอังกฤษ
import java.util.*;
// เก็บวันที่จากที่ผู้เลือก
import java.time.LocalDate;
// เก็บวันเดือนปีไม่มีวันที่
import java.time.YearMonth;
// ใช้ระบุเป็นตัวอักษรแทนตัวเลขเช่น เดือน10 ก็ จะเป็น October
import java.time.format.TextStyle;



public class calendar extends JPanel {
    //จำเดือนปีที่กำลังแแสดงอยู่
    private YearMonth currentYearMonth;
    //ำจวันที่ผู้ใช้กดล่าสุด
    private LocalDate selectedDate;
    // ================= COLORS =================
    private final Color background = new Color(246, 227, 229);
    private final Color purple = new Color(187, 82, 138);
    private final Color darkText = new Color(60, 55, 70);

    private final Color dayBg = new Color(255, 249, 250); // พื้นปุ่มวันที่
    private final Color todayBg = new Color(246, 227, 229); // today
    private final Color selectedBg = new Color(187, 82, 138); // วันที่เรากดเลือก
    private final Color weekendText = new Color(200, 60, 90); // วันหยุดจ้าเสาร์อาทิตย์
    //ป้ายหัวปฏิทิน เช่น “October 2026” จัดกึ่งกลาง เริ่มต้นว่าง แล้ว
    private final JLabel monthYearLabel = new JLabel("", SwingConstants.CENTER);
    //พื้นที่ตารางปุ่มวันที่ ใช้ GridLayout(6, 7) คือ 6 แถว 7 คอลัมน์
    private final JPanel daysPanel = new JPanel(new GridLayout(6, 7));
    //ป้ายข้อความด้านล่าง “Selected: Thursday, October 15, 2026” เริ่มต้นเป็นช่องว่าง 1 ตัวเพื่อจองความสูงไว้
    private final JLabel selectedDateLabel = new JLabel(" ", SwingConstants.CENTER);
    //ที่เก็บ"สิ่งงที่ต้องทำเมื่อผู้ใช้คลิกเลือกวัน"
    private java.util.function.Consumer<LocalDate> onDatePicked;
    // คลาสตั้งค่าเริ่มต้นและทำปฏิทิน
    public calendar() {
        //ดึง เดือนและปีปัจจุบัน จากนาฬิกาเครื่อง
        currentYearMonth = YearMonth.now();
        //ดึง วันนี้ มาตั้งเป็นวันที่ถูกเลือกตั้งต้น
        selectedDate = LocalDate.now();
        setLayout(new BorderLayout(8, 8));
        setBackground(background);
        setPreferredSize(new Dimension(480, 270));

        add(buildHeaderPanel(), BorderLayout.NORTH);
        add(buildCalendarPanel(), BorderLayout.CENTER);
        add(buildFooterPanel(), BorderLayout.SOUTH);

        refreshCalendar();
    }
    //ส่วนหัวของปฏิทิน
    public JPanel buildHeaderPanel() {
        //กรอบหัวของปฏิทิน
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(background);
        //ส่วนปุ่มและสี
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        JButton todayButton = new JButton("Today");
        //ใช้ ลูปวนในอาร์เรย์ของปุ่ม ตั้งพื้นม่วง ตัวอักษรขาวให้ทุกปุ่มพร้อมกัน
        for (JButton b : new JButton[] { prevButton, nextButton, todayButton }) {
            //ช่วยให้สีพื้นแสดงจริงในบางธีมของ Swing (บางระบบจะละเลยสีพื้นของปุ่มถ้าไม่ตั้งค่านี้)
            b.setOpaque(true);
            b.setBackground(purple);
            b.setForeground(Color.WHITE);
        }
        monthYearLabel.setForeground(purple);
        // ปุ่ม <
        prevButton.addActionListener(e -> {
            // มาจาก java.time มันคือ ตัวแปร currentYearMonth มีชนิดข้อมูลเป็น YearMonth จาก java.time
            // ส่วน .minusMonths คือการที่ method ที่คืนค่า YearMonth ใหม่ โดยลบเดือนออกไป 1 เดือนจากค่าปัจจุบัน
            currentYearMonth = currentYearMonth.minusMonths(1);
            // เรียก method สร้างปฏิทินใหม่
            refreshCalendar();
        });
        // ปุ่ม >
        nextButton.addActionListener(e -> {
            // มาจาก java.time มันคือ ตัวแปร currentYearMonth มีชนิดข้อมูลเป็น YearMonth จาก java.time
            // ส่วน .plusMonthss คือการที่ method ที่คืนค่า YearMonth ใหม่ โดยเพิ่มเดือนออกไป 1 เดือนจากค่าปัจจุบัน
            currentYearMonth = currentYearMonth.plusMonths(1);
            // เรียก method สร้างปฏิทินใหม่
            refreshCalendar();
        });
        // ปุ่ม today
        todayButton.addActionListener(e -> {
            // มาจาก java.time มันคือ ตัวแปร currentYearMonth มีชนิดข้อมูลเป็น YearMonth จาก java.time
            // ส่วน .now คือstatic method ที่คืนค่า YearMonth ของ เดือนและปีปัจจุบัน (ดึงจากนาฬิการะบบ) ทำให้ปฏิทินกลับไปแสดง "เดือนนี้" ไม่ว่าก่อนหน้านี้ผู้ใช้จะกด prev/next เลื่อนไปเดือนไหนก็ตาม
            currentYearMonth = currentYearMonth.now();
            // LocalDate.now() คืนค่า วันที่ปัจจุบัน แบบเต็ม (วัน-เดือน-ปี)
            selectedDate = LocalDate.now();
            // เรียก method สร้างปฏิทินใหม่
            refreshCalendar();
        });
        // ตั้งปุ่ม < และ > ให้เรียงข้างกัน ตรงกลาง
        JPanel nevJPanel = new JPanel(new FlowLayout());
        nevJPanel.add(prevButton);
        nevJPanel.add(nextButton);
        nevJPanel.setBackground(background);

        header.add(nevJPanel, BorderLayout.CENTER);
        header.add(todayButton, BorderLayout.EAST);
        // ตั้ง เดือน กับ ปี อยู่บนสุด
        header.add(monthYearLabel, BorderLayout.NORTH);


        return header;
    }
    // สร้างส่วนกลาง โดย มี หัววัน กับ ตาราง
    public JPanel buildCalendarPanel() {
        // กรอบรวม 2 ส่วนไว้ โดย ส่วนแถวหัวอยู่บน แถวตารางอยู่กลาง
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(background);

        // แถวหัววันในสัปดาห์
        // ตั้ง 1 แถว 7 ช่องเท่าๆกัน
        JPanel weekDaysPanel = new JPanel(new GridLayout(1, 7));
        weekDaysPanel.setBackground(background);
        String[] dayNames = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        //วนสร้างช่องทีละตัว โดยแต่ละตัวหนา 14
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
    //สร้างกรอบที่มี selectedDateLabel อยู่กลาง ตั้งฟอนต์และสีเทาเข้ม ใช้แสดงข้อความ “Selected: ...”
    public JPanel buildFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(background);
        selectedDateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        selectedDateLabel.setForeground(darkText);
        footer.add(selectedDateLabel, BorderLayout.CENTER);
        return footer;
    }
    // วาดตารางใหม่เรียกทุกครั้งที่เปลี่ยนเดือนหรือเลือกวัน
    private void refreshCalendar() {
        //ล้างหัวเดือน
        daysPanel.removeAll();

        // อัปเดตหัวข้อ เดือน/ปี (พ.ศ.)
        String monthName = currentYearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int year = currentYearMonth.getYear();
        // ตั้งหัวเดือน
        monthYearLabel.setText(monthName + " " + year);

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        // DayOfWeek: MONDAY=1 ... SUNDAY=7 -> แปลงให้ SUNDAY=0
        //คำนวณช่องว่างก่อนวันที1
        int startOffset = firstOfMonth.getDayOfWeek().getValue() % 7;
        // เติมตาราง 42 ช่อง
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
    // สร้างปุ่มวันที่1วัน
    private JButton buildDayButton(LocalDate date) {
        //ปุ่มแสดงเลขวัน
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
        //ตอนกดปุ่ม
        dayButton.addActionListener(e -> {
            //จำวันที่เลือก วาดปฏิทินใหม่ (เพื่อย้ายไฮไลต์) แล้วแจ้งกลับมา ถ้ามี
            selectedDate = date;
            refreshCalendar();
            if (onDatePicked != null) {
                onDatePicked.accept(date);
            }
        });

        return dayButton;
    }
    // ดึงชื่อวัน ชื่อเดือน และปี มาต่อเป็นข้อความ
    private void updateSelectedLabel() {
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String month = selectedDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int year = selectedDate.getYear();
        selectedDateLabel.setText(
                "Selected: " + dayOfWeek + ", " + month + " " + selectedDate.getDayOfMonth()
                        + ", " + year);
    }
    // ให้คลาสอื่นถามว่าตอนนี้เลือกวันไหน
    public LocalDate getSelectedDate() {
        return selectedDate;
    }
    // ให้คลาสอื่น "ฝาก" สิ่งที่ต้องทำเมื่อมีการคลิกวัน
    public void setOnDatePicked(java.util.function.Consumer<LocalDate> listener) {
        this.onDatePicked = listener;
    }

}