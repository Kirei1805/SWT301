package loipt.example;

import java.io.*;
import java.util.logging.Logger;

public class PathTraversalExample {

    // Tạo logger cho lớp này
    private static final Logger logger = Logger.getLogger(PathTraversalExample.class.getName());

    public static void main(String[] args) throws IOException {
        String userInput = "../secret.txt";
        File file = new File(userInput);

        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            // Thay thế System.out.println bằng logger
            logger.info("Reading file: " + file.getPath());  // Sử dụng logger thay vì System.out.println
            reader.close();
        }
    }
}
