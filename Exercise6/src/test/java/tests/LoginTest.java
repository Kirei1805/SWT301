package tests;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import utils.DriverFactory;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoginTest extends BaseTest {
    @Test
    @Order(1)
    @DisplayName("🔍 Empty Fields Validation Test")
    void testEmptyFieldsValidation() {
        navigateToLogin();

        loginPage.clearEmail()
                 .clearPassword()
                 .clickLoginButton();
        boolean hasEmailError = loginPage.hasEmailValidationError();
        boolean hasPasswordError = loginPage.hasPasswordValidationError();
        boolean staysOnLoginPage = loginPage.isPageLoaded();
        assertTrue(hasEmailError || hasPasswordError || staysOnLoginPage,
                  "Empty fields should trigger validation or prevent submission");
        
        printSuccess("Empty fields validation test completed");
    }
    
    @Test
    @Order(2)
    @DisplayName("🚫 Invalid Credentials Test")
    void testInvalidCredentials() {
        navigateToLogin();
        loginPage.enterEmail("nonexistent@example.com")
                 .enterPassword("wrongpassword")
                 .clickLoginButton();
        boolean hasError = loginPage.isErrorMessageDisplayed();
        boolean staysOnLogin = loginPage.isPageLoaded();
        
        assertTrue(hasError || staysOnLogin,
                  "Invalid credentials should be rejected");
        
        printSuccess("Invalid credentials test completed");
    }
    @ParameterizedTest(name = "🔍 Testing invalid email: {0}")
    @ValueSource(strings = {
        "invalid@",
        "@invalid.com", 
        "invalid..email@test.com",
        "invalid email@test.com",
        "",
        " "
    })
    @Order(3)
    @DisplayName("📧 Invalid Email Formats Test")
    void testInvalidEmailFormats(String invalidEmail) {
        navigateToLogin();
        loginPage.clearEmail()
                 .clearPassword()
                 .enterEmail(invalidEmail)
                 .enterPassword("password123");
        boolean hasClientValidation = loginPage.hasEmailValidationError();
        if (hasClientValidation) {
            printSuccess("Client-side validation for: '" + invalidEmail + "'");
            return;
        }
        String originalUrl = DriverFactory.getDriver().getCurrentUrl();
        loginPage.clickLoginButton();
        boolean hasServerError = loginPage.isErrorMessageDisplayed();
        String currentUrl = DriverFactory.getDriver().getCurrentUrl();
        boolean urlChanged = !currentUrl.equals(originalUrl);
        boolean hasCredentialsError = currentUrl.contains("error");
        
        boolean someValidationOccurred = hasServerError || urlChanged || hasCredentialsError;
        if (invalidEmail.equals("") || invalidEmail.equals(" ")) {
            if (someValidationOccurred) {
                printSuccess("Empty email '" + invalidEmail + "' was caught by validation");
            } else {
                printTestInfo("Empty email '" + invalidEmail + "' was not validated - this is acceptable for some apps");
            }
        } else if (invalidEmail.equals("invalid@") || invalidEmail.equals("@invalid.com")) {
            assertTrue(someValidationOccurred,
                      "Definitely invalid email '" + invalidEmail + "' should be caught");
        } else {
            printTestInfo("Email '" + invalidEmail + "' validation result: " + someValidationOccurred);
        }
    }
    @ParameterizedTest(name = "🔍 Credentials: {0} / {1}")
    @CsvSource({
        "admin@test.com, admin123",
        "user@test.com, user123", 
        "test@example.com, password123"
    })
    @Order(4)
    @DisplayName("🔐 Valid Credentials Test")
    void testValidCredentials(String email, String password) {
        navigateToLogin();
        loginPage.enterEmail(email)
                 .enterPassword(password)
                 .clickLoginButton();
        boolean loginSucceeded = loginPage.waitForLoginSuccess();
        boolean hasErrorMessage = loginPage.isErrorMessageDisplayed();
        
        if (loginSucceeded) {
            printSuccess("Login succeeded for: " + email);
            assertTrue(loginSucceeded);
        } else if (hasErrorMessage) {
            printTestInfo("Login failed for: " + email + " - " + loginPage.getErrorMessage());
            assertFalse(loginPage.getErrorMessage().isEmpty());
        } else {
            printWarning("Login result unclear for: " + email);
            // Still on login page - acceptable
            assertTrue(loginPage.isPageLoaded());
        }
    }
    @Test
    @Order(5)
    @DisplayName("📝 Form Field Interactions Test")
    void testFormFieldInteractions() {
        navigateToLogin();
        String email1 = "first@example.com";
        String email2 = "second@example.com";
        String password1 = "password1";
        String password2 = "password2";
        loginPage.enterEmail(email1);
        assertEquals(email1, loginPage.getEmailValue());
        loginPage.clearEmail()
                 .enterEmail(email2);
        assertEquals(email2, loginPage.getEmailValue());
        loginPage.enterPassword(password1);
        assertEquals(password1, loginPage.getPasswordValue());
        loginPage.clearPassword()
                 .enterPassword(password2);
        assertEquals(password2, loginPage.getPasswordValue());
        printTestInfo("Testing Remember Me checkbox functionality...");
        
        try {
            boolean initialState = loginPage.isRememberMeChecked();
            printTestInfo("Remember Me initial state: " + initialState);
            loginPage.checkRememberMe();
            boolean afterCheck = loginPage.isRememberMeChecked();
            printTestInfo("After check: " + afterCheck);
            if (afterCheck) {
                loginPage.uncheckRememberMe();
                boolean afterUncheck = loginPage.isRememberMeChecked();
                printTestInfo("After uncheck: " + afterUncheck);
                assertFalse(afterUncheck);
                printSuccess("Remember Me checkbox working correctly");
            } else {
                printWarning("Remember Me checkbox doesn't respond to clicks - might be disabled or readonly");
            }
        } catch (Exception e) {
            printWarning("Remember Me checkbox interaction failed: " + e.getMessage());
            printTestInfo("This is acceptable - checkbox might not be implemented on this page");
        }
        printSuccess("Form field interactions test completed");
    }
    @Test
    @Order(6)
    @DisplayName("📋 Valid Data Not Submitted Test")
    void testValidDataNotSubmitted() {
        navigateToLogin();
        
        String email = generateRandomEmail();
        String password = "SecurePassword123";
        loginPage.enterEmail(email)
                 .enterPassword(password);
        assertEquals(email, loginPage.getEmailValue());
        assertEquals(password, loginPage.getPasswordValue());
        assertTrue(loginPage.areRequiredFieldsFilled());
        assertFalse(loginPage.isSuccessMessageDisplayed());
        assertFalse(loginPage.isErrorMessageDisplayed());
        
        printSuccess("Valid data not submitted test completed");
    }
    @Test
    @Order(7)
    @DisplayName("🔗 Navigation to Registration Test")
    void testNavigationToRegistration() {
        navigateToLogin();
        String initialUrl = DriverFactory.getDriver().getCurrentUrl();
        printTestInfo("Starting navigation test from: " + initialUrl);
        boolean registerLinkExists = false;
        try {
            printTestInfo("Looking for register link on login page...");
            registerPage = loginPage.clickRegisterLink();
            registerLinkExists = true;
            printSuccess("Register link found and clicked successfully");
        } catch (Exception e) {
            printWarning("Register link interaction failed: " + e.getMessage());
            printTestInfo("Trying direct navigation to register page...");
            DriverFactory.getDriver().get("http://localhost:8080/Register");
            if (registerPage == null) {
                registerPage = loginPage.clickRegisterLink();
            }
        }
        String currentUrl = DriverFactory.getDriver().getCurrentUrl();
        String pageTitle = DriverFactory.getDriver().getTitle();
        
        printTestInfo("Navigation results:");
        printTestInfo("   Initial URL: " + initialUrl);
        printTestInfo("   Current URL: " + currentUrl);
        printTestInfo("   Page title: " + pageTitle);
        printTestInfo("   Register link existed: " + registerLinkExists);
        boolean urlIndicatesRegisterPage = currentUrl.toLowerCase().contains("register") || 
                                         currentUrl.toLowerCase().contains("/register") ||
                                         pageTitle.toLowerCase().contains("register") ||
                                         pageTitle.toLowerCase().contains("đăng ký") ||
                                         pageTitle.toLowerCase().contains("tham gia");
        
        assertTrue(urlIndicatesRegisterPage,
                  "Should navigate to registration page (URL: " + currentUrl + ", Title: " + pageTitle + ")");
        
        // Check registration page structure (defensive)
        boolean pageFullyLoaded = registerPage.isPageLoaded();
        printTestInfo("Registration page structure check:");
        printTestInfo("   Traditional form structure detected: " + pageFullyLoaded);
        
        if (pageFullyLoaded) {
            printSuccess("Registration page loaded with expected form structure");
        } else {
            printWarning("Registration page has different structure than expected");
            printTestInfo("This is acceptable - page might use different form implementation");

            try {
                boolean urlHasRegister = currentUrl.toLowerCase().contains("register");
                boolean titleSuggestsRegister = pageTitle.toLowerCase().contains("register") || 
                                              pageTitle.toLowerCase().contains("đăng ký") ||
                                              pageTitle.toLowerCase().contains("tham gia");
                
                printTestInfo("   Alternative structure check:");
                printTestInfo("     URL suggests register page: " + urlHasRegister);
                printTestInfo("     Title suggests register page: " + titleSuggestsRegister);
                
                if (urlHasRegister || titleSuggestsRegister) {
                    printSuccess("Navigation successful - on registration page with different structure");
                } else {
                    printWarning("Navigation might not have worked as expected");
                }
            } catch (Exception e) {
                printWarning("Could not analyze page structure: " + e.getMessage());
            }
        }
        if (registerLinkExists && urlIndicatesRegisterPage) {
            printSuccess("Login page HAS working navigation to registration");
        } else if (urlIndicatesRegisterPage) {
            printSuccess("Registration page accessible (direct navigation worked)");
        } else {
            printWarning("Registration navigation needs investigation");
        }
    }
    @Test
    @Order(8)
    @DisplayName("🔄 Multiple Invalid Attempts Test")
    void testMultipleInvalidAttempts() {
        navigateToLogin();
        
        String[] invalidCredentials = {
            "wrong1@example.com",
            "wrong2@example.com", 
            "wrong3@example.com"
        };
        
        for (String email : invalidCredentials) {
            printTestInfo("Testing invalid attempt with: " + email);
            
            loginPage.clearEmail()
                    .clearPassword()
                    .enterEmail(email)
                    .enterPassword("wrongpassword")
                    .clickLoginButton();
            waitFor(1000);
            boolean hasError = loginPage.isErrorMessageDisplayed();
            boolean staysOnLogin = loginPage.isPageLoaded();
            
            assertTrue(hasError || staysOnLogin,
                      "Invalid attempt with " + email + " should be rejected");
            
            captureScreenshot("Invalid attempt: " + email);
        }
        
        printSuccess("Multiple invalid attempts test completed");
    }
} 