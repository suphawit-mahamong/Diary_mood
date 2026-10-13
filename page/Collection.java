package page;

public class Collection {

    //จำผู้ใช้ปัจจุบัน
    private static String currentUser;

    public static void login(String username) { 
        currentUser = username; 
    }
    public static void logout() { 
        currentUser = null; 
    }
    public static String getUser() { 
        return currentUser; 
    }
    public static boolean isLoggedIn() { 
        return currentUser != null; 
    }
}
    
