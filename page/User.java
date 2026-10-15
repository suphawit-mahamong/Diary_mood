package page;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class User {

    private final String username;
    private final String password;
    static final int MIN_LEN = 8;
    static final int MAX_LEN = 20;

    private static final String user = "username,password";
    private static String file = "./data/password.csv";

    // Rep Invariant (RI):
    // - username != null และไม่ว่างเปล่า
    // - password != null และไม่ว่างเปล่า
    // - MIN_LEN <= password.length() <= MAX_LEN
    // - password มีตัวพิมพ์เล็กอย่างน้อย 1 ตัว
    // - password มีตัวพิมพ์ใหญ่อย่างน้อย 1 ตัว
    // - password มีตัวเลขอย่างน้อย 1 ตัว
    //
    // Abstraction Function (AF):
    // AF(username, password) = บัญชีผู้ใช้ 1 บัญชีของแอป Diary mood
    // ที่มีชื่อเข้าสู่ระบบคือ usernameและ รหัสผ่านยืนยันตัวตนคือ password
    //

    /**
     * ตรวจสอบว่า Rep Invariant เป็นจริงหรือไม่
     */

    private void checkRep() {
        assert username != null && username != "";
        assert password != null && password != "";
        assert password.length() >= MIN_LEN;
        assert password.length() <= MAX_LEN;
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i)))
                hasLower = true;
            ;
            if (Character.isUpperCase(password.charAt(i)))
                hasUpper = true;
            if (Character.isDigit(password.charAt(i)))
                hasDigit = true;
        }
        assert hasLower;
        assert hasUpper;
        assert hasDigit;
    }

    // ตรวจ null/ว่างก่อน ถ้าไม่ผ่านโยน IllegalArgumentException แล้วเก็บค่า
    // แล้วเรียก checkRep()
    public User(String Username, String Password) {
        if (Username == null || Username.trim().isEmpty())
            throw new IllegalArgumentException();
        if (Password == null || Password.trim().isEmpty())
            throw new IllegalArgumentException();
        this.username = Username;
        this.password = Password;
        checkRep();
    }

    public String getUsername() {
        return username;
    }

    // อ่านผู้ใช้งาน
    private static List<User> readUsers() {
        List<User> users = new ArrayList<>();
        // อ่านบัญชีทั้งหมดจากไฟล์
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // ข้ามบรรทัดว่างและบรรทัดหัวตาราง
                line = line.trim();
                if (line.isEmpty() || line.equals(user))
                    continue;
                // หาตำแหน่งจุลภาคตัวแรก ถ้าไม่มี หรืออยู่ต้นสุด/ท้ายสุด (ไม่มีชื่อหรือรหัส)
                // ก็ข้าม
                int commaPos = line.indexOf(',');
                if (commaPos <= 0 || commaPos >= line.length() - 1)
                    continue;
                // แยกชื่อก่อนคอมม่าและหลังคอมม่า
                String name = line.substring(0, commaPos);
                String pass = line.substring(commaPos + 1);

                // สร้าง userใส่ในลิสต์ถ้าข้อมูลไม่ถูกเงื่อนไขจะโยน exception ซึ่งถูกจับ
                // แล้วพิมพ์ออกคอนโซลและข้ามบรรทัดนั้นไป
                try {
                    users.add(new User(name, pass));
                } catch (IllegalArgumentException e) {
                    System.out.println(e);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return users;

    }

    // ตรวจสอบชื่อซ้ำ
    public static boolean contains(String Username) {
        if (Username == null)
            return false;
        // ตรวจชื่อซ้ำอ่านทุกบัญชีแล้วเทียบชื่อแบบ ไม่สนตัวพิมพ์เล็ก-ใหญ่ (Tom กับ tom
        // ถือว่าซ้ำ)
        for (User u : readUsers()) {
            if (u.getUsername().equalsIgnoreCase(Username))
                return true;
        }
        return false;
    }

    // เข้าสู่ระบบ
    public static String login(String username, String password) {
        // ช่องชื่อหรือรหัสว่าง → แจ้งให้กรอก
        if (username == null || username.trim().isEmpty())
            return "Please enter your username.";
        if (password == null || password.trim().isEmpty())
            return "Please enter your password.";
        // ตัดช่องว่าง วนหาผู้ใช้ชื่อตรงกัน
        username = username.trim();
        password = password.trim();
        
        for (User u : readUsers()) {
            // ถ้าเจอชื่อแต่รหัสไม่ตรงให้แจ้งว่า Incorrect password.
            if (u.getUsername().equalsIgnoreCase(username)) {
                //ถ้าเจอชื่อและรหัสที่ตรงกัน ให้จำชื่อผู้ใช้ปัจจุบัน และคืน null
                if (u.password.equals(password)) {
                    Collection.login(u.getUsername());
                    return null; // สำเร็จ
                }
                return "Incorrect password.";
            }
        }
        // ไม่เจอเลยให้แจ้งว่า Username not found. Please sign up first.
        return "Username not found. Please sign up first.";
    }

    public static String signin(String username, String password, String confirmPassword) {
        // ตรวจสอบว่ากรอกครบไหม
        if (username == null || username.trim().isEmpty())
            return "Please enter your username.";
        if (password == null || password.trim().isEmpty())
            return "Please enter your password.";
        if (confirmPassword == null || confirmPassword.trim().isEmpty())
            return "Please confirm your password.";
        
        username = username.trim();
        password = password.trim();
        confirmPassword = confirmPassword.trim();
        try {
            // นับว่ามีตัวพิมพ์เล็ก/ใหญ่/ตัวเลขหรือไม่ และความยาว 8-20 ถ้าไม่ผ่านโยน IllegalArgumentException พร้อมข้อความ ซึ่งถูก catch ด้านล่างแล้วคืนข้อความนั้นเป็น error
            boolean hasLower = false, hasUpper = false, hasDigit = false;
            for (int i = 0; i < password.length(); i++) {
                char ch = password.charAt(i);
                if (Character.isLowerCase(ch))
                    hasLower = true;
                if (Character.isUpperCase(ch))
                    hasUpper = true;
                if (Character.isDigit(ch))
                    hasDigit = true;
            }
            if (password.length() < MIN_LEN || password.length() > MAX_LEN || !hasLower || !hasUpper || !hasDigit)
                throw new IllegalArgumentException("Password must be " + MIN_LEN + "-" + MAX_LEN
                        + " characters with a lowercase letter, an uppercase letter, and a number.");

            // ตรวจสอบรหัสตรงกันไหม
            if (!password.equals(confirmPassword))
                return "Password and confirm password do not match";

            // ตรวจสอบชื่อซ้ำ
            if (contains(username))
                return "This username already exists.";
            // ผ่านทั้งหมด สร้าง user แล้ว save และคืน null เมื่อสำเร็จ
            User newUser = new User(username, password);
            newUser.save();
            return null;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    // บันทึก
    public void save() {
        if (contains(username)) {
            throw new IllegalStateException("Username already exists: " + username);
        }

        File f = new File(file);
        boolean needHeader = !f.exists() || f.length() == 0;
        //เขียนไฟล์ ถ้าไฟล์ยังไม่มีหรือว่าง เขียนหัวตารางก่อน แล้วเขียน username,password หนึ่งบรรทัด โดยมีการเขียนต่อท้ายไฟล์ไม่เขียนทับ
        try (BufferedWriter bw = new BufferedWriter((new FileWriter(file, true)))) {
            if (needHeader) {
                bw.write(user + "\n");
            }
            bw.write(username + "," + password + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
