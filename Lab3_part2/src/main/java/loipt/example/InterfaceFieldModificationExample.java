package loipt.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ApplicationConstants {
    // Private constructor để ngăn instantiation
    private ApplicationConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }

    public static final int MAX_USERS = 100;
    // Có thể thêm các constants khác
    public static final int MAX_ADMIN_USERS = 10;
    public static final String DEFAULT_USER_ROLE = "USER";
}

public class InterfaceFieldModificationExample {

    private static final Logger logger = LoggerFactory.getLogger(InterfaceFieldModificationExample.class);

    public static void main(String[] args) {
        logger.info("Max users: {}", ApplicationConstants.MAX_USERS);
        // ApplicationConstants.MAX_USERS = 200; // Vẫn compile-time error
    }
}
