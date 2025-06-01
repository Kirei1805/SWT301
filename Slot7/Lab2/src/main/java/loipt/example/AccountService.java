package loipt.example;
import java.util.regex.Pattern;
public class AccountService {
    /**
     * Validates whether the email address has the correct format.
     * A valid format must contain '@' and a domain with an extension of at least 2 characters.
     */
    public boolean isValidEmail(String email) {
        if (email == null) return false;
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, email);
    }
    /**
     * Registers an account by validating the username, password, and email.
     * All fields must meet format requirements.
     */
    public boolean registerAccount(String username, String password, String email) {
        if (username == null || username.isEmpty()) return false;
        if (password == null || password.length() <= 6) return false;
        if (!isValidEmail(email)) return false;
        return true;
    }
    /**
     * Validates whether the username is valid.
     * A valid username contains only letters, digits, or underscores and must be between 3 and 20 characters.
     */
    public boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) return false;
        String regex = "^[a-zA-Z0-9_]{3,20}$";
        return Pattern.matches(regex, username);
    }
    /**
     * Checks whether a password is strong.
     * A strong password must be at least 8 characters long and contain at least one lowercase letter,
     * one uppercase letter, one digit, and one special character.
     */
    public boolean isStrongPassword(String password) {
        if (password == null) return false;
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.matches(regex, password);}
}
