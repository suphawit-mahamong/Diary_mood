package page;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
// ไว้ทำ animate
import javax.swing.Timer;

public class sidebar extends JPanel {
    //ระยะการขยับแบบ animate
    private static final int STEP = 5;
    // ขนาดความกว้างตอนเปิดของ sidebar
    private static final int SIDEBAR_EXPANDED_WIDTH = 150;
    // ขนาดความกว้างตอนปิดของ sidebar
    private static final int SIDEBAR_COLLAPSED_WIDTH = 0;

    // COLORS
    Color background = new Color(246, 227, 229);
    Color purple = new Color(187, 82, 138);
    Color darkText = new Color(60, 55, 70);
    // เก็บชื่อหน้าที่เปิดอยู่ เพื่อเอาไปคำนวณและปิดปุ่มหน้าที่เปิดอยู่ 
    private final String currentPage;
    //บอก sidebar ว่าควรเปิดหรือปิด โดย false = ควรหุบ, true = ควรกาง ฟังก์ชัน toggle() สลับค่านี้ และ animate() ดูค่านี้เพื่อตัดสินว่าจะเพิ่มหรือลดความกว้าง
    private boolean open = false;
    // ความกว้างเริ่มต้น
    private int currentWidth = 0;
    // ใช้จับเวลา
    private Timer timer;
    // ทำหน้าที่สั่ง open เหมือกันแต่ไม่ได้ใช้
    boolean sidebarExpanded = false;

    // list  ปุ่ม ที่ต้องใช้ และ สมาชิกปุ่ม
    private final java.util.List<JButton> sidebarButtons = new java.util.ArrayList<>();
    private final String[] sidebarLabels = { "HomePage", "HistoryPage", "Logout" };
    // ตัว sidebar 
    public sidebar(String currentPage) {
        //เก็บชื่อหน้าปัจจุบันที่ส่งเข้ามา (เช่น "HomePage") จัด layout แบบเรียงแนวตั้ง ตั้งขนาดเริ่มต้น กว้าง 0 (หุบอยู่) สูงตามที่คำนวณ
        this.currentPage = currentPage;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(background);
        setPreferredSize(new Dimension(SIDEBAR_COLLAPSED_WIDTH, getContentHeight()));
        currentWidth = SIDEBAR_COLLAPSED_WIDTH;
        // สร้างแและตกแต่งตัวปุ่ม (ไม่ได้ใช้เพราะไปสร้างในหน้าที่ต้องใช้แล้ว)
        JButton toggleButton = new JButton("☰");
        toggleButton.setBackground(purple);
        toggleButton.setForeground(Color.WHITE);
        toggleButton.setOpaque(true);
        toggleButton.setBorderPainted(false);
        toggleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggleButton.setMaximumSize(new Dimension(40, 30));
        toggleButton.setFocusPainted(false);
        toggleButton.addActionListener(e -> toggle());
        // วนลูบสร้างปุ่มจาก “HomePage”, “HistoryPage”, “Logout”
        for (String label : sidebarLabels) {
            JButton btn = new JButton(label);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            //กำหนดขนาด 120x35 และตัดระยะขอบในปุ่มเป็น 0 เพื่อให้ข้อความพอดี
            Dimension btnSize = new Dimension(120, 35);
            btn.setMaximumSize(btnSize);
            btn.setPreferredSize(btnSize);
            btn.setMargin(new Insets(0, 0, 0, 0));
            btn.setFocusPainted(false);
            btn.setBackground(purple);
            btn.setForeground(Color.WHITE);

            // ผูก action ตาม label ของปุ่ม
            if (label.equals(currentPage)) {
                btn.setEnabled(false);
            } else {
                switch (label) {
                    case "HomePage":
                        btn.addActionListener(e -> {
                            new homepage();
                            //หาหน้าต่าง (JFrame) ที่ sidebar อยู่ แล้วปิดทิ้ง ผลคือ "เปิดหน้าต่างใหม่ ปิดหน้าต่างเก่า" เหมือนเปลี่ยนหน้า
                            SwingUtilities.getWindowAncestor(this).dispose();
                        });
                        break;

                    case "HistoryPage":
                        btn.addActionListener(e -> {
                            new Historypage();
                            //หาหน้าต่าง (JFrame) ที่ sidebar อยู่ แล้วปิดทิ้ง ผลคือ "เปิดหน้าต่างใหม่ ปิดหน้าต่างเก่า" เหมือนเปลี่ยนหน้า
                            SwingUtilities.getWindowAncestor(this).dispose();
                        });
                        break;

                    case "Logout":
                        btn.addActionListener(e -> {
                            // logoutได้เลยโดยจะมีการล้างชื่อผู้ใช้ให้
                            Collection.logout();
                            new loginpage();
                            //หาหน้าต่าง (JFrame) ที่ sidebar อยู่ แล้วปิดทิ้ง ผลคือ "เปิดหน้าต่างใหม่ ปิดหน้าต่างเก่า" เหมือนเปลี่ยนหน้า
                            SwingUtilities.getWindowAncestor(this).dispose();
                        });
                        break;
                }
            }
            //เก็บปุ่มลงลิสต์ ใส่ลง panel และเว้นช่องว่างแนวตั้ง 10 พิกเซลหลังทุกปุ่ม
            sidebarButtons.add(btn);
            add(btn);
            add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }
    //เริ่มแอนิเมชัน หรือ คือตัวทำanimate
    public void toggle() {
        // สร้าง Timer ที่เรียก animate() ทุก 10 มิลลิวินาที
        timer = new Timer(10, e -> animate());
        // สลับสถานะเปิด จาก (true ↔ false)
        open = !open;
        // เริ่มจับเวลา
        if (timer.isRunning())
            timer.stop();
        timer.start();

    }
    // ตัวanimate
    private void animate() {
        // ขยับความกว้าง: open เป็น true ให้เพิ่ม ไม่งั้นให้ลด โดยขยับเพิ่มตาม STEP ที่ตั้งไว้ โดยเพิ่มลดทีละ 5 พิกเซล
        if (open) {
            currentWidth += STEP;
        } else {
            currentWidth -= STEP;
        }

        // 2. จำกัดไม่ให้หลุดช่วง จำกัดค่าให้อยู่ระหว่าง 0 ถึง 150 (min กันไม่ให้เกิน 150, max กันไม่ให้ต่ำกว่า 0) คำตอบของคอมเมนต์ "ต่ำสุดกับสูงสุดคืออะไร" คือ 0 และ 150 
        currentWidth = Math.max(SIDEBAR_COLLAPSED_WIDTH, Math.min(SIDEBAR_EXPANDED_WIDTH, currentWidth));

        // 3. ตั้งขนาดใหม่แล้วสั่งให้ layout จัดใหม่ ทำให้เห็นแถบค่อยๆ กว้างขึ้น/แคบลง
        setPreferredSize(new Dimension(currentWidth, getContentHeight()));
        revalidate();

        // 4. ถึงปลายทางแล้วหยุด Timer
        if (currentWidth == SIDEBAR_COLLAPSED_WIDTH || currentWidth == SIDEBAR_EXPANDED_WIDTH) {
            timer.stop();
        }

    }
    // คำนวณขนาดพิกเซลที่ต้องใช้ของ จำนวนปุ่ม และใช้เป็นความสูงของแถบ
    private int getContentHeight() {
        // ปุ่ม 3 อัน อันละ 35 + ช่องว่าง 10 ต่อปุ่ม + เผื่อขอบนิดหน่อย
        return sidebarLabels.length * (35 + 10) + 10;
    }

}
