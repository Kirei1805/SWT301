package loipt.example;

import java.util.regex.Pattern;

public class AccountService {

    public boolean isValidEmail(String email) {
        if (email == null) return false;
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, email);
    }

    public boolean registerAccount(String username, String password, String email) {
        if (username == null || username.isEmpty()) return false;
        if (password == null || password.length() <= 6) return false;
        if (!isValidEmail(email)) return false;
        return true;
    }

    public boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) return false;
        String regex = "^[a-zA-Z0-9_]{3,20}$";
        return Pattern.matches(regex, username);
    }

    public boolean isStrongPassword(String password) {
        if (password == null) return false;
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.matches(regex, password);
    }

}



