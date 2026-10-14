package page;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class DiaryStorage {

    private final File folder;

    public DiaryStorage(File folder) {
        this.folder = folder;
        folder.mkdirs();
    }

    public String createNewId() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss"));
    }

    public void save(DiaryEntry entry) throws IOException {
        File file = new File(folder, entry.getId() + ".txt");
        try (BufferedWriter bw = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            bw.write("date=" + entry.getDate());
            bw.newLine();
            bw.write("title=" + entry.getTitle());
            bw.newLine();
            String img = (entry.getImagePath() == null) ? "" : entry.getImagePath();
            bw.write("image=" + img);
            bw.newLine();
            bw.write("content=");
            bw.newLine();
            bw.write(entry.getContent());
        }
    }

    public List<DiaryEntry> loadAll() {
        List<DiaryEntry> result = new ArrayList<>();
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) {
            return result;
        }
        for (File f : files) {
            try {
                List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
                String dateText = "";
                String title = "";
                String image = "";
                StringBuilder content = new StringBuilder();
                boolean inContent = false;

                for (String line : lines) {
                    if (inContent) {
                        if (content.length() > 0) {
                            content.append("\n");
                        }
                        content.append(line);
                        
                    } else if (line.startsWith("date=")) {
                        dateText = line.substring(5);
                    } else if (line.startsWith("title=")) {
                        title = line.substring(6);
                    } else if (line.startsWith("image=")) {
                        image = line.substring(6);
                    } else if (line.equals("content=")) {
                        inContent = true;
                    }
                }
                LocalDate date =LocalDate.parse(dateText);
                String id = f.getName().replace(".txt", "");
                String img = image.isEmpty() ? null : image;
                result.add(new DiaryEntry(id, date, title, content.toString(), img));
            } catch (IOException | DateTimeParseException e) {
                // ข้ามไฟล์นี้ไป
            }

        }
        result.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        return result;
    }

    public boolean delete(DiaryEntry entry){
        File file = new File(folder, entry.getId() + ".txt");
        return file.delete();
    }
}
