package page;

import java.time.LocalDate;

public class DiaryEntry {

    private final String id;
    private LocalDate date;
    private String title;
    private String content;
    private String imagePath;
    private boolean pinned;

    public DiaryEntry(String id, LocalDate date, String title, String content, String imagePath) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
    }

    // getter ทุกตัว
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        try {
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public String getImagePath() {
        return imagePath;
    }

    public LocalDate getDate() {
        return date;
    }

    // setter (ไม่มีของ id)
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public boolean isPinned() {
        return pinned;
    }
    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public static void main(String[] args) {

        DiaryEntry entry = new DiaryEntry("test1", LocalDate.now(), "Hello", "dadw", "");
        System.out.println("id = "+entry.getId());
        System.out.println("content = "+entry.getContent());
        System.out.println("Date = "+entry.getDate());
        System.out.println("title = "+entry.getTitle());
        System.out.println("imagepath = "+entry.getImagePath());
    }
}
