package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    public static final String LOGIN_URL = "http://localhost:8080/Login";

    private final By emailField = By.id("email");
    private final By passwordField = By.id("password");
    private final By loginButton = By.cssSelector("button[type='submit'], input[type='submit'], .login-btn, #login-btn");
    private final By rememberMeCheckbox = By.id("rememberMe");
    private final By forgotPasswordLink = By.linkText("Quên mật khẩu?");
    private final By registerLink = By.linkText("Đăng ký");

    private final By successMessage = By.cssSelector(".alert-success, .success-message, #success-msg");
    private final By errorMessage = By.cssSelector(".alert-danger, .error-message, #error-msg, .text-danger");
    private final By loginForm = By.cssSelector("form, .login-form, #login-form");

    private final By emailError = By.cssSelector("#email + .error, .email-error, [data-field='email'] .error");
    private final By passwordError = By.cssSelector("#password + .error, .password-error, [data-field='password'] .error");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        navigateTo(LOGIN_URL);
        waitForPageLoad();
        return this;
    }

    public boolean isPageLoaded() {
        return isElementVisible(loginForm) && 
               getCurrentUrl().contains("Login") &&
               isElementVisible(emailField) &&
               isElementVisible(passwordField);
    }

    public LoginPage enterEmail(String email) {
        fastType(emailField, email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        fastType(passwordField, password);
        return this;
    }

    public LoginPage clearEmail() {
        clearField(emailField);
        return this;
    }

    public LoginPage clearPassword() {
        clearField(passwordField);
        return this;
    }

    public LoginPage checkRememberMe() {
        if (isElementVisible(rememberMeCheckbox) && !isElementSelected(rememberMeCheckbox)) {
            fastClick(rememberMeCheckbox);
        }
        return this;
    }

    public LoginPage uncheckRememberMe() {
        if (isElementVisible(rememberMeCheckbox) && isElementSelected(rememberMeCheckbox)) {
            fastClick(rememberMeCheckbox);
        }
        return this;
    }

    public LoginPage clickLoginButton() {
        fastClick(loginButton);
        return this;
    }

    public LoginPage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        return this;
    }
    public boolean hasEmailValidationError() {
        return isElementVisible(emailError);
    }

    public boolean hasPasswordValidationError() {
        return isElementVisible(passwordError);
    }

    public boolean isEmailEmpty() {
        return getAttribute(emailField, "value").isEmpty();
    }

    public boolean isPasswordEmpty() {
        return getAttribute(passwordField, "value").isEmpty();
    }

    public boolean areRequiredFieldsFilled() {
        return !isEmailEmpty() && !isPasswordEmpty();
    }

    public boolean isFormValid() {
        return areRequiredFieldsFilled() && 
               !hasEmailValidationError() && 
               !hasPasswordValidationError();
    }

    public boolean isLoginButtonEnabled() {
        return isElementEnabled(loginButton);
    }

    public String getEmailValue() {
        return getAttribute(emailField, "value");
    }

    public String getPasswordValue() {
        return getAttribute(passwordField, "value");
    }

    public boolean isRememberMeChecked() {
        return isElementSelected(rememberMeCheckbox);
    }

    public boolean isSuccessMessageDisplayed() {
        return isElementVisible(successMessage);
    }

    public boolean isErrorMessageDisplayed() {
        return isElementVisible(errorMessage);
    }

    public String getSuccessMessage() {
        return isElementVisible(successMessage) ? getText(successMessage) : "";
    }

    public String getErrorMessage() {
        return isElementVisible(errorMessage) ? getText(errorMessage) : "";
    }

    public RegisterPage clickRegisterLink() {
        if (isElementVisible(registerLink)) {
            fastClick(registerLink);
        } else {
            navigateTo("http://localhost:8080/Register");
        }
        return new RegisterPage(driver);
    }

    public void clickForgotPassword() {
        if (isElementVisible(forgotPasswordLink)) {
            fastClick(forgotPasswordLink);
        }
    }
    public boolean waitForLoginSuccess() {
        try {
            return waitFor(driver -> 
                isSuccessMessageDisplayed() || 
                !getCurrentUrl().contains("Login")
            );
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForLoginError() {
        try {
            return waitFor(driver -> isErrorMessageDisplayed());
        } catch (Exception e) {
            return false;
        }
    }

    public byte[] captureLoginPageScreenshot() {
        return takeScreenshot();
    }

    public LoginPage scrollToLoginForm() {
        scrollToElement(loginForm);
        return this;
    }
} 