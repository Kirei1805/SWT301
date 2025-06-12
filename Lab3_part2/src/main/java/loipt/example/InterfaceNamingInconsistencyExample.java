package loipt.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

interface LoginHandler {
    void login(String username, String password);
}

class LoginHandlerImpl implements LoginHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginHandlerImpl.class); // Tạo logger cho lớp này

    @Override
    public void login(String username, String password) {
        // Thay thế System.out.println bằng logger
        logger.info("Logging in with username: {} and password: {}", username, password);
    }
}
