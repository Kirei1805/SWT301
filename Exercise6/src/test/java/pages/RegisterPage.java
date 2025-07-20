package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class RegisterPage extends BasePage {
    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    private final By emailField = By.id("email");
    private final By fullNameField = By.id("fullName");
    private final By passwordField = By.id("password");
    private final By confirmPasswordField = By.id("confirmPassword");
    private final By phoneField = By.id("phone");
    private final By registerButton = By.xpath("//button[@type='submit']");

    public void enterEmail(String email) {
        type(emailField, email);
    }

    public void enterFullName(String fullName) {
        type(fullNameField, fullName);
    }

    public void enterPassword(String password) {
        type(passwordField, password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        type(confirmPasswordField, confirmPassword);
    }

    public void enterPhone(String phone) {
        type(phoneField, phone);
    }

    public void agreeToTerms() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('agreeTerms').checked = true;");
    }

    public void clickRegister() {
        click(registerButton);
    }
}
