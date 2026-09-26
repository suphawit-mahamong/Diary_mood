package page;
 
import java.awt.*;
import javax.swing.*;
 
public class Historypage extends JFrame {
 
    // ================= COLORS =================
    private final Color background = new Color(246, 227, 229);
    private final Color purple = new Color(187, 82, 138);
    private final Color darkText = new Color(60, 55, 70);
 
    // พาเนลที่เก็บรายการ history ทั้งหมด (ไว้เพื่อลบ/กรองการ์ดออกได้)
    private JPanel listPanel;
 
    public Historypage() {
 
        setTitle("Diary mood");
        setSize(650, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(background);
 
        createUI();
    }
 
    private void createUI() {
 
        //Main Panel
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
 
        //วันที่ + หัวเรื่อง
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
 
 
  
    // MAIN (ทดสอบหน้านี้แยก)
  
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Historypage page = new Historypage();
            page.setVisible(true);
        });
    }
}