package page;

import java.awt.*;
import javax.swing.*;

public class homepage extends JFrame {

    public homepage() {

        setTitle("Diary mood");
        setSize(650, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon img = new ImageIcon("C:\\Users\\sukit\\Downloads\\lib-nobg.png");
        setIconImage(img.getImage());

        createUI();
    }

    private void createUI() {

        // COLORS 

        Color background = new Color(246,227,229);
        Color purple = new Color(187,82,138);
        Color darkText = new Color(60, 55, 70);


        // MAIN PANEL

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(background);

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 25, 15, 25
                )
        );


        // TOP

        JPanel topPanel = new JPanel(
                new BorderLayout()
        );

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


        topPanel.add(
                greetingPanel,
                BorderLayout.WEST
        );


        mainPanel.add(
                topPanel,
                BorderLayout.NORTH
        );


        // CENTER

        JPanel contentPanel = new JPanel();

        contentPanel.setLayout(
                new BoxLayout(
                        contentPanel,
                        BoxLayout.Y_AXIS
                )
        );

        contentPanel.setBackground(background);


        contentPanel.add(
                Box.createVerticalStrut(25)
        );


       
        // NEW DIARY BUTTON


        JButton newDiaryButton =
                new JButton(" + NEW DIARY");

        newDiaryButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        newDiaryButton.setBackground(purple);
        newDiaryButton.setForeground(Color.WHITE);

        newDiaryButton.setFocusPainted(false);

        newDiaryButton.setMaximumSize(
                new Dimension(420, 55)
        );

        newDiaryButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        contentPanel.add(
                newDiaryButton
        );


        contentPanel.add(
                Box.createVerticalStrut(25)
        );


        // PINNED DIARY
     
        JLabel pinnedTitle =
                new JLabel("PINNED DIARY");

        pinnedTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        pinnedTitle.setForeground(darkText);

        pinnedTitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        contentPanel.add(
                pinnedTitle
        );


        contentPanel.add(
                Box.createVerticalStrut(10)
        );


        // pinned Diary 1

        JPanel pinnedDiary1 =
                createPinnedNote(
                        "My first day at university",
                        "15/09/2026"
                );

        contentPanel.add(pinnedDiary1);


        contentPanel.add(
                Box.createVerticalStrut(10)
        );


        // pinned Diary 2

        JPanel pinnedDiary2 =
                createPinnedNote(
                        "My favorite memory",
                        "10/09/2026"
                );

        contentPanel.add(pinnedDiary2);


        contentPanel.add(
                Box.createVerticalStrut(25)
        );



        // list to do

        JLabel ListToDoTitle =
                new JLabel("TODAY");

        ListToDoTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        ListToDoTitle.setForeground(darkText);

        ListToDoTitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        contentPanel.add(ListToDoTitle);


        contentPanel.add(Box.createVerticalStrut(10));


        JPanel todoPanel =
                new JPanel();

        todoPanel.setLayout(
                new BoxLayout(
                        todoPanel,
                        BoxLayout.Y_AXIS
                )
        );

        todoPanel.setBackground(Color.WHITE);

        todoPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 15, 12, 15
                )
        );

        todoPanel.setMaximumSize(
                new Dimension(420, 145)
        );

        todoPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        JCheckBox task1 =
                new JCheckBox(
                        "Writing diary"
                );

        JCheckBox task2 =
                new JCheckBox(
                        "Read a book"
                );

        JCheckBox task3 =
                new JCheckBox(
                        "Do homework"
                );


        task1.setBackground(Color.WHITE);
        task2.setBackground(Color.WHITE);
        task3.setBackground(Color.WHITE);


        task1.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        task2.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        task3.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );


        todoPanel.add(task1);
        todoPanel.add(task2);
        todoPanel.add(task3);


        contentPanel.add(
                todoPanel
        );


        mainPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );



        // แถบไปหน้าอื่น

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                80,
                                5
                        )
                );

        bottomPanel.setBackground(background);


     
        JButton diaryButton =
                new JButton("Diary");


        diaryButton.setBorderPainted(false);

        diaryButton.setContentAreaFilled(false);

        diaryButton.setFocusPainted(false);

        bottomPanel.add(diaryButton);


        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );


        // ADD MAIN PANEL

        add(mainPanel);
    }


  
    // Pinned Note รับ หัวข้อโน้ตกับวันที่
  

    private JPanel createPinnedNote(
            String title,
            String date
    ) {

        JPanel note =
                new JPanel(
                        new BorderLayout()
                );

        note.setBackground(Color.WHITE);

        note.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 15, 12, 15
                )
        );

        note.setMaximumSize(
                new Dimension(420, 65)
        );

        note.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


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




        note.add(
                textPanel,
                BorderLayout.CENTER
        );


        return note;
    }


  
    // MAIN
    

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            homepage home =
                    new homepage();

            home.setVisible(true);

        });
    }
}