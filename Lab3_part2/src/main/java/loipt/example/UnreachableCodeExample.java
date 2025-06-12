package loipt.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnreachableCodeExample {

    // Tạo logger cho lớp này
    private static final Logger logger = LoggerFactory.getLogger(UnreachableCodeExample.class);

    public static int getNumber() {
        // Trả về giá trị, không có mã không thể truy cập sau return
        logger.info("Returning the number 42");
        return 42;
    }

    public static void main(String[] args) {
        // Thay thế System.out.println bằng logger
        logger.info("The number is: {}", getNumber());
    }
}
