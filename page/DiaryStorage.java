package page;
//ใช้ทำเกี่ยวกับไฟล์ พวกแบบอ่านเขียนไฟล์
import java.io.*;
//ใช้ StandardCharsets.UTF_8 ระบุการเข้ารหัสตัวอักษรตอนอ่าน/เขียน เพื่อให้ ภาษาไทยไม่เพี้ยน ไม่ว่าจะรันบนเครื่องที่ตั้งค่า encoding ต่างกัน
import java.nio.charset.StandardCharsets;
//คลาสแบบ NIO ที่ใหม่กว่า ใช้ Files.newBufferedWriter(...) ตอนบันทึก และ Files.readAllLines(...) ตอนอ่านไฟล์ทั้งหมดเป็นลิสต์บรรทัด
import java.nio.file.*;
//เก็บวันที่ของ diary (LocalDate.parse(dateText) ตอนโหลด)
import java.time.LocalDate;
//ใช้ใน createNewId() เอาวันเวลาปัจจุบันมาจัดรูปแบบ yyyy-MM-dd_HHmmss เป็นชื่อไฟล์ของ diary ใหม่
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//จับ error ตอนแปลงข้อความเป็นวันที่ไม่ได้ ใน loadAll() ถ้าไฟล์ไหนวันที่เสีย จะข้ามไฟล์นั้นไป
import java.time.format.DateTimeParseException;
// list , arraylist
import java.util.*;

public class DiaryStorage {
    // File แทนที่อยู่ (path) ของโฟลเดอร์ 
    private final File folder;
    //จำว่าโฟลเดอร์เก็บ diary อยู่ที่ไหน และทำให้แน่ใจว่าโฟลเดอร์นั้นมีอยู่จริง
    public DiaryStorage(File folder) {
        this.folder = folder;
        //สร้างโฟลเดอร์ (รวมโฟลเดอร์แม่ที่ยังไม่มี) ถ้ามีอยู่แล้วก็ไม่ทำอะไร จึงมั่นใจได้ว่าโฟลเดอร์พร้อมใช้เสมอ
        folder.mkdirs();
    }
    // สร้าง โฟลเดอร์ขึ้นมาตามชื่อผู้ใช้งาน
    public static DiaryStorage CurrentUser() {
        String user;
        if (Collection.isLoggedIn()) {
            user = Collection.getUser();
        } else {
            user = "guest";
        }
        return new DiaryStorage(new File("diaries", user));
    }
    // สร้างรหัส diary ใหม่
    public String createNewId() {
        // เอาวันเวลาปัจจุบันมาจัดรูปแบบ เช่น 2026-10-15_143005 ใช้เป็นทั้ง id และชื่อไฟล์ (2026-10-15_143005.txt)
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss"));
    }
    // เซฟข้อมูล Diary
    public void save(DiaryEntry entry) throws IOException {
        // บึกทึกข้อมูลต่างๆเช่น วันที่, หัวข้อ, path ของรูปภาพ และเนื้อหาลงในไฟล์ .txt
        File file = new File(folder, entry.getId() + ".txt");
        try (BufferedWriter bw = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            bw.write("date=" + entry.getDate());
            bw.newLine();
            bw.write("title=" + entry.getTitle());
            bw.newLine();
            // เงื่อนไข ว่าถ้า ไม่มีรูป(imagePath เป็น null) ให้ img เป็นค่าว่าง ถ้ามีให้ใส่ path ข้อรูปนั้น
            String img = (entry.getImagePath() == null) ? "" : entry.getImagePath();
            bw.write("image=" + img);
            bw.newLine();
            bw.write("pinned=" + entry.isPinned());
            bw.newLine();
            bw.write("content=");
            bw.newLine();
            bw.write(entry.getContent());
        }
    }
    // โหลด Diary ทั้งหมด
    public List<DiaryEntry> loadAll() {
        List<DiaryEntry> result = new ArrayList<>();
        // หาไฟล์ โดย ดึงเฉพาะไฟล์ที่ลงท้าย .txt ถ้าโฟลเดอร์อ่านไม่ได้ listFiles คืน null จึงคืนลิสต์ว่างไปเลย
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) {
            return result;
        }
        for (File f : files) {
            try {
                //อ่านทุกบรรทัดของไฟล์เป็นลิสต์
                List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
                String dateText = "";
                String title = "";
                String image = "";
                boolean pinned = false;
                StringBuilder content = new StringBuilder();
                boolean inContent = false;
                // วนลูปเพื่อเช็คทีละตัว
                for (String line : lines) {
                    // ถ้า inContent เป็นจริง ส่วนที่เหลือคือเนื้อหา
                    if (inContent) {
                        //ต่อเข้า StringBuilder คั่นด้วย \n
                        if (content.length() > 0) {
                            content.append("\n");
                        }
                        content.append(line);
                        //ไม่งั้นดูว่าขึ้นต้นด้วยอะไร (date=, title=, ...) แล้วตัดส่วนหน้าออก ด้วย substring
                    } else if (line.startsWith("date=")) {
                        dateText = line.substring(5);
                    } else if (line.startsWith("title=")) {
                        title = line.substring(6);
                    } else if (line.startsWith("image=")) {
                        image = line.substring(6);
                    } else if (line.startsWith("pinned=")) {
                        pinned = line.substring(7).equals("true");
                        // เจอบรรทัด content= เมื่อไหร่ ตั้ง inContent = true
                    } else if (line.equals("content=")) {
                        inContent = true;
                    }
                }
                //แปลงข้อมูลเป็นข้อความจากการที่ตัดมาจาก date=2026-10-15 (ได้ "2026-10-15") โดยแปลงเป็น LocalDate รับเฉพาะรูปแบบ yyyy-MM-dd ถ้าไฟ์เสียหรือรูปผิดจะโยน Excaption
                LocalDate date =LocalDate.parse(dateText);
                // id ได้จากชื่อไฟล์ตัด .txt
                String id = f.getName().replace(".txt", "");
                //รูปที่ว่างแปลงกลับเป็น null พอโหลดกลับมาจึงแปลงสตริงว่างให้เป็น null เพื่อให้สอดคล้องกับที่โปรแกรมใช้ null แทน "ไม่มีรูป"
                String img = image.isEmpty() ? null : image;
                // สร้าง object โดยใช้ค่าที่ แปลงข้อมูล ไว้ในส่วน เนื้อหา เป็น StringBuilder จึงต้อง .toString() ก่อน
                DiaryEntry entry = new DiaryEntry(id, date, title, content.toString(), img);
                // DiaryEntry ไม่รับการปักหมุดไว้เลยต้องตั้งค่าแยกด้วย setter
                entry.setPinned(pinned);
                // เพิ่มเข้า list ที่จะ return
                result.add(entry);
                // ถ้าไฟล์ไหนอ่านไม่ได้้หรือไรจะโยน Exception และข้ามไฟล์ไปเลย
            } catch (IOException | DateTimeParseException e) {
                System.out.println(f.getName() + " " + e);
            }

        }
        // เรียงวันที่จาก ใหม่ไปเก่า
        result.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        return result;
        
    }

    //ลบไฟล์ diary 
    public boolean delete(DiaryEntry entry){
        //ค้นหาไฟล์จากidแล้วลบ ถ้าคืนเป็น true คือลบสำเร็จ แต่ถ้าคืนเป็น false คือลบไม่เสร็จ
        File file = new File(folder, entry.getId() + ".txt");
        return file.delete();
    }
}
