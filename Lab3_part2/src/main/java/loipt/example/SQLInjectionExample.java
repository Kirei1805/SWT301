package loipt.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLInjectionExample {

    private static final Logger logger = LoggerFactory.getLogger(SQLInjectionExample.class);

    public static void main(String[] args) {
        String userInput = "' OR '1'='1";
        String query = "SELECT * FROM users WHERE username = '" + userInput + "'";
        logger.info("Executing query: {}", query);
    }
}
