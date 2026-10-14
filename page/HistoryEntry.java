package page;

import javax.swing.*;
import java.awt.*;

public class HistoryEntry {
    private final String date;
    private String title;

    public HistoryEntry(String date, String title) {
        this.date = date;
        this.title = title;
    }

    public String getDate() { return date; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
}
