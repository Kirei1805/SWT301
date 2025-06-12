package loipt.example;
import java.util.logging.Logger;

public class HardcodedCredentialsExample {

    // Tạo logger cho lớp này
    private static final Logger logger = Logger.getLogger(HardcodedCredentialsExample.class.getName());

    public static void main(String[] args) {
        String username = "admin";
        String password = "123456"; // hardcoded password

        // Sử dụng logger thay vì System.out.println
        if (authenticate(username, password)) {
            logger.info("Access granted");  // Thay thế System.out.println bằng logger
        } else {
            logger.warning("Access denied"); // Thay thế System.out.println bằng logger
        }
    }

    private static boolean authenticate(String user, String pass) {
        // Logic xác thực giả
        return user.equals("admin") && pass.equals("123456");
    }
}

