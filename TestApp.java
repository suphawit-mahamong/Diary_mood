import page.*;

public class TestApp {

   /*  private static int passedCount = 0;
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
    static final int MIN_LEN = 8;
    static final int MAX_LEN = 20;

    static boolean validate(String pw) {
        // TODO: implement ตาม spec ใน README.md (R1-R6)
        if(pw==null) throw new IllegalArgumentException();

        if(pw.length()<MIN_LEN)return false;
        if(pw.length()>MAX_LEN)return false;

        boolean hasLower =false;
        boolean hasUpper =false;
        boolean hasDigit =false;
        for(int i =0; i<pw.length(); i++){
            if(Character.isLowerCase(pw.charAt(i))) hasLower =true;
            if(Character.isUpperCase(pw.charAt(i))) hasUpper =true;
            if(Character.isDigit(pw.charAt(i))) hasDigit =true;
        }
      


        
        return hasLower && hasUpper && hasDigit;
    
    // --- สรุปผล ---
        System.out.println("\n--------------------");
        System.out.println("--- Test Summary ---");
        System.out.println("Passed: " + passedCount + ", Failed: " + failedCount);
        if (failedCount == 0) {
            System.out.println("Excellent! All tests passed!");
        } else {
            System.out.println("Some tests failed.");
        }
    }*/
}
