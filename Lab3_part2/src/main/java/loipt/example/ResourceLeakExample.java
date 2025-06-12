package loipt.example;

import java.io.*;
import java.util.logging.Logger;

public class ResourceLeakExample {

    // Tạo logger cho lớp này
    private static final Logger logger = Logger.getLogger(ResourceLeakExample.class.getName());

    public static void main(String[] args) {
        // Sử dụng try-with-resources để tự động đóng BufferedReader
        try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);  // Thay thế System.out.println bằng logger
            }
        } catch (IOException e) {
            logger.severe("Error reading file: " + e.getMessage());  // Ghi log lỗi khi đọc file
        }
    }
}
