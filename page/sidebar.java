package page;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

public class sidebar extends JPanel {
    private static final int STEP = 5;
    private static final int SIDEBAR_EXPANDED_WIDTH = 150;
    private static final int SIDEBAR_COLLAPSED_WIDTH = 0;

    private final String currentPage;

    private boolean open = false;
    private int currentWidth = 0;
    private Timer timer;
    boolean sidebarExpanded = false;

    private final java.util.List<JButton> sidebarButtons = new java.util.ArrayList<>();
    private final String[] sidebarLabels = { "HomePage", "HistoryPage", "Logout" };

    public sidebar(String currentPage) {
        this.currentPage = currentPage;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(204, 204, 204));
        setPreferredSize(new Dimension(SIDEBAR_COLLAPSED_WIDTH, getContentHeight()));
        currentWidth = SIDEBAR_COLLAPSED_WIDTH;
        JButton toggleButton = new JButton("☰");
        toggleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggleButton.setMaximumSize(new Dimension(40, 30));
        toggleButton.setFocusPainted(false);
        toggleButton.addActionListener(e -> toggle());

        for (String label : sidebarLabels) {
            JButton btn = new JButton(label);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);

            Dimension btnSize = new Dimension(120, 35);
            btn.setMaximumSize(btnSize);
            btn.setPreferredSize(btnSize);
            btn.setMargin(new Insets(0, 0, 0, 0));
            btn.setFocusPainted(false);

            // ผูก action ตาม label ของปุ่ม
            if (label.equals(currentPage)) {
                btn.setEnabled(false);
            } else {
                switch (label) {
                    case "HomePage":
                        btn.addActionListener(e -> {
                            new homepage();
                            SwingUtilities.getWindowAncestor(this).dispose();
                        });
                        break;

                    case "HistoryPage":
                        btn.addActionListener(e -> {
                            new Historypage();
                            SwingUtilities.getWindowAncestor(this).dispose();
                        });
                        break;

                    case "Logout":
                        btn.addActionListener(e -> {
                            new loginpage();
                            SwingUtilities.getWindowAncestor(this).dispose();
                        });
                        break;
                }
            }

            sidebarButtons.add(btn);
            add(btn);
            add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    public void toggle() {
        // TODO: สลับ open, stop timer เก่า, start ใหม่
        timer = new Timer(10, e -> animate());
        open = !open;
        if (timer.isRunning())
            timer.stop();
        timer.start();

    }

    private void animate() {
        // 1. ขยับความกว้าง: open เป็น true ให้เพิ่ม ไม่งั้นให้ลด
        if (open) {
            currentWidth += STEP;
        } else {
            currentWidth -= STEP;
        }

        // 2. จำกัดไม่ให้หลุดช่วง (ต่ำสุดกับสูงสุดคืออะไร?)
        currentWidth = Math.max(SIDEBAR_COLLAPSED_WIDTH, Math.min(SIDEBAR_EXPANDED_WIDTH, currentWidth));

        // 3. ตั้งขนาดใหม่ แล้วบอก Swing ให้จัดวางใหม่
        setPreferredSize(new Dimension(currentWidth, getContentHeight()));
        revalidate();

        // 4. ถึงปลายทางแล้วหยุด Timer
        if (currentWidth == SIDEBAR_COLLAPSED_WIDTH || currentWidth == SIDEBAR_EXPANDED_WIDTH) {
            timer.stop();
        }

    }

    private int getContentHeight() {
        // ปุ่ม 3 อัน อันละ 35 + ช่องว่าง 10 ต่อปุ่ม + เผื่อขอบนิดหน่อย
        return sidebarLabels.length * (35 + 10) + 10;
    }

}
