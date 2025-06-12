package loipt.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OvercatchingExceptionExample {

    // Tạo logger cho lớp này
    private static final Logger logger = LoggerFactory.getLogger(OvercatchingExceptionExample.class);

    public static void main(String[] args) {
        try {
            int[] arr = new int[5];
            // Gây lỗi truy cập ngoài phạm vi
            logger.info("Attempting to access array element at index 10");

            // Ghi log lỗi thay vì sử dụng System.out.println
            logger.info("Array element at index 10: {}", arr[10]); // Lỗi xảy ra ở đây
        } catch (ArrayIndexOutOfBoundsException e) {
            // Thay thế System.out.println bằng logger khi xảy ra lỗi
            logger.error("Error occurred: Array index out of bounds. Message: {}", e.getMessage());
        } catch (RuntimeException e) {
            // Catch bất kỳ RuntimeException nào khác
            logger.error("Runtime error occurred: {}", e.getMessage());
        }
    }
}
