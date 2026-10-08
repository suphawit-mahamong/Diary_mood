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
    private static String file= "password.csv";

    private void checkRep() {
        assert username!=null&&username !="";
        assert password!=null&&password !="";
        assert password.length()>=MIN_LEN;
        assert password.length()<=MAX_LEN;
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        for(int i =0; i<password.length(); i++){
            if(Character.isLowerCase(password.charAt(i))) hasLower = true; ;
            if(Character.isUpperCase(password.charAt(i)))  hasUpper = true;
            if(Character.isDigit(password.charAt(i))) hasDigit = true;
        }
        assert hasLower;
        assert hasUpper;
        assert hasDigit;
    }
    
    public User(String Username,String Password) {
        if(Username==null||Username.trim().isEmpty()) throw new IllegalArgumentException();
        if(Password==null||Password.trim().isEmpty()) throw new IllegalArgumentException();
        this.username = Username;
        this.password =Password;
        checkRep();
    }

    public String getUsername() {
        return username;
    }

    // อ่านผู้ใช้งาน
    private static List<User> readUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("./data/password.csv"))){
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.equals(user))
                    continue;

                int commaPos = line.indexOf(',');
                if (commaPos <= 0 || commaPos >= line.length() - 1)
                    continue;

                String name = line.substring(0, commaPos);
                String pass = line.substring(commaPos + 1);

                try {
                    users.add(new User(name,pass));
                } catch (IllegalArgumentException e) {
                    System.out.println(e);
                }
    }
        }catch (IOException e) {  
            System.out.println(e);
        }
        return users;   

    }

    //ตรวจสอบชื่อซ้ำ
     public static boolean contains(String Username) {
        if (Username == null) return false;
        for (User u : readUsers()) {
            if (u.getUsername().equalsIgnoreCase(Username))
                return true;
        }
        return false;
    }

    //เข้าสู่ระบบ
    public static String login(String username, String password) {
    if (username == null || username.trim().isEmpty())
        return "Please enter your username.";
    if (password == null || password.trim().isEmpty())
        return "Please enter your password.";

    username = username.trim();
    password = password.trim();

    for (User u : readUsers()) {
        if (u.getUsername().equalsIgnoreCase(username)) {
            if (u.password.equals(password))
                return null; // สำเร็จ
            return "Incorrect password.";
        }
    }
    return "Username not found. Please sign up first.";
}

    public static String signin(String username, String password, String confirmPassword) {
        // 1. ตรวจสอบว่ากรอกครบไหม
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
        // 2. ตรวจสอบรหัสตรงกันไหม
        if (!password.equals(confirmPassword))
            return "Password and confirm password do not match";

        // 3. ตรวจสอบชื่อซ้ำ
        if (contains(username)) 
            return"This username already exists.";

        // 4. ตรวจสอบรูปแบบรหัสผ่าน + บันทึก
            if (username.contains(",") || password.contains(","))throw new IllegalArgumentException("Commas (,) are not allowed");
            User newUser = new User(username, password);
            newUser.save();
            return null; // สำเร็จ คืนค่า null
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    //บันทึก
    public void save() {
        if (contains(username)) {
            throw new IllegalStateException("Username already exists: " + username);
        }

        File f = new File(file);
        boolean needHeader = !f.exists() || f.length() == 0;

        try (BufferedWriter bw = new BufferedWriter((new FileWriter("./data/password.csv",true)))) {
            if (needHeader) {
                bw.write(user + "\n");
            } else {
                // ขึ้นบรรทัดใหม่
                try (RandomAccessFile raf = new RandomAccessFile(f, "r")) {
                    long len = raf.length();
                    if (len > 0) {
                        raf.seek(len - 1);
                        if (raf.read() != '\n')
                            bw.write("\n");
                    }
                }
            }
            bw.write(username + "," + password + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

