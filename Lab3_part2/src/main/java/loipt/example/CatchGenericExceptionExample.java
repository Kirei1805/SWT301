package loipt.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;
public class CatchGenericExceptionExample {
    private static final Logger logger = LoggerFactory.getLogger(CatchGenericExceptionExample.class);
    // Khởi tạo Random một lần để tái sử dụng
    private static final Random random = new Random();

    public static void main(String[] args) {
        try {
            // Giả lập việc lấy dữ liệu từ người dùng hoặc cơ sở dữ liệu
            String s = getUserInput();  // Hàm giả lập trả về chuỗi hoặc null

            if (s != null) {
                logger.info("String length: {}", s.length());
            } else {
                logger.warn("String is null, cannot get length");
            }
        } catch (NullPointerException e) {
            logger.error("NullPointerException occurred: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error occurred: {}", e.getMessage(), e);
        }
    }
    private static String getUserInput() {
        String[] possibleInputs = {"Hello World", "Java Spring", "Thymeleaf Template", null};
        return possibleInputs[random.nextInt(possibleInputs.length)];
    }
}
