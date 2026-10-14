import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import page.*;

public class TestApp {

    private static int passedCount = 0;
    private static int failedCount = 0;

    private static void check(String testName, boolean condition) {
        if (condition) {
            System.out.println("PASSED: " + testName);
            passedCount++;
        } else {
            System.out.println("FAILED: " + testName);
            failedCount++;
        }
    }

    
public static void main(String[] args) { 
    boolean assertsOn = false;
    assert assertsOn = true;
    if (!assertsOn) {
        System.out.println("WARNING: assertions disabled" + " - re-run with: java -ea Week06LabTest\n");
    }
     Path file = Paths.get("data/password.csv");
        String oldData = null;
 
        try {
            if (Files.exists(file)) {
                oldData = Files.readString(file);
            }
            Files.createDirectories(file.getParent());
            Files.writeString(file, "username,password\nalice,Passw0rd1\nbob,Secret123\n");
 
            // --- Testing User Constructor ---
            System.out.println("\n--- Testing User Constructor ---");
 
            User u = new User("alice", "Passw0rd1");
            check("Create valid user", u.getUsername().equals("alice"));
 
            boolean caughtNullName = false;
            try {
                new User(null, "Passw0rd1");
            } catch (IllegalArgumentException e) {
                caughtNullName = true;
            }
            check("null username throws IllegalArgumentException", caughtNullName);
 
            boolean caughtBlankPass = false;
            try {
                new User("alice", "   ");
            } catch (IllegalArgumentException e) {
                caughtBlankPass = true;
            }
            check("blank password throws IllegalArgumentException", caughtBlankPass);
 
            
            if (assertsOn) {
                boolean caughtWeak = false;
                try {
                    new User("alice", "abc");
                } catch (AssertionError e) {
                    caughtWeak = true;
                }
                check("weak password violates Rep Invariant", caughtWeak);
            }
 
            System.out.println("\n--- Testing contains ---");
            check("Find existing user", User.contains("alice"));
            check("Find user ignoring case", User.contains("ALICE"));
            check("Unknown user not found", !User.contains("nobody"));
 
            System.out.println("\n--- Testing login ---");
            Collection.logout();
 
            check("Empty username shows message",
                    "Please enter your username.".equals(User.login("", "Passw0rd1")));
            check("Empty password shows message",
                    "Please enter your password.".equals(User.login("alice", "")));
            check("Unknown user shows message",
                    "Username not found. Please sign up first.".equals(User.login("nobody", "Passw0rd1")));
 
            check("Correct login returns null", User.login("alice", "Passw0rd1") == null);
            check("Collection remembers logged-in user", "alice".equals(Collection.getUser()));
 
            Collection.logout();
            check("Wrong password is rejected", User.login("alice", "PassWord") != null);
            check("Wrong password does not log in", !Collection.isLoggedIn());
 
            // --- Testing signin ---
            System.out.println("\n--- Testing signin ---");
 
            check("Password mismatch is rejected",
                    "Password and confirm password do not match".equals(
                            User.signin("carol", "Abcdef12", "Abcdef13")));
 
            String weak = User.signin("carol", "abc", "abc");
            check("Weak password is rejected", weak != null && weak.startsWith("Password must be"));
 
            check("Duplicate username is rejected",
                    "This username already exists.".equals(User.signin("alice", "Abcdef12", "Abcdef12")));
 
            check("Valid signin returns null", User.signin("carol", "Abcdef12", "Abcdef12") == null);
            check("New user is saved", User.contains("carol"));
 
            Collection.logout();
            check("New user can login", User.login("carol", "Abcdef12") == null);
 
        } catch (IOException e) {
            failedCount++;
            System.out.println("FAILED: file error: " + e.getMessage());
        } finally {
            // --- คืนไฟล์เดิม ---
            try {
                if (oldData != null) {
                    Files.writeString(file, oldData);
                } else {
                    Files.deleteIfExists(file);
                }
            } catch (IOException e) {
                System.out.println("WARNING: could not restore " + file + ": " + e.getMessage());
            }
        }
    
    // --- สรุปผล ---
        System.out.println("\n--------------------");
        System.out.println("--- Test Summary ---");
        System.out.println("Passed: " + passedCount + ", Failed: " + failedCount);
        if (failedCount == 0) {
            System.out.println("Excellent! All tests passed!");
        } else {
            System.out.println("Some tests failed.");
        }
    }
}
