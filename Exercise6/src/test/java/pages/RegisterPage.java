package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage extends BasePage {
    
    // URL constants
    public static final String REGISTER_URL = "http://localhost:8080/Register";
    public static final String STUDENT_REGISTER_URL = "http://localhost:8080/Register/Student";
    private final By[] emailFieldFallbacks = {
        By.id("email"),
        By.name("email"),
        By.cssSelector("input[type='email']"),
        By.cssSelector("input[placeholder*='email' i]"),
        By.cssSelector("input[placeholder*='Email' i]"),
        By.xpath("//input[@type='email']"),
        By.xpath("//input[contains(@placeholder, 'email')]")
    };
    private final By[] fullNameFieldFallbacks = {
        By.id("fullName"),
        By.name("fullName"),
        By.id("name"),
        By.name("name"),
        By.cssSelector("input[placeholder*='name' i]"),
        By.cssSelector("input[placeholder*='Name' i]"),
        By.xpath("//input[contains(@placeholder, 'name')]")
    };
    
    private final By[] passwordFieldFallbacks = {
        By.id("password"),
        By.name("password"),
        By.cssSelector("input[type='password']"),
        By.xpath("//input[@type='password']")
    };
    
    private final By[] confirmPasswordFieldFallbacks = {
        By.id("confirmPassword"),
        By.name("confirmPassword"),
        By.id("passwordConfirm"),
        By.name("passwordConfirm"),
        By.cssSelector("input[placeholder*='confirm' i]"),
        By.cssSelector("input[placeholder*='Confirm' i]")
    };
    
    private final By[] phoneFieldFallbacks = {
        By.id("phone"),
        By.name("phone"),
        By.id("phoneNumber"),
        By.name("phoneNumber"),
        By.cssSelector("input[type='tel']"),
        By.cssSelector("input[placeholder*='phone' i]"),
        By.cssSelector("input[placeholder*='Phone' i]")
    };
    
    private final By[] termsCheckboxFallbacks = {
        By.id("terms"),
        By.name("terms"),
        By.id("agreeTerms"),
        By.name("agreeTerms"),
        By.cssSelector("input[type='checkbox']"),
        By.xpath("//input[@type='checkbox']")
    };
    
    private final By[] registerButtonFallbacks = {
        By.cssSelector("button[type='submit']"),
        By.cssSelector("input[type='submit']"),
        By.cssSelector(".register-btn"),
        By.cssSelector("#register-btn"),
        By.cssSelector("button:contains('Register')"),
        By.cssSelector("button:contains('Đăng ký')"),
        By.xpath("//button[contains(text(), 'Register')]"),
        By.xpath("//button[contains(text(), 'Đăng ký')]")
    };

    private final By successMessage = By.cssSelector(".alert-success, .success-message, #success-msg");
    private final By errorMessage = By.cssSelector(".alert-danger, .error-message, #error-msg, .text-danger");
    private final By generalErrorMessage = By.cssSelector(".error, .validation-error, .field-error");

    private final By emailError = By.cssSelector("#email + .error, .email-error, [data-field='email'] .error");
    private final By fullNameError = By.cssSelector("#fullName + .error, .name-error, [data-field='fullName'] .error");
    private final By passwordError = By.cssSelector("#password + .error, .password-error, [data-field='password'] .error");
    private final By confirmPasswordError = By.cssSelector("#confirmPassword + .error, .confirm-password-error");
    private final By phoneError = By.cssSelector("#phone + .error, .phone-error, [data-field='phone'] .error");
    private final By termsError = By.cssSelector("#terms + .error, .terms-error, [data-field='terms'] .error");

    private final By registerForm = By.cssSelector("form, .register-form, #register-form");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    
    public RegisterPage open() {
        try {
            navigateTo(STUDENT_REGISTER_URL);
            if (isPageLoaded()) {
                System.out.println("✅ Successfully navigated to Student Register page");
                return this;
            }
        } catch (Exception e) {
            System.out.println("⚠️ Student Register URL failed, trying general Register URL");
        }
        navigateTo(REGISTER_URL);
        return this;
    }

    public boolean isPageLoaded() {
        boolean hasForm = isElementVisible(registerForm);
        boolean hasEmailField = findEmailField() != null;
        boolean hasPasswordField = findPasswordField() != null;
        
        System.out.println("📋 Register page structure check:");
        System.out.println("   Has form: " + hasForm);
        System.out.println("   Has email field: " + hasEmailField);
        System.out.println("   Has password field: " + hasPasswordField);
        
        return hasForm && hasEmailField && hasPasswordField;
    }

    public RegisterPage enterEmail(String email) {
        WebElement emailField = findEmailField();
        if (emailField != null) {
            fastType(By.id("email"), email);
        } else {
            System.err.println("❌ Email field not found on register page");
        }
        return this;
    }

    public RegisterPage clearEmail() {
        WebElement emailField = findEmailField();
        if (emailField != null) {
            clearField(By.id("email"));
        }
        return this;
    }

    private WebElement findEmailField() {
        for (By locator : emailFieldFallbacks) {
            try {
                System.out.println("🔍 Trying email locator: " + locator);
                WebElement element = waitForVisibility(locator);
                System.out.println("✅ Found email field with: " + locator);
                return element;
            } catch (Exception e) {
                System.out.println("❌ Email locator failed: " + locator);
            }
        }
        
        System.err.println("❌ All email field locators failed");
        return null;
    }

    public RegisterPage enterFullName(String fullName) {
        fastType(fullNameField, fullName);
        return this;
    }

    public RegisterPage enterPassword(String password) {
        fastType(passwordField, password);
        return this;
    }

    public RegisterPage enterConfirmPassword(String confirmPassword) {
        fastType(confirmPasswordField, confirmPassword);
        return this;
    }

    public RegisterPage enterPhone(String phone) {
        fastType(phoneField, phone);
        return this;
    }

    public RegisterPage agreeToTerms() {
        if (isElementVisible(termsCheckbox) && !isElementSelected(termsCheckbox)) {
            fastClick(termsCheckbox);
        }
        return this;
    }

    public RegisterPage disagreeToTerms() {
        if (isElementVisible(termsCheckbox) && isElementSelected(termsCheckbox)) {
            fastClick(termsCheckbox);
        }
        return this;
    }

    public RegisterPage clickRegisterButton() {
        fastClick(registerButton);
        return this;
    }
    public RegisterPage clearFullName() {
        clearField(fullNameField);
        return this;
    }

    public RegisterPage clearPassword() {
        clearField(passwordField);
        return this;
    }

    public RegisterPage clearConfirmPassword() {
        clearField(confirmPasswordField);
        return this;
    }

    public RegisterPage clearPhone() {
        clearField(phoneField);
        return this;
    }

    public RegisterPage clearAllFields() {
        clearEmail();
        clearFullName();
        clearPassword();
        clearConfirmPassword();
        clearPhone();
        return this;
    }

    public boolean hasEmailValidationError() {
        return isElementVisible(emailError);
    }

    public boolean hasFullNameValidationError() {
        return isElementVisible(fullNameError);
    }

    public boolean hasPasswordValidationError() {
        return isElementVisible(passwordError);
    }

    public boolean hasConfirmPasswordValidationError() {
        return isElementVisible(confirmPasswordError);
    }

    public boolean hasPhoneValidationError() {
        return isElementVisible(phoneError);
    }

    public boolean hasTermsValidationError() {
        return isElementVisible(termsError);
    }

    public boolean isEmailEmpty() {
        return getAttribute(emailField, "value").isEmpty();
    }

    public boolean isFullNameEmpty() {
        return getAttribute(fullNameField, "value").isEmpty();
    }

    public boolean isPasswordEmpty() {
        return getAttribute(passwordField, "value").isEmpty();
    }

    public boolean isConfirmPasswordEmpty() {
        return getAttribute(confirmPasswordField, "value").isEmpty();
    }

    public boolean isPhoneEmpty() {
        return getAttribute(phoneField, "value").isEmpty();
    }

    public boolean areAllRequiredFieldsFilled() {
        return !isEmailEmpty() && 
               !isFullNameEmpty() && 
               !isPasswordEmpty() && 
               !isConfirmPasswordEmpty() && 
               !isPhoneEmpty();
    }

    public boolean doPasswordsMatch() {
        String password = getAttribute(passwordField, "value");
        String confirmPassword = getAttribute(confirmPasswordField, "value");
        return password.equals(confirmPassword);
    }

    public boolean areTermsAgreed() {
        return isElementSelected(termsCheckbox);
    }

    public boolean isFormValid() {
        return areAllRequiredFieldsFilled() && 
               doPasswordsMatch() && 
               areTermsAgreed() &&
               !hasEmailValidationError() &&
               !hasFullNameValidationError() &&
               !hasPasswordValidationError() &&
               !hasConfirmPasswordValidationError() &&
               !hasPhoneValidationError() &&
               !hasTermsValidationError();
    }

    public boolean isRegisterButtonEnabled() {
        return isElementEnabled(registerButton);
    }

    public boolean isSuccessMessageDisplayed() {
        return isElementVisible(successMessage);
    }

    public boolean isErrorMessageDisplayed() {
        return isElementVisible(errorMessage) || isElementVisible(generalErrorMessage);
    }

    public String getSuccessMessage() {
        return isElementVisible(successMessage) ? getText(successMessage) : "";
    }

    public String getErrorMessage() {
        if (isElementVisible(errorMessage)) {
            return getText(errorMessage);
        } else if (isElementVisible(generalErrorMessage)) {
            return getText(generalErrorMessage);
        }
        return "";
    }

    public boolean waitForRegistrationSuccess() {
        return waitForRegistrationSuccess(10);
    }
    
    public boolean waitForRegistrationSuccess(int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return customWait.until(driver -> 
                isSuccessMessageDisplayed() || 
                (!getCurrentUrl().contains("Register") && !getCurrentUrl().contains("Student"))
            );
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForRegistrationError() {
        try {
            return waitFor(driver -> isErrorMessageDisplayed());
        } catch (Exception e) {
            return false;
        }
    }
    public String getEmailValue() {
        return getAttribute(emailField, "value");
    }

    public String getFullNameValue() {
        return getAttribute(fullNameField, "value");
    }

    public String getPasswordValue() {
        return getAttribute(passwordField, "value");
    }

    public String getConfirmPasswordValue() {
        return getAttribute(confirmPasswordField, "value");
    }

    public String getPhoneValue() {
        return getAttribute(phoneField, "value");
    }
    public byte[] captureRegisterPageScreenshot() {
        return takeScreenshot();
    }

    public RegisterPage scrollToRegisterForm() {
        scrollToElement(registerForm);
        return this;
    }
    private final By emailField = emailFieldFallbacks[0];
    private final By fullNameField = fullNameFieldFallbacks[0];
    private final By passwordField = passwordFieldFallbacks[0];
    private final By confirmPasswordField = confirmPasswordFieldFallbacks[0];
    private final By phoneField = phoneFieldFallbacks[0];
    private final By termsCheckbox = termsCheckboxFallbacks[0];
    private final By registerButton = registerButtonFallbacks[0];

    private WebElement findPasswordField() {
        for (By locator : passwordFieldFallbacks) {
            try {
                WebElement element = waitForVisibility(locator);
                return element;
            } catch (Exception e) {
            }
        }
        return null;
    }
} 